package org.firstinspires.ftc.teamcode.dynamite.DYNCore.commands.random;

import org.firstinspires.ftc.teamcode.dynamite.DYNCore.commands.Command;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.commands.CommandType;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.variables.primitives.DynBoolean;

public class RngBoolean extends Command {
    public RngBoolean(int line, String out){
        super(line, CommandType.RngBoolean,out);
    }
    @Override
    public void run(){
        super.run();
        int decision = (int)(Math.random()*2);
        if (!varExists(OutVarID)) {
            registerVar(new DynBoolean(true,OutVarID));
        }
        if (decision == 0) getVar(OutVarID).setValue(false);
        else getVar(OutVarID).setValue(true);
    }
}
