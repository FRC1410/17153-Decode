package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Subsystem.AprilTags;

@TeleOp(name="aprilTag localizer test")
public class Robot extends OpMode {
    private AprilTags aprilTags;
    
    public void init() {
        this.aprilTags = new AprilTags(hardwareMap);
        telemetry.setMsTransmissionInterval(50); // literal max due to main loop being capped at 20hz
    }

    @Override
    public void loop() {
        double[] tagData = aprilTags.getRobotPos();
        aprilTags.update();

        telemetry.addData("FPS", this.aprilTags.vision_portal.getFps());
        telemetry.addData("BotX", tagData[0]);
        telemetry.addData("BotY", tagData[1]);
        telemetry.addData("BotH (deg)", Math.toDegrees(tagData[2]));
        telemetry.addData("Detection Count", this.aprilTags.getDetections().size());
        telemetry.addData("Detections","");
        for(int i = 0; i < aprilTags.getDetections().size(); i++){
            telemetry.addData("Detection #"+i, aprilTags.getDetections().get(i).id);
        }
        telemetry.update();
    }
}