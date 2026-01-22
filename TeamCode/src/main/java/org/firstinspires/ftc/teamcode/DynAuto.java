package org.firstinspires.ftc.teamcode;

import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Subsystem.Intake;
import org.firstinspires.ftc.teamcode.Subsystem.IntakeAuto;
import org.firstinspires.ftc.teamcode.Subsystem.Shooter;
import org.firstinspires.ftc.teamcode.Util.RobotStates;
import org.firstinspires.ftc.teamcode.dynamite.DynAutoOpMode;
import org.firstinspires.ftc.teamcode.dynamite.DynCommands.Etc.CustomCommand;
import org.firstinspires.ftc.teamcode.dynamite.DynVar.DynVar;

@Autonomous(name="Dyn Auto")
public class DynAuto extends DynAutoOpMode{
    public Shooter shooter = new Shooter();
    public IntakeAuto intake = new IntakeAuto();

    @Override
    protected void onInit(){
        telemetry.setMsTransmissionInterval(10);
        shooter.init(hardwareMap);
        intake.init(hardwareMap);
    }

    String dynSctiptName = "LocaliserTest.dyn";
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
        return dynSctiptName;
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
                safeSleep(500);
                // feed the shooter
                shooter.feed(1);
                // wait
                safeSleep(5000);
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
        dynAuto.registerCustomCommand("Sleep1s", new CustomCommand.CustomCommandHandler() {
            @Override
            public DynVar execute(String functionName, DynVar input) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                return null;
            }
        });

        dynAuto.registerCustomCommand("Sleep2s", new CustomCommand.CustomCommandHandler() {
            @Override
            public DynVar execute(String functionName, DynVar input) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                return null;
            }
        });

        dynAuto.registerCustomCommand("Sleep3s", new CustomCommand.CustomCommandHandler() {
            @Override
            public DynVar execute(String functionName, DynVar input) {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                return null;
            }
        });
        dynAuto.registerCustomCommand("Sleep", new CustomCommand.CustomCommandHandler() {
            @Override
            public DynVar execute(String functionName, DynVar input) {
                long wait = (long)((double)input.toJava());
                try {
                    Thread.sleep(wait);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
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
    }
}
