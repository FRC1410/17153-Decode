package org.firstinspires.ftc.teamcode.dynamite.InterfaceStuffs;

import androidx.loader.content.Loader;

import com.pedropathing.follower.Follower;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.RobotLog;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.commands.Command;
import org.firstinspires.ftc.teamcode.dynamite.FTCInterface.DYNInterpreter;
import org.firstinspires.ftc.teamcode.dynamite.FaultReporter;
import org.firstinspires.ftc.teamcode.dynamite.PPInterface;
import org.firstinspires.ftc.teamcode.dynamite.TimedLoopThread;

import java.util.ArrayList;
import java.util.List;

public class DYNKindaInterface {
    // this receives the given script (the class), and the interface class,
    // and handles all operations related to running DYN
    // DYN telemetry will be added like before, but UPDATING the telemetry will be handled by the user.

    // this class will be required to be constructed with the following things: telemetry, and the script path, and optionally if the script is on a USB.
    private final Runnable followerUpdater;
    private final InterfaceUtils utils;
    private final Telemetry telemetry;
    private final HardwareMap HWmap;
    private final String script;
    private final boolean USBload;
    private final boolean useRadians;

    public DYNKindaInterface(Runnable followerUpdater, InterfaceUtils utils, Telemetry telemetry, HardwareMap HWmap, String script){
        this(followerUpdater, utils, telemetry, HWmap, script, true);
    }
    public DYNKindaInterface(Runnable followerUpdater, InterfaceUtils utils, Telemetry telemetry, HardwareMap HWmap, String script, boolean USBload){
        this(followerUpdater, utils, telemetry, HWmap, script, USBload, false);
    }
    public DYNKindaInterface(Runnable followerUpdater, InterfaceUtils utils, Telemetry telemetry, HardwareMap HWmap, String script, boolean USBload, boolean useRadians){
        this.followerUpdater = followerUpdater;
        this.utils = utils;
        this.telemetry = telemetry;
        this.HWmap = HWmap;
        this.script = script;
        this.USBload = USBload;
        this.useRadians = useRadians;
    }

    private Runnable opModeStop;
    public void registerOpModeStop(Runnable opModeStop){
        this.opModeStop = opModeStop;
    }

    private Follower follower;
    private final FaultReporter faults = new FaultReporter();
    private volatile boolean workersShutDown = false;
    private PPInterface ppInterface;
    private DYNInterpreter interpreter;
    private Thread DYNThread;
    private TimedLoopThread followerUpdateThread;

    private void registerFollower(Follower follower){
        this.follower = follower;
    }

    public void init(){
        // runs before init(), so stale faults from a previous run of this OpMode are
        // cleared even if a subclass overrides init() without calling super.init()
        faults.reset();
        workersShutDown = false;
        // clear static state left behind by a previous run of this OpMode
        Command.resetRunState();
        // faults.reset() / workersShutDown are handled in internalPreInit(), which runs
        // before this method and cannot be skipped by a subclass override
        // init PP and PPInterface
        ppInterface = new PPInterface(follower,HWmap,telemetry,useRadians);
        // init the DYNInterpreter
        interpreter = new DYNInterpreter(ppInterface,useRadians);
        if (USBload) interpreter.loadFromUSB();
        interpreter.setScriptPath(script);
        interpreter.init();
        // config utils
        utils.registerDYNstuffs(ppInterface,interpreter);
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
            if (Command.isHardStopped() && opModeStop != null){
                opModeStop.run();
            }
        }, "DYN");
        DYNThread.setDaemon(true);
        DYNThread.setUncaughtExceptionHandler((th, t) -> faults.report(FaultReporter.DYN, t));
        // init follower update thread
        followerUpdateThread = new TimedLoopThread(
                followerUpdater,
                utils.getFollowerUpdateRate(),
                cause -> {
                    if (cause != null) faults.report(FaultReporter.FOLLOWER, cause);
                    else faults.noteExit(Thread.currentThread(), "update loop stopped");
                });
    }
    public void start(){
        // set thread priorities
        DYNThread.setPriority(utils.getDynThreadPriority());
        followerUpdateThread.setPriority(utils.getFollowerThreadPriority());
        // start running DYN code
        DYNThread.start();
        // start the pather update loop
        followerUpdateThread.start();
        // link up followerUpdateThread to the interface
        ppInterface.linkPatherUpdateThread(followerUpdateThread);
    }
    public void loop(){
        utils.processJFuncCalls();
        processTelemetry();
        checkWorkerThreads();
    }
    public void stop(){
        // ensure DYN and follower update threads stop cleanly
        shutdownWorkers();
        // If the OpMode was stopped before the next loop tick could rethrow, anything
        // still queued gets logged loudly here instead of vanishing. Subclasses that
        // override stop() should call super.stop(); if one forgets, the fault is still
        // in logcat and stderr - report() writes those before it ever queues.
        faults.flushUndelivered();
    }
    public void initLoop(){
        checkWorkerThreads();
    }

    private String[] activeDYNTelemetry = new String[0];
    private void processTelemetry(){
        ArrayList<String> dynFrame = Command.consumeTelemFrame();
        boolean dynPending = (dynFrame != null);
        if (dynPending){
            activeDYNTelemetry = dynFrame.toArray(new String[0]);
        }
        if (activeDYNTelemetry.length != 0){
            if (useRadians) {
                telemetry.addData("DYNMode", "Radians");
            } else {
                telemetry.addData("DYNMode", "Degrees");
            }
        }
        for (String message : activeDYNTelemetry){
            telemetry.addData("DYN",message);
        }
        if (activeDYNTelemetry.length > 0){
            telemetry.addData("- - - - - ", "- - - - -");
        }
        // we don't update because that's the users job.
    }

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
}
