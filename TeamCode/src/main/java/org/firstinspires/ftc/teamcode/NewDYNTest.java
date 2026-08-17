package org.firstinspires.ftc.teamcode;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.Subsystem.AprilTags;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.commandSequencer.variables.Variable;
import org.firstinspires.ftc.teamcode.dynamite.DynOpMode;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

@TeleOp(name="DYNTest")
public class NewDYNTest extends DynOpMode {
    private Follower pedroFollowsYou;
    private AprilTags aprilTags;

    @Override
    public boolean loadFromUSB() {
        return true;
    }
    @Override
    public String getScriptPath() {
        return "NoMoveTest.dyn";
    }
    public Follower buildFollower(){
        pedroFollowsYou = Constants.createFollower(hardwareMap);
        return pedroFollowsYou;
    }

    public void onInit(){
        aprilTags = new AprilTags(hardwareMap);
        likUpJFunc();
    }

    double counter = 0;
    public void onLoop(){
        reloadGamepads();
        counter+=0.1;
        addData("counter",counter);
        update();
    }

    private volatile Gamepad loadedGamepad;
    private void reloadGamepads() {
        loadedGamepad = new Gamepad();
        loadedGamepad.copy(gamepad1);
    }
    private Gamepad getGamepad1(){
        return loadedGamepad;
    }

    public void updateFollower(){
        pedroFollowsYou.update();
        aprilTags.update();
        if (!aprilTags.getDetections().isEmpty()) {
            Pose tagPose = aprilTags.getPedroPose();
            pedroFollowsYou.setPose(tagPose);
        }
    }

    private void likUpJFunc(){
        registerJFunc("get1A",this::getP1A);
        registerJFunc("get1B",this::getP1B);
        registerJFunc("get1X",this::getP1X);
        registerJFunc("get1Y",this::getP1Y);

        registerJFunc("get1L",this::getP1L);
        registerJFunc("get1R",this::getP1R);
        registerJFunc("get1U",this::getP1U);
        registerJFunc("get1D",this::getP1D);

        registerJFunc("get1RB",this::getP1RB);
        registerJFunc("get1LB",this::getP1LB);
        registerJFunc("get1RT",this::getP1RT);
        registerJFunc("get1LT",this::getP1LT);

        registerJFunc("get1LX",this::getP1LX);
        registerJFunc("get1LY",this::getP1LY);

        registerJFunc("get1RX",this::getP1RX);
        registerJFunc("get1RY",this::getP1RY);
    }

    public Variable getP1A(){
        return makeBooleanVar(getGamepad1().a);
    }
    public Variable getP1B(){
        return makeBooleanVar(getGamepad1().b);
    }
    public Variable getP1X(){
        return makeBooleanVar(getGamepad1().x);
    }
    public Variable getP1Y(){
        return makeBooleanVar(getGamepad1().y);
    }
    public Variable getP1U(){
        return makeBooleanVar(getGamepad1().dpad_up);
    }
    public Variable getP1D(){
        return makeBooleanVar(getGamepad1().dpad_down);
    }
    public Variable getP1L(){
        return makeBooleanVar(getGamepad1().dpad_left);
    }
    public Variable getP1R(){
        return makeBooleanVar(getGamepad1().dpad_right);
    }
    public Variable getP1RB(){
        return makeBooleanVar(getGamepad1().right_bumper);
    }
    public Variable getP1LB(){
        return makeBooleanVar(getGamepad1().left_bumper);
    }
    public Variable getP1RT(){
        return makeNumberVar(getGamepad1().right_trigger);
    }
    public Variable getP1LT(){
        return makeNumberVar(getGamepad1().left_trigger);
    }
    public Variable getP1LX(){
        return makeNumberVar(getGamepad1().left_stick_x);
    }
    public Variable getP1LY(){
        return makeNumberVar(getGamepad1().left_stick_y);
    }
    public Variable getP1RX(){
        return makeNumberVar(getGamepad1().right_stick_x);
    }
    public Variable getP1RY(){
        return makeNumberVar(getGamepad1().right_stick_y);
    }
}
