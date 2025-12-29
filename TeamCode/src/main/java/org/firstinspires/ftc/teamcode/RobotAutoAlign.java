package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.teamcode.pedroPathing.Constants.createFollower;
import static org.firstinspires.ftc.teamcode.pedroPathing.Tuning.drawCurrent;
import static org.firstinspires.ftc.teamcode.pedroPathing.Tuning.drawCurrentAndHistory;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Subsystem.ContinuousServo;
import org.firstinspires.ftc.teamcode.Subsystem.HoodServo;
import org.firstinspires.ftc.teamcode.Subsystem.Intake;
import org.firstinspires.ftc.teamcode.Subsystem.LazySusan;
import org.firstinspires.ftc.teamcode.Subsystem.Shooter;
import org.firstinspires.ftc.teamcode.Util.DriverUtil.ControlScheme;
import org.firstinspires.ftc.teamcode.Util.DriverUtil.Rumbler;
import org.firstinspires.ftc.teamcode.Util.RobotStates;
import org.firstinspires.ftc.teamcode.Util.Toggle;

@TeleOp(name="Robot TeleOp w AutoAlign")
public class RobotAutoAlign extends OpMode {
    // pedro things
    private Follower follower;
    private boolean autoAligning = false;
    private Pose startPose;
    private Pose[] firingPoses = new Pose[3];
    private double[] firingServoPoses = new double[]{0,0,0};
    private String ALIENCE = "BLUE";
    private String startingLocation = "AT GOAL";
    // normal things
    private final Shooter shooter = new Shooter();
    private final Rumbler driverRumbler = new Rumbler();
    private final Rumbler operatorRumbler = new Rumbler();
    private final LazySusan lazySusan = new LazySusan();
    private final Intake intake = new Intake();
    private final HoodServo hoodServo = new HoodServo();
    private final ContinuousServo continuousServo = new ContinuousServo();

    private final Toggle drivetrainToggle = new Toggle();

    @Override
    public void init() {
        ControlScheme.initDriver(gamepad1);
        ControlScheme.initOperator(gamepad2);
        this.intake.init(hardwareMap);
        this.shooter.init(hardwareMap);
        this.hoodServo.init(hardwareMap);
        // starts pedro localiser and drivetrain
        setupPoses();
        this.follower = createFollower(hardwareMap);
        this.follower.setStartingPose(startPose);
    }

    @Override
    public void init_loop() {
        follower.update();
        drawCurrent();
    }

    @Override
    public void start() {
        follower.startTeleopDrive();
        follower.update();
        driverRumbler.startMatchTimer();
        operatorRumbler.startMatchTimer();
    }

    private void setupPoses(){
        if (startingLocation.equals("AT GOAL")){
            if (ALIENCE.equals("BLUE")){
                startPose = new Pose(22.6, 126.8, Math.toRadians(323));

                // at blue goal
                firingPoses[0] = new Pose(23, 126, Math.toRadians(144));
                // far from goal
                firingPoses[1] = new Pose(66, 76, Math.toRadians(135));
                // from small zone
                firingPoses[2] = new Pose(60, 10, Math.toRadians(113));
            } else {
                startPose = new Pose(120, 126, Math.toRadians(217));

                // at red goal
                firingPoses[0] = new Pose(127.5, 127.6,Math.toRadians(38));
                // far from goal
                firingPoses[1] = new Pose(78.5, 77.5, Math.toRadians(48));
                // from small zone
                firingPoses[2] = new Pose(85, 12, Math.toRadians(69));
            }
        } else {
            if (ALIENCE.equals("BLUE")){
                startPose = new Pose(56.5, 8.5, Math.toRadians(90));

                // at blue goal
                firingPoses[0] = new Pose(23, 126, Math.toRadians(144));
                // far from goal
                firingPoses[1] = new Pose(66, 76, Math.toRadians(135));
                // from small zone
                firingPoses[2] = new Pose(60, 10, Math.toRadians(113));
            } else {
                startPose = new Pose(88, 8, Math.toRadians(90));

                // at red goal
                firingPoses[0] = new Pose(127.5, 127.6,Math.toRadians(38));
                // far from goal
                firingPoses[1] = new Pose(78.5, 77.5, Math.toRadians(48));
                // from small zone
                firingPoses[2] = new Pose(85, 12, Math.toRadians(69));
            }
        }
    }

