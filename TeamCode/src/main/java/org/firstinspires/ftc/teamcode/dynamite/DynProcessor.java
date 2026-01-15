package org.firstinspires.ftc.teamcode.dynamite;//import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;

import static org.firstinspires.ftc.teamcode.dynamite.DYNtokenizer.TokenType.*;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.dynamite.DynCommands.Control.ForLoop;
import org.firstinspires.ftc.teamcode.dynamite.DynCommands.Control.IfBlock;
import org.firstinspires.ftc.teamcode.dynamite.DynCommands.Control.RunPath;
import org.firstinspires.ftc.teamcode.dynamite.DynCommands.Control.WhileBlock;
import org.firstinspires.ftc.teamcode.dynamite.DynCommands.DynCommand;
import org.firstinspires.ftc.teamcode.dynamite.DynCommands.Etc.CustomCommand;
import org.firstinspires.ftc.teamcode.dynamite.DynCommands.Etc.DeclareVar;
import org.firstinspires.ftc.teamcode.dynamite.DynCommands.Etc.OutputToTelem;
import org.firstinspires.ftc.teamcode.dynamite.DynCommands.Math.*;
import org.firstinspires.ftc.teamcode.dynamite.DynCommands.Movement.DynPath;
import org.firstinspires.ftc.teamcode.dynamite.DynCommands.Movement.FollowBezierCommand;
import org.firstinspires.ftc.teamcode.dynamite.DynCommands.Movement.GoToCommand;
import org.firstinspires.ftc.teamcode.dynamite.DynCommands.Movement.SetStartPositionCommand;
import org.firstinspires.ftc.teamcode.dynamite.DynCommands.Movement.TurnToCommand;
import org.firstinspires.ftc.teamcode.dynamite.DynExceptions.DynAutoStepException;
import org.firstinspires.ftc.teamcode.dynamite.DynVar.DynVarBuffer;

import org.firstinspires.ftc.teamcode.dynamite.DYNtokenizer.DynTokenizer;
import org.firstinspires.ftc.teamcode.dynamite.DYNtokenizer.Token;
import org.firstinspires.ftc.teamcode.dynamite.DYNtokenizer.TokenType;
import org.firstinspires.ftc.teamcode.dynamite.PPintegration.PedroPathingBridge;

import java.util.*;
import java.util.function.Consumer;

/**
 * DYN Script Processor
 * Converts tokenized DYN scripts into executable command sequences.
 */
public class DynProcessor {
    private DynTokenizer tokeniser;
    private Token[] tokens;
    private int currentIndex = 0;
    private int literalCounter = 0;

    // Runtime components
    private DynVarBuffer varBuffer;
    private final Map<String, DynPath> pathRegistry = new HashMap<>();
    private String mainPathName = null;

    // External integrations
    private PedroPathingBridge pathingBridge;
    private CustomCommand.CustomCommandHandler customCommandHandler;
    private Consumer<String> telemOutput;

    // Registered custom function IDs
    private Set<String> customFuncIds = new HashSet<>();

    public DynProcessor() {
        varBuffer = new DynVarBuffer(new HashMap<>());
    }

    /**
     * Set the PedroPathing bridge for movement commands.
     */
    public void setPathingBridge(PedroPathingBridge bridge) {
        this.pathingBridge = bridge;
    }

    /**
     * Set the handler for custom commands (robot subsystem integration).
     */
    public void setCustomCommandHandler(CustomCommand.CustomCommandHandler handler) {
        this.customCommandHandler = handler;
    }

    /**
     * Set the telemetry output function.
     */
    public void setTelemOutput(Consumer<String> telemOutput) {
        this.telemOutput = telemOutput;
        if (pathingBridge != null) {
            pathingBridge.setTelemOutput(telemOutput);
        }
    }

