package org.firstinspires.ftc.teamcode.dynamite.DYNCore.commands.math.arithmetic;

import org.firstinspires.ftc.teamcode.dynamite.DYNCore.commands.Command;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.commands.CommandType;

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
