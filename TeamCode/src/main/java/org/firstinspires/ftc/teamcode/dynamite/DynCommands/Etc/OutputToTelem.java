package org.firstinspires.ftc.teamcode.dynamite.DynCommands.Etc;

import org.firstinspires.ftc.teamcode.dynamite.DynCommands.DynCommand;
import org.firstinspires.ftc.teamcode.dynamite.DynVar.DynVar;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Command to output text to telemetry.
 */
public class OutputToTelem implements DynCommand {
    private final String messageOrVarId;
    private final boolean isLiteral;

    private Function<String, DynVar> getVar;
    private Consumer<String> telemOutput;

    public OutputToTelem(String messageOrVarId, boolean isLiteral) {
        this.messageOrVarId = messageOrVarId;
        this.isLiteral = isLiteral;
    }

    public void setTelemOutput(Consumer<String> telemOutput) {
        this.telemOutput = telemOutput;
    }

    @Override
    public void init(BiConsumer<String, DynVar> setVar, Function<String, DynVar> getVar) {
        this.getVar = getVar;
    }

    @Override
    public void run() {
        String output;
        if (isLiteral) {
            output = messageOrVarId;
        } else {
            DynVar var = getVar.apply(messageOrVarId);
            output = var != null ? var.toTelemetryString() : messageOrVarId;
        }

        if (telemOutput != null) {
            telemOutput.accept(output);
        } else {
            System.out.println("[TELEM] " + output);
        }
    }

    @Override
    public String getDescription() {
        return "org.firstinspires.ftc.teamcode.dynamite.DynCommands.Etc.OutputToTelem(" + (isLiteral ? "\"" + messageOrVarId + "\"" : messageOrVarId) + ")";
    }
}
