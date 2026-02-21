package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Subsystem.AprilTags;

@TeleOp
public class Robot extends OpMode {
    private AprilTags aprilTags;
    
    public void init() {
//        this.drivetrain.init(hardwareMap);

        this.aprilTags = new AprilTags(hardwareMap);
    }

    @Override
    public void loop() {
        double[] tagData = aprilTags.getRobotPos();

        telemetry.addData("X", tagData[0]);
        telemetry.addData("Y", tagData[1]);
        telemetry.addData("H", tagData[2]);
        telemetry.addData("Camera State", this.aprilTags.vision_portal.getCameraState());
        telemetry.addData("Detection Count", this.aprilTags.april_tag.getDetections().size());
        telemetry.addData("FPS", this.aprilTags.vision_portal.getFps());
        telemetry.update();
    }
}