package org.firstinspires.ftc.teamcode.dynamite.DynCommands.Movement;

import org.firstinspires.ftc.teamcode.dynamite.DynCommands.DynCommand;
import org.firstinspires.ftc.teamcode.dynamite.DynVar.DynVar;
import org.firstinspires.ftc.teamcode.dynamite.PPintegration.PedroPathingBridge;
import org.firstinspires.ftc.teamcode.dynamite.PPintegration.FieldPose;
import org.firstinspires.ftc.teamcode.dynamite.PPintegration.FieldPoint;
import org.firstinspires.ftc.teamcode.dynamite.DynExceptions.DynAutoStepException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * Command to follow a bezier curve path.
 * Syntax: followBezier(startPos, endPos)
 */
public class FollowBezierCommand implements DynCommand {
    private final String startVarId;
    private final String endVarId;
    private final PedroPathingBridge pathingBridge;

    private Function<String, DynVar> getVar;

    public FollowBezierCommand(String startVarId, String endVarId, PedroPathingBridge pathingBridge) {
        this.startVarId = startVarId;
        this.endVarId = endVarId;
        this.pathingBridge = pathingBridge;
    }

    @Override
    public void init(BiConsumer<String, DynVar> setVar, Function<String, DynVar> getVar) {
        this.getVar = getVar;
    }

    @Override
    public void run() {
        DynVar startVar = getVar.apply(startVarId);
        DynVar endVar = getVar.apply(endVarId);

        if (startVar == null) {
            throw new DynAutoStepException("Start position variable not found: " + startVarId);
        }
        if (endVar == null) {
            throw new DynAutoStepException("End position variable not found: " + endVarId);
        }

        if (pathingBridge != null) {
            Object startVal = startVar.getValue();

            // If first arg is a FieldCoord (double[2]), treat it as a control point from current pose
            if (startVal instanceof double[] && ((double[]) startVal).length == 2) {
                double[] cp = (double[]) startVal;
                FieldPoint controlPoint = new FieldPoint(cp[0], cp[1]);
                FieldPose startPose = pathingBridge.getCurrentPose();
                FieldPose endPose = FieldPose.fromDynVar(endVar);

                pathingBridge.followBezier(startPose, endPose, Collections.singletonList(controlPoint));
            } else {
                // Default: both args are poses
                pathingBridge.followBezier(startVar, endVar);
            }
        } else {
            System.out.println("[FollowBezier] " + startVarId + " -> " + endVarId);
        }
    }

    @Override
    public String getDescription() {
        return "FollowBezier(" + startVarId + " -> " + endVarId + ")";
    }
}
