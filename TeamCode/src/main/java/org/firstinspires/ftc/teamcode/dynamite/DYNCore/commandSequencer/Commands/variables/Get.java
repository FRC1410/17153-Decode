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

public class Get extends Command{
    private final String in;
    private final String out;
    private final Token InDex;
    public Get(int line, String in, Token InDex, String out){
        super(line, CommandType.Get, new String[]{in,String.valueOf(InDex.getValue())},out);
        this.in = in;
        this.out = out;
        this.InDex = InDex;
    }

    public void run(){
        super.run();
        Variable target = getVar(in);
        if (target == null){
            throw new CommandException(line,"Set","Variable "+in+" is not defined!");
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
        if (!varExists(this.out)) {
            registerVar(new DynNumber(0,this.out));
        }
        Variable out = getVar(this.out);
        switch(InDex.type()){
            case Boolean -> out.set2get(target,new DynBoolean((boolean)InDex.getValue()));
            case Number -> out.set2get(target,new DynNumber((double)InDex.getValue()));
            case String -> out.set2get(target,new DynString((String)InDex.getValue()));
            case Name -> {
                Variable id = getVar((String)InDex.getValue());
                if (id == null) throw new CommandException(line,"Get","Variable "+InDex.getValue()+" not defined!");
                out.set2get(target,id);
            }
            default -> throw new CommandException(line,"Get","Expected a boolean/number/string/name | Got: "+InDex.type());
        }
    }
}
