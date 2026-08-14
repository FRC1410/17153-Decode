package org.firstinspires.ftc.teamcode.dynamite.DYNCore.commandSequencer.Commands.function;

import org.firstinspires.ftc.teamcode.dynamite.DYNCore.commandSequencer.Commands.Command;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.commandSequencer.Commands.CommandType;

import java.util.ArrayList;

public class DynPath extends Command {
    private final ArrayList<Command> commandList = new ArrayList<>();
    public DynPath(int line){
        super(line, CommandType.DynPath,new String[0]);
    }
    public void addCommand(Command cmd){
        commandList.add(cmd);
    }
    @Override
    public void run(){
        super.run();
        for (Command cmd : commandList){
            if (!running) return; // stop execution
            cmd.run();
        }
    }

    public Command[] getCommandList(){
        return commandList.toArray(new Command[0]);
    }
}
