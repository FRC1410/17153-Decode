package org.firstinspires.ftc.teamcode.dynamite.commandSequencer.Commands.random;

import org.SquidSquad.commandSequencer.Commands.Command;
import org.SquidSquad.commandSequencer.Commands.CommandType;
import org.SquidSquad.commandSequencer.variables.primitives.DynNumber;

public class RngFloat extends Command {
    public RngFloat(int line, String var){
        super(line, CommandType.RngFloat, var);
    }
    @Override
    public void run(){
        super.run();
        float value = (float)(Math.random()*2.0 - 1.0);
        if (!varExists(OutVarID)) {
            registerVar(new DynNumber(0,OutVarID));
        }
        getVar(OutVarID).setValue(value);
    }
}
