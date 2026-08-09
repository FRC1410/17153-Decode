package org.firstinspires.ftc.teamcode.dynamite.commandSequencer.Commands.function;

import org.SquidSquad.commandSequencer.CommandException;
import org.SquidSquad.commandSequencer.Commands.Command;
import org.SquidSquad.commandSequencer.Commands.CommandType;

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
