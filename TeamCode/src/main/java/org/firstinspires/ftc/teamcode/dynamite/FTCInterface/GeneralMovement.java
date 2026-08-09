package org.firstinspires.ftc.teamcode.dynamite.FTCInterface;

import org.firstinspires.ftc.teamcode.dynamite.commandSequencer.CommandException;
import org.firstinspires.ftc.teamcode.dynamite.commandSequencer.variables.complex.DynFieldCord;
import org.firstinspires.ftc.teamcode.dynamite.commandSequencer.variables.complex.DynFieldPos;
import org.firstinspires.ftc.teamcode.dynamite.commandSequencer.variables.primitives.DynNumber;

public class GeneralMovement {
    public static enum moveType {TurnTo,GoTo,Bezier,Spline,SplineLine,SplineSpline}
    private final moveType type;
    private final int line;

    private DynFieldCord[] midPoints;
    private DynFieldCord endCoord;
    private DynFieldPos endPos;
    private DynNumber numArg;

    public GeneralMovement(int line, DynNumber targetHeading){
        this.line = line;
        type = moveType.TurnTo;
        numArg = targetHeading;
    }
    public GeneralMovement(int line, DynFieldPos[] midPoses, DynFieldPos endPos){
        this.line = line;
        type = moveType.Bezier;
    }

    public GeneralMovement(int line, moveType type, DynFieldPos pos){
        if (type == moveType.GoTo || type == moveType.SplineSpline){
            this.line = line;
            this.type = type;
            endPos = pos;
        } else {
            throw new CommandException(line,"Move Robot","Cannot use field position on non GoTo/followSplineSpline commands!");
        }
    }
    public GeneralMovement(int line, moveType type, DynFieldCord coord){
        if (type == moveType.GoTo || type == moveType.Spline || type == moveType.SplineLine){
            this.line = line;
            this.type = type;
            endCoord = coord;
        } else {
            throw new CommandException(line,"Move Robot","Cannot use field coordinate on non GoTo/followSpline/followSplinelinear");
        }
    }
    public GeneralMovement(int line, moveType type, DynNumber num, DynFieldCord coord){
        if (type == moveType.Spline || type == moveType.SplineLine){
            this.line = line;
            this.type = type;
            endCoord = coord;
            numArg = num;
        } else {
            throw new CommandException(line,"Move Robot","Cannot use number and coordinate for non followSpline/followSplineLinear");
        }
    }
    public GeneralMovement(int line, moveType type, DynNumber num, DynFieldPos pos){
        if (type == moveType.SplineLine || type == moveType.SplineSpline){
            this.line = line;
            this.type = type;
            endPos = pos;
            numArg = num;
        } else {
            throw new CommandException(line,"Move Robot","Cannot use number and coordinate for non followSplineLinear/followSplineSpline");
        }
    }
}