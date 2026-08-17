package org.firstinspires.ftc.teamcode.dynamite.DYNCore.commandSequencer.Commands.math.trig;

import org.firstinspires.ftc.teamcode.dynamite.DYNCore.commandSequencer.Commands.Command;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.commandSequencer.Commands.CommandType;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.commandSequencer.Commands.math.MathInCon;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.commandSequencer.variables.primitives.DynNumber;

public class ToRad extends Command {
    private final MathInCon inCon;
    private Double inVal = null;
    public ToRad(int line, double inVal, String out){
        super(line, CommandType.toRad, new String[]{String.valueOf(inVal)},out);
        inCon = MathInCon.I1O1;
        this.inVal = inVal;
    }
    public ToRad(int line, String in, String out){
        super(line, CommandType.toRad, new String[]{in},out);
        inCon = MathInCon.I1O1;
    }
    public ToRad(int line, String var){
        super(line, CommandType.toRad, new String[]{var},var);
        inCon = MathInCon.I1;
    }
    @Override
    public void run(){
        super.run();
        switch(inCon){
            case I1O1 -> {
                if (inVal == null) {
                    if (!varExists(OutVarID)) registerVar(new DynNumber(0, OutVarID));
                    getVar(OutVarID).toDeg(getVar(InVarIDs[0]));
                } else {
                    if (!varExists(OutVarID)) registerVar(new DynNumber(0, OutVarID));
                    getVar(OutVarID).toDeg(new DynNumber(inVal));
                }
            }
            case I1 -> {
                if (!varExists(OutVarID)) registerVar(new DynNumber(0,OutVarID));
                getVar(OutVarID).toRad();
            }
        }
    }
}