    /**
     * Initialize and process a DYN script.
     */
    public void init(Telemetry telemetry, String scriptData, String[] funcIDs) {
        // Set up telemetry - wrap addData to match Consumer<String>
        if (this.telemOutput == null) {
            this.telemOutput = new java.util.function.Consumer<String>() {
                @Override
                public void accept(String msg) {
                    telemetry.addLine("DYN: " + msg);
                    telemetry.update();
                }
            };
        }

        telemetry.addData("DYN", "Init: starting processor...");
        telemetry.update();

        // Store custom function IDs
        customFuncIds.clear();
        for (String id : funcIDs) {
            customFuncIds.add(id);
        }

        telemetry.addData("DYN", "Init: tokenizer setup...");
        telemetry.update();

        // Start tokeniser
        tokeniser = new DynTokenizer(scriptData);

        // Register function ids
        for (String funcID : funcIDs) {
            tokeniser.registerCustomCommand(funcID);
        }

        // Tokenise script
        tokens = tokeniser.tokenize().toArray(new Token[0]);

        telemetry.addData("DYN", "Init: tokenized " + tokens.length + " tokens");
        telemetry.update();

        // Debug token dump disabled to avoid init slowdowns on FTC hardware

        // Process tokens into paths and commands
        currentIndex = 0;
        processScriptTokens();

        telemetry.addData("DYN", "Init: processed script tokens");
        telemetry.update();

        // Initialize all paths
        for (DynPath path : pathRegistry.values()) {
            path.init(varBuffer::setVar, varBuffer::getVar, telemOutput);
        }

        telemetry.addData("DYN", "Init: paths initialized");
        telemetry.update();

        // Debug: Print parsed structure
        System.out.println("=== Parsed Paths ===");
        for (Map.Entry<String, DynPath> entry : pathRegistry.entrySet()) {
            System.out.println("Path: " + entry.getKey() + " with " + entry.getValue().getCommands().size() + " commands");
            for (DynCommand cmd : entry.getValue().getCommands()) {
                System.out.println("  - " + cmd.getDescription());
            }
        }
        System.out.println("Main path: " + mainPathName);
        System.out.println("=== End Parsed Paths ===\n");
    }

    /**
     * Process all tokens and build command structures.
     */
    public void processScriptTokens() {
        int safety = 0;
        long startMs = System.currentTimeMillis();
        while (!isAtEnd()) {
            int beforeIndex = currentIndex;
            Token current = peek();

            switch (current.getType()) {
                case DEF_PATH:
                    parseDefPath();
                    break;
                case AUTO_PATH:
                    parseAutoPath();
                    break;
                case COMMENT:
                case NEWLINE:
                    advance(); // Skip
                    break;
                default:
                    // Skip unknown tokens at top level
                    System.out.println("[WARN] Unexpected top-level token: " + current);
                    advance();
                    break;
            }

            if (currentIndex == beforeIndex) {
                // Ensure progress to prevent infinite loops
                if (telemOutput != null) {
                    telemOutput.accept("[WARN] Parser made no progress at top-level token: " + current);
                }
                advance();
            }

            safety++;
            if (safety > tokens.length * 5) {
                throw new DynAutoStepException("Parser stuck at top level near token: " + current);
            }

            if (System.currentTimeMillis() - startMs > 1500) {
                throw new DynAutoStepException("Parser timeout at top level near token: " + current);
            }
        }
    }

    /**
     * Parse a path definition: def_path name start ... end
     */
    private void parseDefPath() {
        expect(DEF_PATH);
        String pathName = expect(TokenType.IDENTIFIER).getValue();
        expect(TokenType.START);

        DynPath path = new DynPath(pathName, pathRegistry);
        parseBlockContents(path);

        expect(TokenType.END);

        pathRegistry.put(pathName, path);
        System.out.println("[INFO] Registered path: " + pathName);
    }

    /**
     * Parse the autoPath declaration.
     */
    private void parseAutoPath() {
        expect(AUTO_PATH);
        mainPathName = expect(TokenType.IDENTIFIER).getValue();
        System.out.println("[INFO] Main auto path set to: " + mainPathName);
    }

