package org.firstinspires.ftc.teamcode.dynamite.DYNCore.commands;

import org.firstinspires.ftc.teamcode.dynamite.FTCInterface.FTCInterface;
import org.firstinspires.ftc.teamcode.dynamite.FTCInterface.GeneralMovement;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.CommandException;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.commands.movement.splineStuff.SplineType;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.VariableManager;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.variables.Variable;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.variables.VariableException;

import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.function.Function;

public class Command {
    // interface stuff
    private static FTCInterface ftcInterface;
    public static void linkInterface(FTCInterface ftcInterface){
        Command.ftcInterface = ftcInterface;
    }
    // jFunc stuffs
    protected Variable runJFunc(boolean wantOutput, String ID){
        return ftcInterface.runJFunc(line,wantOutput,ID);
    }
    protected Variable runJFunc(boolean wantOutput, String ID, Variable in){
        return ftcInterface.runJFunc(line,wantOutput,ID,in);
    }
    // telemetry thing
    // All access to telemBuffer/wantToUpdateTelem is guarded by telemLock. The buffer is
    // written from the DYN thread (AddData/Clear) and read from the OpMode loop thread
    // (DynAutoOpMode.processTelemetry), so `volatile` was never sufficient: it  only
    // gave the list reference, not its contents.
    private static final Object telemLock = new Object();
    private static final ArrayList<String> telemBuffer = new ArrayList<>();
    private static boolean wantToUpdateTelem = false;

    /** DYN-thread side: append one line to the pending buffer. */
    public static void pushTelemLine(String line){
        synchronized (telemLock){
            telemBuffer.add(line);
        }
    }
    /** DYN-thread side: empty the pending buffer (the DYN `Clear` command). */
    public static void clearTelem(){
        synchronized (telemLock){
            telemBuffer.clear();
        }
    }
    /** DYN-thread side: mark the buffer for publication (the DYN `Update` command). */
    public static void updateTelem(){
        synchronized (telemLock){
            wantToUpdateTelem = true;
        }
    }
    /**
     * Loop-thread side: if an Update is pending, atomically clear the flag and return a
     * defensive copy of the buffer as it stands right now. Returns null if nothing is pending.
     * The copy is taken at read time, matching the previous read-the-live-buffer behaviour,
     * but under the lock so the DYN thread cannot mutate it mid-copy.
     */
    public static ArrayList<String> consumeTelemFrame(){
        synchronized (telemLock){
            if (!wantToUpdateTelem) return null;
            wantToUpdateTelem = false;
            return new ArrayList<>(telemBuffer);
        }
    }
    /**
     * Clears static state left over from a previous OpMode run. The FTC SDK keeps this class
     * loaded between runs, so without this a second run starts with the first run's telemetry
     * still buffered and with running==false if the script ever hit Quit/halt.
     */
    public static void resetRunState(){
        synchronized (telemLock){
            telemBuffer.clear();
            wantToUpdateTelem = false;
        }
        running = true;
        hardStopped = false;
        exitCode = null;
    }
    // misc linker stuff
    protected static Consumer<String> runDynPath;
    protected static Function<String,Boolean> dynPathExists;
    public static void registerPathRunner(Consumer<String> runner, Function<String,Boolean> checker){
        Command.runDynPath = runner;
        Command.dynPathExists = checker;
    }
    // pathPlanner linker stuff
    protected void moveTo(double[] target){
        ftcInterface.runGeneralMove(line,new GeneralMovement(line,target));
    }
    protected void turnTo(double target){
        ftcInterface.runGeneralMove(line,new GeneralMovement(line,target));
    }
    protected void doSpline(double[] target, SplineType type){
        ftcInterface.runGeneralMove(line,new GeneralMovement(line,target,type));
    }
    protected void doSpline(double[] target, double endTan, SplineType type){
        ftcInterface.runGeneralMove(line,new GeneralMovement(line,target,endTan,type));
    }
    protected void doBezier(double[][] target){
        ftcInterface.runGeneralMove(line,new GeneralMovement(line,target));
    }
    protected void setStartPose(double[] start){
        ftcInterface.setStartPos(line,start);
    }
    protected void DYNSleep(long milliseconds){
        ftcInterface.DYNSleep(milliseconds);
    }

    // the rest of the class
    private static VariableManager varManager;
    public static void linkVarMan(VariableManager varMan){
        Command.varManager = varMan;
    }
    protected Variable getVar(String ID){
        // if the in ID is null: dump the command data
        if (ID == null){
            dumpCommand();
        }
        Variable gotten = varManager.getVar(ID);
        if (gotten == null) {
            System.out.println(varManager.toString());
            throw new CommandException(line, type.toString(), "Variable " + ID + " not defined!");
        }
        return gotten;
    }
    protected boolean varExists(String ID){
        if (ID == null){
            dumpCommand();
        }
        Variable gotten = varManager.getVar(ID);
        return gotten!=null;
    }
    protected void registerVar(Variable var){
        varManager.registerVar(var);
    }

