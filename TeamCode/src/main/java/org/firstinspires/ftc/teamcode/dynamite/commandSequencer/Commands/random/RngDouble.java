package org.firstinspires.ftc.teamcode.dynamite.commandSequencer.Commands.random;

import org.firstinspires.ftc.teamcode.dynamite.commandSequencer.CommandException;
import org.firstinspires.ftc.teamcode.dynamite.commandSequencer.Commands.Command;
import org.firstinspires.ftc.teamcode.dynamite.commandSequencer.Commands.CommandType;
import org.firstinspires.ftc.teamcode.dynamite.commandSequencer.variables.Variable;
import org.firstinspires.ftc.teamcode.dynamite.commandSequencer.variables.VariableTypes;
import org.firstinspires.ftc.teamcode.dynamite.commandSequencer.variables.primitives.DynNumber;

public class RngDouble extends Command {
    private double min;
    private double max;
    private String MIN = null;
    private String MAX = null;
    public RngDouble(int line, double min, double max, String var){
        super(line, CommandType.RngDouble,new String[]{String.valueOf(min),String.valueOf(max)},var);
        this.min = min;
        this.max = max;
    }
    public RngDouble(int line, double min, String max, String var){
        super(line, CommandType.RngDouble, new String[]{String.valueOf(min),max},var);
        this.min = min;
        MAX = max;
    }
    public RngDouble(int line, String min, double max, String var){
        super(line, CommandType.RngDouble, new String[]{min,String.valueOf(max)},var);
        MIN = min;
        this.max = max;
    }
    public RngDouble(int line, String min, String max, String var){
        super(line, CommandType.RngDouble, new String[]{min,max},var);
        MIN = min;
        MAX = max;
    }

    @Override
    public void run(){
        super.run();
        if (MIN != null){
            Variable Min = getVar(MIN);
            if (Min.getType() == VariableTypes.Number) min = (int)Math.floor((double)Min.getValue());
            else throw new CommandException(line,"RngInteger","Expected number variable, got: "+Min.getType());
        }
        if (MAX != null){
            Variable Max = getVar(MAX);
            if (Max.getType() == VariableTypes.Number) max = (int)Math.floor((double)Max.getValue());
            else throw new CommandException(line,"RngInteger","Expected number variable, got: "+Max.getType());
        }
        double value = min+Math.random()*(max-min);
        if (!varExists(OutVarID)) {
            registerVar(new DynNumber(0,OutVarID));
        }
        getVar(OutVarID).setValue(value);
    }
}
