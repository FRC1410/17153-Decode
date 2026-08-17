package org.firstinspires.ftc.teamcode.dynamite.DYNCore.commandConstructor;

import org.firstinspires.ftc.teamcode.dynamite.DYNCore.commands.Command;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.commands.controlFlow.For;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.commands.controlFlow.If;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.commands.controlFlow.While;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.commands.function.DynPath;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.tokenizer.Token;

import java.util.Map;

// something to note about this process is that most of the errors
// that we throw here are going to be the most common errors
// for a prog to see while developing with dyn.
// DO YOUR BEST TO NOT TOUCH THE 'i' VALUE MODIFIERS
public class CommandConstructor extends ConstructorUtils{
    public String getMainFuncName(){
        return mainFuncName;
    }
    public Map<String,DynPath> getFuncIDmap(){
        return funcIDmap;
    }
    public void processTokens(Token[] tokenstream){
        givenTokens = tokenstream;
        while (true){
            if (i <= tokenstream.length-1) {
                delegateToken();
            } else {
                break;
            }
        }
    }
    private void delegateToken() {
        Token current = givenTokens[i];
        // we only care about the "starters" of the token stream
        switch (current.type()) {
            case Add, Sub, Mux, Div,
                 Pow, Sqrt, Sin, iSin,
                 Cos, iCos, Tan, iTan,
                 toRad, toDeg,
                 Increment, Decrement -> MathOp.Do();
            case NumberDef, BoolDef, StringDef, List,
                 Json, FieldCord, FieldPos -> VariableOp.Do();
            case Get, Insert, Append,
                 Remove, Set -> ListJsonOp.Do();
            case TurnTo, GoTo,
                 doBez,followSpline,
                 followSplineLinear,
                 followSplineSpline-> MoveOp.Do();
            case DefPath, While, For, If -> FuncLoopIfOp.Do();
            case AddData, Update, Clear -> TelemetryOp.Do();
            case RngBoolean, RngDouble,
                 RngInteger, RngFloat -> RandomOp.Do();
            case PathStartPos -> PathStartOp.Do();
            case Cmd -> jFuncOp.Do();
            case MainPathFunc -> MainPathFuncOp.Do();
            case Run -> RunOp.Do();
            case End -> EndOp.Do();
            case Sleep,Quit,HardQuit -> ExtraOps.Do();
            default -> {
                String tokenVal;
                if (current.getValue() == null){
                    tokenVal = current.type().toString();
                } else {
                    tokenVal = java.lang.String.valueOf(current.getValue());
                }
                throwError("Unknown keyword: " + tokenVal);
            }
        }
    }

    public String toString(){
        StringBuilder debug = new StringBuilder("Command constructor dump:");
        for (String key : funcIDmap.keySet()){
            DynPath func = funcIDmap.get(key);
            debug.append("\n    ");
            debug.append(key);
            debug.append(" -\\/ \n");
            // unravel function
            debug.append(unravelCommand(func));
        }
        return debug.toString();
    }
    private String unravelCommand(Command cmd){
        return unravelCommand(cmd,"    ");
    }
    private String unravelCommand(Command cmd, String indent){
        indent = indent+"    ";
        StringBuilder out = new StringBuilder('\n');
        out.append(indent);
        out.append(cmd.toString());
        if (cmd instanceof DynPath command){
            for (Command c : command.getCommandList()){
                out.append("\n");
                out.append(unravelCommand(c,indent));
            }
        } else if (cmd instanceof For command){
            for (Command c : command.getCommandList()){
                out.append("\n");
                out.append(unravelCommand(c,indent));
            }
        } else if (cmd instanceof If command){
            for (Command c : command.getCommandList()){
                out.append("\n");
                out.append(unravelCommand(c,indent));
            }
        } else if (cmd instanceof While command){
            for (Command c : command.getCommandList()){
                out.append("\n");
                out.append(unravelCommand(c,indent));
            }
        }
        return out.toString();
    }
}