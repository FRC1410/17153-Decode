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

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Subsystem.Drivetrain;
import org.firstinspires.ftc.teamcode.Util.Constants;
import org.firstinspires.ftc.teamcode.Util.Toggle;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants.*;

import java.util.Timer;

@Autonomous(name="Pedro Pathing Test", group="Tests")
public class Auto extends OpMode {
    private Follower follower;
    private int pathState = 0;
    private final Pose startPose = new Pose(0,0, Math.toRadians(0));
    private final Pose pose2 = new Pose(0.01,0.01, Math.toRadians(90));
    private final Pose pose3 = new Pose(0,0, Math.toRadians(180));
    private final Pose pose4 = new Pose(0.01,0.01, Math.toRadians(0));
    private final Pose endPose = new Pose(0,0, Math.toRadians(360));
    private PathChain pathChain = new PathChain();
    private PathChain pathChain2 = new PathChain();
    private PathChain pathChain3 = new PathChain();
    private PathChain pathChain4 = new PathChain();

    public void initialize() {
        this.follower = createFollower(hardwareMap);
    }

    public void buildPaths(){
        // take the paths from dynamite and build them.
        pathChain = follower.pathBuilder()
                .addPath(new BezierLine(startPose, pose2)).setLinearHeadingInterpolation(startPose.getHeading(), endPose.getHeading()).build();
        pathChain2 = follower.pathBuilder()
                .addPath(new BezierLine(pose2, pose3).setLinearHeadingInterpolation(pose2.getHeading(), pose3.getHeading())).build();
        pathChain3 = follower.pathBuilder()
                .addPath(new BezierLine(pose3, pose4).setLinearHeadingInterpolation(pose3.getHeading(), pose4.getHeading())).build();
        pathChain4 = follower.pathBuilder()
                .addPath(new BezierLine(pose4, endPose).setLinearHeadingInterpolation(pose4.getHeading(), endPose.getHeading())).build();
    }

    public void runPath(){
        switch (pathState) {
            case 0: {
                follower.followPath(pathChain);
                pathState = 1;
                break;
            }
            case 1: {
                if (!follower.isBusy()){
                    follower.followPath(pathChain2);
                    pathState -= 1;
                }
                break;
            }
            case 2: {
                if (!follower.isBusy()){
                    follower.followPath(pathChain3);
                    pathState -= 1;
                }
                break;
            }
            case 3: {
                if (!follower.isBusy()){
                    follower.followPath(pathChain4);
                    pathState = -1;
                }
            }
            default: {}
        }
    }

    @Override
    public void init() {
        initialize();
        buildPaths();
        follower.setStartingPose(startPose);
        runPath();
    }
    public void stop(){
        telemetry.addData("X: ",follower.getPose().getX());
        telemetry.addData("Y: ",follower.getPose().getY());
        telemetry.addData("X: ",follower.getPose().getHeading());
        telemetry.update();
    }

    public void loop(){
        if (!follower.isBusy()){
            stop();
        } else {
            telemetry.addData("Follower status: ","Busy");
        }
        follower.update();
        runPath();
        telemetry.addData("X(inc): ",follower.getPose().getX());
        telemetry.addData("Y(inc): ",follower.getPose().getY());
        telemetry.addData("H(deg): ",Math.toDegrees(follower.getPose().getHeading()));
        telemetry.update();
    }
}
