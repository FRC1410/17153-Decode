package org.firstinspires.ftc.teamcode.Util;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class PIDController {
    private final double P;
    private final double I;
    private final double D;
    private double previousError;
    private double integral;
    private boolean firstCalculation = true;

    private ElapsedTime timer = new ElapsedTime();
    private double lastTime = 0;

    public PIDController(double P, double I, double D) {
        this.P = P;
        this.I = I;
        this.D = D;
    }

    public double calculate(double setPoint, double currentPosition) {
        double error = setPoint - currentPosition;
        double currentTime = timer.seconds();
        double deltaTime = currentTime - lastTime;

        // Prevent division by zero and huge derivative on first calculation
        if (firstCalculation || deltaTime <= 0.001) {
            previousError = error;
            lastTime = currentTime;
            firstCalculation = false;
            return P * error;
        }

        integral += error * deltaTime;
        double derivative = (error - previousError) / deltaTime;

        double output = (P * error) + (I * integral) + (D * derivative);

        previousError = error;
        lastTime = currentTime;

        return output;
    }

    public void reset() {
        integral = 0;
        previousError = 0;
        firstCalculation = true;
        lastTime = timer.seconds();
    }
}
