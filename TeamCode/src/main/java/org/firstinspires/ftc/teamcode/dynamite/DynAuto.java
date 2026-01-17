package org.firstinspires.ftc.teamcode.dynamite;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.dynamite.DynCommands.Etc.CustomCommand;
import org.firstinspires.ftc.teamcode.dynamite.DynCommands.Movement.DynPath;
import org.firstinspires.ftc.teamcode.dynamite.DynExceptions.DynAutoStepException;
import org.firstinspires.ftc.teamcode.dynamite.DynVar.DynVar;
import org.firstinspires.ftc.teamcode.dynamite.DynVar.DynVarBuffer;
import org.firstinspires.ftc.teamcode.dynamite.PPintegration.PedroPathingBridge;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Consumer;

/**
 * DynAuto - Main execution engine for DYN autonomous scripts.
 * 
 * This class provides an easy-to-use interface for:
 * - Loading and parsing DYN scripts
 * - Integrating with PedroPathing for robot movement
 * - Registering custom subsystem commands
 * - Running the autonomous routine
 * 
 * Usage example:
 * <pre>
 * DynAuto auto = new DynAuto();
 * auto.setPathingBridge(myPedroPathing);
 * auto.registerCustomCommand("grabSample", (name, input) -> {
 *     // Your subsystem code here
 *     return null;
 * });
 * auto.loadScript("/path/to/auto.dyn", new String[]{"grabSample", "dropSample"});
 * auto.run();
 * </pre>
 */
public class DynAuto {
    private DynProcessor processor;
    private PedroPathingBridge pathingBridge;
    private Telemetry telemetry;
    private final Map<String, CustomCommand.CustomCommandHandler> customHandlers = new HashMap<>();
    private boolean isInitialized = false;

    /**
     * Create DynAuto with an existing telemetry instance.
     */
    public DynAuto(Telemetry telemetry) {
        this.processor = new DynProcessor();
        this.telemetry = telemetry;
        this.pathingBridge = new PedroPathingBridge();
    }

    // ==================== Configuration ====================

    /**
     * Set the PedroPathing bridge for movement commands.
     * Call this before loadScript() to enable movement commands.
     */
    public void setPathingBridge(PedroPathingBridge bridge) {
        this.pathingBridge = bridge;
        processor.setPathingBridge(bridge);
    }

    /**
     * Get the current PedroPathing bridge.
     */
    public PedroPathingBridge getPathingBridge() {
        return pathingBridge;
    }

    /**
     * Set custom telemetry output function.
     */
    public void setTelemOutput(Consumer<String> output) {
        processor.setTelemOutput(output);
        pathingBridge.setTelemOutput(output);
    }

    /**
     * Register a handler for a specific custom command.
     * 
     * @param commandName The name of the command (e.g., "grabSample")
     * @param handler The handler function that executes the command
     */
    public void registerCustomCommand(String commandName, CustomCommand.CustomCommandHandler handler) {
        customHandlers.put(commandName, handler);
    }

    /**
     * Register multiple custom commands with a single handler.
     * The handler will receive the command name and can dispatch accordingly.
     */
    public void setCustomCommandHandler(CustomCommand.CustomCommandHandler handler) {
        processor.setCustomCommandHandler(handler);
    }

    // ==================== Script Loading ====================

    /**
     * Load a DYN script from a file path.
     * 
     * @param filePath Path to the .dyn file
     * @param customFuncIds Array of custom function names used in the script
     */
    public void loadScript(String filePath, String[] customFuncIds) {
        String scriptContent = readFile(filePath);
        loadScriptContent(scriptContent, customFuncIds);
    }

    /**
     * Load a DYN script from a File object.
     */
    public void loadScript(File file, String[] customFuncIds) {
        String scriptContent = readFile(file);
        loadScriptContent(scriptContent, customFuncIds);
    }

    /**
     * Load a DYN script directly from a string.
     * 
     * @param scriptContent The DYN script content
     * @param customFuncIds Array of custom function names used in the script
     */
    public void loadScriptContent(String scriptContent, String[] customFuncIds) {
        // Set up the pathing bridge
        processor.setPathingBridge(pathingBridge);

        // Create a combined handler that checks our registered handlers
        processor.setCustomCommandHandler((name, input) -> {
            CustomCommand.CustomCommandHandler handler = customHandlers.get(name);
            if (handler != null) {
                return handler.execute(name, input);
            }
            System.out.println("[CMD] Executing: " + name);
            return null;
        });

        // Initialize the processor
        processor.init(this.telemetry, scriptContent, customFuncIds);
        isInitialized = true;
    }

    // ==================== Execution ====================

    /**
     * Run the main autonomous path defined in the script.
     */
    public void run() {
        if (!isInitialized) {
            throw new DynAutoStepException("DynAuto not initialized. Call loadScript() first.");
        }

        System.out.println("\n========================================");
        System.out.println("        STARTING DYN AUTONOMOUS");
        System.out.println("========================================\n");

        try {
            processor.runAuto();
        } catch (Exception e) {
            System.err.println("[ERROR] Auto execution failed: " + e.getMessage());
            e.printStackTrace();
            if (telemetry != null) {
                telemetry.addData("DYN ERROR", e.getMessage());
                telemetry.update();
            }
        }

        System.out.println("\n========================================");
        System.out.println("        DYN AUTONOMOUS COMPLETE");
        System.out.println("========================================\n");
    }

    /**
     * Run a specific path by name.
     */
    public void runPath(String pathName) {
        if (!isInitialized) {
            throw new DynAutoStepException("DynAuto not initialized. Call loadScript() first.");
        }

        DynPath path = processor.getPath(pathName);
        if (path == null) {
            throw new DynAutoStepException("Path not found: " + pathName);
        }

        path.run();
    }

    // ==================== Variable Access ====================

    /**
     * Get a variable from the script's variable buffer.
     */
    public DynVar getVariable(String id) {
        return processor.getVarBuffer().getVar(id);
    }

    /**
     * Set a variable in the script's variable buffer.
     */
    public void setVariable(String id, DynVar value) {
        processor.getVarBuffer().setVar(id, value);
    }

    /**
     * Get the variable buffer for direct access.
     */
    public DynVarBuffer getVarBuffer() {
        return processor.getVarBuffer();
    }

    // ==================== Utility ====================

    private String readFile(String filePath) {
        return readFile(new File(filePath));
    }

    private String readFile(File file) {
        StringBuilder content = new StringBuilder();
        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                content.append(scanner.nextLine()).append("\n");
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            throw new DynAutoStepException("Script file not found: " + file.getPath());
        }
        return content.toString();
    }

    /**
     * Check if the auto has been initialized.
     */
    public boolean isInitialized() {
        return isInitialized;
    }

    /**
     * Get the underlying processor for advanced usage.
     */
    public DynProcessor getProcessor() {
        return processor;
    }
}
