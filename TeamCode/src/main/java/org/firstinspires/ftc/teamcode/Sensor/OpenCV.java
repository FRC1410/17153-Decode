package org.firstinspires.ftc.teamcode.Sensor;

import static org.firstinspires.ftc.teamcode.Util.IDs.CAMERA_CV_ID;

import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Size;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.internal.camera.calibration.CameraCalibration;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.VisionProcessor;
import org.firstinspires.ftc.vision.opencv.ColorBlobLocatorProcessor;
import org.firstinspires.ftc.vision.opencv.ColorRange;
import org.firstinspires.ftc.vision.opencv.ImageRegion;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.util.List;
import java.util.ArrayList;

public class OpenCV {

    private VisionPortal visionPortal;
    private ColorBlobLocatorProcessor purpleColorLocator;
    private ColorBlobLocatorProcessor greenColorLocator;
    private VisionProcessor quartersSplitScreen;
    private boolean useQuartersSplitScreen = false;

    private static final double MIN_PURPLE_AREA = 1000.0;
    private static final double MIN_GREEN_AREA = 1000.0;

    // Distance estimation constants
    private static final double KNOWN_BALL_DIAMETER_CM = 7.5; // Adjust to your actual ball diameter
    private static final double FOCAL_LENGTH_PIXELS = 500.0; // CALIBRATE THIS at 24cm - see calibration method
    private static final double CALIBRATION_DISTANCE_CM = 24.0; // Standard calibration distance

    // Distance storage variables
    private double purpleDistance = -1.0;
    private double greenDistance = -1.0;

    public void init(HardwareMap hardwareMap) {
        init(hardwareMap, false);
    }

    public void init(HardwareMap hardwareMap, boolean enableQuartersSplitScreen) {
        this.useQuartersSplitScreen = enableQuartersSplitScreen;

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

        VisionPortal.Builder builder = new VisionPortal.Builder()
                .setCameraResolution(new Size(640, 480))
                .setCamera(hardwareMap.get(WebcamName.class, CAMERA_CV_ID))
                .enableLiveView(true)
                .setAutoStopLiveView(false)
                .setStreamFormat(VisionPortal.StreamFormat.MJPEG);

        if (useQuartersSplitScreen) {
            quartersSplitScreen = createQuartersSplitScreen();
            visionPortal = builder.addProcessor(quartersSplitScreen).build();
        } else {
            visionPortal = builder.addProcessor(purpleColorLocator)
                    .addProcessor(greenColorLocator)
                    .build();
        }
    }

    private boolean purpleDetected = false;
    private boolean greenDetected = false;
    private int purpleBlobCount = 0;
    private int greenBlobCount = 0;
    private boolean topLeftPurple = false;
    private boolean topLeftGreen = false;
    private boolean topRightPurple = false;
    private boolean topRightGreen = false;
    private boolean bottomLeftPurple = false;
    private boolean bottomLeftGreen = false;
    private boolean bottomRightPurple = false;
    private boolean bottomRightGreen = false;

    private List<ColorBlobLocatorProcessor.Blob> filterPurpleBlobsBySize(List<ColorBlobLocatorProcessor.Blob> blobs) {
        List<ColorBlobLocatorProcessor.Blob> filteredBlobs = new ArrayList<>();
        for (ColorBlobLocatorProcessor.Blob blob : blobs) {
            if (blob.getContourArea() >= MIN_PURPLE_AREA) {
                filteredBlobs.add(blob);
            }
        }
        return filteredBlobs;
    }

    private List<ColorBlobLocatorProcessor.Blob> filterGreenBlobsBySize(List<ColorBlobLocatorProcessor.Blob> blobs) {
        List<ColorBlobLocatorProcessor.Blob> filteredBlobs = new ArrayList<>();
        for (ColorBlobLocatorProcessor.Blob blob : blobs) {
            if (blob.getContourArea() >= MIN_GREEN_AREA) {
                filteredBlobs.add(blob);
            }
        }
        return filteredBlobs;
    }

