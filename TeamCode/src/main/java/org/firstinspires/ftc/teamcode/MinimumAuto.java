package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.teamcode.pedroPathing.Constants.createFollower;

import com.bylazar.configurables.PanelsConfigurables;
import com.bylazar.field.FieldManager;
import com.bylazar.field.PanelsField;
import com.bylazar.field.Style;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.math.Vector;
import com.pedropathing.paths.Path;
import com.pedropathing.paths.PathChain;
import com.pedropathing.util.PoseHistory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.Subsystem.Shooter;

@Autonomous(name="PP Auto Min", group="Tests")
public class MinimumAuto extends OpMode {
    private Shooter shooter = new Shooter();

    private Follower follower;
    private PoseHistory poseHistory;
    private int pathState = 0;

    private Pose startPose = new Pose(88,8, Math.toRadians(90)); // start path pos

    private Pose SmiddleArtifactPose = new Pose(100,60, Math.toRadians(0)); // start pickup middle articfacts pos
    private Pose EmiddleArtifactPose = new Pose(125,60, Math.toRadians(0)); // end pickup middle articfacts pos

    private Pose SlastArtifactPose = new Pose(104, 84, Math.toRadians(0));
    private Pose ElastArtifactPose = new Pose(132, 84, Math.toRadians(0));

    private Pose SfirstArtifactPose = new Pose(104, 35, Math.toRadians(0));
    private Pose EfirstArtifactPose = new Pose(132, 35, Math.toRadians(0));

    private Pose ClassifierGatePose = new Pose(128.5, 70.5, 270);

    private Pose shootPose = new Pose(120,120, Math.toRadians(43)); // shooting pose

    private Pose endPose = new Pose(73,120, Math.toRadians(270)); // end of path pose, it currently goes to the center (ish) of the larger shooting area

    private Pose controlPoint0 = new Pose(104, 65); // this is used to stop the robot from hitting the balls when going between the classifier ramp laver, and the nearest set of balls to the end goals.

    private PathChain FromShootingToEndPathChain = new PathChain();

    private PathChain LastArtifactsPathChain = new PathChain();
    private PathChain FromShootingToLastArtifactsPathCain = new PathChain();
    private PathChain FromStartToLastArtifactPathChain = new PathChain();
    private PathChain FromLastArtifactsToShootPathChain = new PathChain();
    private PathChain FromLastArtifactsToEndPathChain = new PathChain();
    private PathChain FromLastArtifactsToClasifierGate = new PathChain();

    private PathChain MiddleArtifactsPathChain = new PathChain();
    private PathChain FromShootingToMiddleArtifactsPathChain = new PathChain();
    private PathChain FromStartToMiddleArtifactsPathChain = new PathChain();
    private PathChain FromMiddleArtifactsToShootPathChain = new PathChain();
    private PathChain FromMiddleArtifactsToEndPathChain = new PathChain();
    private PathChain FromMiddleArtifactsToClassifierGate = new PathChain();

    private PathChain FirstArtifactsPathChain = new PathChain();
    private PathChain FromShootingToFirstArtifactPathChain = new PathChain();
    private PathChain FromStartToFirstArtifactPathChain = new PathChain();
    private PathChain FromFirstArtifactsToShootPathChain = new PathChain();
    private PathChain FromFirstArtifactsToEndPathChain = new PathChain();
    private PathChain FromFirstArtifactsToClasifierGate = new PathChain();

    private PathChain FromClasifierGateToFirstArtifactsPathChain = new PathChain();
    private PathChain FromClasifierGateToMiddleArtifactsPathChain = new PathChain();
    private PathChain FromClassifierGateToLastArtifactsPathChain = new PathChain();
    private PathChain FromStartToClassifierGatePathChain = new PathChain();
    private PathChain FromShootingToClassifierGatePathChain = new PathChain();
    private PathChain FromClassifierGateToEndPathChain = new PathChain();

    private PathChain FromShootingToStartPathChain = new PathChain();
    private PathChain FromStartToShootingPathChain = new PathChain();

    public void initialize() {
        this.follower = createFollower(hardwareMap);
    }

