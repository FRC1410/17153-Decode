package org.firstinspires.ftc.teamcode.dynamite;

import static org.firstinspires.ftc.teamcode.pedroPathing.Constants.createFollower;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.dynamite.PPintegration.DynPedroPathingBridge;
import org.firstinspires.ftc.teamcode.dynamite.DynVar.DynVar;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * DynAutoOpMode - FTC OpMode that executes DYN autonomous scripts with PedroPathing.
 * 
 * This OpMode provides seamless integration between:
 * - DYN scripting language for autonomous routines
 * - PedroPathing for smooth robot movement
 * - Custom subsystem commands for robot mechanisms
 * 
 * SETUP:
 * 1. Place your .dyn script in TeamCode/src/main/assets/
 * 2. Set SCRIPT_NAME to your script filename
 * 3. Register your custom commands in registerCustomCommands()
 * 4. Configure starting pose in getStartPose()
 * 
 * USAGE:
 * - Extend this class for each autonomous routine
 * - Override registerCustomCommands() to add your robot's mechanisms
 * - Override getScriptName() to specify which .dyn file to use
 */
public class DynAutoOpMode extends LinearOpMode {

    // ==================== CONFIGURATION ====================

    protected void initializehardware(){

    }

    /** Name of the .dyn script file in assets folder */
    protected String getScriptName() {
        return "auto.dyn";
    }
    
    /** Starting pose for the autonomous (override in subclass) */
    protected Pose getStartPose() {
        return new Pose(0, 0, Math.toRadians(90));
    }
    
    /** Custom function IDs used in your script */
    protected String[] getCustomFunctionIds() {
        return new String[]{
            "grabSample", "dropSample",
            "raiseArm", "lowerArm",
            "startIntake", "stopIntake",
            "openClaw", "closeClaw"
        };
    }

    // ==================== INTERNAL STATE ====================
    
    protected Follower follower;
    protected DynAuto dynAuto;
    protected DynPedroPathingBridge pathingBridge;

    private static final int DYN_TELEM_MAX_LINES = 255;
    private java.util.ArrayList<String> dynTelemBuffer = new java.util.ArrayList<>();
    
    /**
     * Add a line to the DYN telemetry buffer.
     * Called by the AddData command.
     */
    public void addToTelemBuffer(String line) {
        if (dynTelemBuffer.size() >= DYN_TELEM_MAX_LINES) {
            dynTelemBuffer.remove(0);
        }
        dynTelemBuffer.add(line);
    }
    
    /**
     * Send the DYN telemetry buffer to the driver station and clear it.
     * Called by the Update command.
     */
    public void sendTelemBuffer() {
        // Clear previous telemetry to prevent the DYN pane from growing indefinitely
        try {
            telemetry.clear();
        } catch (Exception ignored) {}

        // Diagnostic: report how many lines we are flushing
        telemetry.addData("DYN_COUNT", dynTelemBuffer.size());
        for (String line : dynTelemBuffer) {
            telemetry.addData("DYN", line);
        }
        telemetry.update();
        dynTelemBuffer = new ArrayList<>();
    }

    // Hardware references (set these in your subclass)
    // protected DcMotor armMotor;
    // protected Servo clawServo;
    // etc.

    // ==================== OPMODE LIFECYCLE ====================