    /**
     * Calculates distance to object based on its apparent size in the image using width
     * Formula: distance = (known_width * focal_length) / perceived_width
     *
     * @param blob The detected blob
     * @return Estimated distance in centimeters, or -1 if calculation fails
     */
    private double calculateDistance(ColorBlobLocatorProcessor.Blob blob) {
        if (blob == null) return -1.0;

        double perceivedWidth = blob.getBoxFit().size.width;

        if (perceivedWidth <= 0) return -1.0;

        double distance = (KNOWN_BALL_DIAMETER_CM * FOCAL_LENGTH_PIXELS) / perceivedWidth;

        return distance;
    }

    /**
     * Calculates distance using contour area instead of width
     * Generally more stable for circular objects
     *
     * @param blob The detected blob
     * @return Estimated distance in centimeters, or -1 if calculation fails
     */
    private double calculateDistanceFromArea(ColorBlobLocatorProcessor.Blob blob) {
        if (blob == null) return -1.0;

        double area = blob.getContourArea();
        if (area <= 0) return -1.0;

        // Assuming circular object, calculate diameter from area
        double perceivedDiameter = 2 * Math.sqrt(area / Math.PI);

        if (perceivedDiameter <= 0) return -1.0;

        double distance = (KNOWN_BALL_DIAMETER_CM * FOCAL_LENGTH_PIXELS) / perceivedDiameter;

        return distance;
    }

    /**
     * Calculates average distance for multiple blobs
     *
     * @param blobs List of detected blobs
     * @return Average distance in centimeters, or -1 if no valid blobs
     */
    private double calculateAverageDistance(List<ColorBlobLocatorProcessor.Blob> blobs) {
        if (blobs.isEmpty()) return -1.0;

        double totalDistance = 0;
        int validCount = 0;

        for (ColorBlobLocatorProcessor.Blob blob : blobs) {
            double distance = calculateDistanceFromArea(blob);
            if (distance > 0) {
                totalDistance += distance;
                validCount++;
            }
        }

        return validCount > 0 ? totalDistance / validCount : -1.0;
    }

