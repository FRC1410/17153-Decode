package org.firstinspires.ftc.teamcode.dynamite.DynCommands;

import org.firstinspires.ftc.teamcode.dynamite.DynVar.DynVar;

import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * Base interface for all DYN commands.
 * All commands follow a common pattern:
 * 1. Construction with variable IDs
 * 2. Initialization with getter/setter functions
 * 3. Execution via run()
 */
public interface DynCommand {
    /**
     * Initialize the command with variable access functions.
     * @param setVar Function to set a variable by ID
     * @param getVar Function to get a variable by ID
     */
    void init(BiConsumer<String, DynVar> setVar, Function<String, DynVar> getVar);

    /**
     * Execute the command.
     */
    void run();

    /**
     * Get a description of this command for debugging.
     */
    default String getDescription() {
        return this.getClass().getSimpleName();
    }
}