    protected String[] InVarIDs;
    protected String OutVarID;

    private final CommandType type;

    protected final int line;

    public Command(int line, CommandType type, String[] InVarIDs, String OutVarID){
        this.line = line;
        this.type = type;
        this.InVarIDs = InVarIDs;
        this.OutVarID = OutVarID;
    }
    public Command(int line, CommandType type, String[] InVarIDs){
        this.line = line;
        this.type = type;
        this.InVarIDs = InVarIDs;
        this.OutVarID = "";
    }
    public Command(int line, CommandType type, String OutVarID){
        this.line = line;
        this.type = type;
        this.InVarIDs = new String[]{};
        this.OutVarID = OutVarID;
    }

    protected void dumpCommand(){
        throw new CommandException(line,type.toString(),getCommandDump());
    }
    public String getCommandDump(){
        StringBuilder dump = new StringBuilder();
        dump.append("\nCOMMAND DUMP TRIGGERED!\n");
        dump.append("Command type: ");
        dump.append(type);
        dump.append("\nCommand line: ");
        dump.append(line);
        dump.append("\nCommand out variable: ");
        dump.append(OutVarID);
        dump.append("\nCommand in variables: ");
        for (String var : InVarIDs){
            dump.append("\n");
            dump.append("    ");
            dump.append(var);
        }
        dump.append("\n");
        return dump.toString();
    }

    public void addCommand(Command cmd){
        throw new CommandException(line,type.toString(),"Cannot add command to "+type+" command type!");
    }

    public void run(){
        VariableException.setLine(line);
    }

    // getters
    public CommandType getType(){
        return type;
    }
    public String[] getInVarIDs(){
        return InVarIDs;
    }
    public String getOutVarID(){
        return OutVarID;
    }
    public int getLine(){
        return line;
    }
    // debug
    public String toString(){
        // "@lineN: Type; [InID1,InID2]; outID"
        StringBuilder out = new StringBuilder("@line" + line + ": ");
        String commandType;
        switch (type){
            case For -> commandType = "For";
            case If -> commandType = "If";
            case While -> commandType = "While";

            case jFunc -> commandType = "jFunc";
            case RunPath -> commandType = "RunPath";
            case DynPath -> commandType = "DynPath";

            case Cos -> commandType = "Cos";
            case iCos -> commandType = "iCos";
            case iSin -> commandType = "iSin";
            case iTan -> commandType = "iTan";
            case Sin -> commandType = "Sin";
            case Tan -> commandType = "Tan";
            case toDeg -> commandType = "toDeg";
            case toRad -> commandType = "toRad";

            case Add -> commandType = "Add";
            case Decrement -> commandType = "Decrement";
            case Div -> commandType = "Div";
            case Increment -> commandType = "Increment";
            case Mux -> commandType = "Mux";
            case Pow -> commandType = "Pow";
            case Sqrt -> commandType = "Sqrt";
            case Sub -> commandType = "Sub";

            case SplineTo -> commandType = "Spline";
            case BezTo ->  commandType = "Bezier";
            case GoTo -> commandType = "GoTo";
            case TurnTo -> commandType = "TurnTo";

            case RngBoolean -> commandType = "RngBoolean";
            case RngDouble -> commandType = "RngDouble";
            case RngFloat -> commandType = "RngFloat";
            case RngInteger -> commandType = "RngInteger";

            case AddData -> commandType = "AddData";
            case Clear -> commandType = "Clear";
            case Update -> commandType = "Update";

            case SetVar -> commandType = "SetVar";
            case AddVar -> commandType = "AddVar";
            case Set -> commandType = "Set";
            case Remove -> commandType = "Remove";
            case Append -> commandType = "Append";
            case Get -> commandType = "Get";
            case Insert -> commandType = "Insert";
            default -> commandType = "Null/Undef (VERY BAD)";
        }
        out.append(commandType).append("; [");
        for (int i = 0; i < InVarIDs.length; i++){
            out.append(InVarIDs[i]);
            if (i != InVarIDs.length-1){
                out.append(", ");
            }
        }
        out.append("]; ").append(OutVarID);
        return out.toString();
    }

    protected static volatile boolean hardStopped = false;
    protected static volatile boolean running = true;
    protected static volatile String exitCode;

    public static boolean isHardStopped(){
        return hardStopped;
    }
    public static boolean hasStopped(){
        return !running;
    }
    public static String getExitCode(){
        return exitCode;
    }

    public static void halt(){
        running = false;
        hardStopped = true;
        exitCode = "-1";
    }
}