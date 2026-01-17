package org.firstinspires.ftc.teamcode.dynamite.PPintegration;

/**
 * Represents a point on the FTC field.
 * Coordinates are in inches from field center.
 */
public class FieldPoint {
    private double x;
    private double y;

    public FieldPoint(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() { return x; }
    public double getY() { return y; }

    public void setX(double x) { this.x = x; }
    public void setY(double y) { this.y = y; }

    /**
     * Calculate distance to another point.
     */
    public double distanceTo(FieldPoint other) {
        double dx = other.x - x;
        double dy = other.y - y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    /**
     * Calculate heading angle to another point in radians.
     */
    public double headingTo(FieldPoint other) {
        return Math.atan2(other.y - y, other.x - x);
    }

    @Override
    public String toString() {
        return "FieldPoint(" + x + ", " + y + ")";
    }
}
