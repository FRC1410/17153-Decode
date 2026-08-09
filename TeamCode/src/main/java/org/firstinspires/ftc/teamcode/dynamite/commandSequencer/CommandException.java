package org.firstinspires.ftc.teamcode.dynamite.commandSequencer;

public class CommandException extends RuntimeException {
    public CommandException(int line, String command, String message) {
        super("\n\nDYN ERROR!\n@line:"+(line+1)+" Command: "+command+" File:\n"+getFileStringError(line)+"\nReason: "+message+"\n");
    }
    public CommandException(int line, int column, String reason){
        super("\n\nDYN ERROR!\n@line:"+(line+1)+" Column:"+column+" File:\n"+getFileStringError(line,column)+"\nReason: "+reason+"\n");
    }

    private static String[] ogFileLines;
    public static void linkFile(String file){
        ogFileLines = file.split("\r\n|\r|\n");
    }

    private static String getFileStringError(int line){
        if (line >= 0 && line < ogFileLines.length) {
            String fileLine = ogFileLines[line];
            StringBuilder upArrows = new StringBuilder();
            boolean upDawg = false;
            for (char c : fileLine.toCharArray()) {
                if (c != ' ') upDawg = true;
                if (upDawg) upArrows.append("^");
                else upArrows.append(' ');
            }
            return fileLine+"\n"+upArrows;
        } else {
            return "Unable to access file. Line index "+line+" is out of bounds";
        }
    }
    private static String getFileStringError(int line, int column){
        if (line >= 0 && line < ogFileLines.length) {
            String fileLine = ogFileLines[line];
            StringBuilder pointerHeads = new StringBuilder();
            for (int x = 0; x < column; x++) pointerHeads.append(' ');
            pointerHeads.append("^^^");
            return fileLine + "\n" + pointerHeads;
        } else {
            return "Unable to access file. Line index "+line+" is out of bounds";
        }
    }
}
