package org.firstinspires.ftc.teamcode.dynamite.commandSequencer.Commands.telemetry;

import org.firstinspires.ftc.teamcode.dynamite.commandSequencer.Commands.Command;
import org.firstinspires.ftc.teamcode.dynamite.commandSequencer.Commands.CommandType;

public class AddData extends Command {
    private final boolean usingliteral;
    private Object literal;
    public AddData(int line, String value){
        super(line, CommandType.AddData,new String[]{value});
        usingliteral = false;
    }
    public AddData(int line, Object literal){
        super(line, CommandType.AddData,new String[]{String.valueOf(literal)});
        usingliteral = true;
        this.literal = literal;
    }
    @Override
    public void run(){
        super.run();
        if (usingliteral) telemBuffer.add("["+(line+1)+"]: "+literal);
        else telemBuffer.add("["+(line+1)+"]: "+getVar(InVarIDs[0]).getTelemetryData());
    }

    // timestamping stuff
    private static final long startTime = System.nanoTime();
    private String getTimestamp(){
        long elapsedNanos = System.nanoTime() - startTime;
        long totalMillis = elapsedNanos / 1_000_000;

        long minutes = (totalMillis / 60000) % 60;
        long seconds = (totalMillis / 1000) % 60;
        long millis = totalMillis % 1000;

        return String.format("%02d:%02d.%03d", minutes, seconds, millis);
    }
}