    /**
     * Parse the contents of a block (between start/end or Wstart/Wend).
     */
    private void parseBlockContents(DynPath path) {
        int safety = 0;
        long startMs = System.currentTimeMillis();
        while (!isAtEnd() && !check(TokenType.END) && !check(TokenType.WEND)) {
            int beforeIndex = currentIndex;
            Token current = peek();

            try {
                DynCommand cmd = parseCommand();
                if (cmd != null) {
                    path.addCommand(cmd);
                }
            } catch (Exception e) {
                System.err.println("[ERROR] Failed to parse at line " + current.getLine() + ": " + e.getMessage());
                // Skip to next line
                while (!isAtEnd() && !check(NEWLINE)) {
                    advance();
                }
                if (!isAtEnd()) advance(); // Skip newline
            }

            if (currentIndex == beforeIndex) {
                if (telemOutput != null) {
                    telemOutput.accept("[WARN] Parser made no progress inside block at token: " + current);
                }
                advance();
            }

            safety++;
            if (safety > tokens.length * 5) {
                throw new DynAutoStepException("Parser stuck inside block near token: " + current);
            }

            if (System.currentTimeMillis() - startMs > 1500) {
                throw new DynAutoStepException("Parser timeout inside block near token: " + current);
            }
        }
    }

    /**
     * Parse a single command and return it.
     */
    private DynCommand parseCommand() {
        Token current = peek();

        switch (current.getType()) {
            // Variable declarations
            case NUM:
                return parseNumDeclaration();
            case BOOL:
                return parseBoolDeclaration();
            case STRING:
                return parseStringDeclaration();
            case FIELD_COORD:
                return parseFieldCoordDeclaration();
            case FIELD_POS:
                return parseFieldPosDeclaration();

            // Math operations
            case ADD:
                return parseBinaryMathOp(new BinaryMathFactory() {
                    @Override public DynCommand create(String id1, String id2, String outId) {
                        return new Add(id1, id2, outId);
                    }
                });
            case SUB:
                return parseBinaryMathOp(new BinaryMathFactory() {
                    @Override public DynCommand create(String id1, String id2, String outId) {
                        return new Sub(id1, id2, outId);
                    }
                });
            case MUX:
                return parseBinaryMathOp(new BinaryMathFactory() {
                    @Override public DynCommand create(String id1, String id2, String outId) {
                        return new Mux(id1, id2, outId);
                    }
                });
            case DIV:
                return parseBinaryMathOp(new BinaryMathFactory() {
                    @Override public DynCommand create(String id1, String id2, String outId) {
                        return new Div(id1, id2, outId);
                    }
                });
            case MOD:
                return parseBinaryMathOp(new BinaryMathFactory() {
                    @Override public DynCommand create(String id1, String id2, String outId) {
                        return new Mod(id1, id2, outId);
                    }
                });
            case POW:
                return parseBinaryMathOp(new BinaryMathFactory() {
                    @Override public DynCommand create(String id1, String id2, String outId) {
                        return new Pow(id1, id2, outId);
                    }
                });
            case SIN:
                return parseUnaryMathOp(new UnaryMathFactory() {
                    @Override public DynCommand create(String inId, String outId) {
                        return new Sin(inId, outId);
                    }
                });
            case COS:
                return parseUnaryMathOp(new UnaryMathFactory() {
                    @Override public DynCommand create(String inId, String outId) {
                        return new Cos(inId, outId);
                    }
                });
            case TAN:
                return parseUnaryMathOp(new UnaryMathFactory() {
                    @Override public DynCommand create(String inId, String outId) {
                        return new Tan(inId, outId);
                    }
                });
            case SQR:
                return parseUnaryMathOp(new UnaryMathFactory() {
                    @Override public DynCommand create(String inId, String outId) {
                        return new Sqrt(inId, outId);
                    }
                });
            case INV_SIN:
                return parseUnaryMathOp(new UnaryMathFactory() {
                    @Override public DynCommand create(String inId, String outId) {
                        return new Inv_Sin(inId, outId);
                    }
                });
            case INV_COS:
                return parseUnaryMathOp(new UnaryMathFactory() {
                    @Override public DynCommand create(String inId, String outId) {
                        return new Inv_Cos(inId, outId);
                    }
                });
            case INV_TAN:
                return parseUnaryMathOp(new UnaryMathFactory() {
                    @Override public DynCommand create(String inId, String outId) {
                        return new Inv_Tan(inId, outId);
                    }
                });
            case TO_RAD:
                return parseUnaryMathOp(new UnaryMathFactory() {
                    @Override public DynCommand create(String inId, String outId) {
                        return new To_Radian(inId, outId);
                    }
                });
            case TO_DEG:
                return parseUnaryMathOp(new UnaryMathFactory() {
                    @Override public DynCommand create(String inId, String outId) {
                        return new To_Degrees(inId, outId);
                    }
                });

            // Movement commands
            case GO_TO:
                return parseGoTo();
            case TURN_TO:
                return parseTurnTo();
            case FOLLOW_BEZIER:
                return parseFollowBezier();
            case PATH_START_POSITION:
                return parsePathStartPosition();

            // Control flow
            case FOR:
                return parseForLoop();
            case WHILE:
                return parseWhileLoop();
            case IF:
                return parseIfBlock();
            case RUN:
                return parseRunPath();

            // Output
            case OUTPUT_2_TELEM:
                return parseOutputToTelem();

            // Custom commands
            case CMD:
                return parseCustomCommand();
            case IDENTIFIER:
                return parseIdentifierCommand();

            // Skip tokens
            case COMMENT:
            case NEWLINE:
                advance();
                return null;

            default:
                System.out.println("[WARN] Unhandled token in block: " + current);
                advance();
                return null;
        }
    }

