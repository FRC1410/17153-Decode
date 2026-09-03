package org.firstinspires.ftc.teamcode;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Subsystem.AprilTags;
import org.firstinspires.ftc.teamcode.dynamite.DynOpMode;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

@Autonomous(name="DynTestbed")
public class DynAuto extends DynOpMode {
    @Override
    public boolean loadFromUSB() {
        return false;
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

    @Override
    public void onInit(){
        registerFunctions();
        aprilTags = new AprilTags(hardwareMap);
    }

    private volatile double visionFPS = 0; // volatile ensures that all changes form one thread happen for all threads
    private volatile Pose roboPose;
    @Override
    public void onLoop(){
        if (roboPose != null) newTelemetry.addData("Pos","X:"+roboPose.getX()+" Y:"+roboPose.getY()+" H:"+Math.toDegrees(roboPose.getHeading()));
        newTelemetry.addData("FPS",visionFPS);
        newTelemetry.update();
    }

    @Override
    public void updateFollower(){
        pedroPather.update();
        roboPose = new Pose(pedroPather.getPose().getX(),pedroPather.getPose().getY(),pedroPather.getHeading());
        aprilTags.update();
        visionFPS = aprilTags.vision_portal.getFps();
        // because this runs at a much higher rate than vision (50hz vs ~15hz)
        // we should only correct the Pedro position whenever vision updates
        if (aprilTags.hasNewPos()) {
            Pose tagPose = aprilTags.getPedroPose();
            pedroPather.setPose(tagPose);
        }
    }

    private void registerFunctions(){}
}