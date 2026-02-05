package org.firstinspires.ftc.teamcode.dynamite.DynCommands.Movement;

import org.firstinspires.ftc.teamcode.dynamite.DynCommands.DynCommand;
import org.firstinspires.ftc.teamcode.dynamite.DynVar.DynVar;
import org.firstinspires.ftc.teamcode.dynamite.PPintegration.PedroPathingBridge;
import org.firstinspires.ftc.teamcode.dynamite.PPintegration.FieldPose;
import org.firstinspires.ftc.teamcode.dynamite.DynExceptions.DynAutoStepException;

import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * Command to set the starting position for the autonomous path.
 * Syntax: PathStartPosition posVar
 * Can be called at any time, but movement commands will fail if this hasn't been called first.
 */
public class SetStartPositionCommand implements DynCommand {
    private final String positionVarId;
    private final PedroPathingBridge pathingBridge;

    private Function<String, DynVar> getVar;

    public SetStartPositionCommand(String positionVarId, PedroPathingBridge pathingBridge) {
        this.positionVarId = positionVarId;
        this.pathingBridge = pathingBridge;
    }

    @Override
    public void init(BiConsumer<String, DynVar> setVar, Function<String, DynVar> getVar) {
        this.getVar = getVar;
    }

    @Override
    public void run() {
        DynVar posVar = getVar.apply(positionVarId);
        if (posVar == null) {
            throw new DynAutoStepException("Start position variable not found: " + positionVarId);
        }

        // Resolve any variable references in FieldPos arrays at runtime (late binding)
        Object val = posVar.getValue();
        double x = 0, y = 0, heading = 0;
        if (val instanceof double[]) {
            double[] arr = (double[]) val;
            if (arr.length >= 3) {
                x = arr[0]; y = arr[1]; heading = arr[2];
            } else if (arr.length == 2) {
                x = arr[0]; y = arr[1];
            }
        } else if (val instanceof Object[]) {
            Object[] arr = (Object[]) val;
            if (arr.length >= 3) {
                x = arr[0] instanceof Number ? ((Number) arr[0]).doubleValue() : 0;
                y = arr[1] instanceof Number ? ((Number) arr[1]).doubleValue() : 0;
                if (arr[2] instanceof Number) {
                    heading = ((Number) arr[2]).doubleValue();
                } else if (arr[2] instanceof String) {
                    DynVar ref = getVar.apply((String) arr[2]);
                    if (ref != null && ref.getValue() instanceof Number) {
                        heading = ((Number) ref.getValue()).doubleValue();
                    }
                }
            } else if (arr.length == 2) {
                x = arr[0] instanceof Number ? ((Number) arr[0]).doubleValue() : 0;
                y = arr[1] instanceof Number ? ((Number) arr[1]).doubleValue() : 0;
            }
        }
        FieldPose pose = new FieldPose(x, y, FieldPose.normalizeHeadingRadians(heading));

        if (pathingBridge != null) {
            System.out.println("[PathStartPosition] Setting start pose var: " + positionVarId + " -> " + pose);
            pathingBridge.setStartPose(pose);
            System.out.println("[PathStartPosition] Start pose set.");
        } else {
            System.out.println("[PathStartPosition] " + positionVarId + " = " + pose);
        }
    }

    @Override
    public String getDescription() {
        return "PathStartPosition(" + positionVarId + ")";
    }
}