    private VisionProcessor createQuartersSplitScreen() {
        return new VisionProcessor() {
            private Mat topLeft;
            private Mat topRight;
            private Mat bottomLeft;
            private Mat bottomRight;

            public void init(int width, int height, CameraCalibration calibration) {
                purpleColorLocator.init(width, height, calibration);
                greenColorLocator.init(width, height, calibration);

                topLeft = new Mat();
                topRight = new Mat();
                bottomLeft = new Mat();
                bottomRight = new Mat();
            }

            public Object processFrame(Mat frame, long captureTimeNanos) {
                int width = frame.width();
                int height = frame.height();
                int halfWidth = width / 2;
                int halfHeight = height / 2;

                Mat topLeftFrame = frame.submat(0, halfHeight, 0, halfWidth).clone();
                Mat topRightFrame = frame.submat(0, halfHeight, halfWidth, width).clone();
                Mat bottomLeftFrame = frame.submat(halfHeight, height, 0, halfWidth).clone();
                Mat bottomRightFrame = frame.submat(halfHeight, height, halfWidth, width).clone();

                purpleColorLocator.processFrame(topLeftFrame.clone(), captureTimeNanos);
                List<ColorBlobLocatorProcessor.Blob> tlPurple = filterPurpleBlobsBySize(purpleColorLocator.getBlobs());
                topLeftPurple = !tlPurple.isEmpty();

                greenColorLocator.processFrame(topLeftFrame, captureTimeNanos);
                List<ColorBlobLocatorProcessor.Blob> tlGreen = filterGreenBlobsBySize(greenColorLocator.getBlobs());
                topLeftGreen = !tlGreen.isEmpty();

                purpleColorLocator.processFrame(topRightFrame.clone(), captureTimeNanos);
                List<ColorBlobLocatorProcessor.Blob> trPurple = filterPurpleBlobsBySize(purpleColorLocator.getBlobs());
                topRightPurple = !trPurple.isEmpty();

                greenColorLocator.processFrame(topRightFrame, captureTimeNanos);
                List<ColorBlobLocatorProcessor.Blob> trGreen = filterGreenBlobsBySize(greenColorLocator.getBlobs());
                topRightGreen = !trGreen.isEmpty();

                purpleColorLocator.processFrame(bottomLeftFrame.clone(), captureTimeNanos);
                List<ColorBlobLocatorProcessor.Blob> blPurple = filterPurpleBlobsBySize(purpleColorLocator.getBlobs());
                bottomLeftPurple = !blPurple.isEmpty();

                greenColorLocator.processFrame(bottomLeftFrame, captureTimeNanos);
                List<ColorBlobLocatorProcessor.Blob> blGreen = filterGreenBlobsBySize(greenColorLocator.getBlobs());
                bottomLeftGreen = !blGreen.isEmpty();

                purpleColorLocator.processFrame(bottomRightFrame.clone(), captureTimeNanos);
                List<ColorBlobLocatorProcessor.Blob> brPurple = filterPurpleBlobsBySize(purpleColorLocator.getBlobs());
                bottomRightPurple = !brPurple.isEmpty();

                greenColorLocator.processFrame(bottomRightFrame, captureTimeNanos);
                List<ColorBlobLocatorProcessor.Blob> brGreen = filterGreenBlobsBySize(greenColorLocator.getBlobs());
                bottomRightGreen = !brGreen.isEmpty();

                purpleBlobCount = tlPurple.size() + trPurple.size() + blPurple.size() + brPurple.size();
                greenBlobCount = tlGreen.size() + trGreen.size() + blGreen.size() + brGreen.size();

                purpleDetected = purpleBlobCount > 0;
                greenDetected = greenBlobCount > 0;

                topLeftFrame.copyTo(frame.submat(0, halfHeight, 0, halfWidth));
                topRightFrame.copyTo(frame.submat(0, halfHeight, halfWidth, width));
                bottomLeftFrame.copyTo(frame.submat(halfHeight, height, 0, halfWidth));
                bottomRightFrame.copyTo(frame.submat(halfHeight, height, halfWidth, width));

                topLeftFrame.release();
                topRightFrame.release();
                bottomLeftFrame.release();
                bottomRightFrame.release();

                Imgproc.line(frame, new Point(halfWidth, 0), new Point(halfWidth, height), new Scalar(255, 255, 255), 3);
                Imgproc.line(frame, new Point(0, halfHeight), new Point(width, halfHeight), new Scalar(255, 255, 255), 3);

                Imgproc.putText(frame, "TOP LEFT", new Point(10, 25), Imgproc.FONT_HERSHEY_SIMPLEX, 0.5, new Scalar(255, 255, 255), 2);
                String tlStatus = (topLeftPurple ? "P" : "") + (topLeftGreen ? "G" : "");
                if (tlStatus.isEmpty()) tlStatus = "NONE";
                Imgproc.putText(frame, tlStatus, new Point(10, 50), Imgproc.FONT_HERSHEY_SIMPLEX, 0.5, (topLeftPurple || topLeftGreen) ? new Scalar(0, 255, 0) : new Scalar(0, 0, 255), 2);

                Imgproc.putText(frame, "TOP RIGHT", new Point(halfWidth + 10, 25), Imgproc.FONT_HERSHEY_SIMPLEX, 0.5, new Scalar(255, 255, 255), 2);
                String trStatus = (topRightPurple ? "P" : "") + (topRightGreen ? "G" : "");
                if (trStatus.isEmpty()) trStatus = "NONE";
                Imgproc.putText(frame, trStatus, new Point(halfWidth + 10, 50), Imgproc.FONT_HERSHEY_SIMPLEX, 0.5, (topRightPurple || topRightGreen) ? new Scalar(0, 255, 0) : new Scalar(0, 0, 255), 2);

                Imgproc.putText(frame, "BOTTOM LEFT", new Point(10, halfHeight + 25), Imgproc.FONT_HERSHEY_SIMPLEX, 0.5, new Scalar(255, 255, 255), 2);
                String blStatus = (bottomLeftPurple ? "P" : "") + (bottomLeftGreen ? "G" : "");
                if (blStatus.isEmpty()) blStatus = "NONE";
                Imgproc.putText(frame, blStatus, new Point(10, halfHeight + 50), Imgproc.FONT_HERSHEY_SIMPLEX, 0.5, (bottomLeftPurple || bottomLeftGreen) ? new Scalar(0, 255, 0) : new Scalar(0, 0, 255), 2);

                Imgproc.putText(frame, "BOTTOM RIGHT", new Point(halfWidth + 10, halfHeight + 25), Imgproc.FONT_HERSHEY_SIMPLEX, 0.5, new Scalar(255, 255, 255), 2);
                String brStatus = (bottomRightPurple ? "P" : "") + (bottomRightGreen ? "G" : "");
                if (brStatus.isEmpty()) brStatus = "NONE";
                Imgproc.putText(frame, brStatus, new Point(halfWidth + 10, halfHeight + 50), Imgproc.FONT_HERSHEY_SIMPLEX, 0.5, (bottomRightPurple || bottomRightGreen) ? new Scalar(0, 255, 0) : new Scalar(0, 0, 255), 2);

                return null;
            }

            public void onDrawFrame(Canvas canvas, int onscreenWidth, int onscreenHeight, float scaleBmpPxToCanvasPx, float scaleCanvasDensity, Object userContext) {
            }
        };
    }

