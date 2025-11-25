package org.firstinspires.ftc.teamcode.pedroPathing;

import com.pedropathing.follower.FollowerConstants;

public class VFpedroFollowerConstants extends FollowerConstants {

    /**
     * Translational PIDF coefficients
     * Default Value: new PIDFCoefficients(0.1,0,0,0);
     */
    public VFpedroPIDFController coefficientsTranslationalPIDF = new VFpedroPIDFController(
            0.1,
            0,
            0,
            0.015);

    /**
     * Translational Integral
     * Default Value: new PIDFCoefficients(0,0,0,0);
     */
    public VFpedroPIDFController integralTranslational = new VFpedroPIDFController(
            0,
            0,
            0,
            0);

    /**
     * Heading error PIDF coefficients
     * Default Value: new PIDFCoefficients(1,0,0,0);
     */
    public VFpedroPIDFController coefficientsHeadingPIDF = new VFpedroPIDFController(
            1,
            0,
            0,
            0.01);


    /**
     * Drive PIDF coefficients
     * Default Value: new FilteredPIDFCoefficients(0.025,0,0.00001,0.6,0);
     */
    public VFpedroPIDFController coefficientsDrivePIDF = new VFpedroPIDFController(
            0.025,
            0,
            0.00001,
            0.6);

    /**
     * Secondary translational PIDF coefficients (don't use integral).
     * Default Value: new PIDFCoefficients(0.3, 0, 0.01, 0)
     */
    public VFpedroPIDFController coefficientsSecondaryTranslationalPIDF = new VFpedroPIDFController(
            0.3,
            0,
            0.01,
            0);

    /**
     * Secondary translational Integral value.
     * Default Value: new PIDFCoefficients(0, 0, 0, 0)
     */
    public VFpedroPIDFController integralSecondaryTranslational = new VFpedroPIDFController(
            0,
            0,
            0,
            0.015);

    /**
     * The limit at which the heading PIDF switches between the main and secondary heading PIDFs.
     * Default Value: Math.PI / 20
     */
    public double headingPIDFSwitch = Math.PI / 20;

    /**
     * Secondary heading error PIDF coefficients.
     * Default Value: new PIDFCoefficients(5, 0, 0.08, 0)
     */
    public VFpedroPIDFController coefficientsSecondaryHeadingPIDF = new VFpedroPIDFController(
            5,
            0,
            0.08,
            0.01);

    /**
     * The limit at which the heading PIDF switches between the main and secondary drive PIDFs.
     * Default Value: 20
     */
    public double drivePIDFSwitch = 20;

    /**
     * Secondary drive PIDF coefficients.
     * Default Value: new FilteredPIDFCoefficients(0.02, 0, 0.000005, 0.6, 0)
     */
    public VFpedroPIDFController coefficientsSecondaryDrivePIDF = new VFpedroPIDFController(
            0.02,
            0,
            0.000005,
            0.6);

    /**
     * This scales the translational error correction power when the Follower is holding a Point.
     * Default Value: 0.45
     */
    public double holdPointTranslationalScaling = 0.45;

    /**
     * This scales the heading error correction power when the Follower is holding a Point.
     * Default Value: 0.35
     */
    public double holdPointHeadingScaling = 0.35;

    /**
     * This is the number of steps the search for the closest point uses. More steps lead to bigger
     * accuracy. However, more steps also take more time.
     * Default Value: 10
     */
    public int BEZIER_CURVE_SEARCH_LIMIT = 10;

    /**
     * This activates/deactivates the secondary translational PIDF. It takes over at a certain translational error
     *
     * @see #translationalPIDFSwitch
     * Default Value: false
     */
    public boolean useSecondaryTranslationalPIDF = false;

    /**
     * Use the secondary heading PIDF. It takes over at a certain heading error
     *
     * @see #headingPIDFSwitch
     * Default Value: false
     */
    public boolean useSecondaryHeadingPIDF = false;

    /**
     * Use the secondary drive PIDF. It takes over at a certain drive error
     *
     * @see #drivePIDFSwitch
     * Default Value: false
     */
    public boolean useSecondaryDrivePIDF = false;

    /**
     * The limit at which the translational PIDF switches between the main and secondary translational PIDFs,
     * if the secondary PID is active.
     * Default Value: 3
     */
    public double translationalPIDFSwitch = 3;

