package org.firstinspires.ftc.teamcode.dynamite.commandSequencer.Commands.util;

import org.SquidSquad.commandSequencer.CommandException;
import org.SquidSquad.commandSequencer.Commands.Command;
import org.SquidSquad.commandSequencer.Commands.CommandType;
import org.SquidSquad.commandSequencer.variables.Variable;
import org.SquidSquad.commandSequencer.variables.VariableTypes;

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
        // TODO: run the main control loop run @20hz while we wait
        try {
            Thread.sleep(Math.round(tim));
        } catch (InterruptedException e) {
            throw new CommandException(line,"Sleep",e.toString());
        }
    }
}
