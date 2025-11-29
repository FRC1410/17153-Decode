package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.teamcode.pedroPathing.Constants.createFollower;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierCurve;
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
    private final Pose startPose = new Pose(30,5, Math.toRadians(180));
    private final Pose pose2 = new Pose(7,15, Math.toRadians(90));
    private final Pose pose3 = new Pose(7,25, Math.toRadians(90));
    private final Pose endPose = new Pose(-10.5,10.5, Math.toRadians(136));
    private final Pose controlPoint = new Pose(8.375,13.875, Math.toRadians(0));
    private PathChain pathChain = new PathChain();
    private PathChain pathChain2 = new PathChain();
    private PathChain pathChain3 = new PathChain();
    private PathChain pathChain4 = new PathChain();
    private PathChain pathChain5 = new PathChain();
    public void initialize() {
        this.follower = createFollower(hardwareMap);
    }

    public void buildPaths(){
        // take the paths from dynamite and build them.
        pathChain = follower.pathBuilder()
                .addPath(new BezierLine(startPose, pose2)).setLinearHeadingInterpolation(startPose.getHeading(),pose2.getHeading()).build();
        pathChain2 = follower.pathBuilder()
                .addPath(new BezierLine(pose2, pose3)).setLinearHeadingInterpolation(pose2.getHeading(),pose3.getHeading()).build();
        pathChain3 = follower.pathBuilder()
                .addPath(new BezierLine(pose3, pose2)).setLinearHeadingInterpolation(pose3.getHeading(),endPose.getHeading()).build();
        pathChain4 = follower.pathBuilder()
                .addPath(new BezierLine(pose2, endPose)).setLinearHeadingInterpolation(endPose.getHeading(),startPose.getHeading()).build();
        pathChain5 = follower.pathBuilder()
                .addPath(new BezierLine(endPose, startPose)).setLinearHeadingInterpolation(endPose.getHeading(),startPose.getHeading()).build();
    }

    public void runPath(){
        switch (pathState) {
            case 0: {
                if (!follower.isBusy()) {
                    follower.followPath(pathChain, false);
                    pathState++;
                }
                break;
            }
            case 1: {
                if (!follower.isBusy()){
                    follower.followPath(pathChain2, false);
                    pathState++;
                }
                break;
            }
            case 2: {
                if (!follower.isBusy()){
                    follower.followPath(pathChain3, false);
                    pathState++;
                }
                break;
            }
            case 3: {
                if (!follower.isBusy()){
                    follower.followPath(pathChain4,false);
                    pathState++;
                }
                break;
            }
            case 4: {
                if (!follower.isBusy()){
                    follower.followPath(pathChain5, false);
                    pathState = 5;
                }
                break;
            }
            case 5: {
                if (!follower.isBusy()){
                    pathState = 0;
                    try{
                        //Thread.sleep(1000);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
                break;
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
        telemetry.addData("X(inc)",follower.getPose().getX());
        telemetry.addData("Y(inc)",follower.getPose().getY());
        telemetry.addData("H(deg)",Math.toDegrees(follower.getPose().getHeading()));
        telemetry.addData("Path State", pathState);
        switch (pathState){
            case 0: telemetry.addData("Next Pose X", pose2.getX());telemetry.addData("Next Pose Y", pose2.getY());telemetry.addData("Next Pose :", Math.toDegrees(pose2.getHeading()));
            case 1: telemetry.addData("Next Pose X", pose3.getX());telemetry.addData("Next Pose Y", pose3.getY());telemetry.addData("Next Pose H", Math.toDegrees(pose3.getHeading()));
            case 2: telemetry.addData("Next Pose X", pose2.getX());telemetry.addData("Next Pose Y", pose2.getY());telemetry.addData("Next Pose H", Math.toDegrees(pose2.getHeading()));
            case 3: telemetry.addData("Next Pose X", endPose.getX());telemetry.addData("Next Pose Y", endPose.getY());telemetry.addData("Next Pose H", Math.toDegrees(endPose.getHeading()));
            case 4: telemetry.addData("Next Pose X", startPose.getX());telemetry.addData("Next Pose Y", startPose.getY());telemetry.addData("Next Pose H", Math.toDegrees(startPose.getHeading()));
            case -1: telemetry.addData("At Path End centripetal","");
        }
        telemetry.addData("Next Pose", pose2.getX());
        telemetry.update();
    }
}
