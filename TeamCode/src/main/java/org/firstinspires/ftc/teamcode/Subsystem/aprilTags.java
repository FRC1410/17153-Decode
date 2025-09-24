package org.firstinspires.ftc.teamcode.Subsystem;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import java.util.List;
import java.util.ArrayList;

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

    // Returns data for the first valid tag with ID 0-40
    public double[] getTagData() {
        List<AprilTagDetection> detections = this.aprilTag.getDetections();
        double range = 0;
        double bearing = 0;
        double id = -1; // -1 indicates no valid tag found

        for (AprilTagDetection detection : detections) {
            // Only process tags with IDs 0-40
            if (detection.id >= 0 && detection.id <= 40) {
                if (detection.ftcPose != null) {
                    range = detection.ftcPose.range;
                    bearing = detection.ftcPose.bearing;
                    id = detection.id;
                    break; // Return first valid tag in range
                }
            }
        }
        return new double[]{range, bearing, id};
    }

    // Returns data for ALL valid tags with IDs 0-40
    public List<double[]> getAllValidTagData() {
        List<AprilTagDetection> detections = this.aprilTag.getDetections();
        List<double[]> validTags = new ArrayList<>();

        for (AprilTagDetection detection : detections) {
            // Only read tags with IDs 0-40
            if (detection.id >= 0 && detection.id <= 40) {
                if (detection.ftcPose != null) {
                    double[] tagData = {
                            detection.ftcPose.range,
                            detection.ftcPose.bearing,
                            detection.id
                    };
                    validTags.add(tagData);
                }
            }
        }
        return validTags;
    }

    // Check if a specific tag ID (0-40) is currently visible
    public boolean isTagVisible(int targetId) {
        if (targetId < 0 || targetId > 40) return false;

        List<AprilTagDetection> detections = this.aprilTag.getDetections();
        for (AprilTagDetection detection : detections) {
            if (detection.id == targetId && detection.ftcPose != null) {
                return true;
            }
        }
        return false;
    }

    // Get data for a specific tag ID (0-40)
    public double[] getSpecificTagData(int targetId) {
        if (targetId < 0 || targetId > 40) {
            return new double[]{0, 0, -1}; // Invalid ID
        }

        List<AprilTagDetection> detections = this.aprilTag.getDetections();
        for (AprilTagDetection detection : detections) {
            if (detection.id == targetId && detection.ftcPose != null) {
                return new double[]{
                        detection.ftcPose.range,
                        detection.ftcPose.bearing,
                        detection.id
                };
            }
        }
        return new double[]{0, 0, -1}; // Tag not found
    }

    // Get count of visible tags in range 0-40
    public int getValidTagCount() {
        List<AprilTagDetection> detections = this.aprilTag.getDetections();
        int count = 0;

        for (AprilTagDetection detection : detections) {
            if (detection.id >= 0 && detection.id <= 40 && detection.ftcPose != null) {
                count++;
            }
        }
        return count;
    }
}