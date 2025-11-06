package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.teamcode.Util.IDs.COLOUR_SENSOR_ID;
import static org.firstinspires.ftc.teamcode.Util.IDs.COLOUR_SENSOR_ID2;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Subsystem.Drivetrain;
import org.firstinspires.ftc.teamcode.Util.DriverUtil.ControlScheme;
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
        ControlScheme.init(gamepad1);


    }
    public void getTelemetry(){
        this.drivetrain.drivetrainData(telemetry);

        telemetry.update();
    }

    @Override
    public void loop() {
        this.drivetrain.mechanumDrive(
            ControlScheme.DRIVE_STRAFE.get(),
                ControlScheme.DRIVE_FB.get(),
                ControlScheme.DRIVE_ROTATE.get(),
                drivetrainToggle.toggleButton(ControlScheme.DRIVE_SLOW_MODE.get())
        );

        this.rumbler.halfimeRumble();

    getTelemetry();
    }
}