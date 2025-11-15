package org.firstinspires.ftc.teamcode.Sensor;

import static org.firstinspires.ftc.teamcode.Util.IDs.CAMERA_CV_ID;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
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
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.util.List;

public class OpenCV {

    private VisionPortal visionPortal;
    private ColorBlobLocatorProcessor purpleColorLocator;
    private ColorBlobLocatorProcessor greenColorLocator;
    private VisionProcessor thirdsSplitScreen;
    private boolean useThirdsSplitScreen = false;

    public void init(HardwareMap hardwareMap) {
        init(hardwareMap, false);
    }


    public void init(HardwareMap hardwareMap, boolean enableThirdsSplitScreen) {
        this.useThirdsSplitScreen = enableThirdsSplitScreen;

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
        if (useThirdsSplitScreen) {
            thirdsSplitScreen = createThirdsSplitScreen();
            visionPortal = builder.addProcessor(thirdsSplitScreen).build();
        } else {
            visionPortal = builder.addProcessor(purpleColorLocator)
                    .addProcessor(greenColorLocator)
                    .build();
        }
    }
    private VisionProcessor createThirdsSplitScreen() {
        return new VisionProcessor() {
            private Mat leftSide;
            private Mat rightSide;

            public void init(int width, int height, CameraCalibration calibration) {
                purpleColorLocator.init(width, height, calibration);
                greenColorLocator.init(width, height, calibration);
                leftSide = new Mat();
                rightSide = new Mat();
            }


            public Object processFrame(Mat frame, long captureTimeNanos) {
                int width = frame.width();
                int height = frame.height();
                int halfWidth = width / 2;

                // Clone frame for each processor
                Mat leftFrame = frame.submat(0, height, 0, halfWidth).clone();
                Mat rightFrame = frame.submat(0, height, halfWidth, width).clone();

                // Process both sides
                purpleColorLocator.processFrame(leftFrame, captureTimeNanos);
                greenColorLocator.processFrame(rightFrame, captureTimeNanos);

                // Copy processed results back to original frame
                leftFrame.copyTo(frame.submat(0, height, 0, halfWidth));
                rightFrame.copyTo(frame.submat(0, height, halfWidth, width));

                // Release temporary mats
                leftFrame.release();
                rightFrame.release();

                // Draw divider line
                Imgproc.line(frame, new Point(halfWidth, 0), new Point(halfWidth, height),
                        new Scalar(255, 255, 255), 3);


                Imgproc.putText(frame, "PURPLE", new Point(10, 35),
                        Imgproc.FONT_HERSHEY_SIMPLEX, 1.0, new Scalar(255, 0, 255), 2);
                Imgproc.putText(frame, "GREEN", new Point(halfWidth + 10, 35),
                        Imgproc.FONT_HERSHEY_SIMPLEX, 1.0, new Scalar(0, 255, 0), 2);

                return null;
            }

            public void onDrawFrame(Canvas canvas, int onscreenWidth, int onscreenHeight,
                                    float scaleBmpPxToCanvasPx, float scaleCanvasDensity, Object userContext) {


            }
        };
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
}
