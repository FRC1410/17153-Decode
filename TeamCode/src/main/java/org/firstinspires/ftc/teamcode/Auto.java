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
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Subsystem.Drivetrain;
import org.firstinspires.ftc.teamcode.Subsystem.Intake;
import org.firstinspires.ftc.teamcode.Subsystem.LazySusan;
import org.firstinspires.ftc.teamcode.Util.Constants;
import org.firstinspires.ftc.teamcode.Util.Toggle;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants.*;

import java.util.Timer;

@Autonomous(name="Pedro Pathing Test", group="Tests")
public class Auto extends OpMode {
    private Follower follower;
    private PoseHistory poseHistory;
    private int pathState = 0;
    private final Pose startPose = new Pose(88,8, Math.toRadians(90)); // start path pos
    private final Pose SmiddleArtifactPose = new Pose(100,60, Math.toRadians(0)); // start pickup middle articfacts pos
    private final Pose EmiddleArtifactPose = new Pose(125,60, Math.toRadians(0)); // end pickup middle articfacts pos
    private final Pose SfirstArtifactsPose = new Pose(104, 84, Math.toRadians(0));
    private final Pose EfirstArtifactsPose = new Pose(132, 84, Math.toRadians(0));
    private final Pose SlastArtifactPose = new Pose(104, 35, Math.toRadians(0));
    private final Pose ElastArtifactPose = new Pose(132, 35, Math.toRadians(0));
    private final Pose endPose = new Pose(84,83, Math.toRadians(136)); // shooting pose
    private PathChain LastArtifactsPathChain = new PathChain();
    private PathChain FromShootingToLastArtifactsPathCain = new PathChain();
    private PathChain FromStartToLastArtifactPathChain = new PathChain();
    private PathChain MiddleArtifactsPathChain = new PathChain();
    private PathChain FromShootingToMiddleArtifactsPathChain = new PathChain();
    private PathChain FromstartToMiddleArtifactsPos = new PathChain();
    private PathChain FirstArtifactsPathChain = new PathChain();
    private PathChain FromShootingToFirstArtifactPathChain = new PathChain();
    private PathChain FromStartToFirstArtifactPathChain = new PathChain();
    private PathChain BackToStartPathChain = new PathChain();
    public void initialize() {
        this.follower = createFollower(hardwareMap);
    }

    public void buildPaths(){
        LastArtifactsPathChain = new PathChain();

    }

