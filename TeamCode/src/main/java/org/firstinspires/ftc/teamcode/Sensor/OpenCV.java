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

public class OpenCV {

    private VisionPortal visionPortal;
    private ColorBlobLocatorProcessor purpleColorLocator;
    private ColorBlobLocatorProcessor greenColorLocator;
    private VisionProcessor quartersSplitScreen;
    private boolean useQuartersSplitScreen = false;

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
    private VisionProcessor createQuartersSplitScreen() {
        return new VisionProcessor() {
            private Mat bottomLeftSide;
            private Mat bottomRightSide;
            private Mat topLeftSide;
            private Mat topRightSide;

            public void init(int width, int height, CameraCalibration calibration) {
                purpleColorLocator.init(width, height, calibration);
                greenColorLocator.init(width, height, calibration);
                bottomLeftSide = new Mat();
                bottomRightSide = new Mat();
                topLeftSide = new Mat();
                topRightSide = new Mat();
            }


            public Object processFrame(Mat frame, long captureTimeNanos) {
                int width = frame.width();
                int height = frame.height();
                int halfWidth = width / 2;
                int halfHeight = height / 2;

                Mat bottomLeftFrame = frame.submat(0, halfHeight, 0, halfWidth).clone();
                Mat bottomRightFrame = frame.submat(0, halfHeight, 0, halfWidth).clone();
                Mat topLeftFrame = frame.submat(0, halfHeight, 0, halfWidth).clone();
                Mat topRightFrame = frame.submat(0, halfHeight, 0, halfWidth).clone();


                topLeftFrame.copyTo(frame.submat(0, halfHeight, 0, halfWidth));
                topRightFrame.copyTo(frame.submat(0, halfHeight, halfWidth, width));
                bottomLeftFrame.copyTo(frame.submat(halfHeight, height, 0, halfWidth));
                bottomRightFrame.copyTo(frame.submat(halfHeight, height, halfWidth, width));


                bottomLeftFrame.release();
                bottomRightFrame.release();
                topLeftFrame.release();
                topRightFrame.release();


                Imgproc.line(frame, new Point(halfWidth, 0), new Point(halfWidth, height),
                        new Scalar(255, 255, 255), 3);
                Imgproc.line(frame, new Point(0, halfHeight), new Point(width, halfHeight),
                        new Scalar(255, 255, 255), 3);

                Imgproc.putText(frame, "TOPLEFT", new Point(10, 35),
                        Imgproc.FONT_HERSHEY_SIMPLEX, 1.0, new Scalar(0, 0, 0), 2);
                Imgproc.putText(frame, "TOPRIGHT", new Point(halfWidth + 10, 35),
                        Imgproc.FONT_HERSHEY_SIMPLEX, 1.0, new Scalar(0, 0, 0), 2);
                Imgproc.putText(frame, "BOTTOMLEFT", new Point(10, halfHeight + 35),
                        Imgproc.FONT_HERSHEY_SIMPLEX, 1.0, new Scalar(0, 0, 0), 2);
                Imgproc.putText(frame, "BOTTOMRIGHT", new Point(halfWidth + 10, halfHeight + 35),
                        Imgproc.FONT_HERSHEY_SIMPLEX, 1.0, new Scalar(0, 0, 0), 2);

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
