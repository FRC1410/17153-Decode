package org.firstinspires.ftc.teamcode.dynamite.DYNCore.commandSequencer.Commands.telemetry;

import org.firstinspires.ftc.teamcode.dynamite.DYNCore.commandSequencer.Commands.Command;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.commandSequencer.Commands.CommandType;

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