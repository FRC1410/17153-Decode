package org.firstinspires.ftc.teamcode.dynamite.PPintegration;

import org.firstinspires.ftc.teamcode.dynamite.DynVar.DynVar;
import org.firstinspires.ftc.teamcode.dynamite.DynExceptions.DynAutoStepException;

/**
 * Represents a pose (position + heading) on the FTC field.
 * Position is in inches, heading is in radians.
 */
public class FieldPose {
    private double x;
    private double y;
    private double heading; // radians

    // Primary constructor: accepts heading in radians (matches DYN spec)
    public FieldPose(double x, double y, double headingRadians) {
        this.x = x;
        this.y = y;
        this.heading = headingRadians;
    }

    // Alternate constructor: allow caller to specify degrees if needed
    public FieldPose(double x, double y, double heading, boolean radians) {
        this.x = x;
        this.y = y;
        this.heading = radians ? heading : Math.toRadians(heading);
    }

    public double getX() { return x; }
    public double getY() { return y; }
    public double getHeading() { return heading; }
    public double getHeadingDegrees() { return Math.toDegrees(heading); }

    public void setX(double x) { this.x = x; }
    public void setY(double y) { this.y = y; }
    public void setHeading(double heading) { this.heading = heading; }

    public FieldPoint toPoint() {
        return new FieldPoint(x, y);
    }

    /**
     * Create from a org.firstinspires.ftc.teamcode.dynamite.DynVar.DynVar containing double[] {x, y, heading}.
     */
    public static FieldPose fromDynVar(DynVar var) {
        Object val = var.getValue();
        if (val instanceof double[]) {
            double[] arr = (double[]) val;
            if (arr.length >= 3) {
                return new FieldPose(arr[0], arr[1], arr[2]);
            } else if (arr.length == 2) {
                return new FieldPose(arr[0], arr[1], 0);
            }
        }
        throw new DynAutoStepException("Cannot create FieldPose from org.firstinspires.ftc.teamcode.dynamite.DynVar.DynVar: " + var);
    }

    @Override
    public String toString() {
        return "FieldPose(" + x + ", " + y + ", " + Math.toDegrees(heading) + "°)";
    }
}
