package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.teamcode.Util.IDs.COLOUR_SENSOR_ID;
import static org.firstinspires.ftc.teamcode.Util.IDs.COLOUR_SENSOR_ID2;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Subsystem.Drivetrain;
import org.firstinspires.ftc.teamcode.Util.Toggle;
import org.firstinspires.ftc.teamcode.Sensor.colorSensor;
import org.firstinspires.ftc.teamcode.Util.DriverUtil.Rumbler;
@TeleOp
public class Robot extends OpMode {
    private final Drivetrain drivetrain = new Drivetrain();

    private final Rumbler rumbler = new Rumbler();

    private final Toggle drivetrainToggle = new Toggle();

    
    public void init() {
        this.drivetrain.init(hardwareMap);


    }

    @Override
    public void loop() {
        this.drivetrain.mechanumDrive(
                gamepad1.left_stick_x,
                gamepad1.left_stick_y,
                gamepad1.right_stick_x,
                drivetrainToggle.toggleButton(gamepad1.a)
        );

        rumbler.rumble();

    }
}