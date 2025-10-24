package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Subsystem.Drivetrain;
import org.firstinspires.ftc.teamcode.Subsystem.Turret;
import org.firstinspires.ftc.teamcode.Subsystem.aprilTags;
import org.firstinspires.ftc.teamcode.Util.RobotStates;
import org.firstinspires.ftc.teamcode.Util.Toggle;

@TeleOp
public class Robot extends OpMode {
//    private final Drivetrain drivetrain = new Drivetrain();
    private final Toggle drivetrainToggle = new Toggle();
    private final Toggle raiseToggle = new Toggle();
//    private aprilTags aprilTags;

    private final Turret turret = new Turret();
    
    public void init() {
//        this.drivetrain.init(hardwareMap);
        this.turret.init(hardwareMap);
//        this.aprilTags = new aprilTags(hardwareMap);
    }

    @Override
    public void loop() {


//        this.drivetrain.mechanumDrive(
//                gamepad1.left_stick_x,
//                gamepad1.left_stick_y,
//                gamepad1.right_stick_x,
//                drivetrainToggle.toggleButton(gamepad1.a)
//        );

//        double[] tagData = aprilTags.getTagData();

        turret.trackAprilTag(telemetry);

//        telemetry.addData("Range", tagData[0]);
//        telemetry.addData("Bearing", tagData[1]);
//        telemetry.addData("ID", tagData[2]);
//        telemetry.addData("Camera State", this.aprilTags.visionPortal.getCameraState());
//        telemetry.addData("Detection Count", this.aprilTags.aprilTag.getDetections().size());
//        telemetry.addData("FPS", this.aprilTags.visionPortal.getFps());
        telemetry.update();
    }
}