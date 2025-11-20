package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Subsystem.Drivetrain;
import org.firstinspires.ftc.teamcode.Subsystem.LazySusan;
import org.firstinspires.ftc.teamcode.Sensor.colorSensor;
import org.firstinspires.ftc.teamcode.Sensor.OpenCV;
@TeleOp
public class Robot extends OpMode {
    private final Drivetrain drivetrain = new Drivetrain();
    private final LazySusan lazySusan = new LazySusan();
//    private final Drivetrain drivetrain = new Drivetrain();
    private final colorSensor colour = new colorSensor();
    private final OpenCV openCV = new OpenCV();

    
    public void init() {
//        this.drivetrain.init(hardwareMap);
//        this.colour.init(hardwareMap, COLOUR_SENSOR_ID, COLOUR_SENSOR_ID2);

        this.lazySusan.init(hardwareMap);
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

        telemetry.addData("--- Lazy Susan Detection ---", "");
        telemetry.addData("Position 1 Color", this.openCV.getColorInPosition(1));
        telemetry.addData("Position 2 Color", this.openCV.getColorInPosition(2));
        telemetry.addData("Position 3 Color", this.openCV.getColorInPosition(3));
        telemetry.addData("Current Susan Position", this.lazySusan.getCurrentPosition());
        telemetry.addData("Shooter Position", this.lazySusan.getShooterPosition());

        updateTelemetry(telemetry);
    }

    @Override
    public void loop() {
        if (gamepad1.x) {
            this.lazySusan.shootColor(this.openCV, OpenCV.ArtifactColor.PURPLE, telemetry);
        } else if (gamepad1.y) {
            this.lazySusan.shootColor(this.openCV, OpenCV.ArtifactColor.GREEN, telemetry);
        } else {
            this.lazySusan.loop(gamepad1.a, gamepad1.b, gamepad1.dpad_up);
        }

        this.lazySusan.susanGoToState();
        doTelem();
    }



    @Override
    public void stop() {
        this.openCV.close();
    }
}