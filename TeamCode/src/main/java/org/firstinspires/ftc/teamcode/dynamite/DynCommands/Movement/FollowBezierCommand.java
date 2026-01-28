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

        // Resolve any variable references in FieldPos arrays at runtime
        FieldPose startPose = null, endPose = null;
        Object startVal = startVar.getValue();
        Object endVal = endVar.getValue();
        if (startVal instanceof double[] && ((double[]) startVal).length == 2) {
            double[] cp = (double[]) startVal;
            FieldPoint controlPoint = new FieldPoint(cp[0], cp[1]);
            // endVar is a FieldPos
            if (endVal instanceof double[] && ((double[]) endVal).length >= 3) {
                double[] arr = (double[]) endVal;
                endPose = new FieldPose(arr[0], arr[1], arr[2]);
            } else if (endVal instanceof Object[] && ((Object[]) endVal).length >= 3) {
                Object[] arr = (Object[]) endVal;
                double x = arr[0] instanceof Number ? ((Number) arr[0]).doubleValue() : 0;
                double y = arr[1] instanceof Number ? ((Number) arr[1]).doubleValue() : 0;
                double heading = 0;
                if (arr[2] instanceof Number) {
                    heading = ((Number) arr[2]).doubleValue();
                } else if (arr[2] instanceof String) {
                    DynVar ref = getVar.apply((String) arr[2]);
                    if (ref != null && ref.getValue() instanceof Number) {
                        heading = ((Number) ref.getValue()).doubleValue();
                    }
                }
                endPose = new FieldPose(x, y, heading);
            }
            startPose = pathingBridge.getCurrentPose();
            pathingBridge.followBezier(startPose, endPose, Collections.singletonList(controlPoint));
        } else {
            // Both args are poses
            double x1 = 0, y1 = 0, h1 = 0, x2 = 0, y2 = 0, h2 = 0;
            if (startVal instanceof double[] && ((double[]) startVal).length >= 3) {
                double[] arr = (double[]) startVal;
                x1 = arr[0]; y1 = arr[1]; h1 = arr[2];
            } else if (startVal instanceof Object[] && ((Object[]) startVal).length >= 3) {
                Object[] arr = (Object[]) startVal;
                x1 = arr[0] instanceof Number ? ((Number) arr[0]).doubleValue() : 0;
                y1 = arr[1] instanceof Number ? ((Number) arr[1]).doubleValue() : 0;
                if (arr[2] instanceof Number) {
                    h1 = ((Number) arr[2]).doubleValue();
                } else if (arr[2] instanceof String) {
                    DynVar ref = getVar.apply((String) arr[2]);
                    if (ref != null && ref.getValue() instanceof Number) {
                        h1 = ((Number) ref.getValue()).doubleValue();
                    }
                }
            }
            if (endVal instanceof double[] && ((double[]) endVal).length >= 3) {
                double[] arr = (double[]) endVal;
                x2 = arr[0]; y2 = arr[1]; h2 = arr[2];
            } else if (endVal instanceof Object[] && ((Object[]) endVal).length >= 3) {
                Object[] arr = (Object[]) endVal;
                x2 = arr[0] instanceof Number ? ((Number) arr[0]).doubleValue() : 0;
                y2 = arr[1] instanceof Number ? ((Number) arr[1]).doubleValue() : 0;
                if (arr[2] instanceof Number) {
                    h2 = ((Number) arr[2]).doubleValue();
                } else if (arr[2] instanceof String) {
                    DynVar ref = getVar.apply((String) arr[2]);
                    if (ref != null && ref.getValue() instanceof Number) {
                        h2 = ((Number) ref.getValue()).doubleValue();
                    }
                }
            }
            startPose = new FieldPose(x1, y1, h1);
            endPose = new FieldPose(x2, y2, h2);
            pathingBridge.followBezier(startPose, endPose, new ArrayList<>());
        }
    }

    @Override
    public String getDescription() {
        return "FollowBezier(" + startVarId + " -> " + endVarId + ")";
    }
}
