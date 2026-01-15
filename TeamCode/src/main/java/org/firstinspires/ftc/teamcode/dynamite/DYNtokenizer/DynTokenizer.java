package org.firstinspires.ftc.teamcode.dynamite.DYNtokenizer;

import org.firstinspires.ftc.teamcode.dynamite.DynExceptions.TokenizerException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Tokenizer for the DYN FTC autonomous programming language.
 * Compatible with FTC SDK - uses only standard Java libraries.
 */
public class DynTokenizer {
    private final String source;
    private final List<Token> tokens = new ArrayList<>();
    private final Map<String, TokenType> keywords = new HashMap<>();
    private final Map<String, TokenType> customCommands = new HashMap<>();

    private int start = 0;
    private int current = 0;
    private int line = 1;
    private int column = 1;

    public DynTokenizer(String source) {
        this.source = source;
        initKeywords();
    }

    private void initKeywords() {
        // Variable types
        keywords.put("FieldCoord", TokenType.FIELD_COORD);
        keywords.put("FieldPos", TokenType.FIELD_POS);
        keywords.put("Num", TokenType.NUM);
        keywords.put("Bool", TokenType.BOOL);
        keywords.put("String", TokenType.STRING);
        keywords.put("List", TokenType.LIST);
        keywords.put("Json", TokenType.JSON);

        // Math operations
        keywords.put("ADD", TokenType.ADD);
        keywords.put("SUB", TokenType.SUB);
        keywords.put("MUX", TokenType.MUX);
        keywords.put("DIV", TokenType.DIV);
        keywords.put("MOD", TokenType.MOD);
        keywords.put("POW", TokenType.POW);
        keywords.put("SQR", TokenType.SQR);
        keywords.put("SIN", TokenType.SIN);
        keywords.put("invSIN", TokenType.INV_SIN);
        keywords.put("invSin", TokenType.INV_SIN);
        keywords.put("COS", TokenType.COS);
        keywords.put("invCOS", TokenType.INV_COS);
        keywords.put("invCos", TokenType.INV_COS);
        keywords.put("TAN", TokenType.TAN);
        keywords.put("invTAN", TokenType.INV_TAN);
        keywords.put("invTan", TokenType.INV_TAN);
        keywords.put("toRad", TokenType.TO_RAD);
        keywords.put("toDeg", TokenType.TO_DEG);

        // Movement commands
        keywords.put("turnTo", TokenType.TURN_TO);
        keywords.put("goTo", TokenType.GO_TO);
        keywords.put("followBezier", TokenType.FOLLOW_BEZIER);

        // Declarations
        keywords.put("PathStartPosition", TokenType.PATH_START_POSITION);
        keywords.put("autoPath", TokenType.AUTO_PATH);

        // Loops and blocks
        keywords.put("while", TokenType.WHILE);
        keywords.put("for", TokenType.FOR);
        keywords.put("Wstart", TokenType.WSTART);
        keywords.put("Wend", TokenType.WEND);
        keywords.put("start", TokenType.START);
        keywords.put("end", TokenType.END);

        // Path declarators
        keywords.put("def_path", TokenType.DEF_PATH);
        keywords.put("RUN", TokenType.RUN);

        // Telemetry
        keywords.put("output2telem", TokenType.OUTPUT_2_TELEM);

        // Logical
        keywords.put("if", TokenType.IF);
        keywords.put("and", TokenType.AND);
        keywords.put("or", TokenType.OR);
        keywords.put("not", TokenType.NOT);

        // Custom commands
        keywords.put("cmd", TokenType.CMD);
        keywords.put("from", TokenType.FROM);
        keywords.put("to", TokenType.TO);
        keywords.put("as", TokenType.AS);

        // Boolean literals
        keywords.put("true", TokenType.BOOLEAN_LITERAL);
        keywords.put("false", TokenType.BOOLEAN_LITERAL);
    }

    /**
     * Register a custom command that can be used with the 'cmd' keyword.
     * Call this before tokenize() to add custom robot subsystem commands.
     */
    public void registerCustomCommand(String commandName) {
        customCommands.put(commandName, TokenType.IDENTIFIER);
    }

    /**
     * Tokenize the source code and return a list of tokens.
     */
    public List<Token> tokenize() {
        while (!isAtEnd()) {
            start = current;
            scanToken();
        }

        tokens.add(new Token(TokenType.EOF, "", line, column));
        return tokens;
    }

