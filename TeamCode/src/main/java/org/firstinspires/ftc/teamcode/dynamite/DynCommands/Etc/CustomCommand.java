package org.firstinspires.ftc.teamcode.dynamite.DynCommands.Etc;

import org.firstinspires.ftc.teamcode.dynamite.DynCommands.DynCommand;
import org.firstinspires.ftc.teamcode.dynamite.DynVar.DynVar;

import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * Command for custom robot subsystem functions.
 * Syntax: cmd functionName from inputVar to outputVar
 *     or: cmd functionName from inputVar
 *     or: cmd functionName
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
         * @param input The input variable (may be null)
         * @return The output variable (may be null)
         */
        DynVar execute(String functionName, DynVar input);
    }

    public CustomCommand(String functionName, String inputVarId, String outputVarId, CustomCommandHandler handler) {
        this.functionName = functionName;
        this.inputVarId = inputVarId;
        this.outputVarId = outputVarId;
        this.handler = handler;
    }

    public CustomCommand(String functionName, String inputVarId, CustomCommandHandler handler) {
        this(functionName, inputVarId, null, handler);
    }

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
        DynVar input = null;
        if (inputVarId != null) {
            input = getVar.apply(inputVarId);
        }

        if (handler != null) {
            DynVar result = handler.execute(functionName, input);

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
