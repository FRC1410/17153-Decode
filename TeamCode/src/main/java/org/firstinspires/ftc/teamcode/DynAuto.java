package org.firstinspires.ftc.teamcode;

import com.pedropathing.follower.Follower;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.teamcode.dynamite.DynOpMode;

@Autonomous(name="Dyn Auto")
@Disabled
public class DynAuto extends DynOpMode {
    @Override
    public boolean loadFromUSB() {
        return false;
    }
    @Override
    public String getScriptPath() {
        return "";
    }
    @Override
    public Follower buildFollower() {
        return null;
    }
    @Override
    public void onInit() {}
    @Override
    public void onLoop() {}

    @Override
    public void updateFollower() {}
}
    /*
    public Shooter shooter = new Shooter();
    public IntakeAuto intake = new IntakeAuto();

    @Override
    protected void onInit(){
        telemetry.setMsTransmissionInterval(50);
        shooter.init(hardwareMap);
        intake.init(hardwareMap);
    }


    // Use a fixed asset copy to avoid stale/packaging issues
    String[] jFuncIDs = new String[]{
            "FireShooter",
            "IntakeOn",
            "IntakeOff",
            "Sleep3s",
            "Sleep2s",
            "Sleep1s",
            "Sleep",
            "getIntakeRPM"
    };

    @Override
    protected String getScriptName(){
        return "AutoTest.dyn";
    }

    @Override
    protected Pose getStartPose(){
        // Match the StartPos from the default path (BLUE_AT_GOAL)
        // Heading 143° converted to radians
        return new Pose(23, 126, Math.toRadians(143));
    }

    @Override
    protected String[] getCustomFunctionIds(){
        return jFuncIDs;
    }

    @Override
    protected void registerCustomCommands(){
        super.registerCustomCommands();

        dynAuto.registerCustomCommand("FireShooter", new CustomCommand.CustomCommandHandler() {
            @Override
            public DynVar execute(String name, DynVar input){
                // rev the shooter
                shooter.run(RobotStates.ShooterStates.FORWARD);
                // wait
                safeSleep(1000);
                // feed the shooter
                shooter.feed(1);
                intake.run(1,0);
                // wait
                safeSleep(3000);
                // stop feeding and running the shooter
                shooter.run(RobotStates.ShooterStates.NEUTRAL);
                shooter.feed(0);
                intake.run(0,0);
                return null;
            }
        });
        dynAuto.registerCustomCommand("IntakeOn", new CustomCommand.CustomCommandHandler() {
            @Override
            public DynVar execute(String functionName, DynVar input) {
                // turn the intake on
                intake.run(1,0);
                return null;
            }
        });
        dynAuto.registerCustomCommand("IntakeOff", new CustomCommand.CustomCommandHandler() {
            @Override
            public DynVar execute(String functionName, DynVar input) {
                // turn the intake off
                intake.run(0,0);
                return null;
            }
        });
        dynAuto.registerCustomCommand("Sleep", new CustomCommand.CustomCommandHandler() {
            @Override
            public DynVar execute(String functionName, DynVar input) {
                long wait = (long)((double)input.toJava());
                safeSleep(wait);
                return null;
            }
        });
        dynAuto.registerCustomCommand("getIntakeRPM",new CustomCommand.CustomCommandHandler() {
            @Override
            public DynVar execute(String functionName, DynVar input) {
                try {
                    return new DynVar("Number","",intake.getRPM());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
        dynAuto.registerCustomCommand("stopMotors", new CustomCommand.CustomCommandHandler() {
            @Override
            public DynVar execute(String functionName, DynVar input) {
                intake.run(0,0);
                shooter.run(RobotStates.ShooterStates.NEUTRAL);
                shooter.feed(0);
                safeSleep(1/20);
                return null;
            }
        });
    }
    */

