package org.firstinspires.ftc.teamcode.dynamite.DYNCore.commands.telemetry;

import org.firstinspires.ftc.teamcode.dynamite.DYNCore.commands.Command;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.commands.CommandType;

public class Update extends Command {
    public Update(int line){
        super(line, CommandType.Update,new String[0],"");
    }
    @Override
    public void run(){
        super.run();
        updateTelem();
    }
}