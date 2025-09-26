package org.firstinspires.ftc.teamcode.Subsystem;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;
import android.util.Size;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;
import org.firstinspires.ftc.vision.apriltag.AprilTagGameDatabase;

import java.util.List;
import java.util.ArrayList;

public class aprilTags {
    private VisionPortal visionPortal;
    private AprilTagProcessor aprilTag;

    public aprilTags(HardwareMap hardwareMapCool) {
        // Create AprilTag processor with optimized settings to reduce false positives
        this.aprilTag = new AprilTagProcessor.Builder()
                .setTagFamily(AprilTagProcessor.TagFamily.TAG_16h5) // Try this first
                // .setTagFamily(AprilTagProcessor.TagFamily.TAG_25h9) // Or try this
                .setDrawAxes(true)
                .setDrawCubeProjection(true)
                .setDrawTagOutline(true)
                .setDrawTagID(true)
                // These settings reduce false positives:
                .setTagLibrary(AprilTagGameDatabase.getCenterStageTagLibrary()) // Use known tag positions if available
                .setOutputUnits(DistanceUnit.INCH, AngleUnit.DEGREES)
                .build();

        // Create VisionPortal with optimized camera settings
        this.visionPortal = new VisionPortal.Builder()
                .setCamera(hardwareMapCool.get(WebcamName.class, "Webcam 1"))
                .addProcessor(this.aprilTag)
                .setCameraResolution(new Size(800, 600)) // Higher resolution for better accuracy
                .setStreamFormat(VisionPortal.StreamFormat.MJPEG)
                .enableLiveView(true)
                .setAutoStopLiveView(false) // Keep live view on for debugging
                .build();
    }

    // Returns data for the first valid tag with ID 0-40 (with quality filtering)
    public double[] getTagData() {
        List<AprilTagDetection> detections = this.aprilTag.getDetections();
        double range = 0;
        double bearing = 0;
        double id = -1; // -1 indicates no valid tag found

        for (AprilTagDetection detection : detections) {
            // Only process tags with IDs 0-40 and good quality detection
            if (detection.id >= 0 && detection.id <= 40 && isGoodDetection(detection)) {
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

    // Quality filter to reduce false positives
    private boolean isGoodDetection(AprilTagDetection detection) {
        // Check if detection has sufficient quality metrics
        if (detection.ftcPose == null) return false;

        // Filter out detections that are too close or too far (likely false positives)
        double range = detection.ftcPose.range;
        if (range < 6 || range > 120) return false; // Reasonable range in inches

        // Check decision margin (confidence) - higher is better
        if (detection.decisionMargin < 50) return false; // Minimum confidence threshold

        // Check hamming distance (error correction) - lower is better
        if (detection.hamming > 2) return false; // Maximum allowed errors

        return true;
    }

    // Returns data for ALL valid tags with IDs 0-40
    public List<double[]> getAllValidTagData() {
        List<AprilTagDetection> detections = this.aprilTag.getDetections();
        List<double[]> validTags = new ArrayList<>();

        for (AprilTagDetection detection : detections) {
            // Only process tags with IDs 0-40
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

    // Debug method to see all detected tags with quality info
    public void debugDetections() {
        List<AprilTagDetection> detections = this.aprilTag.getDetections();
        System.out.println("=== Total detections: " + detections.size() + " ===");

        for (AprilTagDetection detection : detections) {
            System.out.println("Tag ID: " + detection.id +
                    " | Has pose: " + (detection.ftcPose != null) +
                    " | Decision margin: " + detection.decisionMargin +
                    " | Hamming: " + detection.hamming +
                    " | Good detection: " + isGoodDetection(detection));
            if (detection.ftcPose != null) {
                System.out.println("  Range: " + detection.ftcPose.range + " inches");
            }
        }
        System.out.println("=====================================");
    }
}