    @Override
    public void runOpMode() throws InterruptedException {
        // === INIT PHASE - with telemetry markers for debugging ===
        telemetry.addData("Status", "Init: Starting hardware...");
        telemetry.update();
        
        // Initialize hardware
        initializeHardware();
        telemetry.addData("Status", "Init: Hardware done, starting PedroPathing...");
        telemetry.update();
        
        // Initialize PedroPathing
        try {
            initializePedroPathing();
            telemetry.addData("Status", "Init: PedroPathing done, starting DYN...");
            telemetry.update();
        } catch (Exception e) {
            telemetry.addData("ERROR", "PedroPathing init failed: " + e.getMessage());
            telemetry.update();
            throw e;
        }
        
        // Initialize DYN system
        try {
            initializeDynAuto();
            telemetry.addData("Status", "Init: DYN system done, loading script...");
            telemetry.update();
        } catch (Exception e) {
            telemetry.addData("ERROR", "DYN init failed: " + e.getMessage());
            telemetry.update();
            throw e;
        }

        // Allow subclasses to perform custom one-time initialization
        try {
            telemetry.addData("Status", "Init: Running subclass onInit...");
            telemetry.update();
            onInit();
            telemetry.addData("Status", "Init: onInit complete, loading script...");
            telemetry.update();
        } catch (Exception e) {
            telemetry.addData("ERROR", "onInit failed: " + e.getMessage());
            telemetry.update();
            throw e;
        }

        // Load the script
        try {
            loadScript();
            telemetry.addData("Status", "Init: Script loaded!");
            telemetry.update();
        } catch (Exception e) {
            telemetry.addData("ERROR", "Script load failed: " + e.getMessage());
            telemetry.update();
            throw e;
        }
        
        telemetry.addData("Status", "Initialized - Ready to Start");
        telemetry.addData("Script", getScriptName());
        telemetry.update();

        // Wait for start
        waitForStart();

        if (isStopRequested()) return;
        
        // Run the autonomous
        telemetry.addData("Status", "Running Autonomous");
        telemetry.update();
        
        try {
            runAutonomous();
        } catch (Exception e) {
            telemetry.addData("ERROR", e.getMessage());
            telemetry.update();
        }
        
        telemetry.addData("Status", "Autonomous Complete");
        telemetry.update();
    }
    /**
     * Run the autonomous script. Subclasses may override this to perform
     * additional checks or validation, but should call super.runAutonomous()
     * to preserve default behavior (running the script and flushing telemetry).
     */
    protected void runAutonomous(){
        // Flush any previously buffered telemetry before starting
        sendTelemBuffer();

        // Execute the DYN script
        dynAuto.run();

        // Ensure remaining buffer is sent after run completes
        sendTelemBuffer();

        // Keep OpMode alive so telemetry remains visible until user stops
        while (opModeIsActive()){
            idle();
        }
    }

    // ==================== INITIALIZATION ====================

    /**
     * Initialize robot hardware.
     * Override this to set up your motors, servos, sensors.
     */
    protected void initializeHardware() {
        // Example:
        // armMotor = hardwareMap.get(DcMotor.class, "arm");
        // clawServo = hardwareMap.get(Servo.class, "claw");

        telemetry.addData("Hardware", "Initialized");
    }

    /**
     * Initialize PedroPathing follower.
     */
    protected void initializePedroPathing() {
        follower = createFollower(hardwareMap);
        follower.setStartingPose(getStartPose());
        
        telemetry.addData("PedroPathing", "Initialized");
    }

    /**
     * Initialize DYN autonomous system.
     */
    protected void initializeDynAuto() {
        // Create the pathing bridge
        pathingBridge = new DynPedroPathingBridge(follower, this);
        
        // Create DYN auto engine with FTC telemetry
        telemetry.setAutoClear(false); // We'll manually manage telemetry
        dynAuto = new DynAuto(telemetry);
        dynAuto.setPathingBridge(pathingBridge);
        
        // Set telemetry callbacks for buffer-based system
        dynAuto.setTelemOutput(msg -> addToTelemBuffer(msg));
        dynAuto.setUpdateCallback(() -> sendTelemBuffer());

        // Register custom commands
        registerCustomCommands();
        
        telemetry.addData("DYN", "Initialized");
    }

    /**
     * Optional subclass initialization hook.
     *
     * Called once during the OpMode init phase after hardware, PedroPathing,
     * and the DYN system are initialized, and before the script is loaded and
     * before waitForStart().
     *
     * Override in subclasses to perform robot-specific setup such as
     * setting initial servo positions, zeroing sensors, precomputing data,
     * or preparing any state needed before autonomous begins.
     */
    protected void onInit() { /* default: no-op */ }

    private String formatDynMessage(String msg) {
        return msg == null ? "" : msg;
    }