    public void processVision(Telemetry telemetry) {
        List<ColorBlobLocatorProcessor.Blob> purpleBlobs = filterPurpleBlobsBySize(purpleColorLocator.getBlobs());
        List<ColorBlobLocatorProcessor.Blob> greenBlobs = filterGreenBlobsBySize(greenColorLocator.getBlobs());

        // Calculate distances
        purpleDistance = calculateAverageDistance(purpleBlobs);
        greenDistance = calculateAverageDistance(greenBlobs);

        if (useQuartersSplitScreen) {
            telemetry.addData("=== QUADRANT DETECTION ===", "");
            telemetry.addData("Top Left", String.format("Purple: %s | Green: %s",
                    topLeftPurple ? "YES" : "NO",
                    topLeftGreen ? "YES" : "NO"));
            telemetry.addData("Top Right", String.format("Purple: %s | Green: %s",
                    topRightPurple ? "YES" : "NO",
                    topRightGreen ? "YES" : "NO"));
            telemetry.addData("Bottom Left", String.format("Purple: %s | Green: %s",
                    bottomLeftPurple ? "YES" : "NO",
                    bottomLeftGreen ? "YES" : "NO"));
            telemetry.addData("Bottom Right", String.format("Purple: %s | Green: %s",
                    bottomRightPurple ? "YES" : "NO",
                    bottomRightGreen ? "YES" : "NO"));
            telemetry.addLine();
            telemetry.addData("Total Purple Blobs", purpleBlobCount);
            telemetry.addData("Total Green Blobs", greenBlobCount);
            telemetry.addLine();
        }

        // Distance telemetry
        telemetry.addData("=== DISTANCE ESTIMATION ===", "");
        if (purpleDistance > 0) {
            telemetry.addData("Purple Avg Distance", String.format("%.1f cm", purpleDistance));
        } else {
            telemetry.addData("Purple Avg Distance", "N/A");
        }

        if (greenDistance > 0) {
            telemetry.addData("Green Avg Distance", String.format("%.1f cm", greenDistance));
        } else {
            telemetry.addData("Green Avg Distance", "N/A");
        }
        telemetry.addLine();

        telemetry.addData("Purple Artifacts Found", purpleBlobs.size());
        for (int i = 0; i < Math.min(purpleBlobs.size(), 3); i++) {
            ColorBlobLocatorProcessor.Blob blob = purpleBlobs.get(i);
            double blobDistance = calculateDistanceFromArea(blob);
            telemetry.addData("Purple " + (i+1) + " Center", String.format("(%.0f, %.0f)", (double)blob.getBoxFit().center.x, (double)blob.getBoxFit().center.y));
            telemetry.addData("Purple " + (i+1) + " Area", String.format("%.0f", (double)blob.getContourArea()));
            if (blobDistance > 0) {
                telemetry.addData("Purple " + (i+1) + " Distance", String.format("%.1f cm", blobDistance));
            }
        }

        telemetry.addData("Green Artifacts Found", greenBlobs.size());
        for (int i = 0; i < Math.min(greenBlobs.size(), 3); i++) {
            ColorBlobLocatorProcessor.Blob blob = greenBlobs.get(i);
            double blobDistance = calculateDistanceFromArea(blob);
            telemetry.addData("Green " + (i+1) + " Center", String.format("(%.0f, %.0f)", (double)blob.getBoxFit().center.x, (double)blob.getBoxFit().center.y));
            telemetry.addData("Green " + (i+1) + " Area", String.format("%.0f", (double)blob.getContourArea()));
            if (blobDistance > 0) {
                telemetry.addData("Green " + (i+1) + " Distance", String.format("%.1f cm", blobDistance));
            }
        }

        telemetry.addData("Camera State", visionPortal.getCameraState());
        telemetry.addData("FPS", String.format("%.1f", visionPortal.getFps()));
        telemetry.addData("Display Mode", useQuartersSplitScreen ? "Quarters Split Screen" : "Normal");
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
        return filterPurpleBlobsBySize(purpleColorLocator.getBlobs());
    }

