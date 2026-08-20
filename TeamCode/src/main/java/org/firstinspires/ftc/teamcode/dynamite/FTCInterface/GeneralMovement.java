package org.firstinspires.ftc.teamcode.dynamite.FTCInterface;

import org.firstinspires.ftc.teamcode.dynamite.DYNCore.commands.movement.splineStuff.SplineType;

public class GeneralMovement {
    public enum moveType {TurnTo,GoTo,Bezier,Spline,SplineLine,SplineSpline}
    public final moveType type;
    public final int line;

    public double heading;
    public double[] target;
    public Double endTan = null;
    public double[][] bezTarget;

    public GeneralMovement(int line, double[] target){
        this.line = line;
        type = moveType.GoTo;
        this.target = target;
    }
    public GeneralMovement(int line, double target){
        this.line = line;
        type = moveType.TurnTo;
        this.heading = target;
    }
    public GeneralMovement(int line, double[] target, SplineType type){
        this.line = line;
        switch (type){
            case Spline -> this.type = moveType.SplineSpline;
            case Linear -> this.type = moveType.SplineLine;
            default -> this.type = moveType.Spline; // for Normal and just to get the "final" requirement met
        }
        this.target = target;
    }
    public GeneralMovement(int line, double[] target, double endTan, SplineType type){
        this.line = line;
        switch (type){
            case Spline -> this.type = moveType.SplineSpline;
            case Linear -> this.type = moveType.SplineLine;
            default -> this.type = moveType.Spline; // for Normal and just to get the "final" requirement met
        }
        this.target = target;
        this.endTan = endTan;
    }
    public GeneralMovement(int line, double[][] target){
        this.line = line;
        type = moveType.Bezier;
        bezTarget = target;
    }
}