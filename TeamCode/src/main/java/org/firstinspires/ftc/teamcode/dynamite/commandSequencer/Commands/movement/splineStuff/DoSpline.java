package org.firstinspires.ftc.teamcode.dynamite.commandSequencer.Commands.movement.splineStuff;

import org.firstinspires.ftc.teamcode.dynamite.commandSequencer.Commands.Command;
import org.firstinspires.ftc.teamcode.dynamite.commandSequencer.CommandException;
import org.firstinspires.ftc.teamcode.dynamite.commandSequencer.Commands.CommandType;
import org.firstinspires.ftc.teamcode.dynamite.commandSequencer.variables.VariableTypes;

public class DoSpline extends Command {
    private final SplineType type;
    private boolean Literal = false;
    private double[] endPos;
    private String endTanID;
    private Double endTanVal;
    public DoSpline(int line, String Coord, String endTan, SplineType type){
        super (line, CommandType.SplineTo, new String[]{Coord, endTan});
        this.type = type;
        endTanID = endTan;
    }
    public DoSpline(int line, String Coord, double endTan, SplineType type){
        super (line, CommandType.SplineTo, new String[]{Coord, String.valueOf(endTan)});
        this.type = type;
        Literal = true;
        endTanVal = endTan;
    }
    public DoSpline(int line, String Coord, SplineType type){
        super (line, CommandType.SplineTo, new String[]{Coord});
        this.type = type;
    }

    public DoSpline(int line, String Coord, String endTan){
        super (line, CommandType.SplineTo, new String[]{Coord, endTan});
        this.type = SplineType.Normal;
        endTanID = endTan;
    }
    public DoSpline(int line, String Coord, double endTan){
        super (line, CommandType.SplineTo, new String[]{Coord, String.valueOf(endTan)});
        this.type = SplineType.Normal;
        Literal = true;
        endTanVal = endTan;
    }
    public DoSpline(int line, String Coord){
        super (line, CommandType.SplineTo, new String[]{Coord});
        this.type = SplineType.Normal;
    }

    @Override
    public void run(){
        super.run();
        double[] start = getRobotPose.get();
        double[] end = endPos;

        double endTan;
        if (endTanID != null && endTanVal != null) {
            if (Literal) {
                endTan = endTanVal;
            } else {
                if (getVar(endTanID).getType() == VariableTypes.Number) {
                    endTan = (double) getVar(endTanID).getValue();
                } else {
                    throw new CommandException(line,"doSpline","Given variable is not a number!");
                }
            }
        } else {
            endTan = Math.atan2(end[1]-start[1], end[0]-start[0]);
        }

        double[] out = null;
        if (end.length == 2){
            out = new double[]{end[0],end[1],endTan};
        } else if (end.length == 3){
            out = new double[]{end[0],end[1],end[2],endTan};
        }
        doSpline.accept(out,type);
    }
}
