package org.firstinspires.ftc.teamcode.Autos;

import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Subsystem.IntakeAuto;
import org.firstinspires.ftc.teamcode.Subsystem.Shooter;
import org.firstinspires.ftc.teamcode.Util.RobotStates;
import org.firstinspires.ftc.teamcode.dynamite.DynAutoOpMode;
import org.firstinspires.ftc.teamcode.dynamite.DynCommands.Etc.CustomCommand;
import org.firstinspires.ftc.teamcode.dynamite.DynVar.DynVar;

@Autonomous(name="Blue: Goal")
public class BlueAtGoal extends DynAutoOpMode {
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
            "Sleep"
    };

    @Override
    protected String getScriptName(){
        return "B_AG.dyn";
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
                safeSleep(2000);
                // feed the shooter
                shooter.feed(0.7f);
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
                safeSleep(500); // time for spool
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
    }
}
