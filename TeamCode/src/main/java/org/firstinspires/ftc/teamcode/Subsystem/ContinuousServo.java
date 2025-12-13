package org.firstinspires.ftc.teamcode.Subsystem;

import static org.firstinspires.ftc.teamcode.Util.IDs.*;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Util.Toggle;

public class ContinuousServo {
    private CRServo servo;
    private final Toggle toggle = new Toggle();
    private boolean running = false;
    private double power = 1.0;

    public void init(HardwareMap hardwareMap) {
        this.servo = hardwareMap.get(CRServo.class, CONTINUOUS_SERVO_ID);
    }

    public void setPower(double power) {
        this.power = power;
    }

    public void loop(boolean toggleButton) {
        running = toggle.toggleButton(toggleButton);

        if (running) {
            servo.setPower(-1 * (power));
        } else {
            servo.setPower(0);
        }
    }

    public void stop() {
        running = false;
        servo.setPower(0);
    }

    public boolean isRunning() {
        return running;
    }

    public void continuousServoTelem(Telemetry telemetry) {
        telemetry.addData("Continuous Servo", running ? "Running" : "Stopped");
        telemetry.addData("Continuous Servo Power", String.format("%.2f", power));
    }
}

