package org.firstinspires.ftc.teamcode.dynamite.commandSequencer.Commands.math.arithmetic;

import org.SquidSquad.commandSequencer.Commands.Command;
import org.SquidSquad.commandSequencer.Commands.CommandType;
import org.SquidSquad.commandSequencer.Commands.math.MathInCon;
import org.SquidSquad.commandSequencer.variables.primitives.DynNumber;

public class Div extends Command {
    private final MathInCon inCon;
    private Double in1 = null;
    private Double in2 = null;
    public Div(int line, String in1, String in2, String out){
        super(line, CommandType.Div,new String[]{in1,in2},out);
        inCon = MathInCon.I2O1;
    }
    public Div(int line, String in1, double in2, String out){
        super(line, CommandType.Div,new String[]{in1,String.valueOf(in2)},out);
        inCon = MathInCon.I2O1;
        this.in2 = in2;
    }
    public Div(int line, double in1, String in2, String out){
        super(line, CommandType.Div,new String[]{String.valueOf(in2)},out);
        inCon = MathInCon.I2O1;
        this.in2 = in1;
    }
    public Div(int line, double in1, double in2, String out){
        super(line, CommandType.Div, new String[]{String.valueOf(in1),String.valueOf(in2)},out);
        inCon = MathInCon.I2O1;
        this.in2 = in1;
        this.in1 = in1;
    }
    public Div(int line, String in1, String in2){
        super(line, CommandType.Div,new String[]{in1,in2},in1);
        inCon = MathInCon.I2;
    }
    public Div(int line, double in1, String in2){
        super(line, CommandType.Div,new String[]{String.valueOf(in1),in2},in2);
        inCon = MathInCon.I2;
        this.in1 = in1;
    }
    public Div(int line, String in1, double in2){
        super(line, CommandType.Div,new String[]{in1,String.valueOf(in2)},in1);
        inCon = MathInCon.I2;
        this.in2 = in2;
    }

    @Override
    public void run(){
        super.run();
        if (in1 != null || in2 != null){
            if (in1 != null && in2 != null){
                getVar(OutVarID).Div(
                        new DynNumber(in1),
                        new DynNumber(in2));
            } else if (in1 != null){
                switch (inCon) {
                    case I1O1 -> getVar(OutVarID).Div(
                            new DynNumber(in1),
                            getVar(InVarIDs[1]));
                    case I2 -> getVar(OutVarID).Div(
                            new DynNumber(in1));
                }
            } else {
                switch (inCon) {
                    case I1O1 -> getVar(OutVarID).Div(
                            getVar(InVarIDs[0]),
                            new DynNumber(in2));
                    case I2 -> getVar(OutVarID).Div(
                            new DynNumber(in2));
                }
            }
        } else {
            switch (inCon) {
                case I2O1 ->
                        getVar(getOutVarID()).Div(getVar(InVarIDs[0]), getVar(InVarIDs[1]));
                case I2 -> getVar(getOutVarID()).Div(getVar(InVarIDs[1]));
            }
        }
    }
}
