package org.firstinspires.ftc.teamcode.dynamite.commandSequencer.Commands;

import org.SquidSquad.commandSequencer.CommandException;
import org.SquidSquad.commandSequencer.Commands.movement.splineStuff.SplineType;
import org.SquidSquad.commandSequencer.VariableManager;
import org.SquidSquad.commandSequencer.variables.Variable;
import org.SquidSquad.commandSequencer.variables.VariableException;

import java.util.ArrayList;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class Command {
    // telemetry thing
    protected static final ArrayList<String> telemBuffer = new ArrayList<>();
    // misc linker stuff
    protected static Consumer<String> runDynPath;
    protected static Function<String,Boolean> dynPathExists;
    public static void registerPathRunner(Consumer<String> runner, Function<String,Boolean> checker){
        Command.runDynPath = runner;
        Command.dynPathExists = checker;
    }
    // pathPlanner linker stuff
    protected static Consumer<double[]> moveTo;
    protected static Consumer<Double> turnTo;
    public static void registerCommonActions(Consumer<double[]> movey, Consumer<Double> turny){
        Command.moveTo = movey;
        Command.turnTo = turny;
    }

    protected static boolean doFollowerSpline = false;
    protected static boolean doFollowerBez = false;
    protected static BiConsumer<double[], SplineType> doSpline;
    protected static Consumer<double[][]> doBez;
    public static void registerDoSpline(BiConsumer<double[], SplineType> spliney){
        Command.doFollowerSpline = true;
        Command.doSpline = spliney;
    }
    public static void registerDoBez(Consumer<double[][]> bezzy){
        Command.doFollowerBez = true;
        Command.doBez = bezzy;
    }

    protected static boolean isLinearFollower = true;
    protected static Supplier<double[]> getBotPos;
    public static void registerPosGetter(Supplier<double[]> getter){
        Command.isLinearFollower = false;
        Command.getBotPos = getter;
    }

    protected static Runnable updateLoop;
    protected static Runnable updateFollower;
    protected static Supplier<Boolean> isIdle;
    public static void registerMainLoop(Runnable updater){
        Command.updateLoop = updater;
    }
    public static void registerFollowerUpdater(Runnable updater, Supplier<Boolean> isIdle){
        Command.updateFollower = updater;
        Command.isIdle = isIdle;
    }
    protected static Supplier<double[]> getRobotPose;
    public static void registerRobotPose(Supplier<double[]> roboPose){
        getRobotPose = roboPose;
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

    protected static boolean hardStopped = false;
    protected static boolean running = true;
    protected static String exitCode;

    public static boolean isHardStopped(){
        return hardStopped;
    }
    public static boolean hasStopped(){
        return !running;
    }
    public static String getExitCode(){
        return exitCode;
    }
}
