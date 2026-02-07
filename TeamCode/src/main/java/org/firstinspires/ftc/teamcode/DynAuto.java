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
        /*
        if (Alience == "BLUE"){
            if (Pos == "At_Goal"){
                return  "B_AG.dyn";
            } else if (Pos == "At_Far"){
                return "B_AF.dyn";
            } else {
                return "B_AS.dyn";
            }
        } else {
            if (Pos == "At_Goal"){
                return "R_AG.dyn";
            } else if (Pos == "At_Far"){
                return "R_AF.dyn";
            } else {
                return "R_AS.dyn";
            }
        }
         */
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
}