    // ==================== Variable Declarations ====================

    private DynCommand parseNumDeclaration() {
        expect(NUM);
        String varName = expect(TokenType.IDENTIFIER).getValue();
        Object value = parseValue();
        return new DeclareVar("Number", varName, value);
    }

    private DynCommand parseBoolDeclaration() {
        expect(TokenType.BOOL);
        String varName = expect(TokenType.IDENTIFIER).getValue();
        Token valueToken = advance();
        boolean value = "true".equals(valueToken.getValue());
        return new DeclareVar("Boolean", varName, value);
    }

    private DynCommand parseStringDeclaration() {
        expect(TokenType.STRING);
        String varName = expect(TokenType.IDENTIFIER).getValue();
        String value = expect(TokenType.STRING_LITERAL).getValue();
        return new DeclareVar("String", varName, value);
    }

    private DynCommand parseFieldCoordDeclaration() {
        expect(TokenType.FIELD_COORD);
        String varName = expect(TokenType.IDENTIFIER).getValue();

        double[] coords = parseCoordinates(2);
        return new DeclareVar("Field cords", varName, coords);
    }

    private DynCommand parseFieldPosDeclaration() {
        expect(TokenType.FIELD_POS);
        String varName = expect(TokenType.IDENTIFIER).getValue();

        double[] coords = parseCoordinates(3);
        return new DeclareVar("Field pos", varName, coords);
    }

    /**
     * Parse coordinates like (x, y) or (x, y, h) or just x y h
     */
    private double[] parseCoordinates(int count) {
        double[] result = new double[count];

        boolean hasParen = check(TokenType.LPAREN);
        if (hasParen) advance();

        for (int i = 0; i < count; i++) {
            if (i > 0 && check(TokenType.COMMA)) advance(); // Skip comma

            Object val = parseValue();
            if (val instanceof Number) {
                result[i] = ((Number) val).doubleValue();
            } else if (val instanceof String) {
                // Could be a variable reference - use 0 as placeholder
                // The actual value will be resolved at runtime
                result[i] = 0;
            }
        }

        if (hasParen && check(TokenType.RPAREN)) advance();

        return result;
    }

