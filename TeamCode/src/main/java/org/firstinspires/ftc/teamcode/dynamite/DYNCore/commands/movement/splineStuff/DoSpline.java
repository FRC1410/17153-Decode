package org.firstinspires.ftc.teamcode.dynamite.DYNCore.commands.movement.splineStuff;

import org.firstinspires.ftc.teamcode.dynamite.DYNCore.commands.Command;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.commands.CommandType;

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
        double[] end = endPos;
        doSpline(end,type);
    }
}
