package org.firstinspires.ftc.teamcode.Sensor;

import static org.firstinspires.ftc.teamcode.Util.IDs.CAMERA_CV_ID;

import android.graphics.Color;
import android.util.Size;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.opencv.ColorBlobLocatorProcessor;
import org.firstinspires.ftc.vision.opencv.ColorRange;
import org.firstinspires.ftc.vision.opencv.ImageRegion;

import java.util.List;

public class OpenCV {

    private VisionPortal visionPortal;
    private ColorBlobLocatorProcessor purpleColorLocator;
    private ColorBlobLocatorProcessor greenColorLocator;

    public void init(HardwareMap hardwareMap) {
        purpleColorLocator = new ColorBlobLocatorProcessor.Builder()
                .setTargetColorRange(ColorRange.ARTIFACT_PURPLE)
                .setContourMode(ColorBlobLocatorProcessor.ContourMode.EXTERNAL_ONLY)
                .setRoi(ImageRegion.asUnityCenterCoordinates(-0.75, 0.75, 0.75, -0.75))
                .setDrawContours(true)
                .setBoxFitColor(0)
                .setCircleFitColor(Color.rgb(255, 0, 255))
                .setBlurSize(5)
                .setDilateSize(15)
                .setErodeSize(15)
                .setMorphOperationType(ColorBlobLocatorProcessor.MorphOperationType.CLOSING)
                .build();

        greenColorLocator = new ColorBlobLocatorProcessor.Builder()
                .setTargetColorRange(ColorRange.ARTIFACT_GREEN)
                .setContourMode(ColorBlobLocatorProcessor.ContourMode.EXTERNAL_ONLY)
                .setRoi(ImageRegion.asUnityCenterCoordinates(-0.75, 0.75, 0.75, -0.75))
                .setDrawContours(true)
                .setBoxFitColor(0)
                .setCircleFitColor(Color.rgb(0, 255, 0))
                .setBlurSize(5)
                .setDilateSize(15)
                .setErodeSize(15)
                .setMorphOperationType(ColorBlobLocatorProcessor.MorphOperationType.CLOSING)
                .build();

        visionPortal = new VisionPortal.Builder()
                .addProcessor(purpleColorLocator)
                .addProcessor(greenColorLocator)
                .setCameraResolution(new Size(640, 480))
                .setCamera(hardwareMap.get(WebcamName.class, CAMERA_CV_ID))
                .enableLiveView(true)
                .setAutoStopLiveView(false)
                .setStreamFormat(VisionPortal.StreamFormat.MJPEG)
                .build();
    }

    public void processVision(Telemetry telemetry) {
        List<ColorBlobLocatorProcessor.Blob> purpleBlobs = purpleColorLocator.getBlobs();
        List<ColorBlobLocatorProcessor.Blob> greenBlobs = greenColorLocator.getBlobs();

        telemetry.addData("Purple Artifacts Found", purpleBlobs.size());
        for (int i = 0; i < Math.min(purpleBlobs.size(), 3); i++) {
            ColorBlobLocatorProcessor.Blob blob = purpleBlobs.get(i);
            telemetry.addData("Purple " + (i+1) + " Center", String.format("(%.0f, %.0f)",
                (double)blob.getBoxFit().center.x, (double)blob.getBoxFit().center.y));
            telemetry.addData("Purple " + (i+1) + " Area", String.format("%.0f", (double)blob.getContourArea()));
        }

        telemetry.addData("Green Artifacts Found", greenBlobs.size());
        for (int i = 0; i < Math.min(greenBlobs.size(), 3); i++) {
            ColorBlobLocatorProcessor.Blob blob = greenBlobs.get(i);
            telemetry.addData("Green " + (i+1) + " Center", String.format("(%.0f, %.0f)",
                (double)blob.getBoxFit().center.x, (double)blob.getBoxFit().center.y));
            telemetry.addData("Green " + (i+1) + " Area", String.format("%.0f", (double)blob.getContourArea()));
        }

        telemetry.addData("Camera State", visionPortal.getCameraState());
        telemetry.addData("FPS", String.format("%.1f", visionPortal.getFps()));
        telemetry.addData("Stream Available", visionPortal.getCameraState() == VisionPortal.CameraState.STREAMING);

        if (visionPortal.getCameraState() != VisionPortal.CameraState.STREAMING) {
            telemetry.addData("Stream Status", "Attempting to start stream...");
            startStreaming();
        } else {
            telemetry.addData("Stream Status", "ACTIVE - Check Driver Station Camera View");
        }
    }

