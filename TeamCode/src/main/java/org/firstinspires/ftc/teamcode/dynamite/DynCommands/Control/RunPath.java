package org.firstinspires.ftc.teamcode.dynamite.DynCommands.Control;

import org.firstinspires.ftc.teamcode.dynamite.DynCommands.DynCommand;
import org.firstinspires.ftc.teamcode.dynamite.DynCommands.Movement.DynPath;
import org.firstinspires.ftc.teamcode.dynamite.DynExceptions.DynAutoStepException;
import org.firstinspires.ftc.teamcode.dynamite.DynVar.DynVar;

import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * Command to run another defined path.
 * Syntax: RUN pathName
 */
public class RunPath implements DynCommand {
    private final String pathName;
    private final Map<String, DynPath> pathRegistry;

    private BiConsumer<String, DynVar> setVar;
    private Function<String, DynVar> getVar;

    public RunPath(String pathName, Map<String, DynPath> pathRegistry) {
        this.pathName = pathName;
        this.pathRegistry = pathRegistry;
    }

    @Override
    public void init(BiConsumer<String, DynVar> setVar, Function<String, DynVar> getVar) {
        this.setVar = setVar;
        this.getVar = getVar;
    }

    @Override
    public void run() {
        DynPath target = pathRegistry.get(pathName);
        if (target != null) {
            target.run();
        } else {
            throw new DynAutoStepException("Path not found: " + pathName);
        }
    }

    @Override
    public String getDescription() {
        return "org.firstinspires.ftc.teamcode.dynamite.DynCommands.Control.RunPath(" + pathName + ")";
    }
}
