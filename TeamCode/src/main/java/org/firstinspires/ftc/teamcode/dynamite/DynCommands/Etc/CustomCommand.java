package org.firstinspires.ftc.teamcode.dynamite.DynCommands.Etc;

import org.firstinspires.ftc.teamcode.dynamite.DynCommands.DynCommand;
import org.firstinspires.ftc.teamcode.dynamite.DynVar.DynVar;

import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * Command for custom robot subsystem functions.
 * Supports 4 usage scenarios:
 * 
 * 1. No input/output: cmd functionName
 * 2. Input only: cmd functionName from inputVar
 * 3. Output only: cmd functionName to outputVar
 * 4. Both input and output: cmd functionName from inputVar to outputVar
 * 
 * Examples:
 *   cmd grabSample                      // Scenario 1: simple action
 *   cmd setArmSpeed from speedValue     // Scenario 2: pass input
 *   cmd getArmPosition to currentPos    // Scenario 3: get output
 *   cmd moveArm from target to actual   // Scenario 4: both input/output
 */
public class CustomCommand implements DynCommand {
    private final String functionName;
    private final String inputVarId;
    private final String outputVarId;
    private final CustomCommandHandler handler;

    private BiConsumer<String, DynVar> setVar;
    private Function<String, DynVar> getVar;

    /**
     * Interface for handling custom commands.
     * Implement this in your robot subsystem classes.
     */
    public interface CustomCommandHandler {
        /**
         * Execute a custom command.
         * @param functionName The name of the function to execute
         * @param input The input variable (may be null if no input provided)
         * @return The output variable (may be null if no output needed)
         */
        DynVar execute(String functionName, DynVar input);
    }

    /**
     * Full constructor - supports all 4 scenarios.
     * Used by DynProcessor for: cmd name from input to output
     */
    public CustomCommand(String functionName, String inputVarId, String outputVarId, CustomCommandHandler handler) {
        this.functionName = functionName;
        this.inputVarId = inputVarId;
        this.outputVarId = outputVarId;
        this.handler = handler;
    }

    /**
     * Scenario 2: Input only - cmd name from input
     * Convenience constructor when no output is needed.
     */
    public CustomCommand(String functionName, String inputVarId, CustomCommandHandler handler) {
        this(functionName, inputVarId, null, handler);
    }

    /**
     * Scenario 1: No input/output - cmd name
     * Convenience constructor for simple commands with no input or output.
     */
    public CustomCommand(String functionName, CustomCommandHandler handler) {
        this(functionName, null, null, handler);
    }

    @Override
    public void init(BiConsumer<String, DynVar> setVar, Function<String, DynVar> getVar) {
        this.setVar = setVar;
        this.getVar = getVar;
    }

    @Override
    public void run() {
        // Scenario 2 & 4: Get input variable if provided
        DynVar input = null;
        if (inputVarId != null) {
            input = getVar.apply(inputVarId);
        }

        if (handler != null) {
            DynVar result = handler.execute(functionName, input);

            // Scenario 3 & 4: Set output variable if provided and result is not null
            if (outputVarId != null && result != null) {
                setVar.accept(outputVarId, result);
            }
        } else {
            System.out.println("[CMD] No handler for: " + functionName);
        }
    }

    public String getFunctionName() {
        return functionName;
    }

    @Override
    public String getDescription() {
        StringBuilder sb = new StringBuilder("org.firstinspires.ftc.teamcode.dynamite.DynCommands.Etc.CustomCommand(" + functionName);
        if (inputVarId != null) {
            sb.append(" from ").append(inputVarId);
        }
        if (outputVarId != null) {
            sb.append(" to ").append(outputVarId);
        }
        sb.append(")");
        return sb.toString();
    }
}
