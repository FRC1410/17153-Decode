package org.firstinspires.ftc.teamcode.dynamite.DYNCore.commands.math.trig;

import org.firstinspires.ftc.teamcode.dynamite.DYNCore.commands.Command;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.commands.CommandType;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.commands.math.MathInCon;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.variables.primitives.DynNumber;

public class Cos extends Command {
    private final MathInCon inCon;
    private boolean inIsNum = false;
    private double inNum;
    public Cos(int line, String in, String out){
        super(line, CommandType.Cos,new String[]{in},out);
        inCon = MathInCon.I1O1;
    }
    public Cos(int line, String var){
        super(line, CommandType.Cos,new String[]{var},var);
        inCon = MathInCon.I1;
    }
    public Cos(int line, double in, String out){
        super(line, CommandType.Cos,new String[]{String.valueOf(in)},out);
        inCon = MathInCon.I1O1;
        inIsNum = true;
        inNum = in;
    }

    @Override
    public void run(){
        super.run();
        if (inIsNum){
            getVar(OutVarID).Cos(new DynNumber(inNum));
        } else {
            switch (inCon) {
                case I1O1 -> getVar(OutVarID).Cos(getVar(InVarIDs[0]));
                case I1 -> getVar(OutVarID).Cos();
            }
        }
    }
}