    public void runPath() {
        switch (pathState) {
            case 0: {
                if (!follower.isBusy()) {
                    //follower.followPath(pathChain, false);
                    pathState++;
                }
                break;
            }
            case 1: {
                if (!follower.isBusy()){
                    //follower.followPath(pathChain2, true);
                    pathState++;
                }
                break;
            }
            case 2: {
                if (!follower.isBusy()){
                    //follower.followPath(pathChain3, false);
                    pathState++;
                }
                break;
            }
            case 3: {
                if (!follower.isBusy()){
                    //follower.followPath(pathChain4,true);
                    pathState++;
                }
                break;
            }
            case 4: {
                if (!follower.isBusy()){
                    pathState = 0;
                    try{
                        follower.pausePathFollowing();
                        Thread.sleep(1000);
                        follower.resumePathFollowing();
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
        PanelsConfigurables.INSTANCE.refreshClass(this);
        Drawing.init();
        poseHistory = follower.getPoseHistory();
        Drawing.drawPoseHistory(poseHistory);
        Drawing.drawRobot(follower.getPose());
        Drawing.sendPacket();
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

// for pedro panels interface
class Drawing {
    public static final double ROBOT_RADIUS = 9; // woah
    private static final FieldManager panelsField = PanelsField.INSTANCE.getField();

    private static final Style robotLook = new Style(
            "", "#3F51B5", 0.75
    );
    private static final Style historyLook = new Style(
            "", "#4CAF50", 0.75
    );

    /**
     * This prepares Panels Field for using Pedro Offsets
     */
    public static void init() {
        panelsField.setOffsets(PanelsField.INSTANCE.getPresets().getPEDRO_PATHING());
    }

    /**
     * This draws everything that will be used in the Follower's telemetryDebug() method. This takes
     * a Follower as an input, so an instance of the DashbaordDrawingHandler class is not needed.
     *
     * @param follower Pedro Follower instance.
     */
    public static void drawDebug(Follower follower) {
        if (follower.getCurrentPath() != null) {
            drawPath(follower.getCurrentPath(), robotLook);
            Pose closestPoint = follower.getPointFromPath(follower.getCurrentPath().getClosestPointTValue());
            drawRobot(new Pose(closestPoint.getX(), closestPoint.getY(), follower.getCurrentPath().getHeadingGoal(follower.getCurrentPath().getClosestPointTValue())), robotLook);
        }
        drawPoseHistory(follower.getPoseHistory(), historyLook);
        drawRobot(follower.getPose(), historyLook);

        sendPacket();
    }

    /**
     * This draws a robot at a specified Pose with a specified
     * look. The heading is represented as a line.
     *
     * @param pose  the Pose to draw the robot at
     * @param style the parameters used to draw the robot with
     */
    public static void drawRobot(Pose pose, Style style) {
        if (pose == null || Double.isNaN(pose.getX()) || Double.isNaN(pose.getY()) || Double.isNaN(pose.getHeading())) {
            return;
        }

        panelsField.setStyle(style);
        panelsField.moveCursor(pose.getX(), pose.getY());
        panelsField.circle(ROBOT_RADIUS);

        Vector v = pose.getHeadingAsUnitVector();
        v.setMagnitude(v.getMagnitude() * ROBOT_RADIUS);
        double x1 = pose.getX() + v.getXComponent() / 2, y1 = pose.getY() + v.getYComponent() / 2;
        double x2 = pose.getX() + v.getXComponent(), y2 = pose.getY() + v.getYComponent();

        panelsField.setStyle(style);
        panelsField.moveCursor(x1, y1);
        panelsField.line(x2, y2);
    }

    /**
     * This draws a robot at a specified Pose. The heading is represented as a line.
     *
     * @param pose the Pose to draw the robot at
     */
    public static void drawRobot(Pose pose) {
        drawRobot(pose, robotLook);
    }

    /**
     * This draws a Path with a specified look.
     *
     * @param path  the Path to draw
     * @param style the parameters used to draw the Path with
     */
    public static void drawPath(Path path, Style style) {
        double[][] points = path.getPanelsDrawingPoints();

        for (int i = 0; i < points[0].length; i++) {
            for (int j = 0; j < points.length; j++) {
                if (Double.isNaN(points[j][i])) {
                    points[j][i] = 0;
                }
            }
        }

        panelsField.setStyle(style);
        panelsField.moveCursor(points[0][0], points[0][1]);
        panelsField.line(points[1][0], points[1][1]);
    }

    /**
     * This draws all the Paths in a PathChain with a
     * specified look.
     *
     * @param pathChain the PathChain to draw
     * @param style     the parameters used to draw the PathChain with
     */
    public static void drawPath(PathChain pathChain, Style style) {
        for (int i = 0; i < pathChain.size(); i++) {
            drawPath(pathChain.getPath(i), style);
        }
    }

    /**
     * This draws the pose history of the robot.
     *
     * @param poseTracker the PoseHistory to get the pose history from
     * @param style       the parameters used to draw the pose history with
     */
    public static void drawPoseHistory(PoseHistory poseTracker, Style style) {
        panelsField.setStyle(style);

        int size = poseTracker.getXPositionsArray().length;
        for (int i = 0; i < size - 1; i++) {

            panelsField.moveCursor(poseTracker.getXPositionsArray()[i], poseTracker.getYPositionsArray()[i]);
            panelsField.line(poseTracker.getXPositionsArray()[i + 1], poseTracker.getYPositionsArray()[i + 1]);
        }
    }

    /**
     * This draws the pose history of the robot.
     *
     * @param poseTracker the PoseHistory to get the pose history from
     */
    public static void drawPoseHistory(PoseHistory poseTracker) {
        drawPoseHistory(poseTracker, historyLook);
    }

    /**
     * This tries to send the current packet to FTControl Panels.
     */
    public static void sendPacket() {
        panelsField.update();
    }
}