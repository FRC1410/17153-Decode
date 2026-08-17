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

public class Remove extends Command{
    private final String target;
    private final Token InDex;
    private String out = null;
    public Remove(int line, Token InDex, String target){
        super(line, CommandType.RngDouble, new String[]{InDex.toString(),target});
        this.target = target;
        this.InDex = InDex;
    }
    public Remove(int line, Token InDex, String target, String out){
        super(line, CommandType.RngDouble, new String[]{InDex.toString(),target});
        this.target = target;
        this.InDex = InDex;
        this.out = out;
    }

    public void run(){
        super.run();
        Variable tg = getVar(target);
        VariableTypes targetType = tg.getType();
        if (targetType != VariableTypes.FieldCord &&
                targetType != VariableTypes.FieldPos &&
                targetType != VariableTypes.List &&
                targetType != VariableTypes.Json){
            throw new CommandException(line,"Remove","Cannot use non FieldCord/FieldPos/List/Json for this operation!");
        }
        if (out == null){
            switch (InDex.type()){
                case Boolean -> tg.remove(new DynBoolean((boolean)InDex.getValue()));
                case Number -> tg.remove(new DynNumber((double)InDex.getValue()));
                case String -> tg.remove(new DynString((String)InDex.getValue()));
                case Name -> {
                    Variable name = getVar((String)InDex.getValue());
                    if (name == null) throw new CommandException(line,"Remove","Variable "+InDex.getValue()+" not defined!");
                    tg.remove(name);
                }
                default -> throw new CommandException(line,"Remove","Expected a boolean/number/string/name | Got: "+InDex.type());
            }
        } else {
            if (!varExists(out)){
                registerVar(new DynNumber(0,out));
            }
            Variable outIe = getVar(out);
            switch (InDex.type()){
                case Boolean -> {
                    Variable id = new DynBoolean((boolean)InDex.getValue());
                    outIe.set2get(tg,id);
                    tg.remove(id);
                }
                case Number -> {
                    if (tg.getType() == VariableTypes.Json){
                        Variable id = new DynNumber((double)InDex.getValue());
                        outIe.set2get(tg,id);
                        tg.remove(id);
                    } else {
                        int id = (int)Math.floor((double)InDex.getValue());
                        outIe.set2get(tg,id);
                        tg.remove(id);
                    }
                }
                case String -> {
                    Variable id = new DynString((String)InDex.getValue());
                    outIe.set2get(tg,id);
                    tg.remove(id);
                }
                case Name -> {
                    Variable name = getVar((String)InDex.getValue());
                    if (name == null) throw new CommandException(line,"Remove","Variable "+InDex.getValue()+" not defined!");
                    outIe.set2get(tg,name);
                    tg.remove(name);
                }
                default -> throw new CommandException(line,"Remove","Expected a boolean/number/string/name | Got: "+InDex.type());
            }
        }
    }
}