    // "AT GOAL" means that we are starting in the, if it is anything else, it will assume we are starting in the small starting area.
    private String startingLocation = "AT GOAL";
    private String ALIENCE = "BLUE"; // defines what it is named. what allience we are on.
    private void shiftPosesBasedOnAlliance(){
        if (ALIENCE.equals("BLUE")){
            // flip along the middle by modifying X values with: -x + 144
            SmiddleArtifactPose = new Pose(-1*SfirstArtifactPose.getX()+144, SfirstArtifactPose.getY(), SfirstArtifactPose.getHeading());
            EmiddleArtifactPose = new Pose(-1*EmiddleArtifactPose.getX()+144, EmiddleArtifactPose.getY(), EmiddleArtifactPose.getHeading());

            SlastArtifactPose = new Pose(-1*SlastArtifactPose.getX()+144, SlastArtifactPose.getY(), SlastArtifactPose.getHeading());
            ElastArtifactPose = new Pose(-1*ElastArtifactPose.getX()+144, ElastArtifactPose.getY(), ElastArtifactPose.getHeading());

            SfirstArtifactPose = new Pose(-1*SfirstArtifactPose.getX()+144, SfirstArtifactPose.getY(), SfirstArtifactPose.getHeading());
            EfirstArtifactPose = new Pose(-1*EfirstArtifactPose.getX()+144, EfirstArtifactPose.getY(), EfirstArtifactPose.getHeading());

            shootPose = new Pose(-1*shootPose.getX()+144, shootPose.getY(), 130);

            endPose = new Pose(-1*endPose.getX()+144, endPose.getY(), endPose.getHeading());

            ClassifierGatePose = new Pose(-1*ClassifierGatePose.getX()+144, ClassifierGatePose.getY(), 90);

            controlPoint0 = new Pose(-1*controlPoint0.getX()+144, controlPoint0.getY());
        }
        if (startingLocation.equals("AT GOAL")){
            if (ALIENCE.equals("BLUE")){
                startPose = new Pose(23, 126, 323);
            } else {
                startPose = new Pose(120, 126, 217);
            }
        } else {
            if (ALIENCE.equals("BLUE")){
                startPose = new Pose(56.5, 8.5, 90);
            } else {
                startPose = new Pose(88, 8, 90);
            }
        }
    }
    public void buildPaths(){
        // i have not tested this function, lets hope it works!
        shiftPosesBasedOnAlliance();
        // i sure hope you like spaghetti!
        FromStartToShootingPathChain = follower.pathBuilder()
                .addPath(new BezierLine(startPose, shootPose))
                .setLinearHeadingInterpolation(startPose.getHeading(), shootPose.getHeading())
                .build();
        FromShootingToEndPathChain = follower.pathBuilder()
                .addPath(new BezierLine(shootPose, endPose))
                .setLinearHeadingInterpolation(shootPose.getHeading(), endPose.getHeading())
                .build();
        FromLastArtifactsToEndPathChain = follower.pathBuilder()
                .addPath(new BezierLine(ElastArtifactPose, endPose))
                .setLinearHeadingInterpolation(ElastArtifactPose.getHeading(), endPose.getHeading())
                .build();
        FromLastArtifactsToClasifierGate = follower.pathBuilder()
                .addPath(new BezierLine(ElastArtifactPose, ClassifierGatePose))
                .setLinearHeadingInterpolation(ElastArtifactPose.getHeading(), ClassifierGatePose.getHeading())
                .build();
        FromMiddleArtifactsToEndPathChain = follower.pathBuilder()
                .addPath(new BezierLine(EmiddleArtifactPose, endPose))
                .setLinearHeadingInterpolation(EmiddleArtifactPose.getHeading(), endPose.getHeading())
                .build();
        FromMiddleArtifactsToClassifierGate = follower.pathBuilder()
                .addPath(new BezierLine(EmiddleArtifactPose, ClassifierGatePose))
                .setLinearHeadingInterpolation(EmiddleArtifactPose.getHeading(), ClassifierGatePose.getHeading())
                .build();
        FromFirstArtifactsToEndPathChain = follower.pathBuilder()
                .addPath(new BezierLine(EfirstArtifactPose, endPose))
                .setLinearHeadingInterpolation(EfirstArtifactPose.getHeading(), endPose.getHeading())
                .build();
        FromFirstArtifactsToClasifierGate = follower.pathBuilder()
                .addPath(new BezierLine(EfirstArtifactPose,ClassifierGatePose))
                .setLinearHeadingInterpolation(EfirstArtifactPose.getHeading(), ClassifierGatePose.getHeading())
                .build();
        FromClasifierGateToFirstArtifactsPathChain = follower.pathBuilder()
                .addPath(new BezierCurve(ClassifierGatePose, controlPoint0, SfirstArtifactPose)) // funny bezier curve to avoid the balls
                .setLinearHeadingInterpolation(ClassifierGatePose.getHeading(), SfirstArtifactPose.getHeading())
                .build();
        FromClasifierGateToMiddleArtifactsPathChain = follower.pathBuilder()
                .addPath(new BezierLine(ClassifierGatePose, SmiddleArtifactPose))
                .setLinearHeadingInterpolation(ClassifierGatePose.getHeading(), SmiddleArtifactPose.getHeading())
                .build();
        FromClassifierGateToLastArtifactsPathChain = follower.pathBuilder()
                .addPath(new BezierLine(ClassifierGatePose, SlastArtifactPose))
                .setLinearHeadingInterpolation(ClassifierGatePose.getHeading(), SlastArtifactPose.getHeading())
                .build();
        FromStartToClassifierGatePathChain = follower.pathBuilder()
                .addPath(new BezierLine(startPose, ClassifierGatePose))
                .setLinearHeadingInterpolation(startPose.getHeading(), ClassifierGatePose.getHeading())
                .build();
        FromShootingToClassifierGatePathChain = follower.pathBuilder()
                .addPath(new BezierLine(shootPose, ClassifierGatePose))
                .setLinearHeadingInterpolation(shootPose.getHeading(), ClassifierGatePose.getHeading())
                .build();
        FromClassifierGateToEndPathChain = follower.pathBuilder()
                .addPath(new BezierLine(ClassifierGatePose, endPose))
                .setLinearHeadingInterpolation(ClassifierGatePose.getHeading(), endPose.getHeading())
                .build();
        LastArtifactsPathChain = follower.pathBuilder()
                .addPath(new BezierLine(SlastArtifactPose,ElastArtifactPose))
                .setLinearHeadingInterpolation(SlastArtifactPose.getHeading(), ElastArtifactPose.getHeading())
                .build();
        FromShootingToLastArtifactsPathCain = follower.pathBuilder()
                .addPath(new BezierLine(shootPose,SlastArtifactPose))
                .setLinearHeadingInterpolation(shootPose.getHeading(), SlastArtifactPose.getHeading())
                .build();
        FromStartToLastArtifactPathChain = follower.pathBuilder()
                .addPath(new BezierLine(startPose,SlastArtifactPose))
                .setLinearHeadingInterpolation(startPose.getHeading(), SlastArtifactPose.getHeading())
                .build();
        FromLastArtifactsToShootPathChain = follower.pathBuilder()
                .addPath(new BezierLine(ElastArtifactPose,shootPose))
                .setLinearHeadingInterpolation(ElastArtifactPose.getHeading(), shootPose.getHeading())
                .build();
        MiddleArtifactsPathChain = follower.pathBuilder()
                .addPath(new BezierLine(SmiddleArtifactPose,EmiddleArtifactPose))
                .setLinearHeadingInterpolation(SmiddleArtifactPose.getHeading(), EmiddleArtifactPose.getHeading())
                .build();
        FromShootingToMiddleArtifactsPathChain = follower.pathBuilder()
                .addPath(new BezierLine(shootPose,SmiddleArtifactPose))
                .setLinearHeadingInterpolation(shootPose.getHeading(), SmiddleArtifactPose.getHeading())
                .build();
        FromStartToMiddleArtifactsPathChain = follower.pathBuilder()
                .addPath(new BezierLine(startPose,SmiddleArtifactPose))
                .setLinearHeadingInterpolation(startPose.getHeading(),SmiddleArtifactPose.getHeading())
                .build();
        FromMiddleArtifactsToShootPathChain = follower.pathBuilder()
                .addPath(new BezierLine(EmiddleArtifactPose,shootPose))
                .setLinearHeadingInterpolation(EmiddleArtifactPose.getHeading(), shootPose.getHeading())
                .build();
        FirstArtifactsPathChain = follower.pathBuilder()
                .addPath(new BezierLine(SfirstArtifactPose, EfirstArtifactPose))
                .setLinearHeadingInterpolation(SfirstArtifactPose.getHeading(), EfirstArtifactPose.getHeading())
                .build();
        FromShootingToFirstArtifactPathChain = follower.pathBuilder()
                .addPath(new BezierLine(shootPose, SfirstArtifactPose))
                .setLinearHeadingInterpolation(shootPose.getHeading(), SfirstArtifactPose.getHeading())
                .build();
        FromStartToFirstArtifactPathChain = follower.pathBuilder()
                .addPath(new BezierLine(startPose, SfirstArtifactPose))
                .setLinearHeadingInterpolation(startPose.getHeading(), SfirstArtifactPose.getHeading())
                .build();
        FromFirstArtifactsToShootPathChain = follower.pathBuilder()
                .addPath(new BezierLine(EfirstArtifactPose,shootPose))
                .setLinearHeadingInterpolation(EfirstArtifactPose.getHeading(), shootPose.getHeading())
                .build();
        FromShootingToStartPathChain = follower.pathBuilder()
                .addPath(new BezierLine(shootPose,startPose))
                .setLinearHeadingInterpolation(shootPose.getHeading(),startPose.getHeading())
                .build();
    }

