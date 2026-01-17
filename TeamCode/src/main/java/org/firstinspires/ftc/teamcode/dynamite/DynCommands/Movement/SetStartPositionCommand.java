package org.firstinspires.ftc.teamcode.dynamite.DynCommands.Movement;

import org.firstinspires.ftc.teamcode.dynamite.DynCommands.DynCommand;
import org.firstinspires.ftc.teamcode.dynamite.DynVar.DynVar;
import org.firstinspires.ftc.teamcode.dynamite.PPintegration.PedroPathingBridge;
import org.firstinspires.ftc.teamcode.dynamite.DynExceptions.DynAutoStepException;

import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * Command to set the starting position for the autonomous path.
 * Syntax: PathStartPosition posVar
 * MUST be the first movement command in an auto.
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

        if (pathingBridge != null) {
            pathingBridge.setStartPose(posVar);
        } else {
            System.out.println("[PathStartPosition] " + positionVarId + " = " + posVar);
        }
    }

    @Override
    public String getDescription() {
        return "PathStartPosition(" + positionVarId + ")";
    }
}
