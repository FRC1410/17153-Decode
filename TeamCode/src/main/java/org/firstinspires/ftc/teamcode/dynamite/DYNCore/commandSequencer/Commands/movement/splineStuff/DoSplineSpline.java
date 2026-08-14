package org.firstinspires.ftc.teamcode.dynamite.DYNCore.commandSequencer.Commands.movement.splineStuff;

public class DoSplineSpline extends DoSpline {
    public DoSplineSpline(int line, String endTangent, String endPoint) {
        super(line, endPoint, endTangent, SplineType.Spline);
    }
    public DoSplineSpline(int line, double endTangent, String endPoint) {
        super(line, endPoint, endTangent, SplineType.Spline);
    }
    public DoSplineSpline(int line, String endPoint) {
        super(line, endPoint, SplineType.Spline);
    }
}