    /**
     * Parse a value (number literal, identifier, or expression).
     */
    private Object parseValue() {
        Token current = peek();

        if (current.getType() == TokenType.NUMBER_LITERAL) {
            advance();
            return Double.parseDouble(current.getValue());
        } else if (current.getType() == TokenType.IDENTIFIER) {
            advance();
            return current.getValue(); // Variable reference
        } else if (current.getType() == TokenType.MINUS) {
            advance();
            Token num = expect(TokenType.NUMBER_LITERAL);
            return -Double.parseDouble(num.getValue());
        } else if (current.getType() == TokenType.STRING_LITERAL) {
            advance();
            return current.getValue();
        } else if (current.getType() == TokenType.BOOLEAN_LITERAL) {
            advance();
            return "true".equals(current.getValue());
        }

        // Default
        advance();
        return current.getValue();
    }

    // ==================== Math Operations ====================

    @FunctionalInterface
    interface BinaryMathFactory {
        DynCommand create(String id1, String id2, String outId);
    }

    @FunctionalInterface
    interface UnaryMathFactory {
        DynCommand create(String inId, String outId);
    }

    @FunctionalInterface
    interface UnaryMathFactorySingle {
        DynCommand create(String id);
    }

    /**
     * Parse binary math operation: OP val1 val2 to result
     */
    private DynCommand parseBinaryMathOp(BinaryMathFactory factory) {
        advance(); // Skip operation token
        String val1 = ensureNumberVar(expectIdentifierOrLiteral());
        String val2 = ensureNumberVar(expectIdentifierOrLiteral());

        if (check(TokenType.TO)) {
            advance();
            String result = expect(TokenType.IDENTIFIER).getValue();
            return factory.create(val1, val2, result);
        }

        // If no 'to', result goes to val1
        return factory.create(val1, val2, val1);
    }

    /**
     * Parse unary math operation: OP val to result OR OP val
     */
    private DynCommand parseUnaryMathOp(UnaryMathFactory factory) {
        advance(); // Skip operation token
        String val = ensureNumberVar(expectIdentifierOrLiteral());

        if (check(TokenType.TO)) {
            advance();
            String result = expect(TokenType.IDENTIFIER).getValue();
            return factory.create(val, result);
        }

        // If no 'to', result goes back to val
        return factory.create(val, val);
    }

    private String expectIdentifierOrLiteral() {
        Token current = peek();
        if (current.getType() == TokenType.IDENTIFIER ||
                current.getType() == TokenType.NUMBER_LITERAL) {
            advance();
            return current.getValue();
        }
        throw new DynAutoStepException("Expected identifier or number at line " + current.getLine());
    }

    private String ensureNumberVar(String tokenValue) {
        if (tokenValue == null) {
            return null;
        }
        try {
            double literal = Double.parseDouble(tokenValue);
            String constId = "__lit_" + literalCounter++;
            try {
                varBuffer.setVar(constId, new org.firstinspires.ftc.teamcode.dynamite.DynVar.DynVar("Number", constId, literal));
            } catch (Exception e) {
                throw new DynAutoStepException("Failed to create literal var for value: " + tokenValue);
            }
            return constId;
        } catch (NumberFormatException e) {
            return tokenValue;
        }
    }

    // ==================== Movement Commands ====================

    private DynCommand parseGoTo() {
        expect(TokenType.GO_TO);

        // Handle optional parentheses
        boolean hasParen = check(TokenType.LPAREN);
        if (hasParen) advance();

        String posVar = expect(TokenType.IDENTIFIER).getValue();

        if (hasParen && check(TokenType.RPAREN)) advance();

        return new GoToCommand(posVar, pathingBridge);
    }

    private DynCommand parseTurnTo() {
        expect(TokenType.TURN_TO);

        boolean hasParen = check(TokenType.LPAREN);
        if (hasParen) advance();

        String heading = expectIdentifierOrLiteral();

        String speed = "30"; // Default speed
        if (check(TokenType.COMMA)) advance();
        if (!check(TokenType.RPAREN) && !check(NEWLINE) && !isAtEnd()) {
            speed = expectIdentifierOrLiteral();
        }

        if (hasParen && check(TokenType.RPAREN)) advance();

        // Try to parse as literals
        try {
            double h = Double.parseDouble(heading);
            double s = Double.parseDouble(speed);
            return new TurnToCommand(h, s, pathingBridge);
        } catch (NumberFormatException e) {
            return new TurnToCommand(heading, speed, pathingBridge);
        }
    }

