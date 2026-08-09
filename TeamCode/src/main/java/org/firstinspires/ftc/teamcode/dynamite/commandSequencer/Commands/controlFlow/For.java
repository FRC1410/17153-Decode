package org.firstinspires.ftc.teamcode.dynamite.commandSequencer.Commands.controlFlow;

import org.SquidSquad.commandSequencer.CommandException;
import org.SquidSquad.commandSequencer.Commands.Command;
import org.SquidSquad.commandSequencer.Commands.CommandType;
import org.SquidSquad.commandSequencer.variables.Variable;
import org.SquidSquad.commandSequencer.variables.complex.DynFieldCord;
import org.SquidSquad.commandSequencer.variables.complex.DynFieldPos;
import org.SquidSquad.commandSequencer.variables.complex.DynJson;
import org.SquidSquad.commandSequencer.variables.complex.DynList;
import org.SquidSquad.commandSequencer.variables.primitives.DynBoolean;
import org.SquidSquad.commandSequencer.variables.primitives.DynNumber;
import org.SquidSquad.commandSequencer.variables.primitives.DynString;
import org.SquidSquad.Tokenizer.Token;

import java.util.ArrayList;
import java.util.Map;
import java.util.stream.IntStream;

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
