package org.firstinspires.ftc.teamcode;

import android.util.Size;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.hardware.camera.BuiltinCameraDirection;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.Subsystem.Drivetrain;
import org.firstinspires.ftc.teamcode.Subsystem.AprilTags;
import org.firstinspires.ftc.teamcode.Util.Toggle;
import org.firstinspires.ftc.vision.VisionPortal;

import java.util.Locale;

/*
REFERENCE CODE, DO NOT UNCOMMENT
@TeleOp
public class Robot extends OpMode {
    private final Drivetrain drivetrain = new Drivetrain();
    private final Toggle drivetrainToggle = new Toggle();
    private final Toggle raiseToggle = new Toggle();
    private AprilTags aprilTags;
    
    public void init() {
//        this.drivetrain.init(hardwareMap);

        this.aprilTags = new AprilTags(hardwareMap);
    }

    @Override
    public void loop() {

//        this.drivetrain.mechanumDrive(
//                gamepad1.left_stick_x,
//                gamepad1.left_stick_y,
//                gamepad1.right_stick_x,
//                drivetrainToggle.toggleButton(gamepad1.a)
//        );

        double[] tagData = aprilTags.getTagData();

        telemetry.addData("Range", tagData[0]);
        telemetry.addData("Bearing", tagData[1]);
        telemetry.addData("ID", tagData[2]);
        telemetry.addData("Camera State", this.aprilTags.vision_portal.getCameraState());
        telemetry.addData("Detection Count", this.aprilTags.april_tag.getDetections().size());
        telemetry.addData("FPS", this.aprilTags.vision_portal.getFps());
        telemetry.update();
    }
}
*/
@TeleOp(name = "calibrationCamera", group = "Utility")
public class Robot extends LinearOpMode {
    /*
     * EDIT THESE PARAMETERS AS NEEDED
     */
    final boolean USING_WEBCAM = false;
    final BuiltinCameraDirection INTERNAL_CAM_DIR = BuiltinCameraDirection.BACK;
    final int RESOLUTION_WIDTH = 640;
    final int RESOLUTION_HEIGHT = 480;

    // Internal state
    boolean lastX;
    int frameCount;
    long capReqTime;

    @Override
    public void runOpMode()
    {
        VisionPortal portal;

        if (USING_WEBCAM)
        {
            portal = new VisionPortal.Builder()
                    .setCamera(hardwareMap.get(WebcamName.class, "Webcam 1"))
                    .setCameraResolution(new Size(RESOLUTION_WIDTH, RESOLUTION_HEIGHT))
                    .build();
        }
        else
        {
            portal = new VisionPortal.Builder()
                    .setCamera(INTERNAL_CAM_DIR)
                    .setCameraResolution(new Size(RESOLUTION_WIDTH, RESOLUTION_HEIGHT))
                    .build();
        }

        while (!isStopRequested())
        {
            boolean x = gamepad1.x;

            if (x && !lastX)
            {
                portal.saveNextFrameRaw(String.format(Locale.US, "CameraFrameCapture-%06d", frameCount++));
                capReqTime = System.currentTimeMillis();
            }

            lastX = x;

            telemetry.addLine("######## Camera Capture Utility ########");
            telemetry.addLine(String.format(Locale.US, " > Resolution: %dx%d", RESOLUTION_WIDTH, RESOLUTION_HEIGHT));
            telemetry.addLine(" > Press X (or Square) to capture a frame");
            telemetry.addData(" > Camera Status", portal.getCameraState());

            if (capReqTime != 0)
            {
                telemetry.addLine("\nCaptured Frame!");
            }

            if (capReqTime != 0 && System.currentTimeMillis() - capReqTime > 1000)
            {
                capReqTime = 0;
            }

            telemetry.update();
        }
    }
}
