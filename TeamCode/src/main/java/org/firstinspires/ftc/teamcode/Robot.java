package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.teamcode.Util.IDs.COLOUR_SENSOR_ID;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Subsystem.Drivetrain;
import org.firstinspires.ftc.teamcode.Util.Toggle;
import org.firstinspires.ftc.teamcode.Sensor.colorSensor;
@TeleOp
public class Robot extends OpMode {
//    private final Drivetrain drivetrain = new Drivetrain();
    private final colorSensor colour = new colorSensor();

    
    public void init() {
//        this.drivetrain.init(hardwareMap);
        this.colour.init(hardwareMap, COLOUR_SENSOR_ID);

    }

    @Override
    public void loop() {
        this.colour.updateRGB();
        this.colour.colourData(telemetry);
//        this.drivetrain.mechanumDrive(
//                gamepad1.left_stick_x,
//                gamepad1.left_stick_y,
//                gamepad1.right_stick_x,
//                drivetrainToggle.toggleButton(gamepad1.a)
//        );

    }
}