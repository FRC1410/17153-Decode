package org.firstinspires.ftc.teamcode.dynamite.DYNCore.commands.math.trig;

import org.firstinspires.ftc.teamcode.dynamite.DYNCore.commands.Command;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.commands.CommandType;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.commands.math.MathInCon;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.variables.primitives.DynNumber;

public class Sin extends Command {
    private final MathInCon inCon;
    private boolean inIsNum = false;
    private double inNum;
    public Sin(int line,String in, String out){
        super(line, CommandType.Sin,new String[]{in},out);
        inCon = MathInCon.I1O1;
    }
    public Sin(int line,String var){
        super(line, CommandType.Sin, new String[]{var},var);
        inCon = MathInCon.I1;
    }
    public Sin(int line, double in, String out){
        super(line, CommandType.Sin, new String[]{String.valueOf(in)},out);
        inCon = MathInCon.I1O1;
        inIsNum = true;
        inNum = in;
    }

    @Override
    public void run(){
        super.run();
        if (inIsNum){
            getVar(OutVarID).Sin(new DynNumber(inNum));
        } else {
            switch (inCon) {
                case I1O1 -> getVar(OutVarID).Sin(getVar(InVarIDs[0]));
                case I1 -> getVar(OutVarID).Sin();
            }
        }
    }
}
