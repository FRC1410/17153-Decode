package org.firstinspires.ftc.teamcode.dynamite.DynExceptions;

/**
 * Enhanced exception thrown when tokenization fails
 */
public class TokenizerException extends RuntimeException {
    private final int line;
    private final int column;
    private final String context;
    private final ErrorType errorType;

    public enum ErrorType {
        UNEXPECTED_CHARACTER,
        UNTERMINATED_STRING,
        UNTERMINATED_COMMENT,
        SYNTAX_ERROR,
        GENERAL_ERROR
    }

    public TokenizerException(String message, int line, int column) {
        this(message, line, column, null, ErrorType.GENERAL_ERROR);
    }

    public TokenizerException(String message, int line, int column, String context, ErrorType errorType) {
        super(formatMessage(message, line, column, context));
        this.line = line;
        this.column = column;
        this.context = context;
        this.errorType = errorType;
    }

    private static String formatMessage(String message, int line, int column, String context) {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Tokenizer error at line %d, column %d: %s", line, column, message));
        if (context != null && !context.isEmpty()) {
            sb.append("\nContext: ").append(context);
        }
        return sb.toString();
    }

    public int getLine() {
        return line;
    }

    public int getColumn() {
        return column;
    }

    public String getContext() {
        return context;
    }

    public ErrorType getErrorType() {
        return errorType;
    }
}