    private void scanToken() {
        char c = advance();

        switch (c) {
            case '(': addToken(TokenType.LPAREN); break;
            case ')': addToken(TokenType.RPAREN); break;
            case '[': addToken(TokenType.LBRACKET); break;
            case ']': addToken(TokenType.RBRACKET); break;
            case '{': addToken(TokenType.LBRACE); break;
            case '}': addToken(TokenType.RBRACE); break;
            case ',': addToken(TokenType.COMMA); break;
            case '.': addToken(TokenType.DOT); break;
            case ':': addToken(TokenType.COLON); break;
            case '+': addToken(TokenType.PLUS); break;
            case '-':
                if (isDigit(peek())) {
                    number();
                } else {
                    addToken(TokenType.MINUS);
                }
                break;
            case '*': addToken(TokenType.STAR); break;
            case '%': addToken(TokenType.PERCENT); break;

            case '=':
                addToken(match('=') ? TokenType.EQUAL_EQUAL : TokenType.EQUALS);
                break;
            case '!':
                if (match('=')) {
                    addToken(TokenType.NOT_EQUAL);
                } else {
                    addToken(TokenType.NOT);
                }
                break;
            case '<':
                addToken(match('=') ? TokenType.LESS_EQUAL : TokenType.LESS_THAN);
                break;
            case '>':
                addToken(match('=') ? TokenType.GREATER_EQUAL : TokenType.GREATER_THAN);
                break;

            case '&':
                if (match('&')) {
                    addToken(TokenType.AND);
                } else {
                    throw new TokenizerException("Unexpected character '&'. Did you mean '&&'?", line, column);
                }
                break;

            case '|':
                if (match('|')) {
                    addToken(TokenType.OR);
                } else {
                    throw new TokenizerException("Unexpected character '|'. Did you mean '||'?", line, column);
                }
                break;

            case '/':
                if (match('/')) {
                    // Single-line comment
                    while (peek() != '\n' && !isAtEnd()) advance();
                } else {
                    // Division operator
                    addToken(TokenType.SLASH);
                }
                break;

            case '\'':
                // Multi-line comment (triple single-quote)
                if (match('\'') && match('\'')) {
                    multiLineComment();
                } else {
                    throw new TokenizerException("Unexpected character '''", line, column);
                }
                break;

            case '"':
                string();
                break;

            case ' ':
            case '\r':
            case '\t':
                // Ignore whitespace
                break;

            case '\n':
                line++;
                column = 1;
                break;

            default:
                if (isDigit(c)) {
                    number();
                } else if (isAlpha(c)) {
                    identifier();
                } else {
                    throw new TokenizerException("Unexpected character '" + c + "'", line, column);
                }
                break;
        }
    }

    private void multiLineComment() {
        while (!isAtEnd()) {
            if (peek() == '\'' && peekNext() == '\'' && peekNextNext() == '\'') {
                advance(); // consume first '
                advance(); // consume second '
                advance(); // consume third '
                return;
            }
            if (peek() == '\n') {
                line++;
                column = 1;
            }
            advance();
        }
        throw new TokenizerException("Unterminated multi-line comment", line, column);
    }

    private void string() {
        int startLine = line;
        int startColumn = column;

        while (peek() != '"' && !isAtEnd()) {
            if (peek() == '\n') {
                line++;
                column = 1;
            }
            advance();
        }

        if (isAtEnd()) {
            throw new TokenizerException("Unterminated string", startLine, startColumn);
        }

        // Consume the closing "
        advance();

        // Trim the surrounding quotes
        String value = source.substring(start + 1, current - 1);
        tokens.add(new Token(TokenType.STRING_LITERAL, value, startLine, startColumn));
    }

    private void number() {
        while (isDigit(peek())) advance();

        // Look for a decimal part
        if (peek() == '.' && isDigit(peekNext())) {
            // Consume the "."
            advance();
            while (isDigit(peek())) advance();
        }

        String value = source.substring(start, current);
        tokens.add(new Token(TokenType.NUMBER_LITERAL, value, line, column - value.length()));
    }

    private void identifier() {
        while (isAlphaNumeric(peek()) || peek() == '_') advance();

        String text = source.substring(start, current);
        TokenType type = keywords.get(text);

        // Default to IDENTIFIER for unknown words (including custom commands)
        if (type == null) {
            type = TokenType.IDENTIFIER;
        }

        tokens.add(new Token(type, text, line, column - text.length()));
    }

    private boolean isAtEnd() {
        return current >= source.length();
    }

    private char advance() {
        column++;
        return source.charAt(current++);
    }

    private char peek() {
        if (isAtEnd()) return '\0';
        return source.charAt(current);
    }

    private char peekNext() {
        if (current + 1 >= source.length()) return '\0';
        return source.charAt(current + 1);
    }

    private char peekNextNext() {
        if (current + 2 >= source.length()) return '\0';
        return source.charAt(current + 2);
    }

    private boolean match(char expected) {
        if (isAtEnd()) return false;
        if (source.charAt(current) != expected) return false;

        current++;
        column++;
        return true;
    }

    private void addToken(TokenType type) {
        String text = source.substring(start, current);
        tokens.add(new Token(type, text, line, column - text.length()));
    }

    private boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    private boolean isAlpha(char c) {
        return (c >= 'a' && c <= 'z') ||
               (c >= 'A' && c <= 'Z') ||
               c == '_';
    }

    private boolean isAlphaNumeric(char c) {
        return isAlpha(c) || isDigit(c);
    }

    /**
     * Get the list of registered custom commands
     */
    public Map<String, TokenType> getCustomCommands() {
        return new HashMap<>(customCommands);
    }
}
