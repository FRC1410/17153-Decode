package org.firstinspires.ftc.teamcode.dynamite.DYNCore.commandSequencer.Commands.variables;

import org.firstinspires.ftc.teamcode.dynamite.DYNCore.commandSequencer.CommandException;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.commandSequencer.Commands.Command;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.commandSequencer.Commands.CommandType;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.commandSequencer.variables.Variable;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.commandSequencer.variables.VariableTypes;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.commandSequencer.variables.primitives.DynBoolean;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.commandSequencer.variables.primitives.DynNumber;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.commandSequencer.variables.primitives.DynString;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.tokenizer.Token;

public class Append extends Command{
    private final Token inTk;
    private Token idx = null;
    public Append(int line, Token in, String out){
        super(line, CommandType.Append, new String[]{String.valueOf(in.getValue())},out);
        inTk = in;
    }
    public Append(int line, Token in, Token InDex, String out){
        super(line, CommandType.Append, new String[]{String.valueOf(in.getValue()),String.valueOf(InDex.getValue())},out);
        inTk = in;
        idx = InDex;
    }

    public void run(){
        super.run();
        Variable target = getVar(OutVarID);
        if (target == null){
            throw new CommandException(line,"Append","Variable "+OutVarID+" is not defined!");
        }
        else {
            VariableTypes targetType = target.getType();
            if (targetType != VariableTypes.FieldCord &&
                targetType != VariableTypes.FieldPos &&
                targetType != VariableTypes.List &&
                targetType != VariableTypes.Json){
                throw new CommandException(line,"Append","Cannot use non FieldCord/FieldPos/List/Json for this operation!");
            }
        }
        if (idx == null){
            switch (inTk.type()){
                case Boolean -> target.append(new DynBoolean((boolean)inTk.getValue()));
                case Number -> target.append(new DynNumber((double)inTk.getValue()));
                case String -> target.append(new DynString((String)inTk.getValue()));
                case Name -> {
                    Variable in = getVar((String)inTk.getValue());
                    if (in == null) throw new CommandException(line,"Append","Variable "+inTk.getValue()+" not defined!");
                    target.append(in);
                }
                default -> throw new CommandException(line,"Append","Expected a boolean/number/string/name | Got: "+inTk.type());
            }
        } else {
            Variable id;
            switch (idx.type()){
                case Boolean -> id = new DynBoolean((boolean)idx.getValue());
                case Number -> id = new DynNumber((double)idx.getValue());
                case String -> id = new DynString((String)idx.getValue());
                case Name -> {
                    Variable in = getVar((String)idx.getValue());
                    if (in == null) throw new CommandException(line,"Append","Variable "+idx.getValue()+" not defined!");
                    id = in;
                }
                default -> throw new CommandException(line,"Append","Cannot use "+idx.type()+" as index/id!");
            }
            switch (inTk.type()){
                case Boolean -> target.append(new DynBoolean((boolean)inTk.getValue()),id);
                case Number -> target.append(new DynNumber((double)inTk.getValue()),id);
                case String -> target.append(new DynString((String)inTk.getValue()),id);
                case Name -> {
                    Variable in = getVar((String)inTk.getValue());
                    if (in == null) throw new CommandException(line,"Append","Variable "+inTk.getValue()+" not defined!");
                    target.append(in,id);
                }
                default -> throw new CommandException(line,"Append","Expected a boolean/number/string/name | Got: "+idx.type());
            }
        }
    }
}
