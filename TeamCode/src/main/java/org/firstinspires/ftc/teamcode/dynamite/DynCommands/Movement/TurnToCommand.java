package org.firstinspires.ftc.teamcode.dynamite.DynCommands.Movement;

import org.firstinspires.ftc.teamcode.dynamite.DynCommands.DynCommand;
import org.firstinspires.ftc.teamcode.dynamite.DynVar.DynVar;
import org.firstinspires.ftc.teamcode.dynamite.PPintegration.PedroPathingBridge;

import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * Command to turn the robot to a heading.
 * Syntax: turnTo(headingDegrees, degreesPerSecond)
 */
public class TurnToCommand implements DynCommand {
    private final String headingVarId;    // Can be literal or variable
    private final String speedVarId;      // Can be literal or variable
    private final Double headingLiteral;
    private final Double speedLiteral;
    private final PedroPathingBridge pathingBridge;

    private Function<String, DynVar> getVar;

    /**
     * Constructor with variable references.
     */
    public TurnToCommand(String headingVarId, String speedVarId, PedroPathingBridge pathingBridge) {
        this.headingVarId = headingVarId;
        this.speedVarId = speedVarId;
        this.headingLiteral = null;
        this.speedLiteral = null;
        this.pathingBridge = pathingBridge;
    }

    /**
     * Constructor with literal values.
     */
    public TurnToCommand(double heading, double speed, PedroPathingBridge pathingBridge) {
        this.headingVarId = null;
        this.speedVarId = null;
        this.headingLiteral = heading;
        this.speedLiteral = speed;
        this.pathingBridge = pathingBridge;
    }

    @Override
    public void init(BiConsumer<String, DynVar> setVar, Function<String, DynVar> getVar) {
        this.getVar = getVar;
    }

    @Override
    public void run() {
        double heading;
        double speed;

        if (headingLiteral != null) {
            heading = headingLiteral;
        } else {
            DynVar headingVar = getVar.apply(headingVarId);
            heading = headingVar != null ? headingVar.asDouble() : Double.parseDouble(headingVarId);
        }

        if (speedLiteral != null) {
            speed = speedLiteral;
        } else {
            DynVar speedVar = getVar.apply(speedVarId);
            speed = speedVar != null ? speedVar.asDouble() : Double.parseDouble(speedVarId);
        }

        if (pathingBridge != null) {
            pathingBridge.turnTo(heading, speed);
        } else {
            System.out.println("[TurnTo] heading=" + heading + "°, speed=" + speed + "°/s");
        }
    }

    @Override
    public String getDescription() {
        if (headingLiteral != null) {
            return "TurnTo(" + headingLiteral + "°, " + speedLiteral + "°/s)";
        }
        return "TurnTo(" + headingVarId + ", " + speedVarId + ")";
    }
}
