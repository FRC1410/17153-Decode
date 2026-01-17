package org.firstinspires.ftc.teamcode.dynamite.DynCommands.Etc;

import org.firstinspires.ftc.teamcode.dynamite.DynCommands.DynCommand;
import org.firstinspires.ftc.teamcode.dynamite.DynExceptions.DynAutoStepException;
import org.firstinspires.ftc.teamcode.dynamite.DynVar.DynVar;
import org.firstinspires.ftc.teamcode.dynamite.DynExceptions.DynVarException;

import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * Command to declare a variable.
 * Supports: Num, Bool, String, FieldCoord, FieldPos
 */
public class DeclareVar implements DynCommand {
    private final String varType;
    private final String varId;
    private final Object initialValue;

    private BiConsumer<String, DynVar> setVar;
    private Function<String, DynVar> getVar;

    public DeclareVar(String varType, String varId, Object initialValue) {
        this.varType = varType;
        this.varId = varId;
        this.initialValue = initialValue;
    }

    @Override
    public void init(BiConsumer<String, DynVar> setVar, Function<String, DynVar> getVar) {
        this.setVar = setVar;
        this.getVar = getVar;
    }

    @Override
    public void run() {
        try {
            DynVar newVar;
            switch (varType) {
                case "Number":
                    if (initialValue instanceof Number) {
                        newVar = new DynVar("Number", varId, ((Number) initialValue).doubleValue());
                    } else if (initialValue instanceof String) {
                        // Reference to another variable
                        DynVar ref = getVar.apply((String) initialValue);
                        if (ref != null) {
                            newVar = new DynVar("Number", varId, ref.asDouble());
                        } else {
                            newVar = new DynVar("Number", varId, Double.parseDouble((String) initialValue));
                        }
                    } else {
                        newVar = new DynVar("Number", varId, 0.0);
                    }
                    break;

                case "Boolean":
                    if (initialValue instanceof Boolean) {
                        newVar = new DynVar("Boolean", varId, (Boolean) initialValue);
                    } else if (initialValue instanceof String) {
                        newVar = new DynVar("Boolean", varId, Boolean.parseBoolean((String) initialValue));
                    } else {
                        newVar = new DynVar("Boolean", varId, false);
                    }
                    break;

                case "String":
                    newVar = new DynVar("String", varId, String.valueOf(initialValue));
                    break;

                case "Field coordinates":
                case "Field cords":
                    if (initialValue instanceof double[]) {
                        newVar = new DynVar("Field cords", varId, (double[]) initialValue);
                    } else {
                        newVar = new DynVar("Field cords", varId, new double[]{0, 0});
                    }
                    break;

                case "Field position":
                case "Field pos":
                    if (initialValue instanceof double[]) {
                        newVar = new DynVar("Field pos", varId, (double[]) initialValue);
                    } else {
                        newVar = new DynVar("Field pos", varId, new double[]{0, 0, 0});
                    }
                    break;

                default:
                    throw new DynAutoStepException("Unknown variable type: " + varType);
            }
            setVar.accept(varId, newVar);
        } catch (Exception e) {
            throw new DynAutoStepException("Failed to declare variable: " + varId + " - " + e.getMessage());
        }
    }

    @Override
    public String getDescription() {
        return "org.firstinspires.ftc.teamcode.dynamite.DynCommands.Etc.DeclareVar(" + varType + " " + varId + " = " + initialValue + ")";
    }
}
