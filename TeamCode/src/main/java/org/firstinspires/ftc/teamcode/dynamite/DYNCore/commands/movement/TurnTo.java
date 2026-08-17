package org.firstinspires.ftc.teamcode.dynamite.DYNCore.commands.movement;

import org.firstinspires.ftc.teamcode.dynamite.DYNCore.commands.Command;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.CommandException;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.commands.CommandType;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.variables.VariableTypes;

public class TurnTo extends Command {
    private final boolean Literal;
    private double angle;
    public TurnTo(int line, String var){
        super(line, CommandType.TurnTo, new String[]{var});
        Literal = false;
    }
    public TurnTo(int line, double value){
        super(line, CommandType.TurnTo, new String[]{String.valueOf(value)});
        Literal = true;
        angle = value;
    }

    @Override
    public void run(){
        super.run();
        if (Literal){
            turnTo(angle);
        } else {
            if (getVar(InVarIDs[0]).getType() == VariableTypes.Number){
                turnTo((double)getVar(InVarIDs[0]).getValue());
            } else {
                throw new CommandException(line, "Turn To", "cannot use variable type "+getVar(InVarIDs[0]).getType().toString()+" as angle to go to.");
            }
        }
    }
}
