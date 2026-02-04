package org.firstinspires.ftc.teamcode.dynamite.DynCommands.Etc;

import org.firstinspires.ftc.teamcode.dynamite.DynCommands.DynCommand;
import org.firstinspires.ftc.teamcode.dynamite.DynVar.DynVar;

import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * Update command - Sends the telemetry buffer to the driver station and clears it.
 * 
 * Syntax:
 *   Update
 * 
 * Usage:
 *   AddData "Line 1"
 *   AddData "Line 2"
 *   Update  // Sends both lines to driver station and clears buffer
 * 
 * Example:
 *   AddData "Starting autonomous"
 *   Update
 *   
 *   Wait 1000
 *   AddData "Moving to position"
 *   Update
 */
public class Update implements DynCommand {
    
    private static Runnable updateCallback = null;
    
    /**
     * Set the callback function that sends and clears the telemetry buffer.
     * This is set by DynProcessor during initialization.
     */
    public static void setUpdateCallback(Runnable callback) {
        updateCallback = callback;
    }
    
    @Override
    public void init(BiConsumer<String, DynVar> setVar, Function<String, DynVar> getVar) {
        // No initialization needed
    }
    
    @Override
    public void run() {
        if (updateCallback != null) {
            updateCallback.run();
        } else {
            System.out.println("[WARN] Update: No update callback set");
        }
    }
    
    @Override
    public String getDescription() {
        return "Update()";
    }
}
