package org.firstinspires.ftc.teamcode.dynamite.DYNtokenizer;

import org.firstinspires.ftc.teamcode.dynamite.DynExceptions.TokenizerException;

import java.util.List;

/**
 * Example usage of the DYN Tokenizer for FTC autonomous programs.
 * This class demonstrates how to integrate the tokenizer with your FTC OpMode.
 */
public class DynTokenizerExample {

    public static void main(String[] args) {
        // Example DYN script showcasing all tokenizer features
        String dynScript = 
            "// Sample FTC auto script\n" +
            "def_path main start\n" +
            "    PathStartPosition = startPos\n" +
            "    FieldPos startPos (0, 0, 90)\n" +
            "    FieldCoord waypoint1 (24, 24)\n" +
            "    \n" +
            "    Num speed 0.8\n" +
            "    Num angle 45\n" +
            "    Num angleRad 0\n" +
            "    Bool isBlueAlliance true\n" +
            "    Bool isReady false\n" +
            "    \n" +
            "    // Convert degrees to radians\n" +
            "    toRad angle to angleRad\n" +
            "    \n" +
            "    // Trigonometric functions\n" +
            "    Num sinVal 0\n" +
            "    SIN angleRad to sinVal\n" +
            "    COS angleRad to sinVal\n" +
            "    invSin sinVal to angle\n" +
            "    \n" +
            "    goTo(waypoint1)\n" +
            "    turnTo(180, 45)\n" +
            "    \n" +
            "    // Logical operators - using keywords\n" +
            "    if (isBlueAlliance and isReady) start\n" +
            "        AddData \"Running blue auto\"\n" +
            "    end\n" +
            "    \n" +
            "    // Logical operators - using symbols\n" +
            "    if (isBlueAlliance && not isReady) start\n" +
            "        AddData \"Waiting...\"\n" +
            "    end\n" +
            "    \n" +
            "    // Arithmetic operators\n" +
            "    ADD speed 0.1 to speed\n" +
            "    SUB speed 0.05 to speed\n" +
            "    MUX speed 2 to speed\n" +
            "    MOD angle 360 to angle\n" +
            "    \n" +
            "    // For loop with counter\n" +
            "    for 5 as i start\n" +
            "        ADD speed 0.1 to speed\n" +
            "    end\n" +
            "    \n" +
            "    // While loop with comparison\n" +
            "    while (angle < 90) start\n" +
            "        ADD angle 1 to angle\n" +
            "    end\n" +
            "    \n" +
            "    cmd grabSample from samplePos to armPos\n" +
            "end\n" +
            "\n" +
            "autoPath main\n";

        // Create tokenizer
        DynTokenizer tokenizer = new DynTokenizer(dynScript);

        // Register custom commands (these would be your robot subsystem functions)
        tokenizer.registerCustomCommand("grabSample");
        tokenizer.registerCustomCommand("releaseSample");
        tokenizer.registerCustomCommand("extendArm");
        tokenizer.registerCustomCommand("retractArm");

        try {
            // Tokenize the script
            List<Token> tokens = tokenizer.tokenize();

            // Print all tokens
            System.out.println("=== DYN Tokenizer Output ===\n");
            for (Token token : tokens) {
                System.out.println(token);
            }

            System.out.println("\n=== Tokenization Complete ===");
            System.out.println("Total tokens: " + tokens.size());

        } catch (TokenizerException e) {
            System.err.println("Tokenization failed: " + e.getMessage());
        }
    }

    /**
     * Example of how to use in an FTC OpMode:
     *
     * @LinearOpMode
     * public class DynAutoOpMode extends LinearOpMode {
     *     @Override
     *     public void runOpMode() {
     *         String script = readDynScript(); // Load your .dyn file
     *
     *         DynTokenizer tokenizer = new DynTokenizer(script);
     *
     *         // Register your robot's custom commands
     *         tokenizer.registerCustomCommand("intake");
     *         tokenizer.registerCustomCommand("outtake");
     *         tokenizer.registerCustomCommand("liftUp");
     *         tokenizer.registerCustomCommand("liftDown");
     *
     *         List<Token> tokens = tokenizer.tokenize();
     *
     *         // Pass tokens to your parser/interpreter
     *         DynParser parser = new DynParser(tokens);
     *         DynProgram program = parser.parse();
     *
     *         waitForStart();
     *
     *         // Execute the program
     *         program.execute(this);
     *     }
     * }
     */
}
