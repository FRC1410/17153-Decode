package org.firstinspires.ftc.teamcode.dynamite.DYNCore.commands.random;

import org.firstinspires.ftc.teamcode.dynamite.DYNCore.CommandException;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.commands.Command;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.commands.CommandType;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.variables.Variable;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.variables.VariableTypes;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.variables.primitives.DynNumber;

public class RngInteger extends Command {
    int min;
    int max;
    String MIN;
    String MAX;
    public RngInteger(int line, double min, double max, String var){
        super(line, CommandType.RngInteger, var);
        int newMin = (int)min;
        int newMax = (int)max;
        this.min = newMin;
        this.max = newMax;
    }
    public RngInteger(int line, double min, String max, String var){
        super(line, CommandType.RngInteger, var);
        MAX = max;
        this.min = (int)min;
    }
    public RngInteger(int line, String min, double max, String var){
        super(line, CommandType.RngInteger, var);
        MIN = min;
        this.max = (int)max;
    }
    public RngInteger(int line, String min, String max, String var){
        super(line, CommandType.RngInteger, var);
        MIN = min;
        MAX = max;
    }
    @Override
    public void run(){
        super.run();
        int maxBound;
        int minBound;
        if (MAX == null){
            if (MIN == null){
                maxBound = max;
                minBound = min;
            } else {
                maxBound = max;
                Variable var = getVar(MIN);
                if (var.getType() != VariableTypes.Number) throw new CommandException(line,"RngInteger","Cannot use non number value as random bound!");
                minBound = (int)((double)var.getValue());
            }
        } else {
            if (MIN == null){
                Variable var1 = getVar(MAX);
                if (var1.getType() != VariableTypes.Number) throw new CommandException(line,"RngInteger","Cannot use non number value as random bound!");
                maxBound = (int)((double)var1.getValue());
                minBound = min;
            } else {
                Variable var1 = getVar(MAX);
                if (var1.getType() != VariableTypes.Number) throw new CommandException(line,"RngInteger","Cannot use non number value as random bound!");
                maxBound = (int)((double)var1.getValue());
                Variable var = getVar(MIN);
                if (var.getType() != VariableTypes.Number) throw new CommandException(line,"RngInteger","Cannot use non number value as random bound!");
                minBound = (int)((double)var.getValue());
            }
        }
        int value = (int)(Math.random()*(maxBound-minBound+1))+minBound;
        if (!varExists(OutVarID)) {
            registerVar(new DynNumber(0,OutVarID));
        }
        getVar(OutVarID).setValue(value);
    }
}
