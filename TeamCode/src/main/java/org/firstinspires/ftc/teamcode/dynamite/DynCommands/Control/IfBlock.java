package org.firstinspires.ftc.teamcode.dynamite.DynCommands.Control;

import org.firstinspires.ftc.teamcode.dynamite.DynCommands.DynCommand;
import org.firstinspires.ftc.teamcode.dynamite.DynVar.DynVar;
import org.firstinspires.ftc.teamcode.dynamite.DynExceptions.DynAutoStepException;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * Command for IF conditionals.
 * Syntax: if (condition) start ... end
 */
public class IfBlock implements DynCommand {
    private final String conditionVarId;
    private final String compareValue; // null for simple boolean check
    private final String compareOp;    // ==, !=, <, >, <=, >=
    private final List<DynCommand> body = new ArrayList<>();

    private BiConsumer<String, DynVar> setVar;
    private Function<String, DynVar> getVar;

    /**
     * Simple boolean condition: if (boolVar)
     */
    public IfBlock(String conditionVarId) {
        this.conditionVarId = conditionVarId;
        this.compareValue = null;
        this.compareOp = null;
    }

    /**
     * Comparison condition: if (var op value)
     */
    public IfBlock(String conditionVarId, String compareOp, String compareValue) {
        this.conditionVarId = conditionVarId;
        this.compareOp = compareOp;
        this.compareValue = compareValue;
    }

    public void addCommand(DynCommand cmd) {
        body.add(cmd);
    }

    public List<DynCommand> getBody() {
        return body;
    }

    @Override
    public void init(BiConsumer<String, DynVar> setVar, Function<String, DynVar> getVar) {
        this.setVar = setVar;
        this.getVar = getVar;

        for (DynCommand cmd : body) {
            cmd.init(setVar, getVar);
        }
    }

    @Override
    public void run() {
        boolean conditionMet = evaluateCondition();

        if (conditionMet) {
            for (DynCommand cmd : body) {
                cmd.run();
            }
        }
    }

    private boolean evaluateCondition() {
        DynVar condVar = getVar.apply(conditionVarId);
        if (condVar == null) {
            throw new DynAutoStepException("Condition variable not found: " + conditionVarId);
        }

        // Simple boolean check
        if (compareOp == null || compareValue == null) {
            // Accept boolean variables, numeric truthiness (non-zero), and non-empty strings
            try {
                return condVar.asBoolean();
            } catch (Exception ignored) {
                // not a boolean, try numeric
            }
            try {
                return condVar.asDouble() != 0.0;
            } catch (Exception ignored) {
                // not a number, try string
            }
            try {
                return !condVar.asString().isEmpty();
            } catch (Exception ignored) {
                // fallback
            }
            throw new DynAutoStepException("Condition variable is not usable as boolean: " + conditionVarId);
        }

        // Comparison operation (number/boolean/string)
        DynVar rightVar = getVar.apply(compareValue);

        // Try number comparison first
        try {
            double leftValue = condVar.asDouble();
            double rightValue = rightVar != null ? rightVar.asDouble() : Double.parseDouble(compareValue);
            switch (compareOp) {
                case "==":
                    return leftValue == rightValue;
                case "!=":
                    return leftValue != rightValue;
                case "<":
                    return leftValue < rightValue;
                case ">":
                    return leftValue > rightValue;
                case "<=":
                    return leftValue <= rightValue;
                case ">=":
                    return leftValue >= rightValue;
                default:
                    throw new DynAutoStepException("Unknown comparison operator: " + compareOp);
            }
        } catch (Exception ignored) {
            // Fall through to boolean/string comparison
        }

        // Boolean comparison
        try {
            boolean leftBool = condVar.asBoolean();
            boolean rightBool;
            if (rightVar != null) {
                rightBool = rightVar.asBoolean();
            } else {
                rightBool = Boolean.parseBoolean(compareValue);
            }
            switch (compareOp) {
                case "==":
                    return leftBool == rightBool;
                case "!=":
                    return leftBool != rightBool;
                default:
                    throw new DynAutoStepException("Invalid boolean comparison operator: " + compareOp);
            }
        } catch (Exception ignored) {
            // Fall through to string comparison
        }

        // String comparison
        String leftStr = condVar.asString();
        String rightStr = rightVar != null ? rightVar.asString() : compareValue;
        switch (compareOp) {
            case "==":
                return leftStr.equals(rightStr);
            case "!=":
                return !leftStr.equals(rightStr);
            default:
                throw new DynAutoStepException("Invalid string comparison operator: " + compareOp);
        }
    }

    @Override
    public String getDescription() {
        if (compareOp == null) {
            return "org.firstinspires.ftc.teamcode.dynamite.DynCommands.Control.IfBlock(" + conditionVarId + ", body=" + body.size() + " commands)";
        }
        return "org.firstinspires.ftc.teamcode.dynamite.DynCommands.Control.IfBlock(" + conditionVarId + " " + compareOp + " " + compareValue + ", body=" + body.size() + " commands)";
    }
}