    public Pose getNearestFirePose(Pose currentPose){
        double closestDistance = Double.MAX_VALUE;
        Pose closestPose = new Pose();
        for (int i = 0; i < firingPoses.length; i++){
            Pose checkingPose = firingPoses[i];
            double distance = Math.sqrt(Math.pow(checkingPose.getX()-currentPose.getX(),2)+Math.pow(checkingPose.getY()- currentPose.getY(), 2));
            if (distance < closestDistance){
                closestDistance = distance;
                closestPose = checkingPose;
            }
        }
        return closestPose;
    }
    public double getNearestPosesServoVal(Pose currentPose){
        double closestDistance = Double.MAX_VALUE;
        double closestFiringServoPos = 0;
        for (int i = 0; i < firingPoses.length; i++){
            Pose checkingPose = firingPoses[i];
            double distance = Math.sqrt(Math.pow(checkingPose.getX()-currentPose.getX(),2)+Math.pow(checkingPose.getY()- currentPose.getY(), 2));
            if (distance < closestDistance){
                closestDistance = distance;
                closestFiringServoPos = firingServoPoses[i];
            }
        }
        return closestFiringServoPos;
    }

    public void doTelemetry() {
        // pedro
        // normal
        this.hoodServo.hoodTelem(telemetry);
        this.shooter.addTelemetry(telemetry);
        telemetry.update();
    }
    @Override
    public void loop() {
        // do the autoalign things
        if (autoAligning){
            if (!follower.isBusy()) {
                // get current pose and find the nearest firing position
                Pose currentPose = follower.getPose();
                // get and set servo
                double servoVal = getNearestPosesServoVal(currentPose);
                hoodServo.RAWloop(servoVal);
                // get fire pose
                Pose firingPose = getNearestFirePose(currentPose);
                // create path
                PathChain path = follower.pathBuilder()
                        .addPath(new BezierLine(currentPose, firingPose))
                        .setLinearHeadingInterpolation(currentPose.getHeading(), firingPose.getHeading())
                        .build();
                // start running path
                follower.followPath(path, true);
                // set back to normal teleop
                autoAligning = false;
            }
        } else {
            if (!follower.isBusy()) {
                // move the bot
                follower.setTeleOpDrive(-gamepad1.left_stick_y, -gamepad1.left_stick_x, -gamepad1.right_stick_x, true);
                // update localiser
                follower.update();
            }
        }
        // noraml stuff
        if (ControlScheme.SHOOTER_CYCLE.get()) {
            this.shooter.cycle(telemetry);
        }

        if (ControlScheme.SHOOTER_REVERSE.get()) {
            this.shooter.runBackward();
        } else {
            this.shooter.stopBackward();
        }

        this.shooter.update();

        if (this.shooter.isAtTargetRPM() && this.shooter.shooterStatus == RobotStates.ShooterStates.FORWARD) {
            this.intake.run(
                    ControlScheme.INTAKE_IN.get(),
                    ControlScheme.INTAKE_OUT.get()
            );
        } else {
            this.intake.run(0, 0);
        }

        this.hoodServo.loop(
                ControlScheme.HOOD_POS_ONE.get(),
                ControlScheme.HOOD_POS_TWO.get(),
                ControlScheme.HOOD_POS_THREE.get(),
                ControlScheme.HOOD_POS_FOUR.get(),
                ControlScheme.HOOD_POS_FIVE.get()
        );

        this.driverRumbler.halftimeRumble(gamepad1);
        this.operatorRumbler.halftimeRumble(gamepad2);

        doTelemetry();
    }
}
