package org.firstinspires.ftc.teamcode.dynamite.DynCommands.Etc;

import org.firstinspires.ftc.teamcode.dynamite.DynCommands.DynCommand;
import org.firstinspires.ftc.teamcode.dynamite.DynVar.DynVar;

import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * Wait/Sleep command - Pauses execution for a specified number of milliseconds.
 * 
 * Syntax:
 *   Wait milliseconds
 *   Wait variableNameWithMilliseconds
 * 
 * Examples:
 *   Wait 500           // Wait 500 milliseconds
 *   Num delay 1000
 *   Wait delay         // Wait 1000 milliseconds
 */
public class Wait implements DynCommand {
    private final long milliseconds;
    private final String varId;  // If waiting for a variable's value
    
    private Function<String, DynVar> getVar;
    
    // Thread reference for interruption (used by LinearOpMode)
    private static final long START_TIME = System.currentTimeMillis();
    private static long opModeStopTime = Long.MAX_VALUE;  // Will be set by OpMode
    
    /**
     * Create a Wait command with a fixed milliseconds value.
     */
    public Wait(long milliseconds) {
        this.milliseconds = milliseconds;
        this.varId = null;
    }
    
    /**
     * Create a Wait command that reads milliseconds from a variable.
     */
    public Wait(String varId) {
        this.milliseconds = -1;  // Unused when using variable
        this.varId = varId;
    }
    
    @Override
    public void init(BiConsumer<String, DynVar> setVar, Function<String, DynVar> getVar) {
        this.getVar = getVar;
    }
    
    @Override
    public void run() {
        long delayMs = milliseconds;
        
        // If reading from variable, get the value
        if (varId != null && getVar != null) {
            try {
                DynVar var = getVar.apply(varId);
                if (var != null && var.getValue() != null) {
                    delayMs = ((Number) var.getValue()).longValue();
                } else {
                    System.err.println("[WAIT] Variable '" + varId + "' not found or not a number");
                    return;
                }
            } catch (Exception e) {
                System.err.println("[WAIT] Error reading variable '" + varId + "': " + e.getMessage());
                return;
            }
        }
        
        if (delayMs < 0) {
            System.err.println("[WAIT] Invalid delay: " + delayMs + "ms");
            return;
        }
        
        try {
            Thread.sleep(delayMs);
        } catch (InterruptedException e) {
            System.out.println("[WAIT] Interrupted after " + delayMs + "ms");
            Thread.currentThread().interrupt();
        }
    }
    
    @Override
    public String getDescription() {
        if (varId != null) {
            return "wait(" + varId + ")";
        }
        return "wait(" + milliseconds + "ms)";
    }
}
