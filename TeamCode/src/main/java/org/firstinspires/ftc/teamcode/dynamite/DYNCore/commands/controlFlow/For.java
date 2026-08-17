package org.firstinspires.ftc.teamcode.dynamite.DYNCore.commands.controlFlow;

import org.firstinspires.ftc.teamcode.dynamite.DYNCore.CommandException;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.commands.Command;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.commands.CommandType;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.variables.Variable;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.variables.complex.DynFieldCord;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.variables.complex.DynFieldPos;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.variables.complex.DynJson;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.variables.complex.DynList;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.variables.primitives.DynBoolean;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.variables.primitives.DynNumber;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.variables.primitives.DynString;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.tokenizer.Token;

import java.util.ArrayList;
import java.util.Map;

public class For extends Command {
    private final Token looped;
    private final ArrayList<Command> innerCommands = new ArrayList<>();
    public For(int line, Token looped, String target){
        super(line, CommandType.For,new String[]{looped.toString()},target); // tell the Command class our inputs
        this.looped = looped;
    }

    public void addCommand(Command command){innerCommands.add(command);}

    @Override
    public void run(){
        super.run();
        if (!running) return; // stop execution
        Variable loopedVar = null;
        switch (looped.type()){
            case Boolean -> loopedVar = new DynBoolean((boolean)looped.getValue());
            case Number -> loopedVar = new DynNumber((double)looped.getValue());
            case String -> loopedVar = new DynString((String)looped.getValue());
            case Name -> {
                Variable l = getVar((String)looped.getValue());
                if (l == null) throw new CommandException(line,"For","Variable "+looped.getValue()+" not defined!");
                loopedVar = l;
            }
            default -> throw new CommandException(line,"For","Cannot use "+looped.type()+" as looped value!");
        }
        switch (loopedVar.getType()){
            case List -> {
                ArrayList<Variable> arrayItems = (ArrayList<Variable>)loopedVar.getValue();
                for (Variable item : arrayItems){
                    if (!varExists(super.getOutVarID())) {
                        // god this is so cursed
                        switch (item.getType()) {
                            case Json -> registerVar(new DynJson((Map<Variable, Variable>) item.getValue(), getOutVarID()));
                            case FieldCord -> registerVar(new DynFieldCord((Variable[]) item.getValue(), getOutVarID()));
                            case FieldPos -> registerVar(new DynFieldPos((Variable[]) item.getValue(), getOutVarID()));
                            case List -> registerVar(new DynList((ArrayList<Variable>) item.getValue(), getOutVarID()));
                            case Boolean -> registerVar(new DynBoolean((boolean) item.getValue(), getOutVarID()));
                            case Number -> registerVar(new DynNumber((double) item.getValue(), getOutVarID()));
                            case String -> registerVar(new DynString((String) item.getValue(), getOutVarID()));
                        }
                    } else {
                        getVar(super.getOutVarID()).setVariable(item);
                    }
                    for (Command cmd : innerCommands){
                        if (!running) return; // stop execution
                        cmd.run();
                    }
                }
            }
            case Number -> {
                int Number = (int)((double)loopedVar.getValue());
                for (int i = 0; i < Number; i++){
                    if (!varExists(super.getOutVarID())) {
                        registerVar(new DynNumber(i,super.getOutVarID()));
                    }
                    getVar(super.getOutVarID()).setValue(i);
                    for (Command cmd : innerCommands){
                        if (!running) return; // stop execution
                        cmd.run();
                    }
                }
            }
            case String -> {
                for (char chunk : ((String)loopedVar.getValue()).toCharArray()){
                    if (!varExists(getOutVarID())){registerVar(new DynString("",getOutVarID()));}
                    getVar(super.getOutVarID()).setValue(chunk);
                    for (Command cmd : innerCommands){
                        if (!running) return; // stop execution
                        cmd.run();
                    }
                }
            }
        }
    }
    public Command[] getCommandList(){
        return innerCommands.toArray(new Command[0]);
    }
}
