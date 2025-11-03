package org.firstinspires.ftc.teamcode;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.Path;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Subsystem.Drivetrain;
import org.firstinspires.ftc.teamcode.Util.Toggle;

import java.util.Timer;

@Autonomous(name="Pedro Pathing Test", group="Tests")
public class Auto extends LinearOpMode {
    private Follower follower;
    private Timer pathTimer, actionTimer, opmodeTimer;
    private int pathState;
    private Path autoPath;
    private final Drivetrain drivetrain = new Drivetrain();

    private final Toggle drivetrainToggle = new Toggle();

    private final Pose startPose = new Pose(0,0, Math.toRadians(90));
    private final Pose endPose = new Pose(10,0, Math.toRadians(90));
    private PathChain pathChain = new PathChain();
    public void buildPaths(){
        // take the paths from dynamite and build them.
        pathChain = follower.pathBuilder()
                .addPath(new BezierLine(startPose, endPose))
                .setLinearHeadingInterpolation(startPose.getHeading(), endPose.getHeading())
                .build();
    }

    @Override
    public void runOpMode() {
        buildPaths();
        waitForStart();
        // run pedro path

        while (opModeIsActive()){
            // move the robot forward like 10in
            follower.followPath(pathChain);
        }
    }
}
