package org.firstinspires.ftc.teamcode.dynamite.PPintegration;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a path segment for Pedro Pathing.
 * Can be a line, bezier curve, or turn in place.
 */
public class PathSegment {
    public enum SegmentType {
        LINE,           // Straight line movement
        BEZIER,         // Bezier curve
        TURN_IN_PLACE,  // Turn without moving
        HOLD            // Hold position
    }

    private final SegmentType type;
    private final FieldPose startPose;
    private final FieldPose endPose;
    private final List<FieldPoint> controlPoints; // For bezier curves
    private final double speed;                    // Movement speed

    /**
     * Create a line segment.
     */
    public static PathSegment line(FieldPose start, FieldPose end) {
        return new PathSegment(SegmentType.LINE, start, end, null, 1.0);
    }

    /**
     * Create a bezier curve segment.
     */
    public static PathSegment bezier(FieldPose start, FieldPose end, List<FieldPoint> controlPoints) {
        return new PathSegment(SegmentType.BEZIER, start, end, controlPoints, 1.0);
    }

    /**
     * Create a turn in place segment.
     */
    public static PathSegment turn(FieldPose pose, double targetHeadingDegrees, double degreesPerSecond) {
        // Convert degrees to radians for FieldPose (which stores heading in radians)
        FieldPose endPose = new FieldPose(pose.getX(), pose.getY(), Math.toRadians(targetHeadingDegrees));
        return new PathSegment(SegmentType.TURN_IN_PLACE, pose, endPose, null, degreesPerSecond);
    }

    private PathSegment(SegmentType type, FieldPose startPose, FieldPose endPose,
                        List<FieldPoint> controlPoints, double speed) {
        this.type = type;
        this.startPose = startPose;
        this.endPose = endPose;
        this.controlPoints = controlPoints != null ? controlPoints : new ArrayList<>();
        this.speed = speed;
    }

    public SegmentType getType() { return type; }
    public FieldPose getStartPose() { return startPose; }
    public FieldPose getEndPose() { return endPose; }
    public List<FieldPoint> getControlPoints() { return controlPoints; }
    public double getSpeed() { return speed; }

    /**
     * Calculate the approximate length of this segment.
     */
    public double getLength() {
        switch (type) {
            case LINE:
                return startPose.toPoint().distanceTo(endPose.toPoint());
            case BEZIER:
                // Approximate bezier length using control point distances
                double length = 0;
                FieldPoint prev = startPose.toPoint();
                for (FieldPoint cp : controlPoints) {
                    length += prev.distanceTo(cp);
                    prev = cp;
                }
                length += prev.distanceTo(endPose.toPoint());
                return length;
            case TURN_IN_PLACE:
                return Math.abs(endPose.getHeading() - startPose.getHeading());
            default:
                return 0;
        }
    }

    @Override
    public String toString() {
        return "PathSegment{" + type + " from " + startPose + " to " + endPose + "}";
    }
}
