package org.firstinspires.ftc.teamcode.dynamite.PPintegration;

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
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import java.util.List;

/**
 * Bridge between DYN commands and actual PedroPathing execution.
 * 
 * This class translates DYN movement commands (goTo, turnTo, followBezier)
 * into PedroPathing PathChain objects using follower.pathBuilder() and executes them.
 * 
 * Uses the correct PedroPathing API pattern:
 *   PathChain chain = follower.pathBuilder()
 *       .addPath(new BezierLine(startPose, endPose))
 *       .setLinearHeadingInterpolation(startHeading, endHeading)
 *       .build();
 *   follower.followPath(chain, holdEnd);
 */
public class DynPedroPathingBridge extends PedroPathingBridge {

    private final Follower follower;
    private final LinearOpMode opMode;
    
    // Configuration
    private boolean holdEndOfPath = true;        // Whether to hold position at end of path

    public DynPedroPathingBridge(Follower follower, LinearOpMode opMode) {
        this.follower = follower;
        this.opMode = opMode;
        this.isSimulation = false; // We're on real hardware
    }

    // ==================== CONFIGURATION ====================

    public void setHoldEndOfPath(boolean hold) {
        this.holdEndOfPath = hold;
    }

    // ==================== MOVEMENT IMPLEMENTATION ====================

    @Override
    public void setStartPose(FieldPose pose) {
        Drawing.init();
        super.setStartPose(pose);
        
        // Set PedroPathing starting pose
        Pose ppPose = MappingUtils.toPedroPose(pose);
        follower.setStartingPose(ppPose);
        
        // Debug: confirm follower starting pose after set
        try {
            System.out.println("[DynPedro] setStartPose called with FieldPose: " + pose + ", mapped PedroPose: " + ppPose);
            System.out.println("[DynPedro] follower.getPose() after set: " + follower.getPose());
        } catch (Exception e) {
            System.out.println("[DynPedro] setStartPose debug failed: " + e.getMessage());
        }
        log("Start pose set: " + ppPose);

        // Verify mapping is 1:1 at runtime (will log a warning if not)
        verifyMappingRoundTrip(pose);
    }

    @Override
    public void goTo(FieldPose target) {
        if (!opMode.opModeIsActive()) return;
        
        super.goTo(target);
        
        // Get current and target poses
        Pose currentPose = follower.getPose();
        Pose targetPose = toPedroPose(target);
        // Debug: log mapping details
        System.out.println("[DynPedro] goTo called with FieldPose: " + target + ", mapped to Pedro Pose: " + targetPose + ", follower pose: " + currentPose);
        
        // Build PathChain using follower.pathBuilder()
        PathChain pathChain = follower.pathBuilder()
            .addPath(new BezierLine(currentPose, targetPose))
            .setLinearHeadingInterpolation(currentPose.getHeading(), targetPose.getHeading())
            .build();
        
        // Follow the path
        executePathChain(pathChain);
    }

    @Override
    public void turnTo(double headingDegrees, double degreesPerSecond) {
        if (!opMode.opModeIsActive()) return;
        
        super.turnTo(headingDegrees, degreesPerSecond);
        
        double targetRadians = Math.toRadians(headingDegrees);
        Pose currentPose = follower.getPose();
        
        // Create a PathChain that turns in place (same start and end pose)
        PathChain pathChain = follower.pathBuilder()
            .addPath(new BezierLine(currentPose, currentPose))
            .setConstantHeadingInterpolation(targetRadians)
            .build();
        
        // Follow the path (turn in place)
        executePathChain(pathChain);
    }

