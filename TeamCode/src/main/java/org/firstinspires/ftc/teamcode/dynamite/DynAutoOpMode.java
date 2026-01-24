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
    private void runAutonomous(){
        dynAuto.run();
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
