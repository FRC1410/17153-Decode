package org.firstinspires.ftc.teamcode.dynamite.DynCommands.Control;

import org.firstinspires.ftc.teamcode.dynamite.DynCommands.DynCommand;
import org.firstinspires.ftc.teamcode.dynamite.DynVar.DynVar;
import org.firstinspires.ftc.teamcode.dynamite.DynExceptions.DynAutoStepException;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * Command for WHILE loops.
 * Syntax: while (condition) start ... end
 */
public class WhileBlock implements DynCommand {
    private final String conditionVarId;
    private final String compareValue;
    private final String compareOp;
    private final List<DynCommand> body = new ArrayList<>();
    private final int maxIterations;

    private BiConsumer<String, DynVar> setVar;
    private Function<String, DynVar> getVar;

    /**
     * Simple boolean condition: while (boolVar)
     */
    public WhileBlock(String conditionVarId) {
        this(conditionVarId, null, null, 10000);
    }

    /**
     * Comparison condition: while (var op value)
     */
    public WhileBlock(String conditionVarId, String compareOp, String compareValue) {
        this(conditionVarId, compareOp, compareValue, 10000);
    }

    /**
     * Full constructor with max iterations safety.
     */
    public WhileBlock(String conditionVarId, String compareOp, String compareValue, int maxIterations) {
        this.conditionVarId = conditionVarId;
        this.compareOp = compareOp;
        this.compareValue = compareValue;
        this.maxIterations = maxIterations;
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
        int iterations = 0;

        while (evaluateCondition() && iterations < maxIterations) {
            for (DynCommand cmd : body) {
                cmd.run();
            }
            iterations++;
        }

        if (iterations >= maxIterations) {
            System.err.println("[WARNING] org.firstinspires.ftc.teamcode.dynamite.DynCommands.Control.WhileBlock hit max iterations limit: " + maxIterations);
        }
    }

    private boolean evaluateCondition() {
        DynVar condVar = getVar.apply(conditionVarId);
        if (condVar == null) {
            throw new DynAutoStepException("Condition variable not found: " + conditionVarId);
        }

        // Simple boolean check
        if (compareOp == null || compareValue == null) {
            return condVar.isTrue();
        }

        // Comparison operation
        double leftValue = condVar.asDouble();
        double rightValue;

        DynVar rightVar = getVar.apply(compareValue);
        if (rightVar != null) {
            rightValue = rightVar.asDouble();
        } else {
            rightValue = Double.parseDouble(compareValue);
        }

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
    }

    @Override
    public String getDescription() {
        if (compareOp == null) {
            return "org.firstinspires.ftc.teamcode.dynamite.DynCommands.Control.WhileBlock(" + conditionVarId + ", body=" + body.size() + " commands)";
        }
        return "org.firstinspires.ftc.teamcode.dynamite.DynCommands.Control.WhileBlock(" + conditionVarId + " " + compareOp + " " + compareValue + ", body=" + body.size() + " commands)";
    }
}
