package org.firstinspires.ftc.teamcode.Util;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class PIDController {
    private final double P;
    private final double I;
    private final double D;
    private double previousError;
    private double integral;

    private ElapsedTime timer = new ElapsedTime();

    public PIDController(double P, double I, double D) {
        this.P = P;
        this.I = I;
        this.D = D;
    }

    public double calculate(double setPoint, double currentPosition) {
        double error = setPoint - currentPosition;
        integral += error * timer.seconds();
        double derivative = (error - previousError) / timer.seconds();

        double output = (P * error) + (I * integral) + (D * derivative);

        previousError = error;
        timer.reset();

        return output;
    }


}
