package org.firstinspires.ftc.teamcode.dynamite.commandSequencer.Commands.math.trig;

import org.firstinspires.ftc.teamcode.dynamite.commandSequencer.Commands.Command;
import org.firstinspires.ftc.teamcode.dynamite.commandSequencer.Commands.CommandType;
import org.firstinspires.ftc.teamcode.dynamite.commandSequencer.Commands.math.MathInCon;
import org.firstinspires.ftc.teamcode.dynamite.commandSequencer.variables.primitives.DynNumber;

public class iTan extends Command {
    private final MathInCon inCon;
    private boolean inIsNum = false;
    private double inNum;
    public iTan(int line,String in, String out){
        super(line, CommandType.iTan,new String[]{in},out);
        inCon = MathInCon.I1O1;
    }
    public iTan(int line, String var){
        super(line, CommandType.iTan,new String[]{var},var);
        inCon = MathInCon.I1;
    }
    public iTan(int line, double in, String out){
        super(line, CommandType.iTan,new String[]{String.valueOf(in)},out);
        inCon = MathInCon.I1O1;
        inIsNum = true;
        inNum = in;
    }

    @Override
    public void run(){
        super.run();
        if (inIsNum) {
            getVar(OutVarID).iTan(new DynNumber(inNum));
        } else {
            switch (inCon) {
                case I1O1 -> getVar(OutVarID).iTan(getVar(InVarIDs[0]));
                case I1 -> getVar(OutVarID).iTan();
            }
        }
    }
}