    public void startStreaming() {
        if (visionPortal != null) {
            visionPortal.resumeStreaming();
        }
    }

    public void stopStreaming() {
        if (visionPortal != null) {
            visionPortal.stopStreaming();
        }
    }

    public boolean isStreaming() {
        return visionPortal != null && visionPortal.getCameraState() == VisionPortal.CameraState.STREAMING;
    }

    public List<ColorBlobLocatorProcessor.Blob> getPurpleBlobs() {
        return purpleColorLocator.getBlobs();
    }

    public List<ColorBlobLocatorProcessor.Blob> getGreenBlobs() {
        return greenColorLocator.getBlobs();
    }

    public void close() {
        if (visionPortal != null) {
            visionPortal.close();
        }
    }

    public ColorBlobLocatorProcessor.Blob getLargestPurpleBlob() {
        List<ColorBlobLocatorProcessor.Blob> blobs = getPurpleBlobs();
        if (blobs.isEmpty()) return null;

        ColorBlobLocatorProcessor.Blob largest = blobs.get(0);
        for (ColorBlobLocatorProcessor.Blob blob : blobs) {
            if (blob.getContourArea() > largest.getContourArea()) {
                largest = blob;
            }
        }
        return largest;
    }

    public ColorBlobLocatorProcessor.Blob getLargestGreenBlob() {
        List<ColorBlobLocatorProcessor.Blob> blobs = getGreenBlobs();
        if (blobs.isEmpty()) return null;

        ColorBlobLocatorProcessor.Blob largest = blobs.get(0);
        for (ColorBlobLocatorProcessor.Blob blob : blobs) {
            if (blob.getContourArea() > largest.getContourArea()) {
                largest = blob;
            }
        }
        return largest;
    }

    public enum ArtifactColor {
        PURPLE,
        GREEN,
        EMPTY
    }

    public ArtifactColor getColorInPosition(int position) {
        if (position < 1 || position > 3) return ArtifactColor.EMPTY;

        List<ColorBlobLocatorProcessor.Blob> purpleBlobs = getPurpleBlobs();
        List<ColorBlobLocatorProcessor.Blob> greenBlobs = getGreenBlobs();

        double minX = getMinXForPosition(position);
        double maxX = getMaxXForPosition(position);

        for (ColorBlobLocatorProcessor.Blob blob : purpleBlobs) {
            double x = blob.getBoxFit().center.x;
            if (x >= minX && x <= maxX) {
                return ArtifactColor.PURPLE;
            }
        }

        for (ColorBlobLocatorProcessor.Blob blob : greenBlobs) {
            double x = blob.getBoxFit().center.x;
            if (x >= minX && x <= maxX) {
                return ArtifactColor.GREEN;
            }
        }

        return ArtifactColor.EMPTY;
    }

    private double getMinXForPosition(int position) {
        switch (position) {
            case 1: return 0;
            case 2: return 213;
            case 3: return 427;
            default: return 0;
        }
    }

    private double getMaxXForPosition(int position) {
        switch (position) {
            case 1: return 213;
            case 2: return 427;
            case 3: return 640;
            default: return 640;
        }
    }

    public Integer findPositionForColor(ArtifactColor desiredColor) {
        for (int position = 1; position <= 3; position++) {
            if (getColorInPosition(position) == desiredColor) {
                return position;
            }
        }
        return null;
    }
}
