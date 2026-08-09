package org.firstinspires.ftc.teamcode.dynamite.commandSequencer.Commands.math.arithmetic;

import org.SquidSquad.commandSequencer.Commands.Command;
import org.SquidSquad.commandSequencer.Commands.CommandType;

public class Increment extends Command {
    public Increment(int line, String var){
        super(line, CommandType.Increment,new String[]{var});
    }
    @Override
    public void run(){
        super.run();
        getVar(InVarIDs[0]).Inc();
    }
}
