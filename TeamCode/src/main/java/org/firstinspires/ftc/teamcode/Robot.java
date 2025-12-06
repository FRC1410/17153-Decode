package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.teamcode.Util.IDs.COLOUR_SENSOR_ID;
import static org.firstinspires.ftc.teamcode.Util.IDs.COLOUR_SENSOR_ID2;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Subsystem.Drivetrain;
import org.firstinspires.ftc.teamcode.Subsystem.LazySusan;
import org.firstinspires.ftc.teamcode.Util.Toggle;
import org.firstinspires.ftc.teamcode.Sensor.colorSensor;
import org.firstinspires.ftc.teamcode.Sensor.OpenCV;
@TeleOp
public class Robot extends OpMode {
    private final Drivetrain drivetrain = new Drivetrain();
    private final LazySusan lazySusan = new LazySusan();

    private final colorSensor colour = new colorSensor();
    private final OpenCV openCV = new OpenCV();


    public void init() {
        this.drivetrain.init(hardwareMap);
        this.colour.init(hardwareMap, COLOUR_SENSOR_ID, COLOUR_SENSOR_ID2);

        this.openCV.init(hardwareMap, false);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        this.openCV.startStreaming();
        telemetry.update();
    }
    public void doTelem(){
        this.openCV.processVision(telemetry);
        updateTelemetry(telemetry);
        this.drivetrain.init(hardwareMap);
        this.lazySusan.init(hardwareMap);
    }

    @Override
    public void loop() {
        Toggle drivetrainToggle = null;
        this.drivetrain.mechanumDrive(
                gamepad1.left_stick_x,
                gamepad1.left_stick_y,
                gamepad1.right_stick_x,
                drivetrainToggle.toggleButton(gamepad1.a)
        );
     this.lazySusan.loop(gamepad1.x, gamepad1.a, gamepad1.b, gamepad1.right_bumper);
        doTelem();
    }



    @Override
    public void stop() {
        this.openCV.close();
    }
}