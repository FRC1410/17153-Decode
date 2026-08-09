package org.firstinspires.ftc.teamcode.dynamite.commandSequencer.Commands.movement.splineStuff;

public class DoSplineLinear extends DoSpline {
    public DoSplineLinear(int line, String endTangent, String endPoint) {
        super(line, endPoint, endTangent, SplineType.Linear);
    }
    public DoSplineLinear(int line, double endTangent, String endPoint) {
        super(line, endPoint, endTangent, SplineType.Linear);
    }
    public DoSplineLinear(int line, String endPoint) {
        super(line, endPoint, SplineType.Linear);
    }
}
