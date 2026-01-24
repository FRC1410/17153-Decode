package org.firstinspires.ftc.teamcode.dynamite.PPintegration;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
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
        super.setStartPose(pose);
        
        // Set PedroPathing starting pose
        Pose ppPose = toPedroPose(pose);
        follower.setStartingPose(ppPose);
        
        // Debug: confirm follower starting pose after set
        try {
            System.out.println("[DynPedro] setStartPose called with FieldPose: " + pose + ", mapped PedroPose: " + ppPose);
            System.out.println("[DynPedro] follower.getPose() after set: " + follower.getPose());
        } catch (Exception e) {
            System.out.println("[DynPedro] setStartPose debug failed: " + e.getMessage());
        }
        log("Start pose set: " + ppPose);
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
                    toPedroPose(start),
                    toPedroPose(end)
                ))
                .setLinearHeadingInterpolation(startHeading, endHeading)
                .build();
        } else if (controlPoints.size() == 1) {
            // Quadratic bezier (3 poses)
            pathChain = follower.pathBuilder()
                .addPath(new BezierCurve(
                    toPedroPose(start),
                    toPedroPoseFromPoint(controlPoints.get(0)),
                    toPedroPose(end)
                ))
                .setLinearHeadingInterpolation(startHeading, endHeading)
                .build();
        } else if (controlPoints.size() == 2) {
            // Cubic bezier (4 poses)
            pathChain = follower.pathBuilder()
                .addPath(new BezierCurve(
                    toPedroPose(start),
                    toPedroPoseFromPoint(controlPoints.get(0)),
                    toPedroPoseFromPoint(controlPoints.get(1)),
                    toPedroPose(end)
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
        
        // Wait for path completion using isBusy() and update() loop
        while (opMode.opModeIsActive() && !opMode.isStopRequested() && follower.isBusy()) {
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
            
            opMode.idle();
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
     */
    private Pose toPedroPose(FieldPose pose) {
        // Map DYN coordinates to PedroPathing coordinates.
        // Pedro's X-axis appears to run to robot-right and Y-axis runs forward on this robot.
        // Desired mapping:
        //  - DYN X (forward) -> Pedro Y (forward)
        //  - DYN Y (strafe right) -> Pedro X (right)
        // Also adjust heading because DYN heading=0 faces forward (DYN X),
        // while Pedro heading=0 faces right (Pedro X). Convert by +90deg (pi/2).
        double pedroHeading = pose.getHeading() + Math.PI / 2.0;
        return new Pose(pose.getY(), pose.getX(), pedroHeading);
    }

    /**
     * Convert DYN FieldPoint to PedroPathing Pose (heading defaults to 0).
     * Used for control points in Bezier curves.
     */
    private Pose toPedroPoseFromPoint(FieldPoint point) {
        // Same mapping for control points (no heading)
        // For control points, set heading to Pedro's 0 reference (right).
        return new Pose(point.getY(), point.getX(), Math.PI / 2.0);
    }

    /**
     * Convert PedroPathing Pose to DYN FieldPose.
     */
    private FieldPose toFieldPose(Pose pose) {
        // Inverse of toPedroPose: Pedro X -> DYN Y, Pedro Y -> DYN X
        // Convert Pedro heading back to DYN heading by subtracting pi/2.
        double dynHeading = pose.getHeading() - Math.PI / 2.0;
        return new FieldPose(pose.getY(), pose.getX(), dynHeading);
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
