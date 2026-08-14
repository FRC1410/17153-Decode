package org.firstinspires.ftc.teamcode.dynamite.DYNCore.commandSequencer.Commands.util;

import org.firstinspires.ftc.teamcode.dynamite.DYNCore.commandSequencer.CommandException;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.commandSequencer.Commands.Command;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.commandSequencer.Commands.CommandType;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.commandSequencer.variables.Variable;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.commandSequencer.variables.VariableTypes;

public class Sleep extends Command{
    Double time = null;
    String timeVar = null;
    public Sleep(int line, double time){
        super(line, CommandType.Sleep, new String[]{String.valueOf(time)});
        this.time = time;
    }
    public Sleep(int line, String timeVar){
        super(line, CommandType.Sleep, new String[]{timeVar});
        this.timeVar = timeVar;
    }

    @Override
    public void run(){
        super.run();
        double tim;
        if (time == null){
            Variable timmy = getVar(timeVar);
            if (timmy.getType() == VariableTypes.Number){
                tim = (double)timmy.getValue();
            } else {
                throw new CommandException(line,"Sleep","Cannot use non number as sleep time!");
            }
        } else {
            tim = time;
        }
        DYNSleep(Math.round(tim));
    }
}