    /**
     * Threshold that the turn and turnTo methods will be considered to be finished
     * In Radians
     * Default Value: 0.01
     */
    public double turnHeadingErrorThreshold = 0.01;

    /**
     * Centripetal force to power scaling
     * Default Value: 0.0005
     */
    public double centripetalScaling = 0.0005;

    /**
     * This is the default value for the automatic hold end. If this is set to true, the Follower will
     * automatically hold the end when it reaches the end of the Path.
     * Default Value: true
     */
    public boolean automaticHoldEnd = true;

    /**
     * This is the mass of the robot. This is used to calculate the centripetal force.
     * Default Value: 10.65
     */
    public double mass = 10.65;

    /** Acceleration of the drivetrain when power is cut in inches/second^2 (should be negative)
     * if not negative, then the robot thinks that its going to go faster under 0 power
     *  Default Value: -34.62719
     * This value is found via 'ForwardZeroPowerAccelerationTuner'*/
    public double forwardZeroPowerAcceleration = -34.62719;

    /** Acceleration of the drivetrain when power is cut in inches/second^2 (should be negative)
     * if not negative, then the robot thinks that its going to go faster under 0 power
     *  Default Value: -78.15554
     * This value is found via 'LateralZeroPowerAccelerationTuner'*/
    public double lateralZeroPowerAcceleration = -78.15554;

    public VFpedroFollowerConstants secondaryTranslationalPIDFCoefficients(VFpedroPIDFController secondaryTranslationalPIDFCoefficients) {
        this.coefficientsSecondaryTranslationalPIDF = secondaryTranslationalPIDFCoefficients;
        useSecondaryTranslationalPIDF = true;
        return this;
    }

    public VFpedroFollowerConstants headingPIDFSwitch(double headingPIDFSwitch) {
        this.headingPIDFSwitch = headingPIDFSwitch;
        return this;
    }
    public VFpedroFollowerConstants drivePIDFSwitch(double drivePIDFSwitch) {
        this.drivePIDFSwitch = drivePIDFSwitch;
        return this;
    }

    public VFpedroFollowerConstants holdPointTranslationalScaling(double holdPointTranslationalScaling) {
        this.holdPointTranslationalScaling = holdPointTranslationalScaling;
        return this;
    }

    public VFpedroFollowerConstants holdPointHeadingScaling(double holdPointHeadingScaling) {
        this.holdPointHeadingScaling = holdPointHeadingScaling;
        return this;
    }

    public VFpedroFollowerConstants BEZIER_CURVE_SEARCH_LIMIT(int BEZIER_CURVE_SEARCH_LIMIT) {
        this.BEZIER_CURVE_SEARCH_LIMIT = BEZIER_CURVE_SEARCH_LIMIT;
        return this;
    }

    public VFpedroFollowerConstants useSecondaryTranslationalPIDF(boolean useSecondaryTranslationalPIDF) {
        this.useSecondaryTranslationalPIDF = useSecondaryTranslationalPIDF;
        return this;
    }

    public VFpedroFollowerConstants useSecondaryHeadingPIDF(boolean useSecondaryHeadingPIDF) {
        this.useSecondaryHeadingPIDF = useSecondaryHeadingPIDF;
        return this;
    }

    public VFpedroFollowerConstants useSecondaryDrivePIDF(boolean useSecondaryDrivePIDF) {
        this.useSecondaryDrivePIDF = useSecondaryDrivePIDF;
        return this;
    }

    public VFpedroFollowerConstants translationalPIDFSwitch(double translationalPIDFSwitch) {
        this.translationalPIDFSwitch = translationalPIDFSwitch;
        return this;
    }

    public VFpedroFollowerConstants turnHeadingErrorThreshold(double turnHeadingErrorThreshold) {
        this.turnHeadingErrorThreshold = turnHeadingErrorThreshold;
        return this;
    }

    public VFpedroFollowerConstants centripetalScaling(double centripetalScaling) {
        this.centripetalScaling = centripetalScaling;
        return this;
    }

    public VFpedroFollowerConstants automaticHoldEnd(boolean automaticHoldEnd) {
        this.automaticHoldEnd = automaticHoldEnd;
        return this;
    }

