package org.firstinspires.ftc.teamcode.pedroPathing;

import com.pedropathing.control.FilteredPIDFCoefficients;
import com.pedropathing.control.PIDFCoefficients;

import org.firstinspires.ftc.teamcode.Util.PIDController;

public class VFpedroPIDFController extends PIDController {
    public VFpedroPIDFController(double P, double I, double D, double F) {
        super(P, I, D);
    }
    public void setCoefficients(VFpedroPIDFController in){}
    public void setCoefficients(FilteredPIDFCoefficients in){}
    public void reset(){}
    private PIDFCoefficients coefficients;

    private double previousError;
    private double error;
    private double position;
    private double targetPosition;
    private double errorIntegral;
    private double errorDerivative;
    private double feedForwardInput;

    private long previousUpdateTimeNano;
    private long deltaTimeNano;

    /**
     * This creates a new PIDFController from a CustomPIDFCoefficients.
     *
     * @param set the coefficients to use.
     */
    /**
     * This takes the current error and runs the PIDF on it.
     *
     * @return this returns the value of the PIDF from the current error.
     */
    public double run() {
        return this.calculate(targetPosition, position);
    }

    /**
     * This can be used to update the PIDF's current position when inputting a current position and
     * a target position to calculate error. This will update the error from the current position to
     * the target position specified.
     *
     * @param position This is the current position.
     */
    public void updatePosition(double position) {
        this.position = position;
        previousError = error;
        error = targetPosition - this.position;

        deltaTimeNano = System.nanoTime() - previousUpdateTimeNano;
        previousUpdateTimeNano = System.nanoTime();

        errorIntegral += error * (deltaTimeNano / Math.pow(10.0, 9));
        errorDerivative = (error - previousError) / (deltaTimeNano / Math.pow(10.0, 9));
    }

    /**
     * As opposed to updating position against a target position, this just sets the error to some
     * specified value.
     *
     * @param error The error specified.
     */
    public void updateError(double error) {
        previousError = this.error;
        this.error = error;
        long nanoTime = System.nanoTime();

        deltaTimeNano = nanoTime - previousUpdateTimeNano;
        previousUpdateTimeNano = nanoTime;

        errorIntegral += error * (deltaTimeNano / Math.pow(10.0, 9));
        errorDerivative = (error - previousError) / (deltaTimeNano / Math.pow(10.0, 9));
    }

    /**
     * This can be used to update the feedforward equation's input, if applicable.
     *
     * @param input the input into the feedforward equation.
     */
    public void updateFeedForwardInput(double input) {
        feedForwardInput = input;
    }

    /**
     * This resets all the PIDF's error and position values, as well as the time stamps.
     */

    /**
     * This is used to set the target position if the PIDF is being run with current position and
     * target position inputs rather than error inputs.
     *
     * @param set this sets the target position.
     */
    public void setTargetPosition(double set) {
        targetPosition = set;
    }

    /**
     * This returns the target position of the PIDF.
     *
     * @return this returns the target position.
     */
    public double getTargetPosition() {
        return targetPosition;
    }

    /**
     * This is used to set the coefficients of the PIDF.
     *
     * @param set the coefficients that the PIDF will use.
     */
    public void setCoefficients(PIDFCoefficients set) {
        coefficients = set;
    }

    /**
     * This returns the PIDF's current coefficients.
     *
     * @return this returns the current coefficients.
     */
    public PIDFCoefficients getCoefficients() {
        return coefficients;
    }

    /**
     * This sets the proportional (P) coefficient of the PIDF only.
     *
     * @param set this sets the P coefficient.
     */
    public void setP(double set) {
        coefficients.P = set;
    }

    /**
     * This returns the proportional (P) coefficient of the PIDF.
     *
     * @return this returns the P coefficient.
     */
    public double P() {
        return coefficients.P;
    }

    /**
     * This sets the integral (I) coefficient of the PIDF only.
     *
     * @param set this sets the I coefficient.
     */
    public void setI(double set) {
        coefficients.I = set;
    }

    /**
     * This returns the integral (I) coefficient of the PIDF.
     *
     * @return this returns the I coefficient.
     */
    public double I() {
        return coefficients.I;
    }

    /**
     * This sets the derivative (D) coefficient of the PIDF only.
     *
     * @param set this sets the D coefficient.
     */
    public void setD(double set) {
        coefficients.D = set;
    }

    /**
     * This returns the derivative (D) coefficient of the PIDF.
     *
     * @return this returns the D coefficient.
     */
    public double D() {
        return coefficients.D;
    }

    /**
     * This sets the feedforward (F) constant of the PIDF only.
     *
     * @param set this sets the F constant.
     */
    public void setF(double set) {
        coefficients.F = set;
    }

    /**
     * This returns the feedforward (F) constant of the PIDF.
     *
     * @return this returns the F constant.
     */
    public double F() {
        return coefficients.F;
    }

    /**
     * This returns the current error of the PIDF.
     *
     * @return this returns the error.
     */
    public double getError() {
        return error;
    }

    /**
     * This returns the current derivative of the error.
     * @return the derivative
     */
    public double getErrorDerivative() {
        return errorDerivative;
    }
}
