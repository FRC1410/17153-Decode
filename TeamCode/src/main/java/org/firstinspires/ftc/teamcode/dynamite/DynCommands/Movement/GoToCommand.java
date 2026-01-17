package org.firstinspires.ftc.teamcode.dynamite.DynCommands.Movement;

import org.firstinspires.ftc.teamcode.dynamite.DynCommands.DynCommand;
import org.firstinspires.ftc.teamcode.dynamite.DynVar.DynVar;
import org.firstinspires.ftc.teamcode.dynamite.PPintegration.PedroPathingBridge;
import org.firstinspires.ftc.teamcode.dynamite.DynExceptions.DynAutoStepException;

import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * Command to move the robot to a position.
 * Syntax: goTo(positionVar)
 */
public class GoToCommand implements DynCommand {
    private final String positionVarId;
    private final PedroPathingBridge pathingBridge;

    private Function<String, DynVar> getVar;

    public GoToCommand(String positionVarId, PedroPathingBridge pathingBridge) {
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
            throw new DynAutoStepException("Position variable not found: " + positionVarId);
        }

        if (pathingBridge != null) {
            pathingBridge.goTo(posVar);
        } else {
            System.out.println("[GoTo] " + positionVarId + " = " + posVar);
        }
    }

    @Override
    public String getDescription() {
        return "GoTo(" + positionVarId + ")";
    }
}
