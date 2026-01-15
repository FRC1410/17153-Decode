package org.firstinspires.ftc.teamcode.dynamite;

import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.dynamite.DynCommands.Etc.CustomCommand;
import org.firstinspires.ftc.teamcode.dynamite.DynVar.DynVar;

/**
 * Test OpMode for verifying DYN system functionality.
 * 
 * This OpMode runs a comprehensive test script that exercises:
 * - Variable declaration and assignment
 * - Basic math operations (add, sub, mux, div, mod, pow, sqrt)
 * - Trigonometry functions (sin, cos, tan)
 * - Angle conversion (to_radian, to_degrees)
 * - For loops
 * - Nested math calculations
 * - Movement commands (setStartPosition, goTo, turnTo)
 * - Wait command
 * - Custom command registration
 * 
 * Watch telemetry output for test results.
 */
@Autonomous(name = "DYN System Test", group = "Test")
public class DynSystemTestOpMode extends DynAutoOpMode {

    private int testCommandCount = 0;
    private int testWithArgValue = 0;

    @Override
    protected String getScriptName() {
        return "DynSystemTest.dyn";
    }

    @Override
    protected Pose getStartPose() {
        return new Pose(0, 0, 0);
    }

    @Override
    protected String[] getCustomFunctionIds() {
        return new String[] {
            "testCommand",
            "testWithArg"
        };
    }

    @Override
    protected void registerCustomCommands() {
        super.registerCustomCommands();
        // Simple test command - just increments counter
        dynAuto.registerCustomCommand("testCommand", new CustomCommand.CustomCommandHandler() {
            @Override
            public DynVar execute(String name, DynVar input) {
                testCommandCount++;
                telemetry.addData("testCommand", "Called " + testCommandCount + " time(s)");
                telemetry.update();
                return null;
            }
        });

        // Test command with argument - stores the value
        dynAuto.registerCustomCommand("testWithArg", new CustomCommand.CustomCommandHandler() {
            @Override
            public DynVar execute(String name, DynVar input) {
                if (input != null) {
                    try {
                        Object val = input.getValue();
                        if (val instanceof Number) {
                            testWithArgValue = ((Number) val).intValue();
                        } else {
                            testWithArgValue = Integer.parseInt(val.toString());
                        }
                        telemetry.addData("testWithArg", "Received value: " + testWithArgValue);
                    } catch (Exception e) {
                        telemetry.addData("testWithArg", "Error: " + e.getMessage());
                    }
                } else {
                    telemetry.addData("testWithArg", "No input received");
                }
                telemetry.update();
                return null;
            }
        });
    }

    public void runAutonomous() {
        telemetry.addLine("========================================");
        telemetry.addLine("    DYN SYSTEM TEST STARTING");
        telemetry.addLine("========================================");
        telemetry.update();

        // Run the test script
        dynAuto.run();

        // Show final results
        telemetry.addLine("========================================");
        telemetry.addLine("    TEST RESULTS SUMMARY");
        telemetry.addLine("========================================");
        telemetry.addData("testCommand calls", testCommandCount);
        telemetry.addData("testWithArg value", testWithArgValue);
        telemetry.addLine("========================================");
        telemetry.update();

        // Keep telemetry visible
        while (opModeIsActive()) {
            idle();
        }
    }
}
