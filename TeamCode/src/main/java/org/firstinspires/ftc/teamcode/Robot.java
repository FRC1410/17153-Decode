package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Subsystem.Drivetrain;
import org.firstinspires.ftc.teamcode.Subsystem.AprilTags;
import org.firstinspires.ftc.teamcode.Util.Toggle;

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