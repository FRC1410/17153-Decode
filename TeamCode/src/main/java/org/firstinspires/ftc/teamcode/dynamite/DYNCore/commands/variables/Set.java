package org.firstinspires.ftc.teamcode.dynamite.DYNCore.commands.variables;

import org.firstinspires.ftc.teamcode.dynamite.DYNCore.CommandException;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.commands.Command;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.commands.CommandType;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.variables.Variable;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.variables.VariableTypes;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.variables.primitives.DynBoolean;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.variables.primitives.DynNumber;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.variables.primitives.DynString;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.tokenizer.Token;

public class Set extends Command{
    private final Token InDex;
    private final Token in;
    public Set(int line, Token InDex, Token in, String outVar){
        // we take in tokens for 2 reasons:
        // 1. I don't want to work on this too much.
        // 2. It works, and the logic is dead simple.
        // 3. It means that I don't have to make a million constructors. (haha get pranked)
        super(line, CommandType.Set, new String[]{InDex.getValue().toString(),in.getValue().toString()},outVar);
        this.InDex = InDex;
        this.in = in;
    }

    @Override
    public void run(){
        super.run();
        Variable target = getVar(OutVarID);
        if (target == null){
            throw new CommandException(line,"Set","Variable "+OutVarID+" is not defined!");
        }
        else {
            VariableTypes targetType = target.getType();
            if (targetType != VariableTypes.FieldCord &&
                targetType != VariableTypes.FieldPos &&
                targetType != VariableTypes.List &&
                targetType != VariableTypes.Json){
                throw new CommandException(line,"Set","Cannot use non FieldCord/FieldPos/List/Json for this operation!");
            }
        }
        VariableTypes targetType = target.getType();
        switch (InDex.type()){
            case Boolean -> {
                if (targetType != VariableTypes.Json) throw new CommandException(line,"Set","Cannot use boolean as non Json index!");
                switch (in.type()){
                    case Boolean -> {
                        boolean index = (boolean)InDex.getValue();
                        boolean inVal = (boolean)in.getValue();
                        target.set(new DynBoolean(index), new DynBoolean(inVal));
                    }
                    case Number -> {
                        boolean index = (boolean)InDex.getValue();
                        double inVal = (double)in.getValue();
                        target.set(new DynBoolean(index), new DynNumber(inVal));
                    }
                    case String -> {
                        boolean index = (boolean)InDex.getValue();
                        String inVal = (String)in.getValue();
                        target.set(new DynBoolean(index), new DynString(inVal));
                    }
                    case Name -> {
                        boolean index = (boolean)InDex.getValue();
                        Variable inVal = getVar((String)in.getValue());
                        if (inVal == null) throw new CommandException(line,"Set","Variable "+in.getValue()+" not defined!");
                        target.set(new DynBoolean(index), inVal);
                    }
                    default -> throw new CommandException(line,"Set","Expected a boolean/number/string/name | Got: "+in.type());
                }
            }
            case Number -> {
                switch (in.type()){
                    case Boolean -> {
                        int index = (int)Math.floor((double)InDex.getValue());
                        boolean inVal = (boolean)in.getValue();
                        target.set(index, new DynBoolean(inVal));
                    }
                    case Number -> {
                        int index = (int)Math.floor((double)InDex.getValue());
                        double inVal = (double)in.getValue();
                        target.set(index, new DynNumber(inVal));
                    }
                    case String -> {
                        int index = (int)Math.floor((double)InDex.getValue());
                        String inVal = (String)in.getValue();
                        target.set(index, new DynString(inVal));
                    }
                    case Name -> {
                        int index = (int)Math.floor((double)InDex.getValue());
                        Variable inVal = getVar((String)in.getValue());
                        if (inVal == null) throw new CommandException(line,"Set","Variable "+in.getValue()+" not defined!");
                        target.set(index, inVal);
                    }
                    default -> throw new CommandException(line,"Set","Expected a boolean/number/string/name | Got: "+in.type());
                }
            }
            case String -> {
                if (targetType == VariableTypes.List) throw new CommandException(line,"Set","Cannot use string as List index!");
                switch (in.type()){
                    case Boolean -> {
                        String index = (String)InDex.getValue();
                        boolean inVal = (boolean)in.getValue();
                        target.set(new DynString(index), new DynBoolean(inVal));
                    }
                    case Number -> {
                        String index = (String)InDex.getValue();
                        double inVal = (double)in.getValue();
                        target.set(new DynString(index), new DynNumber(inVal));
                    }
                    case String -> {
                        String index = (String)InDex.getValue();
                        String inVal = (String)in.getValue();
                        target.set(new DynString(index), new DynString(inVal));
                    }
                    case Name -> {
                        String index = (String)InDex.getValue();
                        Variable inVal = getVar((String)in.getValue());
                        if (inVal == null) throw new CommandException(line,"Set","Variable "+in.getValue()+" not defined!");
                        target.set(new DynString(index), inVal);
                    }
                    default -> throw new CommandException(line,"Set","Expected a boolean/number/string/name | Got: "+in.type());
                }
            }
            case Name -> {
                switch (in.type()){
                    case Boolean -> {
                        Variable index = getVar((String)InDex.getValue());
                        if (index == null) throw new CommandException(line,"Set","Variable "+InDex.getValue()+" not defined!");
                        boolean inVal = (boolean)in.getValue();
                        target.set(index, new DynBoolean(inVal));
                    }
                    case Number -> {
                        Variable index = getVar((String)InDex.getValue());
                        if (index == null) throw new CommandException(line,"Set","Variable "+InDex.getValue()+" not defined!");
                        double inVal = (double)in.getValue();
                        target.set(index, new DynNumber(inVal));
                    }
                    case String -> {
                        Variable index = getVar((String)InDex.getValue());
                        if (index == null) throw new CommandException(line,"Set","Variable "+InDex.getValue()+" not defined!");
                        String inVal = (String)in.getValue();
                        target.set(index, new DynString(inVal));
                    }
                    case Name -> {
                        Variable index = getVar((String)InDex.getValue());
                        if (index == null) throw new CommandException(line,"Set","Variable "+InDex.getValue()+" not defined!");
                        Variable inVal = getVar((String)in.getValue());
                        target.set(index, inVal);
                    }
                    default -> throw new CommandException(line,"Set","Expected a boolean/number/string/name | Got: "+in.type());
                }
            }
            default -> throw new CommandException(line,"Set","Expected a boolean/number/string/name | Got: "+InDex.type());
        }
    }
}