    /**
     * Load the DYN script from assets.
     */
    protected void loadScript() {
        try {
            telemetry.addData("Script", "Loading: " + getScriptName());
            telemetry.update();
            
            String scriptContent = loadAssetFile(getScriptName());
            
            telemetry.addData("Script", "Asset loaded, processing...");
            telemetry.update();

            // Diagnostic telemetry: show script length and tail to detect truncation
            try {
                telemetry.addData("ScriptLen", scriptContent.length());
                String[] scriptLines = scriptContent.split("\\r?\\n", -1);
                telemetry.addData("ScriptLines", scriptLines.length);
                int lastIdx = scriptLines.length - 1;
                telemetry.addData("ScriptLastLine", scriptLines[lastIdx]);
                String tail = (lastIdx >= 2 ? scriptLines[lastIdx-2] + " | " : "")
                        + (lastIdx >= 1 ? scriptLines[lastIdx-1] + " | " : "")
                        + scriptLines[lastIdx];
                telemetry.addData("ScriptTail", tail);
                telemetry.update();

                // Token-level diagnostics: token count, END token positions, and last tokens
                try {
                    org.firstinspires.ftc.teamcode.dynamite.DYNtokenizer.DynTokenizer tok = new org.firstinspires.ftc.teamcode.dynamite.DYNtokenizer.DynTokenizer(scriptContent);
                    java.util.List<org.firstinspires.ftc.teamcode.dynamite.DYNtokenizer.Token> toks = tok.tokenize();
                    telemetry.addData("TokenCount", toks.size());
                    // collect END/WEND token lines
                    java.util.List<Integer> endLines = new java.util.ArrayList<>();
                    for (org.firstinspires.ftc.teamcode.dynamite.DYNtokenizer.Token t : toks) {
                        if (t.getType() == org.firstinspires.ftc.teamcode.dynamite.DYNtokenizer.TokenType.END || t.getType() == org.firstinspires.ftc.teamcode.dynamite.DYNtokenizer.TokenType.WEND) {
                            endLines.add(t.getLine());
                        }
                    }
                    telemetry.addData("ENDlines", endLines.toString());

                    // Build a short summary of the last ~12 tokens
                    StringBuilder lastTokens = new StringBuilder();
                    int startIdx = Math.max(0, toks.size() - 12);
                    for (int i = startIdx; i < toks.size(); i++) {
                        org.firstinspires.ftc.teamcode.dynamite.DYNtokenizer.Token t = toks.get(i);
                        lastTokens.append(t.getType().name()).append(":").append(t.getValue()).append(" ");
                    }
                    telemetry.addData("LastTokens", lastTokens.toString());
                    telemetry.update();
                } catch (Exception e) {
                    telemetry.addData("TokenDiagErr", e.getMessage());
                    telemetry.update();
                }

            } catch (Exception e) {
                telemetry.addData("ScriptDiagErr", e.getMessage());
                telemetry.update();
            }
            
            // Preflight: check for unclosed blocks and auto-append missing 'end' tokens
            try {
                org.firstinspires.ftc.teamcode.dynamite.DYNtokenizer.DynTokenizer preTok = new org.firstinspires.ftc.teamcode.dynamite.DYNtokenizer.DynTokenizer(scriptContent);
                java.util.List<org.firstinspires.ftc.teamcode.dynamite.DYNtokenizer.Token> preTokens = preTok.tokenize();
                int openBlocks = 0;
                for (org.firstinspires.ftc.teamcode.dynamite.DYNtokenizer.Token tk : preTokens) {
                    org.firstinspires.ftc.teamcode.dynamite.DYNtokenizer.TokenType tt = tk.getType();
                    if (tt == org.firstinspires.ftc.teamcode.dynamite.DYNtokenizer.TokenType.START || tt == org.firstinspires.ftc.teamcode.dynamite.DYNtokenizer.TokenType.WSTART) {
                        openBlocks++;
                    } else if (tt == org.firstinspires.ftc.teamcode.dynamite.DYNtokenizer.TokenType.END || tt == org.firstinspires.ftc.teamcode.dynamite.DYNtokenizer.TokenType.WEND) {
                        if (openBlocks > 0) openBlocks--;
                    }
                }

                if (openBlocks > 0) {
                    telemetry.addData("ScriptFix", "Detected " + openBlocks + " unclosed block(s) - appending missing 'end' lines");
                    StringBuilder sb = new StringBuilder(scriptContent);
                    sb.append("\n// DYN_AUTO_FIX: Appended ").append(openBlocks).append(" missing end(s)\n");
                    for (int i = 0; i < openBlocks; i++) sb.append("end\n");
                    scriptContent = sb.toString();
                    telemetry.addData("ScriptFixedLen", scriptContent.length());
                    telemetry.update();
                }
            } catch (Exception e) {
                telemetry.addData("ScriptFixErr", e.getMessage());
                telemetry.update();
            }

            dynAuto.loadScriptContent(scriptContent, getCustomFunctionIds());
            
            telemetry.addData("Script", "Loaded: " + getScriptName());
            telemetry.update();
        } catch (IOException e) {
            telemetry.addData("ERROR", "Failed to load script: " + e.getMessage());
            telemetry.addData("TIP", "Make sure " + getScriptName() + " exists in TeamCode/src/main/assets/");
            telemetry.update();
        } catch (Exception e) {
            telemetry.addData("ERROR", "Script processing failed: " + e.getMessage());
            telemetry.update();
        }
    }