    private DynCommand parseFollowBezier() {
        expect(TokenType.FOLLOW_BEZIER);

        boolean hasParen = check(TokenType.LPAREN);
        if (hasParen) advance();

        String startVar = expect(TokenType.IDENTIFIER).getValue();

        if (check(TokenType.COMMA)) advance();

        String endVar = expect(TokenType.IDENTIFIER).getValue();

        if (hasParen && check(TokenType.RPAREN)) advance();

        return new FollowBezierCommand(startVar, endVar, pathingBridge);
    }

    private DynCommand parsePathStartPosition() {
        expect(TokenType.PATH_START_POSITION);
        String posVar = expect(TokenType.IDENTIFIER).getValue();
        return new SetStartPositionCommand(posVar, pathingBridge);
    }

    // ==================== Control Flow ====================

    private DynCommand parseForLoop() {
        expect(TokenType.FOR);

        // for N as iteratorVar start ... end
        String countStr = expectIdentifierOrLiteral();
        int count;
        try {
            count = Integer.parseInt(countStr);
        } catch (NumberFormatException e) {
            count = 10; // Default
        }

        expect(TokenType.AS);
        String iteratorVar = expect(TokenType.IDENTIFIER).getValue();
        expect(TokenType.START);

        ForLoop forLoop = new ForLoop(count, iteratorVar);

        // Parse body
        while (!isAtEnd() && !check(TokenType.END)) {
            DynCommand cmd = parseCommand();
            if (cmd != null) {
                forLoop.addCommand(cmd);
            }
        }

        expect(TokenType.END);

        return forLoop;
    }

    private DynCommand parseWhileLoop() {
        expect(TokenType.WHILE);

        // while (condition) start ... end
        expect(TokenType.LPAREN);

        String condVar = expect(TokenType.IDENTIFIER).getValue();
        String compareOp = null;
        String compareValue = null;

        // Check for comparison operator
        Token next = peek();
        if (isComparisonOp(next.getType())) {
            compareOp = advance().getValue();
            compareValue = expectIdentifierOrLiteral();
        }

        expect(TokenType.RPAREN);

        // Check for Wstart or start
        if (check(TokenType.WSTART)) {
            advance();
        } else if (check(TokenType.START)) {
            advance();
        }

        WhileBlock whileBlock;
        if (compareOp != null) {
            whileBlock = new WhileBlock(condVar, compareOp, compareValue);
        } else {
            whileBlock = new WhileBlock(condVar);
        }

        // Parse body
        while (!isAtEnd() && !check(TokenType.END) && !check(TokenType.WEND)) {
            DynCommand cmd = parseCommand();
            if (cmd != null) {
                whileBlock.addCommand(cmd);
            }
        }

        // Expect end or Wend
        if (check(TokenType.WEND)) {
            advance();
        } else if (check(TokenType.END)) {
            advance();
        }

        return whileBlock;
    }

    private DynCommand parseIfBlock() {
        expect(TokenType.IF);

        // if (condition) start ... end
        boolean hasParen = check(TokenType.LPAREN);
        if (hasParen) advance();

        String condVar = expect(TokenType.IDENTIFIER).getValue();
        String compareOp = null;
        String compareValue = null;

        Token next = peek();
        if (isComparisonOp(next.getType())) {
            compareOp = advance().getValue();
            compareValue = expectIdentifierOrLiteral();
        }

        if (hasParen && check(TokenType.RPAREN)) advance();

        expect(TokenType.START);

        IfBlock ifBlock;
        if (compareOp != null) {
            ifBlock = new IfBlock(condVar, compareOp, compareValue);
        } else {
            ifBlock = new IfBlock(condVar);
        }

        // Parse body
        while (!isAtEnd() && !check(TokenType.END)) {
            DynCommand cmd = parseCommand();
            if (cmd != null) {
                ifBlock.addCommand(cmd);
            }
        }

        expect(TokenType.END);

        return ifBlock;
    }

