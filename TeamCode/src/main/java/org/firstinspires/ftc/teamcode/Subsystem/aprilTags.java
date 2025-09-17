package org.firstinspires.ftc.teamcode.Subsystem;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import java.util.List;

public class aprilTags extends OpMode {
    private VisionPortal visionPortal;
    private AprilTagProcessor aprilTag;

    public void init(){
        this.aprilTag = AprilTagProcessor.easyCreateWithDefaults();
        this.visionPortal = VisionPortal.easyCreateWithDefaults(hardwareMap.get(WebcamName.class, "Webcam 1"), this.aprilTag);
    }

    public void loop() {
        List<AprilTagDetection> detections = this.aprilTag.getDetections();
        double range = 0;
        double bearing = 0;
        boolean tagFound = false;

        for (AprilTagDetection detection : detections) {
            if (detection.ftcPose != null) {
                range = detection.ftcPose.range;
                bearing = detection.ftcPose.bearing;

                tagFound = true;
                break;
            }
        }

        telemetry.addData("Range", range);
        telemetry.addData("Bearing", bearing);
        telemetry.update();

    }
}
