package org.firstinspires.ftc.teamcode.dynamite.DYNCore.commandSequencer.Commands.movement;

import org.firstinspires.ftc.teamcode.dynamite.DYNCore.commandSequencer.CommandException;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.commandSequencer.Commands.Command;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.commandSequencer.Commands.CommandType;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.commandSequencer.variables.Variable;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.commandSequencer.variables.VariableTypes;

public class SetStartPose extends Command {
    public SetStartPose(int line, String varName){
        super(line, CommandType.SetStartPose, new String[]{varName});
    }

    @Override
    public void run(){
        double[] pose;
        Variable pony = getVar(InVarIDs[0]);
        if (pony.getType() == VariableTypes.FieldPos){
            Variable[] varPose = (Variable[])pony.getValue();
            Variable x = varPose[0];
            Variable y = varPose[1];
            Variable h = varPose[2];
            pose = new double[]{
                    (double)x.getValue(),
                    (double)y.getValue(),
                    (double)h.getValue()};
        } else if (pony.getType() == VariableTypes.FieldCord) {
            Variable[] varPose = (Variable[])pony.getValue();
            Variable x = varPose[0];
            Variable y = varPose[1];
            pose = new double[]{
                    (double)x.getValue(),
                    (double)y.getValue()};
        } else {
            throw new CommandException(line,"SetStartPose","Cannot use "+pony.getType()+" for field start location!");
        }
        setStartPose(pose);
    }
}
