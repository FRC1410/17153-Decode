package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.teamcode.pedroPathing.Constants.createFollower;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.Path;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Subsystem.Drivetrain;
import org.firstinspires.ftc.teamcode.Util.Constants;
import org.firstinspires.ftc.teamcode.Util.Toggle;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants.*;
import org.firstinspires.ftc.teamcode.pedroPathing.VFpedroFollower;

import java.util.Timer;

@Autonomous(name="Pedro Pathing Test", group="Tests")
public class Auto extends LinearOpMode {
    private VFpedroFollower follower;
    private final Pose startPose = new Pose(0, 0, Math.toRadians(90));
    private final Pose endPose = new Pose(0, 200, Math.toRadians(90));
    private PathChain pathChain;

    @Override
    public void runOpMode() {
        // Initialize
        follower = createFollower(hardwareMap);
        follower.setStartingPose(startPose);

        // Build paths
        pathChain = follower.pathBuilder()
                .addPath(new BezierLine(startPose, endPose))
                .setLinearHeadingInterpolation(startPose.getHeading(), endPose.getHeading())
                .build();

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        waitForStart();

        // Start the path
        follower.followPath(pathChain);

        while (opModeIsActive()) {
            follower.update();

            // DETAILED DEBUG INFO
            telemetry.addData("=== STATUS ===", "");
            telemetry.addData("Follower Busy", follower.isBusy());
            telemetry.addData("At Parametric End", follower.atParametricEnd());
            telemetry.addData("Current T Value", follower.getCurrentTValue());

            telemetry.addData("=== POSE ===", "");
            telemetry.addData("X", follower.getPose().getX());
            telemetry.addData("Y", follower.getPose().getY());
            telemetry.addData("Heading (deg)", Math.toDegrees(follower.getPose().getHeading()));

            telemetry.addData("=== ERRORS ===", "");
            telemetry.addData("Drive Error", follower.getDriveError());
            telemetry.addData("Heading Error", follower.getHeadingError());
            telemetry.addData("Trans Error", follower.getTranslationalError().getMagnitude());

            telemetry.addData("=== VECTORS ===", "");
            telemetry.addData("Drive Vector", follower.getDriveVector().toString());
            telemetry.addData("Corrective Vector", follower.getCorrectiveVector().toString());
            telemetry.addData("Heading Vector", follower.getHeadingVector().toString());

            telemetry.addData("=== PIDF FLAGS ===", "");
            telemetry.addData("Use Drive", follower.getUseDrive());
            telemetry.addData("Use Heading", follower.getUseHeading());
            telemetry.addData("Use Translational", follower.getUseTranslational());
            telemetry.addData("Use Centripetal", follower.getUseCentripetal());

            telemetry.addData("=== DRIVETRAIN ===", "");
            telemetry.addData("Max Power", follower.getMaxPowerScaling());

            telemetry.update();

            if (!follower.isBusy()) {
                sleep(2000); // Pause so you can see final telemetry
                break;
            }
        }
    }
}