    public VFpedroFollowerConstants mass(double mass) {
        this.mass = mass;
        return this;
    }

    public VFpedroFollowerConstants forwardZeroPowerAcceleration(double forwardZeroPowerAcceleration) {
        this.forwardZeroPowerAcceleration = forwardZeroPowerAcceleration;
        return this;
    }

    public VFpedroFollowerConstants lateralZeroPowerAcceleration(double lateralZeroPowerAcceleration) {
        this.lateralZeroPowerAcceleration = lateralZeroPowerAcceleration;
        return this;
    }

    public double getDrivePIDFSwitch() {
        return drivePIDFSwitch;
    }

    public void setDrivePIDFSwitch(double drivePIDFSwitch) {
        this.drivePIDFSwitch = drivePIDFSwitch;
    }

    public double getHoldPointTranslationalScaling() {
        return holdPointTranslationalScaling;
    }

    public void setHoldPointTranslationalScaling(double holdPointTranslationalScaling) {
        this.holdPointTranslationalScaling = holdPointTranslationalScaling;
    }

    public double getHoldPointHeadingScaling() {
        return holdPointHeadingScaling;
    }

    public void setHoldPointHeadingScaling(double holdPointHeadingScaling) {
        this.holdPointHeadingScaling = holdPointHeadingScaling;
    }

    public int getBEZIER_CURVE_SEARCH_LIMIT() {
        return BEZIER_CURVE_SEARCH_LIMIT;
    }

    public void setBEZIER_CURVE_SEARCH_LIMIT(int BEZIER_CURVE_SEARCH_LIMIT) {
        this.BEZIER_CURVE_SEARCH_LIMIT = BEZIER_CURVE_SEARCH_LIMIT;
    }

    public boolean isUseSecondaryTranslationalPIDF() {
        return useSecondaryTranslationalPIDF;
    }

    public void setUseSecondaryTranslationalPIDF(boolean useSecondaryTranslationalPIDF) {
        this.useSecondaryTranslationalPIDF = useSecondaryTranslationalPIDF;
    }

    public boolean isUseSecondaryHeadingPIDF() {
        return useSecondaryHeadingPIDF;
    }

    public void setUseSecondaryHeadingPIDF(boolean useSecondaryHeadingPIDF) {
        this.useSecondaryHeadingPIDF = useSecondaryHeadingPIDF;
    }

    public boolean isUseSecondaryDrivePIDF() {
        return useSecondaryDrivePIDF;
    }

    public void setUseSecondaryDrivePIDF(boolean useSecondaryDrivePIDF) {
        this.useSecondaryDrivePIDF = useSecondaryDrivePIDF;
    }

    public double getTranslationalPIDFSwitch() {
        return translationalPIDFSwitch;
    }

    public void setTranslationalPIDFSwitch(double translationalPIDFSwitch) {
        this.translationalPIDFSwitch = translationalPIDFSwitch;
    }

    public double getTurnHeadingErrorThreshold() {
        return turnHeadingErrorThreshold;
    }

    public void setTurnHeadingErrorThreshold(double turnHeadingErrorThreshold) {
        this.turnHeadingErrorThreshold = turnHeadingErrorThreshold;
    }

    public double getCentripetalScaling() {
        return centripetalScaling;
    }

    public void setCentripetalScaling(double centripetalScaling) {
        this.centripetalScaling = centripetalScaling;
    }

    public boolean isAutomaticHoldEnd() {
        return automaticHoldEnd;
    }

    public void setAutomaticHoldEnd(boolean automaticHoldEnd) {
        this.automaticHoldEnd = automaticHoldEnd;
    }

    public double getMass() {
        return mass;
    }

    public void setMass(double mass) {
        this.mass = mass;
    }

    public double getForwardZeroPowerAcceleration() {
        return forwardZeroPowerAcceleration;
    }

    public void setForwardZeroPowerAcceleration(double forwardZeroPowerAcceleration) {
        this.forwardZeroPowerAcceleration = forwardZeroPowerAcceleration;
    }

    public double getLateralZeroPowerAcceleration() {
        return lateralZeroPowerAcceleration;
    }

    public void setLateralZeroPowerAcceleration(double lateralZeroPowerAcceleration) {
        this.lateralZeroPowerAcceleration = lateralZeroPowerAcceleration;
    }
}