    @Override
    public void followBezier(FieldPose start, FieldPose end, List<FieldPoint> controlPoints) {
        if (!opMode.opModeIsActive()) return;
        
        super.followBezier(start, end, controlPoints);
        
        PathChain pathChain;
        double startHeading = Math.toRadians(start.getHeadingDegrees());
        double endHeading = Math.toRadians(end.getHeadingDegrees());
        
        if (controlPoints.isEmpty()) {
            // Simple line
            pathChain = follower.pathBuilder()
                .addPath(new BezierLine(
                    MappingUtils.toPedroPose(start),
                    MappingUtils.toPedroPose(end)
                ))
                .setLinearHeadingInterpolation(startHeading, endHeading)
                .build();
        } else if (controlPoints.size() == 1) {
            // Quadratic bezier (3 poses)
            pathChain = follower.pathBuilder()
                .addPath(new BezierCurve(
                    MappingUtils.toPedroPose(start),
                    MappingUtils.toPedroPoseFromPoint(controlPoints.get(0)),
                    MappingUtils.toPedroPose(end)
                ))
                .setLinearHeadingInterpolation(startHeading, endHeading)
                .build();
        } else if (controlPoints.size() == 2) {
            // Cubic bezier (4 poses)
            pathChain = follower.pathBuilder()
                .addPath(new BezierCurve(
                    MappingUtils.toPedroPose(start),
                    MappingUtils.toPedroPoseFromPoint(controlPoints.get(0)),
                    MappingUtils.toPedroPoseFromPoint(controlPoints.get(1)),
                    MappingUtils.toPedroPose(end)
                ))
                .setLinearHeadingInterpolation(startHeading, endHeading)
                .build();
        } else {
            // More control points - use first two
            pathChain = follower.pathBuilder()
                .addPath(new BezierCurve(
                    toPedroPose(start),
                    toPedroPoseFromPoint(controlPoints.get(0)),
                    toPedroPoseFromPoint(controlPoints.get(1)),
                    toPedroPose(end)
                ))
                .setLinearHeadingInterpolation(startHeading, endHeading)
                .build();
        }
        
        executePathChain(pathChain);
    }

    // ==================== PATH EXECUTION ====================

