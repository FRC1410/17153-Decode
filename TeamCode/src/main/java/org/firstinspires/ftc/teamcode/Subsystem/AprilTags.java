package org.firstinspires.ftc.teamcode.Subsystem;

import android.util.Size;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import java.util.List;

public class AprilTags {
    public VisionPortal vision_portal;
    public AprilTagProcessor april_tag;

    public AprilTags(HardwareMap hardwareMapCool) {
        // NOTE: The values/configs here are very finicky, don't change them unless absolutely necessary
        try {
            // This is the camera data, and where we get said data.
            this.april_tag = new AprilTagProcessor.Builder()
                    .setTagFamily(AprilTagProcessor.TagFamily.TAG_36h11)
                    .setDrawAxes(true)
                    .setDrawCubeProjection(true)
                    .setDrawTagOutline(true)
                    .setDrawTagID(true)
                    .setOutputUnits(DistanceUnit.INCH, AngleUnit.DEGREES)
                    .build();

            // This is the connection to the physical camera, including the id
            this.vision_portal = new VisionPortal.Builder()
                    .setCamera(hardwareMapCool.get(WebcamName.class, "Webcam 1"))
                    .addProcessor(this.april_tag)
                    .setCameraResolution(new Size(1280, 800))
                    .enableLiveView(true)
                    .setAutoStopLiveView(false)
                    .build();
        } catch (Exception e) {
            throw new RuntimeException("Camera init failed: " + e.getMessage());
        }
    }

    /**
     * Gets tag data on the distance to the tag, the angle, and the ID of the tag
     * @return Range, Bearing, Tag ID in a double[]
     */
    public double[] getTagData() {
        List<AprilTagDetection> detections = this.april_tag.getDetections();

        // Return detection count as first value for debugging
        if (detections.isEmpty()) {
            return new double[]{-999, 0, 0}; // -999 = no detections at all
        }

        AprilTagDetection detection = detections.get(0);
        double id = detection.id;
        double range = 0;
        double bearing = 0;

        if (detection.ftcPose != null) {
            range = detection.ftcPose.range;
            bearing = detection.ftcPose.bearing;
        } else {
            range = -888; // -888 = detection exists but no pose
            bearing = -888;
        }

        return new double[]{range, bearing, id};
    }
}
