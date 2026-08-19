package org.firstinspires.ftc.teamcode.dynAutos;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Subsystem.AprilTags;
import org.firstinspires.ftc.teamcode.Subsystem.Intake;
import org.firstinspires.ftc.teamcode.Subsystem.Shooter;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.variables.Variable;
import org.firstinspires.ftc.teamcode.dynamite.DynOpMode;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

@Autonomous(name="\uD83D\uDFE6BlueLower", group="Blue")
public class BlueLower extends DynOpMode {
    @Override
    public boolean loadFromUSB() {
        return false;
    }
    @Override
    public String getScriptPath() {
        return "Blue/LowStart.dyn";
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

    private volatile double visionFPS = 0; // volatile ensures that all changes form one thread happen for all threads
    @Override
    public void onLoop(){
        intake.intakeTelem(newTelemetry);
        shooter.addTelemetry(newTelemetry);
        newTelemetry.addData("FPS",visionFPS);
        newTelemetry.update();
    }

    @Override
    public void updateFollower(){
        pedroPather.update();
        aprilTags.update();
        visionFPS = aprilTags.vision_portal.getFps();
        // because this runs at a much higher rate than vision (50hz vs ~15hz)
        // we should only correct the Pedro position whenever vision updates
        if (aprilTags.hasNewPos()) {
            Pose tagPose = aprilTags.getPedroPose();
            pedroPather.setPose(tagPose);
        }
    }

    private void registerFunctions(){
        registerJFunc("cycleShoot",this::cycleShooter);
        registerJFunc("feedShoot",this::feedShooter);
        registerJFunc("stopFeedShooter",this::stopShooterFeed);
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
    private void stopShooterFeed(){
        shooter.feed(0);
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
