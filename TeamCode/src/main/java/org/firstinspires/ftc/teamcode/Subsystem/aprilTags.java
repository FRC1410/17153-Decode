package org.firstinspires.ftc.teamcode.Subsystem;

import android.util.Size;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagGameDatabase;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import java.util.List;

public class aprilTags {
    public VisionPortal visionPortal;
    public AprilTagProcessor aprilTag;

    public aprilTags(HardwareMap hardwareMapCool) {
        try {
            this.aprilTag = new AprilTagProcessor.Builder()
                    .setTagFamily(AprilTagProcessor.TagFamily.TAG_36h11)
                    .setDrawAxes(true)
                    .setDrawCubeProjection(true)
                    .setDrawTagOutline(true)
                    .setDrawTagID(true)
                    .setOutputUnits(DistanceUnit.INCH, AngleUnit.DEGREES)
                    .build();

            this.visionPortal = new VisionPortal.Builder()
                    .setCamera(hardwareMapCool.get(WebcamName.class, "Webcam 1"))
                    .addProcessor(this.aprilTag)
                    .setCameraResolution(new Size(1280, 800))
                    .enableLiveView(true)
                    .setAutoStopLiveView(false)
                    .build();
        } catch (Exception e) {
            throw new RuntimeException("Camera init failed: " + e.getMessage());
        }
    }
    public double[] getTagData() {
        List<AprilTagDetection> detections = this.aprilTag.getDetections();

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
