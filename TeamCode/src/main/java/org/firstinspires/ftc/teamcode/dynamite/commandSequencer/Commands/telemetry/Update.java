package org.firstinspires.ftc.teamcode.dynamite.commandSequencer.Commands.telemetry;

import org.firstinspires.ftc.teamcode.dynamite.commandSequencer.Commands.Command;
import org.firstinspires.ftc.teamcode.dynamite.commandSequencer.Commands.CommandType;

import java.io.IOException;

public class Update extends Command {
    public Update(int line){
        super(line, CommandType.Clear,new String[0],"");
    }
    @Override
    public void run(){
        super.run();
        // clear the terminal for UNIX system (I don't care about windows)
        // Source - https://stackoverflow.com/a/33379766
        // Retrieved 2026-08-03, License - CC BY-SA 3.0
        try {
            Runtime.getRuntime().exec("cls");
        } catch (IOException e) {
            try {
                Runtime.getRuntime().exec("clear");
            } catch (IOException ex) {
                System.out.println("Unable to clear CLI");
            }
        }

        for (String telemChunk : telemBuffer){
            System.out.println("DYN@"+telemChunk);
        }
    }
}
