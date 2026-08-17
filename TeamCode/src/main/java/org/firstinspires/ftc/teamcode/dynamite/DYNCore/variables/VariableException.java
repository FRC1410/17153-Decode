package org.firstinspires.ftc.teamcode.dynamite.DYNCore.variables;

import org.firstinspires.ftc.teamcode.dynamite.DYNCore.CommandException;

public class VariableException extends CommandException {
    public VariableException(String method, String involvedVars, String message) {
        super(currentLine,method,"Failed to use vars "+involvedVars+" because "+message);
    }
    
    public static int currentLine = 0;
    public static void setLine(int line){
        currentLine = line;
    }
}