    /**
     * Execute a PathChain and wait for completion.
     * Uses follower.followPath(pathChain, holdEnd) pattern.
     * Includes timeout protection to prevent infinite loops.
     */
    private void executePathChain(PathChain pathChain) {
        if (!opMode.opModeIsActive()) return;
        
        // Start following the path
        try {
            System.out.println("[DynPedro] Executing PathChain: " + pathChain.toString());
        } catch (Exception e) {
            System.out.println("[DynPedro] Executing PathChain (toString failed): " + e.getMessage());
        }
        follower.followPath(pathChain, holdEndOfPath);
        
        // Timeout after 30 seconds to prevent infinite loop
        long startTime = System.currentTimeMillis();
        long timeoutMs = 30000;
        boolean pedroActive = true;
        // Wait for path completion using isBusy() and update() loop
        while (opMode.opModeIsActive() && !opMode.isStopRequested() && pedroActive) {
            try {
                Thread.sleep(10); //sleep a good sleep, to make it wait for pedro a little
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            if (follower.isBusy()) {
                // Check for timeout
                if (System.currentTimeMillis() - startTime > timeoutMs) {
                    log("Path execution timed out after " + timeoutMs + "ms");
                    follower.breakFollowing();
                    break;
                }
                follower.update();

                // Update telemetry
                Pose pose = follower.getPose();
                opMode.telemetry.addData("Position", "X: %.1f, Y: %.1f", pose.getX(), pose.getY());
                opMode.telemetry.addData("Heading", "%.1f deg", Math.toDegrees(pose.getHeading()));
                opMode.telemetry.update();

                Drawing.drawPoseHistory(follower.getPoseHistory());
                Drawing.drawRobot(pose);
                Drawing.sendPacket();

                opMode.idle();
            } else {
                pedroActive = false;
            }
        }
        
        // Update current pose after path completes
        Pose finalPose = follower.getPose();
        // Convert Pedro pose back to DYN FieldPose
        currentPose = toFieldPose(finalPose);
    }

    /**
     * Execute a pre-built PathChain and wait for completion.
     * Useful for complex paths built externally.
     */
    public void followPathChain(PathChain pathChain) {
        executePathChain(pathChain);
    }

    /**
     * Execute a PathChain without waiting for completion (non-blocking).
     * Useful for running paths while doing subsystem actions.
     */
    public void followPathChainAsync(PathChain pathChain) {
        if (!opMode.opModeIsActive()) return;
        follower.followPath(pathChain, holdEndOfPath);
    }

    /**
     * Update the follower. Call this in your loop when using async path following.
     */
    public void update() {
        follower.update();
    }

    /**
     * Check if the follower is currently busy following a path.
     */
    @Override
    public boolean isBusy() {
        return follower.isBusy();
    }

    /**
     * Wait for the robot to stop moving before continuing execution.
     * Uses follower.isBusy() to check if movement is complete.
     */
    @Override
    public void waitForIdle() {
        if (!opMode.opModeIsActive()) return;
        
        // Wait until robot is no longer busy
        while (opMode.opModeIsActive() && !opMode.isStopRequested() && follower.isBusy()) {
            follower.update();
            opMode.idle();
        }
    }

    /**
     * Pause path following (useful for subsystem actions during path).
     */
    public void pausePathFollowing() {
        follower.breakFollowing();
    }

    // ==================== CONVERSIONS ====================

    /**
     * Convert DYN FieldPose to PedroPathing Pose.
     *
     * Use a direct 1:1 mapping so DYN (x,y,heading) maps to Pedro (x,y,heading) without swapping
     * axes or applying heading offsets. Heading is represented in radians in both systems.
     */
    private Pose toPedroPose(FieldPose pose) {
        // 1:1 mapping between DYN FieldPose and Pedro Pathing Pose:
        // - DYN X -> Pedro X
        // - DYN Y -> Pedro Y
        // - Heading is the same (radians)
        return new Pose(pose.getX(), pose.getY(), pose.getHeading());
    }

    /**
     * Convert DYN FieldPoint to PedroPathing Pose (heading defaults to 0).
     * Used for control points in Bezier curves.
     *
     * Use direct mapping: FieldPoint(x,y) -> Pedro Pose(x,y, heading=0)
     */
    private Pose toPedroPoseFromPoint(FieldPoint point) {
        // 1:1 mapping for control points (x,y). Heading defaults to 0 radians.
        return new Pose(point.getX(), point.getY(), 0.0);
    }

    /**
     * Convert PedroPathing Pose to DYN FieldPose.
     *
     * Inverse of the 1:1 mapping: Pedro (x,y,heading) -> DYN (x,y,heading)
     */
    private FieldPose toFieldPose(Pose pose) {
        // Direct mapping back to FieldPose
        return new FieldPose(pose.getX(), pose.getY(), pose.getHeading());
    }

    /**
     * Runtime verification that the round-trip mapping preserves values.
     * Logs a warning if a mismatch larger than small epsilons is detected.
     */
    private void verifyMappingRoundTrip(FieldPose pose) {
        final double EPS_POS = 1e-6;
        final double EPS_HEAD = 1e-4;
        Pose pp = toPedroPose(pose);
        FieldPose round = toFieldPose(pp);
        if (Math.abs(round.getX() - pose.getX()) > EPS_POS ||
            Math.abs(round.getY() - pose.getY()) > EPS_POS ||
            Math.abs(round.getHeading() - pose.getHeading()) > EPS_HEAD) {
            log("[DynPedro] Mapping mismatch: original " + pose + " -> Pedro " + pp + " -> round " + round);
        } else {
            log("[DynPedro] 1:1 mapping verified for pose: " + pose);
        }
    }

    // ==================== ACCESSORS ====================

    /**
     * Get the underlying PedroPathing follower.
     */
    public Follower getFollower() {
        return follower;
    }

    /**
     * Get the current robot pose from PedroPathing.
     */
    public Pose getCurrentPedroPose() {
        return follower.getPose();
    }

    @Override
    protected void log(String message) {
        if (telemOutput != null) {
            telemOutput.accept("[PP] " + message);
        }
        if (opMode != null) {
            opMode.telemetry.addData("Pedro", message);
        }
    }
}


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