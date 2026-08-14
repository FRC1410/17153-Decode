package org.firstinspires.ftc.teamcode.dynamite;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.RobotLog;

import org.firstinspires.ftc.teamcode.dynamite.DYNCore.commandSequencer.Commands.Command;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.commandSequencer.variables.Variable;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.commandSequencer.variables.VariableTypes;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.commandSequencer.variables.complex.DynFieldPos;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.commandSequencer.variables.primitives.DynBoolean;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.commandSequencer.variables.primitives.DynNumber;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.commandSequencer.variables.primitives.DynString;
import org.firstinspires.ftc.teamcode.dynamite.FTCInterface.DYNInterpreter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class DynAutoOpMode extends OpMode {
    // Main stuff

    // DYN settings
    String scriptPath = "Main.dyn";
    boolean loadFromUSB = false;
    public abstract boolean loadFromUSB();
    public abstract String getScriptPath();
    public abstract Follower buildFollower();
    private double followerUpdateRate = 50.0;
    public final void setFollowerUpdateRate(double frequency){
        followerUpdateRate = frequency;
    }

    // DYN language scripts
    private DYNInterpreter interpreter;
    private PPInterface ppInterface;
    private TimedLoopThread followerUpdateThread;
    private Thread DYNThread;

    @Override
    public final void init(){
        // load user-set settings
        loadFromUSB = loadFromUSB();
        scriptPath = getScriptPath();
        // runs before init(), so stale faults from a previous run of this OpMode are
        // cleared even if a subclass overrides init() without calling super.init()
        faults.reset();
        workersShutDown = false;
        // clear static state left behind by a previous run of this OpMode
        Command.resetRunState();
        // faults.reset() / workersShutDown are handled in internalPreInit(), which runs
        // before this method and cannot be skipped by a subclass override
        // setTelemetrySpeed
        telemetry.setMsTransmissionInterval(100);
        // init PP and PPInterface
        ppInterface = new PPInterface(buildFollower(),hardwareMap,telemetry);
        // init the DYNInterpreter
        interpreter = new DYNInterpreter(ppInterface);
        if (loadFromUSB) interpreter.loadFromUSB();
        interpreter.setScriptPath(scriptPath);
        interpreter.init();
        // init DYN Thread
        // Three layers so this thread cannot end without the OpMode knowing why:
        //   catch   - a script/command/variable exception is logged and queued
        //   finally - marks the thread accounted-for even on a clean exit
        //   UEH     - catches anything that escapes the catch block itself
        DYNThread = new Thread(() -> {
            try {
                String exitCode = interpreter.runScript();
                faults.noteExit(Thread.currentThread(), "script finished, exit code " + exitCode);
            } catch (Throwable t) {
                faults.report(FaultReporter.DYN, t);
            } finally {
                faults.noteExit(Thread.currentThread());
            }
            if (Command.isHardStopped()){
                requestOpModeStop();
            }
        }, "DYN");
        DYNThread.setDaemon(true);
        DYNThread.setUncaughtExceptionHandler((th, t) -> faults.report(FaultReporter.DYN, t));
        onInit();
    }
    @Override
    public final void init_loop(){
        onInitLoop();
        checkWorkerThreads();
    }
    @Override
    public final void start(){
        // link JFuncs
        ppInterface.linkJFuncs(functionJFuncs,consumerJFuncs,supplierJFuncs,runnableJFuncs);
        // start running DYN code
        DYNThread.start();
        // start the pather update loop
        followerUpdateThread = new TimedLoopThread(
                this::updateFollower,
                followerUpdateRate,
                cause -> {
                    if (cause != null) faults.report(FaultReporter.FOLLOWER, cause);
                    else faults.noteExit(Thread.currentThread(), "update loop stopped");
                });
        followerUpdateThread.start();
        // link up followerUpdateThread to the interface
        ppInterface.linkPatherUpdateThread(followerUpdateThread,this::updateFollower);
        // run user code
        onStart();
    }
    @Override
    public final void loop(){
        processTelemetry();
        checkWorkerThreads();
        // run user code
        onLoop();
        // checkup on running threads
        checkWorkerThreads();
    }
    @Override
    public final void stop(){
        // ensure DYN and follower update threads stop cleanly
        shutdownWorkers();
        // If the OpMode was stopped before the next loop tick could rethrow, anything
        // still queued gets logged loudly here instead of vanishing. Subclasses that
        // override stop() should call super.stop(); if one forgets, the fault is still
        // in logcat and stderr - report() writes those before it ever queues.
        faults.flushUndelivered();
        // run user code
        onStop();
    }

    protected abstract void onInit();
    protected void onInitLoop(){};
    protected void onStart(){};
    protected abstract void onLoop();
    protected abstract void updateFollower();
    protected void onStop(){}
    // used for:
    // follower position updating
    // vison management
    // used to ensure that no race conditions happen between pather updates and path action execution

    // these methods are responsible for handling any errors the follower update thread or the DYN thread throw
    // and also the general management of said threads
    private final FaultReporter faults = new FaultReporter();
    private volatile boolean workersShutDown = false;
    private void checkWorkerThreads(){
        // liveness audit first: this is what catches a thread that died without throwing
        faults.auditThread(FaultReporter.DYN, DYNThread);
        if (followerUpdateThread != null){
            faults.auditThread(FaultReporter.FOLLOWER, followerUpdateThread.getThread());
        }

        if (!faults.hasPending()) return;

        List<FaultReporter.Fault> all = faults.drain();
        shutdownWorkers();

        // Telemetry carries many lines, so the full trace lands on the Driver Station
        // screen, not just in the log. Best-effort: the log write already happened at
        // report() time, so failure here costs nothing.
        try {
            processTelemetry();
            telemetry.clearAll();
            for (FaultReporter.Fault f : all){
                telemetry.addLine(f.header());
                for (String traceLine : f.traceLines()){
                    telemetry.addLine(traceLine);
                }
            }
            telemetry.update();
        } catch (RuntimeException telemetryFailed) {
            RobotLog.ww(FaultReporter.TAG, "could not publish fault to telemetry: %s", telemetryFailed);
        }

        throw FaultReporter.toThrowable(all);
    }
    private void shutdownWorkers(){
        if (workersShutDown) return;
        workersShutDown = true;
        faults.beginShutdown();

        try {
            if (interpreter != null) interpreter.halt();
        } catch (RuntimeException e) {
            RobotLog.ww(FaultReporter.TAG, "interpreter.halt() failed: %s", e);
        }
        try {
            // bounded, and TimedLoopThread.stop no longer joins under its own monitor,
            // so this cannot stall the OpMode thread past the SDK watchdog
            if (followerUpdateThread != null) followerUpdateThread.stop(250);
        } catch (RuntimeException e) {
            RobotLog.ww(FaultReporter.TAG, "follower thread stop failed: %s", e);
        }
        try {
            if (DYNThread != null && DYNThread.isAlive()){
                DYNThread.interrupt();
                DYNThread.join(250);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    // DYN variable management
    // variable type checks
    protected static boolean isVariableFieldCoord(Variable var){
        return var.getType() == VariableTypes.FieldCord;
    }
    protected static boolean isVariableBoolean(Variable var){
        return var.getType() == VariableTypes.Boolean;
    }
    protected static boolean isVariableFieldPose(Variable var){
        return var.getType() == VariableTypes.FieldPos;
    }
    protected static boolean isVariableString(Variable var){
        return var.getType() == VariableTypes.String;
    }
    protected static boolean isVariableNumber(Variable var){
        return var.getType() == VariableTypes.Number;
    }
    protected static boolean isVariableList(Variable var){
        return var.getType() == VariableTypes.List;
    }
    protected static boolean isVariableJson(Variable var){
        return var.getType() == VariableTypes.Json;
    }
    // variable value extractors
    protected static Map<Variable,Variable> getJsonFromVariable(Variable var){
        if (isVariableJson(var)){
            return (Map<Variable,Variable>)var.getValue();
        } else {
            throw new RuntimeException("Variable "+var.getTelemetryData()+" is not a Json!");
        }
    }
    protected static ArrayList<Variable> getListFromVariable(Variable var){
        if (isVariableList(var)){
            return (ArrayList<Variable>)var.getValue();
        } else {
            throw new RuntimeException("Variable "+var.getTelemetryData()+" is not a List!");
        }
    }
    protected static boolean getBooleanFromVariable(Variable var){
        if (isVariableBoolean(var)){
            return (boolean)var.getValue();
        } else {
            throw new RuntimeException("Variable "+var.getTelemetryData()+" is not a Boolean!");
        }
    }
    protected static double getNumberFromVariable(Variable var){
        if (isVariableNumber(var)){
            return (double)var.getValue();
        } else {
            throw new RuntimeException("Variable "+var.getTelemetryData()+" is not a Number!");
        }
    }
    protected static String getStringFromVariable(Variable var){
        if (isVariableString(var)){
            return (String)var.getValue();
        } else {
            throw new RuntimeException("Variable "+var.getTelemetryData()+" is not a String!");
        }
    }
    protected static Pose getFieldPosFromVariable(Variable var){
        if (isVariableFieldCoord(var)) {
            Variable[] coords = (Variable[]) var.getValue();
            return new Pose(
                    (double) coords[0].getValue(),
                    (double) coords[1].getValue());
        } else if (isVariableFieldPose(var)){
            Variable[] pose = (Variable[])var.getValue();
            return new Pose(
                    (double)pose[0].getValue(),
                    (double)pose[1].getValue(),
                    (double)pose[2].getValue());
        } else {
            throw new RuntimeException("Variable "+var.getTelemetryData()+" is not a Field Position/Coordinate!");
        }
    }
    // Variable makers
    protected final Variable makeBooleanVar(boolean value){
        Variable var = new DynBoolean(value);
        interpreter.registerVar(var);
        return var;
    }
    protected final Variable makeNumberVar(double value){
        Variable var = new DynNumber(value);
        interpreter.registerVar(var);
        return var;
    }
    protected final Variable makeStringVar(String value){
        Variable var = new DynString(value);
        interpreter.registerVar(var);
        return var;
    }
    protected final Variable makePoseVar(Pose value){
        Variable var = new DynFieldPos(value.getX(),value.getY(),value.getHeading());
        interpreter.registerVar(var);
        return var;
    }
    protected final Variable makeVarCopy(Variable var){
        Variable val = var.getClone();
        interpreter.registerVar(var);
        return val;
    }

    // JFunc management
    private final Map<String,Function<Variable,Variable>> functionJFuncs = new HashMap<>();
    private final Map<String,Consumer<Variable>> consumerJFuncs = new HashMap<>();
    private final Map<String,Supplier<Variable>> supplierJFuncs = new HashMap<>();
    private final Map<String,Runnable> runnableJFuncs = new HashMap<>();
    protected final void registerJFunc(String ID, Function<Variable,Variable> func){
        functionJFuncs.put(ID,func);
    }
    protected final void registerJFunc(String ID, Consumer<Variable> func){
        consumerJFuncs.put(ID,func);
    }
    protected final void registerJFunc(String ID, Supplier<Variable> func){
        supplierJFuncs.put(ID,func);
    }
    protected final void registerJFunc(String ID, Runnable func){
        runnableJFuncs.put(ID,func);
    }

    // telemetry management
    // telemAddData/telemUpdate may be called from the DYN thread (JFuncs run there), so the
    // buffer needs the same lock discipline as Command's.
    private final Object userTelemLock = new Object();
    private volatile boolean thisWantToUpdate = false;
    private final ArrayList<String[]> thisTelemBuffer = new ArrayList<>();
    protected final void addData(String caption, String message){
        synchronized (userTelemLock){
            thisTelemBuffer.add(new String[]{caption,message});
        }
    }
    protected final void addData(String caption, Object message){
        addData(caption,message.toString());
    }
    protected final void update(){
        thisWantToUpdate = true;
    }
    String[] activeDYNTelemetry = new String[0];
    String[][] activeUserTelemetry = new String[0][];
    private void processTelemetry() {
        // DYN telemetry: one atomic check-and-take replaces the old
        // check / read / reset sequence, which could drop an Update that landed between
        // the read and the reset.
        ArrayList<String> dynFrame = Command.consumeTelemFrame();
        boolean dynPending = (dynFrame != null);
        if (dynPending){
            activeDYNTelemetry = dynFrame.toArray(new String[0]);
        }

        // user telemetry: thisWantToUpdate is deliberately left latched once set, preserving
        // the existing free-running behavior (every loop drains whatever telemAddData has
        // queued since the last pass).
        boolean userPending = thisWantToUpdate;
        if (userPending){
            synchronized (userTelemLock){
                activeUserTelemetry = thisTelemBuffer.toArray(new String[0][]);
                thisTelemBuffer.clear();
            }
        }

        if (!dynPending && !userPending) return;

        for (String message : activeDYNTelemetry){
            telemetry.addData("DYN",message);
        }
        if (activeUserTelemetry.length != 0) {
            telemetry.addData("- - - - - ", "- - - - -");
        }
        for (String[] message : activeUserTelemetry){
            telemetry.addData(message[0],message[1]);
        }
        telemetry.update();
    }
}