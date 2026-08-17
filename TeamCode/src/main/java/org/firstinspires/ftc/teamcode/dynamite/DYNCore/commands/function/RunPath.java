package org.firstinspires.ftc.teamcode.dynamite.DYNCore.commands.function;

import org.firstinspires.ftc.teamcode.dynamite.DYNCore.CommandException;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.commands.Command;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.commands.CommandType;

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