    /**
     * Load a file from the assets folder.
     */
    protected String loadAssetFile(String filename) throws IOException {
        StringBuilder content = new StringBuilder();
        InputStream is = hardwareMap.appContext.getAssets().open(filename);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        String line;
        while ((line = reader.readLine()) != null) {
            content.append(line).append("\n");
        }
        reader.close();
        return content.toString();
    }

    // ==================== CUSTOM COMMANDS ====================

    /**
     * Register custom commands for your robot's subsystems.
     * Override this method to add your own commands.
     */
    protected void registerCustomCommands() {
        // Register custom commands for DYN script access.
        // All 4 scenarios are supported:
        // 1. No input/output:    cmd grabSample
        // 2. Input only:         cmd setArmSpeed from speedVar
        // 3. Output only:        cmd getArmPosition to posVar
        // 4. Both input/output:  cmd moveArm from targetPos to actualPos
        
        // Scenario 1: Simple commands with no input or output
        // Example intake commands
        dynAuto.registerCustomCommand("startIntake", (name, input) -> {
            // intakeMotor.setPower(1.0);
            telemetry.addData("CMD", "Starting intake");
            return null;  // No output
        });

        dynAuto.registerCustomCommand("stopIntake", (name, input) -> {
            // intakeMotor.setPower(0.0);
            telemetry.addData("CMD", "Stopping intake");
            return null;  // No output
        });

        // Scenario 1: Simple claw commands
        dynAuto.registerCustomCommand("openClaw", (name, input) -> {
            // clawServo.setPosition(0.0);
            telemetry.addData("CMD", "Opening claw");
            return null;  // No output
        });

        dynAuto.registerCustomCommand("closeClaw", (name, input) -> {
            // clawServo.setPosition(1.0);
            telemetry.addData("CMD", "Closing claw");
            return null;  // No output
        });

        // Scenario 1: Simple arm commands
        dynAuto.registerCustomCommand("raiseArm", (name, input) -> {
            // moveArmToPosition(ARM_UP);
            telemetry.addData("CMD", "Raising arm");
            return null;  // No output
        });

        dynAuto.registerCustomCommand("lowerArm", (name, input) -> {
            // moveArmToPosition(ARM_DOWN);
            telemetry.addData("CMD", "Lowering arm");
            return null;  // No output
        });

        // Scenario 1 & 3: Compound actions
        dynAuto.registerCustomCommand("grabSample", (name, input) -> {
            // closeClaw();
            // sleep(300);
            telemetry.addData("CMD", "Grabbing sample");
            try {
                return new DynVar("Boolean", "grabbed", true);  // Scenario 3: Has output
            } catch (Exception e) {
                return null;
            }
        });

        dynAuto.registerCustomCommand("dropSample", (name, input) -> {
            // openClaw();
            // sleep(300);
            telemetry.addData("CMD", "Dropping sample");
            return null;  // No output
        });
    }

    // ==================== HELPER METHODS ====================

    /**
     * Check if the OpMode should continue running.
     */
    public boolean shouldContinue() {
        return opModeIsActive() && !isStopRequested();
    }

    /**
     * Sleep with OpMode stop check.
     */
    protected void safeSleep(long ms) {
        long startTime = System.currentTimeMillis();
        while (opModeIsActive() && (System.currentTimeMillis() - startTime) < ms) {
            idle();
        }
    }
}
