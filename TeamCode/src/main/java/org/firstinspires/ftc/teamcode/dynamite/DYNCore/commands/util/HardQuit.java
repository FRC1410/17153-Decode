package org.firstinspires.ftc.teamcode.dynamite.DYNCore.commands.util;

import org.firstinspires.ftc.teamcode.dynamite.DYNCore.commands.Command;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.commands.CommandType;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.variables.primitives.DynString;

public class HardQuit extends Command {
    private boolean literal = true;
    private String varID;
    private Double code;

    public HardQuit(int line){
        super(line, CommandType.HardQuit,"");
    }
    public HardQuit(int line, double code){
        super(line, CommandType.HardQuit,new String[]{String.valueOf(code)});
        this.code = code;
    }
    public HardQuit(int line, String code){
        super(line, CommandType.HardQuit,new String[]{code});
        varID = code;
        literal = false;
    }
    public HardQuit(int line, DynString varCode){
        super(line, CommandType.HardQuit,new String[]{varCode.toString()});
        varID = (String)varCode.getValue();
    }

    @Override
    public void run(){
        super.run();
        hardStopped = true;
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
