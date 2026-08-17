package org.firstinspires.ftc.teamcode.dynamite.DYNCore.commands.util;

import org.firstinspires.ftc.teamcode.dynamite.DYNCore.CommandException;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.commands.Command;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.commands.CommandType;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.variables.Variable;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.variables.primitives.DynNumber;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.variables.primitives.DynString;

import java.util.ArrayList;
import java.util.Map;

public class Count extends Command {
    private final boolean isLiteral;
    private final String value;
    public Count(int line, String inVal, String out){
        super(line, CommandType.Count, new String[]{inVal});
        isLiteral = false;
        value = inVal;
    }
    public Count(int line, DynString inVal, String out){
        super(line, CommandType.Count, new String[]{inVal.getValue().toString()});
        isLiteral = true;
        value = inVal.getValue().toString();
    }

    @Override
    public void run(){
        super.run();
        if (!varExists(OutVarID)) registerVar(new DynNumber(0,OutVarID));
        Variable out = getVar(OutVarID);
        if (isLiteral){
            out.setValue(value.length());
        } else {
            Variable in = getVar(value);
            switch (in.getType()){
                case List -> out.setValue(((ArrayList<?>)in.getValue()).size());
                case Json -> out.setValue(((Map<?,?>)in.getValue()).size());
                case String -> out.setValue(((String)in.getValue()).length());
                default -> throw new CommandException(line,"Count","Can only use String/Json/List variables!");
            }
        }
    }
}
