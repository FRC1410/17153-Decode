package org.firstinspires.ftc.teamcode.dynamite.commandSequencer.Commands.util;

import org.SquidSquad.commandSequencer.Commands.Command;
import org.SquidSquad.commandSequencer.Commands.CommandType;
import org.SquidSquad.commandSequencer.variables.primitives.DynString;

public class Quit extends Command {
    private boolean literal = true;
    private String varID;
    private Double code;

    public Quit(int line){
        super(line, CommandType.HardQuit,"");
    }
    public Quit(int line, double code){
        super(line, CommandType.HardQuit,new String[]{String.valueOf(code)});
        this.code = code;
    }
    public Quit(int line, String code){
        super(line, CommandType.HardQuit,new String[]{code});
        varID = code;
        literal = false;
    }
    public Quit(int line, DynString varCode){
        super(line, CommandType.HardQuit,new String[]{varCode.toString()});
        varID = (String)varCode.getValue();
    }

    @Override
    public void run(){
        super.run();
        running = false;
        if (code == null){
            if (literal){
                exitCode = varID;
            } else {
                exitCode = getVar(varID).toString();
            }
        } else {
            exitCode = String.valueOf(code);
        }
    }
}