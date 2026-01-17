package org.firstinspires.ftc.teamcode.dynamite.DynCommands.Control;

import org.firstinspires.ftc.teamcode.dynamite.DynCommands.DynCommand;
import org.firstinspires.ftc.teamcode.dynamite.DynExceptions.DynAutoStepException;
import org.firstinspires.ftc.teamcode.dynamite.DynVar.DynVar;
import org.firstinspires.ftc.teamcode.dynamite.DynExceptions.DynVarException;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * Command for FOR loops.
 * Syntax: for N as iteratorVar start ... end
 */
public class ForLoop implements DynCommand {
    private final int iterations;
    private final String iteratorVarId;
    private final List<DynCommand> body = new ArrayList<>();

    private BiConsumer<String, DynVar> setVar;
    private Function<String, DynVar> getVar;

    public ForLoop(int iterations, String iteratorVarId) {
        this.iterations = iterations;
        this.iteratorVarId = iteratorVarId;
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
        try {
            // Create iterator variable
            DynVar iterator = new DynVar("Number", iteratorVarId, 0.0);
            setVar.accept(iteratorVarId, iterator);

            for (int i = 0; i < iterations; i++) {
                // Update iterator
                getVar.apply(iteratorVarId).setValue((double) i);

                // Execute body
                for (DynCommand cmd : body) {
                    cmd.run();
                }
            }
        } catch (Exception e) {
            throw new DynAutoStepException("org.firstinspires.ftc.teamcode.dynamite.DynCommands.Control.ForLoop error: " + e.getMessage());
        }
    }

    @Override
    public String getDescription() {
        return "org.firstinspires.ftc.teamcode.dynamite.DynCommands.Control.ForLoop(" + iterations + " as " + iteratorVarId + ", body=" + body.size() + " commands)";
    }
}
