package org.firstinspires.ftc.teamcode.Subsystem;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import java.util.List;

public class aprilTags {
    private VisionPortal visionPortal;
    private AprilTagProcessor aprilTag;

    public aprilTags(HardwareMap hardwareMapCool) {
        this.aprilTag = AprilTagProcessor.easyCreateWithDefaults();
        this.visionPortal = VisionPortal.easyCreateWithDefaults(
                hardwareMapCool.get(WebcamName.class, "Webcam 1"),
                this.aprilTag
        );
    }

    public double[] getTagData() {
        List<AprilTagDetection> detections = this.aprilTag.getDetections();
        double range = 0;
        double bearing = 0;
        double id = 0;

        for (AprilTagDetection detection : detections) {
            id = detection.id;
            if (detection.ftcPose != null) {
                range = detection.ftcPose.range;
                bearing = detection.ftcPose.bearing;
                break;
            }
        }
        return new double[]{range, bearing, id};
    }
}
