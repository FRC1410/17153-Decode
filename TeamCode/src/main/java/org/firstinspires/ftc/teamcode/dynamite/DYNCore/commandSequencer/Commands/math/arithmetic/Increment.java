package org.firstinspires.ftc.teamcode.dynamite.DYNCore.commandSequencer.Commands.math.arithmetic;

import org.firstinspires.ftc.teamcode.dynamite.DYNCore.commandSequencer.Commands.Command;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.commandSequencer.Commands.CommandType;

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
