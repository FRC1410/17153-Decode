package org.firstinspires.ftc.teamcode.dynamite.DYNCore.commands.math.trig;

import org.firstinspires.ftc.teamcode.dynamite.DYNCore.commands.Command;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.commands.CommandType;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.commands.math.MathInCon;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.variables.primitives.DynNumber;

public class iCos extends Command {
    private final MathInCon inCon;
    private boolean inIsNum = false;
    private double inNum;
    public iCos(int line, String in, String out){
        super(line, CommandType.iCos,new String[]{in},out);
        inCon = MathInCon.I1O1;
    }
    public iCos(int line, String in){
        super(line, CommandType.iCos, new String[]{in},in);
        inCon = MathInCon.I1;
    }
    public iCos(int line, double in, String out){
        super(line, CommandType.iCos, new String[]{String.valueOf(in)},out);
        inCon = MathInCon.I1O1;
        inIsNum = true;
        inNum = in;
    }

    @Override
    public void run(){
        super.run();
        if (inIsNum){
            getVar(OutVarID).iCos(new DynNumber(inNum));
        } else {
            switch (inCon) {
                case I1O1 -> getVar(OutVarID).iCos(getVar(InVarIDs[0]));
                case I1 -> getVar(OutVarID).iCos();
            }
        }
    }
}
