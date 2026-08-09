package org.firstinspires.ftc.teamcode.dynamite.commandSequencer.Commands.variables;

import org.SquidSquad.commandSequencer.CommandException;
import org.SquidSquad.commandSequencer.Commands.Command;
import org.SquidSquad.commandSequencer.Commands.CommandType;
import org.SquidSquad.commandSequencer.variables.Variable;
import org.SquidSquad.commandSequencer.variables.VariableTypes;
import org.SquidSquad.commandSequencer.variables.primitives.DynBoolean;
import org.SquidSquad.commandSequencer.variables.primitives.DynNumber;
import org.SquidSquad.commandSequencer.variables.primitives.DynString;
import org.SquidSquad.Tokenizer.Token;

public class Insert extends Command{
    private final String target;
    private final Token InDex;
    private final Token in;
    public Insert(int line, Token in, Token InDex, String target){
        super(line,CommandType.Insert,new String[]{String.valueOf(in.getValue()),String.valueOf(InDex.getValue())},target);
        this.target = target;
        this.InDex = InDex;
        this.in = in;
    }

    public void run(){
        super.run();
        Variable target = getVar(OutVarID);
        if (target == null){
            throw new CommandException(line,"Insert","Variable "+OutVarID+" is not defined!");
        }
        else {
            VariableTypes targetType = target.getType();
            if (targetType != VariableTypes.FieldCord &&
                targetType != VariableTypes.FieldPos &&
                targetType != VariableTypes.List &&
                targetType != VariableTypes.Json){
                throw new CommandException(line,"Insert","Cannot use non FieldCord/FieldPos/List/Json for this operation!");
            }
        }
        Variable id;
        switch (InDex.type()){
            case Boolean -> id = new DynBoolean((boolean)InDex.getValue());
            case Number -> id = new DynNumber((double)InDex.getValue());
            case String -> id = new DynString((String)InDex.getValue());
            case Name -> {
                Variable ID = getVar((String)InDex.getValue());
                if (ID == null) throw new CommandException(line,"Insert","Variable "+InDex.getValue()+" not defined!");
                id = ID;
            }
            default -> throw new CommandException(line,"Insert","Expected a boolean/number/string/name | Got: "+InDex.type());
        }
        switch (in.type()){
            case Boolean -> target.insertVar(new DynBoolean((boolean)in.getValue()),id);
            case Number -> target.insertVar(new DynNumber((double)in.getValue()),id);
            case String -> target.insertVar(new DynString((String)in.getValue()),id);
            case Name -> {
                Variable in = getVar((String)this.in.getValue());
                if (in == null) throw new CommandException(line,"Insert","Variable "+InDex+" not defined!");
                target.insertVar(in,id);
            }
            default -> throw new CommandException(line,"Insert","Expected a boolean/number/string/name | Got: "+in.type());
        }
    }
}
