package org.firstinspires.ftc.teamcode.dynamite.commandSequencer.Commands.math.trig;

import org.firstinspires.ftc.teamcode.dynamite.commandSequencer.Commands.Command;
import org.firstinspires.ftc.teamcode.dynamite.commandSequencer.Commands.CommandType;
import org.firstinspires.ftc.teamcode.dynamite.commandSequencer.Commands.math.MathInCon;
import org.firstinspires.ftc.teamcode.dynamite.commandSequencer.variables.primitives.DynNumber;

public class iSin extends Command {
    private final MathInCon inCon;
    private boolean inIsNum = false;
    private double inNum;
    public iSin(int line, String in, String out){
        super(line, CommandType.iSin, new String[]{in},out);
        inCon = MathInCon.I1O1;
    }
    public iSin(int line, String var){
        super(line, CommandType.iSin, new String[]{var},var);
        inCon = MathInCon.I1;
    }
    public iSin(int line, double in, String out){
        super(line, CommandType.iSin, new String[]{String.valueOf(in)},out);
        inCon = MathInCon.I1O1;
        inIsNum = true;
        inNum = in;
    }

    @Override
    public void run(){
        super.run();
        if (inIsNum){
            getVar(OutVarID).iSin(new DynNumber(inNum));
        } else {
            switch (inCon) {
                case I1O1 -> getVar(OutVarID).iSin(getVar(InVarIDs[0]));
                case I1 -> getVar(OutVarID).iSin();
            }
        }
    }
}
