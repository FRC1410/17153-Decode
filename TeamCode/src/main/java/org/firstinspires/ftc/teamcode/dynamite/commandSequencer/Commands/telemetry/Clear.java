package org.firstinspires.ftc.teamcode.dynamite.commandSequencer.Commands.telemetry;

import org.firstinspires.ftc.teamcode.dynamite.commandSequencer.Commands.Command;
import org.firstinspires.ftc.teamcode.dynamite.commandSequencer.Commands.CommandType;

public class Clear extends Command {
    public Clear(int line){
        super(line, CommandType.Clear, new String[0],"");
    }
    @Override
    public void run(){
        super.run();
        telemBuffer.clear();
    }
}
