package org.firstinspires.ftc.teamcode.dynamite.PPintegration;

import org.firstinspires.ftc.teamcode.dynamite.DynVar.DynVar;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Pedro Pathing integration interface.
 * This class provides a bridge between DYN commands and PedroPathing library.
 * 
 * To use with actual PedroPathing:
 * 1. Extend this class
 * 2. Override the execute methods to call actual PedroPathing code
 * 3. Pass your implementation to DynAuto
 */
public class PedroPathingBridge {
    
    protected FieldPose currentPose;
    protected FieldPose startPose;
    protected final List<PathSegment> queuedSegments = new ArrayList<>();
    protected Consumer<String> telemOutput;
    protected boolean isSimulation = true;
    protected boolean startPoseSet = false; // Track if start pose has been set

    public PedroPathingBridge() {
        this.currentPose = new FieldPose(0, 0, 0);
        this.startPose = new FieldPose(0, 0, 0);
    }

    /**
     * Set telemetry output for debugging.
     */
    public void setTelemOutput(Consumer<String> telemOutput) {
        this.telemOutput = telemOutput;
    }

    /**
     * Set whether this is a simulation (no actual robot movement).
     */
    public void setSimulation(boolean simulation) {
        this.isSimulation = simulation;
    }

    /**
     * Set the starting position for the autonomous path.
     * Can be called at any time, but movement commands will fail if not called before them.
     */
    public void setStartPose(FieldPose pose) {
        this.startPose = pose;
        this.currentPose = new FieldPose(pose.getX(), pose.getY(), pose.getHeadingDegrees());
        this.startPoseSet = true;
        log("Start pose set: " + pose);
    }

    /**
     * Set start pose from org.firstinspires.ftc.teamcode.dynamite.DynVar.DynVar.
     */
    public void setStartPose(DynVar posVar) {
        setStartPose(FieldPose.fromDynVar(posVar));
    }

    /**
     * Get the current robot pose.
     */
    public FieldPose getCurrentPose() {
        return currentPose;
    }

    /**
     * Check if the robot is currently moving.
     * Override this in subclasses to use actual robot state.
     * @return true if the robot is currently executing a movement, false otherwise
     */
    public boolean isBusy() {
        // Base implementation always returns false (simulation mode)
        return false;
    }

    /**
     * Wait for robot to stop moving.
     * Subclasses should override this to wait using actual robot state.
     */
    public void waitForIdle() {
        // Base implementation does nothing (simulation mode)
        while (isBusy()) {
            // Wait loop - will be overridden in subclasses
        }
    }

    /**
     * Validate that start pose has been set before executing movement.
     * Throws exception if start pose not set.
     */
    protected void validateStartPoseSet() {
        if (!startPoseSet) {
            throw new RuntimeException(
                "[DYN ERROR] PathStartPosition must be called before any movement commands (goTo, turnTo, followBezier)"
            );
        }
    }

    /**
     * Move to a position in a straight line.
     * Override this method to implement actual PedroPathing goTo.
     */
    public void goTo(FieldPose target) {
        validateStartPoseSet();
        waitForIdle();
        
        log("GoTo: " + target);
        PathSegment segment = PathSegment.line(currentPose, target);
        queuedSegments.add(segment);
        
        if (!isSimulation) {
            executeSegment(segment);
        }
        
        currentPose = target;
    }

    /**
     * Move to a position from org.firstinspires.ftc.teamcode.dynamite.DynVar.DynVar.
     */
    public void goTo(DynVar posVar) {
        goTo(FieldPose.fromDynVar(posVar));
    }

    /**
     * Turn to a specific heading.
     * Override this method to implement actual PedroPathing turnTo.
     * 
     * @param headingDegrees Target heading in degrees
     * @param degreesPerSecond Turn speed in degrees per second
     */
    public void turnTo(double headingDegrees, double degreesPerSecond) {
        validateStartPoseSet();
        waitForIdle();
        
        log("TurnTo: " + headingDegrees + "° at " + degreesPerSecond + "°/s");
        PathSegment segment = PathSegment.turn(currentPose, headingDegrees, degreesPerSecond);
        queuedSegments.add(segment);
        
        if (!isSimulation) {
            executeSegment(segment);
        }
        
        currentPose = new FieldPose(currentPose.getX(), currentPose.getY(), headingDegrees);
    }

    /**
     * Follow a bezier curve path.
     * Override this method to implement actual PedroPathing bezier paths.
     */
    public void followBezier(FieldPose start, FieldPose end, List<FieldPoint> controlPoints) {
        validateStartPoseSet();
        waitForIdle();
        
        log("FollowBezier: " + start + " -> " + end + " with " + controlPoints.size() + " control points");
        PathSegment segment = PathSegment.bezier(start, end, controlPoints);
        queuedSegments.add(segment);
        
        if (!isSimulation) {
            executeSegment(segment);
        }
        
        currentPose = end;
    }

    /**
     * Simplified bezier with just start and end poses.
     */
    public void followBezier(DynVar startVar, DynVar endVar) {
        FieldPose start = FieldPose.fromDynVar(startVar);
        FieldPose end = FieldPose.fromDynVar(endVar);
        followBezier(start, end, new ArrayList<>());
    }

    /**
     * Execute a path segment.
     * Override this method to implement actual robot movement.
     */
    protected void executeSegment(PathSegment segment) {
        // In simulation mode, just log
        log("Executing segment: " + segment);
        
        // Override this in your actual implementation to call PedroPathing
        // Example for actual implementation:
        // switch (segment.getType()) {
        //     case LINE:
        //         follower.followPath(buildLinePath(segment));
        //         break;
        //     case BEZIER:
        //         follower.followPath(buildBezierPath(segment));
        //         break;
        //     case TURN_IN_PLACE:
        //         follower.turnTo(segment.getEndPose().getHeading());
        //         break;
        // }
        // waitForPathComplete();
    }

    /**
     * Get all queued path segments.
     */
    public List<PathSegment> getQueuedSegments() {
        return new ArrayList<>(queuedSegments);
    }

    /**
     * Clear queued segments.
     */
    public void clearSegments() {
        queuedSegments.clear();
    }

    /**
     * Get the total path length of all queued segments.
     */
    public double getTotalPathLength() {
        return queuedSegments.stream().mapToDouble(PathSegment::getLength).sum();
    }

    protected void log(String message) {
        if (telemOutput != null) {
            telemOutput.accept("[Pedro] " + message);
        } else {
            System.out.println("[Pedro] " + message);
        }
    }
}