    public void runPath() {
        switch (pathState) {
            // you can consider each case in this to be 1 whole step in the auto sequence.
            case 0: {
                // wait for robot to not be running a path.
                if (!follower.isBusy()){
                    follower.pausePathFollowing();
                    shooter.cycle(telemetry);
                    follower.resumePathFollowing();
                    // start next auto step
                    pathState++;
                }
                break;
            }
            case 1: {
                if (!follower.isBusy()) {
                    // if you wanted to start a subsystem here just do it like so:
                    // make sure the function gives enough time for it to startup
                    // here since we are at the shooting position, we want to shoot the balls
                    // shoot the balls
                    follower.followPath(FromStartToShootingPathChain, false);
                    // start intake
                    pathState = -1;
                }
                break;
            }
            default: {}
        }
    }

    @Override
    public void init() {
        shooter.init(hardwareMap);
        initialize();
        buildPaths();
        follower.setStartingPose(startPose);
        runPath();
        PanelsConfigurables.INSTANCE.refreshClass(this);
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
        poseHistory = follower.getPoseHistory();
        Drawing.drawPoseHistory(poseHistory);
        Drawing.drawRobot(follower.getPose());
        Drawing.sendPacket();
        telemetry.addData("X(inc)",follower.getPose().getX());
        telemetry.addData("Y(inc)",follower.getPose().getY());
        telemetry.addData("H(deg)",Math.toDegrees(follower.getPose().getHeading()));
        telemetry.addData("Path State", pathState);
        switch (pathState){
            //case 0: telemetry.addData("Next Pose X", pose2.getX());telemetry.addData("Next Pose Y", pose2.getY());telemetry.addData("Next Pose :", Math.toDegrees(pose2.getHeading()));
            //case 1: telemetry.addData("Next Pose X", pose3.getX());telemetry.addData("Next Pose Y", pose3.getY());telemetry.addData("Next Pose H", Math.toDegrees(pose3.getHeading()));
            //case 2: telemetry.addData("Next Pose X", endPose.getX());telemetry.addData("Next Pose Y", endPose.getY());telemetry.addData("Next Pose H", Math.toDegrees(endPose.getHeading()));
            //case 3: telemetry.addData("Next Pose X", startPose.getX());telemetry.addData("Next Pose Y", startPose.getY());telemetry.addData("Next Pose H", Math.toDegrees(startPose.getHeading()));
            //case 4: telemetry.addData("Next Pose X", startPose.getX());telemetry.addData("Next Pose Y", startPose.getY());telemetry.addData("Next Pose H", Math.toDegrees(startPose.getHeading()));
            //case -1: telemetry.addData("At Path End","");
        }
        telemetry.update();
    }
}
