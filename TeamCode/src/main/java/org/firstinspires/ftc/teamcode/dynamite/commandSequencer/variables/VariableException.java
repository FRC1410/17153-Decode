package org.firstinspires.ftc.teamcode.dynamite.commandSequencer.variables;

import org.SquidSquad.commandSequencer.CommandException;

public class VariableException extends CommandException {
    public VariableException(String method, String involvedVars, String message) {
        super(currentLine,method,"Failed to use vars "+involvedVars+" because "+message);
    }
    
    public static int currentLine = 0;
    public static void setLine(int line){
        currentLine = line;
    }
}