    public List<ColorBlobLocatorProcessor.Blob> getGreenBlobs() {
        return filterGreenBlobsBySize(greenColorLocator.getBlobs());
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

    public boolean isPurpleDetected() {
        return purpleDetected;
    }

    public boolean isGreenDetected() {
        return greenDetected;
    }

    public int getPurpleBlobCount() {
        return purpleBlobCount;
    }

    public int getGreenBlobCount() {
        return greenBlobCount;
    }

    public boolean isAnythingDetected() {
        return purpleDetected || greenDetected;
    }

    public boolean isTopLeftPurpleDetected() {
        return topLeftPurple;
    }

    public boolean isTopLeftGreenDetected() {
        return topLeftGreen;
    }

    public boolean isTopRightPurpleDetected() {
        return topRightPurple;
    }

    public boolean isTopRightGreenDetected() {
        return topRightGreen;
    }

    public boolean isBottomLeftPurpleDetected() {
        return bottomLeftPurple;
    }

    public boolean isBottomLeftGreenDetected() {
        return bottomLeftGreen;
    }

    public boolean isBottomRightPurpleDetected() {
        return bottomRightPurple;
    }

    public boolean isBottomRightGreenDetected() {
        return bottomRightGreen;
    }

    // Distance getter methods
    public double getPurpleDistance() {
        return purpleDistance;
    }

    public double getGreenDistance() {
        return greenDistance;
    }

    public double getLargestPurpleBlobDistance() {
        ColorBlobLocatorProcessor.Blob blob = getLargestPurpleBlob();
        return calculateDistanceFromArea(blob);
    }

    public double getLargestGreenBlobDistance() {
        ColorBlobLocatorProcessor.Blob blob = getLargestGreenBlob();
        return calculateDistanceFromArea(blob);
    }

    /**
     * CALIBRATION METHOD - Run this to determine your focal length constant
     *
     * 1. Place a ball at a known distance (e.g., 50cm) from the camera
     * 2. Measure the actual distance accurately
     * 3. Run this method and note the calculated focal length
     * 4. Update FOCAL_LENGTH_PIXELS constant with this value
     *
     * @param knownDistanceCm The actual measured distance to the ball
     * @param blob The detected blob at that distance
     * @return The calculated focal length to use as a constant
     */
    public double calibrateFocalLength(double knownDistanceCm, ColorBlobLocatorProcessor.Blob blob) {
        if (blob == null) return -1.0;

        double area = blob.getContourArea();
        double perceivedDiameter = 2 * Math.sqrt(area / Math.PI);

        // Focal Length = (Perceived Width × Distance) / Real Width
        double focalLength = (perceivedDiameter * knownDistanceCm) / KNOWN_BALL_DIAMETER_CM;

        return focalLength;
    }
}