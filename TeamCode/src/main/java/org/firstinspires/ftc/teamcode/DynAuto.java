package org.firstinspires.ftc.teamcode;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.teamcode.Subsystem.AprilTags;
import org.firstinspires.ftc.teamcode.Subsystem.Intake;
import org.firstinspires.ftc.teamcode.Subsystem.Shooter;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.variables.Variable;
import org.firstinspires.ftc.teamcode.dynamite.DynOpMode;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

@Autonomous(name="Dyn Auto")
public class DynAuto extends DynOpMode {
    @Override
    public boolean loadFromUSB() {
        return false; // I currently do not have a USB drive to use for in-workshop testing
    }
    @Override
    public String getScriptPath() {
        return "Main.dyn";
    }

    Follower pedroPather;
    @Override
    public Follower buildFollower() {
        pedroPather = Constants.createFollower(hardwareMap);
        return pedroPather;
    }

    AprilTags aprilTags;
    Shooter shooter;
    Intake intake;
    @Override
    public void onInit(){
        registerFunctions();
        aprilTags = new AprilTags(hardwareMap);
        // init subsystems
        shooter = new Shooter();
        shooter.init(hardwareMap);
        intake = new Intake();
        intake.init(hardwareMap);
    }

    private int mainCounter = 0;
    private int visibleCounter = 0;
    @Override
    public void onLoop(){
        mainCounter++;
        if (mainCounter%20 == 0) visibleCounter++;
        intake.intakeTelem(newTelemetry);
        shooter.addTelemetry(newTelemetry);
        newTelemetry.addData("Counter",visibleCounter);
        newTelemetry.update();
    }

    @Override
    public void updateFollower(){
        pedroPather.update();
        aprilTags.update();
        if (!aprilTags.getDetections().isEmpty()) {
            Pose tagPose = aprilTags.getPedroPose();
            pedroPather.setPose(tagPose);
        }
    }

    private void registerFunctions(){
        registerJFunc("cycleShoot",this::cycleShooter);
        registerJFunc("feedShoot",this::feedShooter);
        registerJFunc("getShooterState",this::getShooterState);

        registerJFunc("intakeOn",this::intakeOn);
        registerJFunc("intakeOff",this::intakeOff);
        registerJFunc("intakeOut",this::intakeOut);
    }

    private Variable getShooterState(){
        return makeStringVar(shooter.shooterStatus.toString());
    }
    private void cycleShooter(){
        shooter.cycle(newTelemetry);
        newTelemetry.addData("Shooter","cycled!");
    }
    private void feedShooter(){
        shooter.feed(1);
    }
    private void intakeOn(){
        intake.run(1,0);
    }
    private void intakeOff(){
        intake.run(0,0);
    }
    private void intakeOut(){
        intake.run(0,1);
    }
}