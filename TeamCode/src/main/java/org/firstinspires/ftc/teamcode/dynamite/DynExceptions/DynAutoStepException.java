package org.firstinspires.ftc.teamcode.dynamite.DynExceptions;

public class DynAutoStepException extends RuntimeException {
    public DynAutoStepException(String error) {
        super(error);
    }

    public DynAutoStepException(String error, String relatedID){
        super(buildMessage(error, new String[]{relatedID}));
    }

    public DynAutoStepException(String error, String[] relatedIDs) {
        super(buildMessage(error, relatedIDs));
    }

    private static String buildMessage(String error, String[] relatedIDs) {
        StringBuilder out = new StringBuilder(error).append(" Related IDs: ");
        for (int i = 0; i < relatedIDs.length; i++) {
            out.append(relatedIDs[i]);
            if (i < relatedIDs.length - 1) {
                out.append(", ");
            }
        }
        return out.toString();
    }
}