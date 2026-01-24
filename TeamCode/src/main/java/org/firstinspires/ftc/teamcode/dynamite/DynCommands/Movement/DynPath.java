package org.firstinspires.ftc.teamcode.dynamite.DynCommands.Movement;

import org.firstinspires.ftc.teamcode.dynamite.DynCommands.DynCommand;
import org.firstinspires.ftc.teamcode.dynamite.DynExceptions.DynAutoStepException;
import org.firstinspires.ftc.teamcode.dynamite.DynVar.DynVar;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Represents a defined path/function in DYN.
 * Paths contain a sequence of commands that can be executed.
 */
public class DynPath {
    private final String name;
    private final List<DynCommand> commands = new ArrayList<>();
    private final List<DynPath> nestedPaths = new ArrayList<>();
    private final Map<String, DynPath> pathRegistry;

    private BiConsumer<String, DynVar> setVar;
    private Function<String, DynVar> getVar;
    private Consumer<String> telemOutput;

    public DynPath(String name, Map<String, DynPath> pathRegistry) {
        this.name = name;
        this.pathRegistry = pathRegistry;
    }

    public String getName() {
        return name;
    }

    public void addCommand(DynCommand cmd) {
        commands.add(cmd);
    }

    public void addNestedPath(DynPath path) {
        nestedPaths.add(path);
    }

    public List<DynCommand> getCommands() {
        return commands;
    }

    /**
     * Initialize all commands in this path with variable access functions.
     */
    public void init(BiConsumer<String, DynVar> setVar, Function<String, DynVar> getVar, Consumer<String> telemOutput) {
        this.setVar = setVar;
        this.getVar = getVar;
        this.telemOutput = telemOutput;

        for (DynCommand cmd : commands) {
            cmd.init(setVar, getVar);
        }

        for (DynPath nested : nestedPaths) {
            nested.init(setVar, getVar, telemOutput);
        }
    }

    /**
     * Execute all commands in this path sequentially.
     */
    public void run() {
        for (int i = 0; i < commands.size(); i++) {
            DynCommand cmd = commands.get(i);
            try {
                System.out.println("[DynPath] Running command #" + i + ": " + cmd.getDescription());
                cmd.run();
                System.out.println("[DynPath] Completed command #" + i + ": " + cmd.getDescription());
            } catch (Exception e) {
                String msg = "[ERROR] Command failed (#" + i + "): " + cmd.getDescription() + " -> " + e.getMessage();
                if (telemOutput != null) {
                    telemOutput.accept(msg);
                } else {
                    System.err.println(msg);
                }
                throw e;
            }
        }
    }

    /**
     * Run another path by name (for RUN command).
     */
    public void runPath(String pathName) {
        DynPath target = pathRegistry.get(pathName);
        if (target != null) {
            target.run();
        } else {
            throw new DynAutoStepException("Path not found: " + pathName);
        }
    }

    @Override
    public String toString() {
        return "org.firstinspires.ftc.teamcode.dynamite.DynCommands.Movement.DynPath{name='" + name + "', commands=" + commands.size() + "}";
    }
}
