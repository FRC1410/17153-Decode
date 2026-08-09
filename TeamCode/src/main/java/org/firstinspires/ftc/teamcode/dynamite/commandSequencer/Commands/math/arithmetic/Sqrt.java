package org.firstinspires.ftc.teamcode.dynamite.commandSequencer.Commands.math.arithmetic;

import org.SquidSquad.commandSequencer.Commands.Command;
import org.SquidSquad.commandSequencer.Commands.CommandType;
import org.SquidSquad.commandSequencer.Commands.math.MathInCon;
import org.SquidSquad.commandSequencer.variables.primitives.DynNumber;

public class Sqrt extends Command {
    private final MathInCon inCon;
    private boolean inIsNum = false;
    private double inNum;
    public Sqrt(int line, String in, String out){
        super(line, CommandType.Sqrt,new String[]{in},out);
        inCon = MathInCon.I1O1;
    }
    public Sqrt(int line, String var){
        super(line, CommandType.Sqrt, new String[]{var},var);
        inCon = MathInCon.I1;
    }

    public Sqrt(int line, double in, String out) {
        super(line, CommandType.Sqrt, new String[]{String.valueOf(in)}, out);
        inCon = MathInCon.I1O1;
        inIsNum = true;
        inNum = in;
    }

    @Override
    public void run(){
        super.run();
        if (inIsNum){
            // https://media.tenor.com/q6DLf3ymgNAAAAAe/chrollo-chrollo-crying.png
            getVar(OutVarID).Sqrt(new DynNumber(inNum));
        } else {
            switch (inCon) {
                case I1O1 -> getVar(OutVarID).Sqrt(getVar(InVarIDs[0]));
                case I1 -> getVar(OutVarID).Sqrt();
            }
        }
    }
}
