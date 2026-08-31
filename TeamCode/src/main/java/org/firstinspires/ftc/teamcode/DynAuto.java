package org.firstinspires.ftc.teamcode;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.Subsystem.AprilTags;
import org.firstinspires.ftc.teamcode.Subsystem.Intake;
import org.firstinspires.ftc.teamcode.Subsystem.Shooter;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.variables.Variable;
import org.firstinspires.ftc.teamcode.dynamite.InterfaceStuffs.DYNFunctionalInterface;
import org.firstinspires.ftc.teamcode.dynamite.InterfaceStuffs.InterfaceUtils;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

@Autonomous(name="DynTestbed")
public class DynAuto extends OpMode {

    Follower pedroPather = Constants.createFollower(hardwareMap);
    AprilTags aprilTags;
    Shooter shooter;
    Intake intake;

    InterfaceUtils DYNUtil;
    DYNFunctionalInterface DYNInterface;
    public void init(){
        // DYN stuff
        DYNUtil = new InterfaceUtils();
        DYNInterface = new DYNFunctionalInterface(this::updateFollower,DYNUtil,telemetry,hardwareMap,"Main.dyn",true,false);
        DYNInterface.init();
        DYNInterface.registerOpModeStop(this::requestOpModeStop); // allows DYN to fully stop the OpMode
        // main stuff
        registerFunctions();
        aprilTags = new AprilTags(hardwareMap);
        // init subsystems
        shooter = new Shooter();
        shooter.init(hardwareMap);
        intake = new Intake();
        intake.init(hardwareMap);
    }

    public void init_loop(){
        DYNInterface.initLoop();
    }
    public void start(){
        DYNInterface.start();
    }

    private volatile double visionFPS = 0; // volatile ensures that all changes form one thread happen for all threads
    public void loop(){
        DYNInterface.loop();
        intake.intakeTelem(telemetry);
        shooter.addTelemetry(telemetry);
        telemetry.addData("FPS",visionFPS);
        telemetry.update();
    }

    public void stop(){
        DYNInterface.stop();
    }

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
        DYNUtil.registerJFunc("cycleShoot",this::cycleShooter);
        DYNUtil.registerJFunc("feedShoot",this::feedShooter);
        DYNUtil.registerJFunc("getShooterState",this::getShooterState);

        DYNUtil.registerJFunc("intakeOn",this::intakeOn);
        DYNUtil.registerJFunc("intakeOff",this::intakeOff);
        DYNUtil.registerJFunc("intakeOut",this::intakeOut);
    }

    private Variable getShooterState(){
        return DYNUtil.makeStringVar(shooter.shooterStatus.toString());
    }
    private void cycleShooter(){
        shooter.cycle(telemetry);
        telemetry.addData("Shooter","cycled!");
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