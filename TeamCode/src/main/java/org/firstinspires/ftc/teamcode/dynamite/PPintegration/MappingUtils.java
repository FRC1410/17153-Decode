package org.firstinspires.ftc.teamcode.dynamite.PPintegration;

import com.pedropathing.geometry.Pose;

/**
 * Utilities for mapping between DYN Field types and PedroPathing types.
 * These are public so they can be unit-tested and reused.
 */
public final class MappingUtils {
    private MappingUtils() {}

    public static Pose toPedroPose(FieldPose pose) {
        return new Pose(pose.getX(), pose.getY(), pose.getHeading());
    }

    public static Pose toPedroPoseFromPoint(FieldPoint point) {
        return new Pose(point.getX(), point.getY(), 0.0);
    }

    public static FieldPose toFieldPose(Pose pose) {
        return new FieldPose(pose.getX(), pose.getY(), pose.getHeading());
    }
}
