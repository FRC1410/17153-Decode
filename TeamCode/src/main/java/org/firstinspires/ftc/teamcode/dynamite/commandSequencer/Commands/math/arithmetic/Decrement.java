package org.firstinspires.ftc.teamcode.dynamite.commandSequencer.Commands.math.arithmetic;

import org.SquidSquad.commandSequencer.Commands.Command;
import org.SquidSquad.commandSequencer.Commands.CommandType;

public class Decrement extends Command {
    public Decrement(int line, String var){
        super(line, CommandType.Decrement,new String[]{var});
    }
    @Override
    public void run(){
        super.run();
        getVar(super.InVarIDs[0]).Dec();
    }
}
