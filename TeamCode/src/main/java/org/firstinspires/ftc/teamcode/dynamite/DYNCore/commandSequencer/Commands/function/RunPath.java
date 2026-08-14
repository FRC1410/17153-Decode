package org.firstinspires.ftc.teamcode.dynamite.DYNCore.commandSequencer.Commands.function;

import org.firstinspires.ftc.teamcode.dynamite.DYNCore.commandSequencer.CommandException;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.commandSequencer.Commands.Command;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.commandSequencer.Commands.CommandType;

public class RunPath extends Command {
    public RunPath(int line, String pathID){
        super(line, CommandType.RunPath,new String[0],pathID);
    }
    @Override
    public void run(){
        super.run();
        if (dynPathExists.apply(OutVarID)) {
            runDynPath.accept(OutVarID);
        } else {
            throw new CommandException(line,"RunPath","Path ID: "+OutVarID+" unknown!");
        }
    }
}