    private boolean isComparisonOp(TokenType type) {
        return type == TokenType.LESS_THAN ||
                type == TokenType.GREATER_THAN ||
                type == TokenType.LESS_EQUAL ||
                type == TokenType.GREATER_EQUAL ||
                type == TokenType.EQUAL_EQUAL ||
                type == TokenType.NOT_EQUAL;
    }

    private DynCommand parseRunPath() {
        expect(TokenType.RUN);
        String pathName = expect(TokenType.IDENTIFIER).getValue();
        return new RunPath(pathName, pathRegistry);
    }

    // ==================== Output ====================

    private DynCommand parseOutputToTelem() {
        expect(TokenType.OUTPUT_2_TELEM);

        Token valueToken = peek();
        boolean isLiteral = valueToken.getType() == TokenType.STRING_LITERAL;
        String message = advance().getValue();

        OutputToTelem cmd = new OutputToTelem(message, isLiteral);
        cmd.setTelemOutput(telemOutput);
        return cmd;
    }

    // ==================== Custom Commands ====================

    private DynCommand parseCustomCommand() {
        expect(TokenType.CMD);
        String funcName = expect(TokenType.IDENTIFIER).getValue();

        String inputVar = null;
        String outputVar = null;

        if (check(TokenType.FROM)) {
            advance();
            inputVar = expect(TokenType.IDENTIFIER).getValue();
        }

        if (check(TokenType.TO)) {
            advance();
            outputVar = expect(TokenType.IDENTIFIER).getValue();
        }

        return new CustomCommand(funcName, inputVar, outputVar, customCommandHandler);
    }

    /**
     * Handle identifier that might be a custom function call.
     */
    private DynCommand parseIdentifierCommand() {
        Token idToken = advance();
        String funcName = idToken.getValue();

        // Check if this is a registered custom function
        if (customFuncIds.contains(funcName)) {
            return new CustomCommand(funcName, customCommandHandler);
        }

        // Otherwise, it might be an assignment or other construct
        System.out.println("[WARN] Unknown identifier command: " + funcName);
        return null;
    }

    // ==================== Token Navigation ====================

    private Token peek() {
        if (currentIndex >= tokens.length) {
            return tokens[tokens.length - 1];
        }
        return tokens[currentIndex];
    }

    private Token advance() {
        if (!isAtEnd()) currentIndex++;
        return tokens[currentIndex - 1];
    }

    private boolean check(TokenType type) {
        if (isAtEnd()) return false;
        return peek().getType() == type;
    }

    private Token expect(TokenType type) {
        if (check(type)) return advance();
        Token current = peek();
        throw new DynAutoStepException("Expected " + type + " but got " + current.getType() +
                " at line " + current.getLine() + ", col " + current.getColumn());
    }

    private boolean isAtEnd() {
        if (currentIndex >= tokens.length) {
            return true;
        }
        return tokens[currentIndex].getType() == TokenType.EOF;
    }

    // ==================== Execution ====================

    /**
     * Get the main path to execute.
     */
    public DynPath getMainPath() {
        if (mainPathName == null) {
            throw new DynAutoStepException("No autoPath defined in script");
        }
        return pathRegistry.get(mainPathName);
    }

    /**
     * Get a path by name.
     */
    public DynPath getPath(String name) {
        return pathRegistry.get(name);
    }

    /**
     * Get all registered paths.
     */
    public Map<String, DynPath> getAllPaths() {
        return Collections.unmodifiableMap(pathRegistry);
    }

    /**
     * Get the variable buffer.
     */
    public DynVarBuffer getVarBuffer() {
        return varBuffer;
    }

    /**
     * Execute the main autonomous path.
     */
    public void runAuto() {
        DynPath main = getMainPath();
        if (main != null) {
            System.out.println("[INFO] Running auto: " + mainPathName);
            main.run();
            System.out.println("[INFO] Auto complete");
        }
    }
}