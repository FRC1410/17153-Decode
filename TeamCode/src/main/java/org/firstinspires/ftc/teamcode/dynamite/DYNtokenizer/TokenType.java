package org.firstinspires.ftc.teamcode.dynamite.DYNtokenizer;

/**
 * Token types for the DYN FTC auto language
 */
public enum TokenType {
    // Variable types
    FIELD_COORD,
    FIELD_POS,
    NUM,
    BOOL,
    STRING,
    LIST,
    JSON,

    // Math operations
    ADD,
    SUB,
    MUX,
    DIV,
    MOD,
    POW,
    SQR,
    SIN,
    INV_SIN,
    COS,
    INV_COS,
    TAN,
    INV_TAN,
    TO_RAD,
    TO_DEG,

    // Movement commands
    TURN_TO, 
    GO_TO,
    FOLLOW_BEZIER,

    // Declarations
    PATH_START_POSITION,
    AUTO_PATH,

    // Loops
    WHILE,
    FOR,
    WSTART,
    WEND,
    START,
    END,

    // Path declarators
    DEF_PATH,
    RUN,

    // Telemetry
    OUTPUT_2_TELEM,

    // Logical
    IF,
    AND,
    OR,
    NOT,

    // Custom commands
    CMD,
    FROM,
    TO,
    AS,

    // Literals and identifiers
    IDENTIFIER,
    NUMBER_LITERAL,
    STRING_LITERAL,
    BOOLEAN_LITERAL,

    // Symbols
    LPAREN,
    RPAREN,
    LBRACKET,
    RBRACKET,
    LBRACE,
    RBRACE,
    COMMA,
    DOT,
    EQUALS,
    COLON,
    SLASH,
    STAR,
    PERCENT,
    PLUS,
    MINUS,

    // Comparison operators
    EQUAL_EQUAL,
    NOT_EQUAL,
    LESS_THAN,
    GREATER_THAN,
    LESS_EQUAL,
    GREATER_EQUAL,

    // Special
    COMMENT,
    NEWLINE,
    EOF
}
