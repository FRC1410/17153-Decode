package org.firstinspires.ftc.teamcode.dynamite;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.CommandException;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.commands.Command;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.variables.Variable;
import org.firstinspires.ftc.teamcode.dynamite.FTCInterface.FTCInterface;
import org.firstinspires.ftc.teamcode.dynamite.FTCInterface.GeneralMovement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

class PPInterface implements FTCInterface {
    private final HardwareMap hardwareMap;
    private final Telemetry telemetry;
    private final Follower pather;
    private boolean hasStartPosBeenSet;
    private final boolean processInRad;

    public PPInterface(Follower pather, HardwareMap hardwareMap, Telemetry telemetry, boolean processInRad){
        this.processInRad = processInRad;
        this.hardwareMap = hardwareMap;
        this.telemetry = telemetry;
        this.pather = pather;
    }

    @Override
    public void setStartPos(int line, double[] pos) {
        if (!hasStartPosBeenSet) {
            hasStartPosBeenSet = true;
            if (pos.length == 2) {
                pather.setStartingPose(new Pose(pos[0], pos[1]));
            } else if (pos.length == 3) {
                pather.setStartingPose(new Pose(pos[0], pos[1], pos[2]));
            }
        } else {
            throw new CommandException(line,"SetStartPose","Cannot set start pos when its already been set!");
        }
    }

    @Override
    public void runGeneralMove(int line, GeneralMovement move) {
        if (hasStartPosBeenSet) {
            switch (move.type) {
                case Bezier -> {
                    // assemble points
                    Pose[] midPoints = new Pose[move.bezTarget.length-1];
                    Pose endPose;
                    if (move.bezTarget[move.bezTarget.length].length == 3){
                        endPose = new Pose(
                                move.bezTarget[move.bezTarget.length][0],
                                move.bezTarget[move.bezTarget.length][1],
                                move.bezTarget[move.bezTarget.length][2]);
                    } else {
                        endPose = new Pose(
                                move.bezTarget[move.bezTarget.length][0],
                                move.bezTarget[move.bezTarget.length][1]);
                    }
                    for (int i = 0; i < move.bezTarget.length-1; i++){
                        double[] givenPose = move.bezTarget[i];
                        if (givenPose.length == 3){
                            midPoints[i] = new Pose(
                                    givenPose[0],
                                    givenPose[1],
                                    givenPose[2]);
                        } else {
                            midPoints[i] = new Pose(
                                    givenPose[0],
                                    givenPose[1]);
                        }
                    }
                    // build into a PathChain
                    ArrayList<Pose> poseList = new ArrayList<>();
                    poseList.addAll(Arrays.asList(midPoints));
                    poseList.add(endPose);
                    // do deg->rad processing
                    if (!processInRad) {
                        for (int i = 0; i < poseList.size(); i++) {
                            // convert to rad, because that's what PP uses
                            Pose oldPose = poseList.get(i);
                            double poseAngle = Math.toRadians(oldPose.getHeading());
                            poseList.set(i, new Pose(oldPose.getX(), oldPose.getY(), poseAngle));
                        }
                    }
                    BezierCurve bezier = new BezierCurve(poseList);
                    // make this as close to PP interaction as possible
                    preMoveProcess();
                    // use PP
                    Pose currentPose = pather.getPose();
                    PathChain plannedpath = pather.pathBuilder().addPath(bezier).setLinearHeadingInterpolation(currentPose.getHeading(), endPose.getHeading()).build();
                    pather.followPath(plannedpath);
                }
                case TurnTo -> {
                    preMoveProcess();
                    double angleDelta = pather.getPose().getHeading()-move.heading;
                    if (!processInRad) angleDelta = Math.toRadians(angleDelta);
                    pather.turn(angleDelta);
                }
                case GoTo -> {
                    Pose endPose;
                    if (move.target.length == 3){
                        endPose = new Pose(
                                move.target[0],
                                move.target[1],
                                move.target[2]);
                    } else {
                        endPose = new Pose(
                                move.target[0],
                                move.target[1]);
                    }
                    if (!processInRad) endPose = new Pose(endPose.getX(),endPose.getY(),Math.toRadians(endPose.getHeading()));
                    preMoveProcess();
                    Pose start = pather.getPose();
                    BezierLine linePath = new BezierLine(start,endPose);
                    PathChain calculatedPath = pather.pathBuilder().addPath(linePath).setLinearHeadingInterpolation(start.getHeading(), endPose.getHeading()).build();
                    pather.followPath(calculatedPath);
                }
                default -> throw new RuntimeException("Pedro Pathing does not support this kind of movement!");
            }
            // wait for the move to end
            postMoveProcess();
        } else {
            throw new CommandException(line,"Move","Cannot move robot until start pose has been set!");
        }
    }

    private void preMoveProcess(){
        patherUpdateThread.stop();
    }
    private void postMoveProcess(){
        // we wait until PP is done moving
        long periodNanos = (long) (1_000_000_000.0 / patherUpdateThread.getUpdateRate());
        while (pather.isBusy()){
            // limit to update limit
            long startTime = System.nanoTime();
            patherUpdateThread.itterUpdate();
            long elapsed = System.nanoTime()-startTime;
            long remaining = periodNanos - elapsed;
            try {
                long millis = remaining / 1_000_000;
                int nanos = (int) (remaining % 1_000_000);
                Thread.sleep(millis, nanos);
            } catch (InterruptedException e) {
                System.out.println(e);
            }
        }
        patherUpdateThread.start();
    }

    public volatile boolean requested = false;
    public volatile boolean processed = false;
    public final Object lock = new Object(); // bc ofc Android Studio says it should be final, not volatile
    public volatile String funcID = null;
    public volatile int ranLine;
    public volatile boolean wantOutput = false;
    public volatile Variable inVar = null;
    public volatile Variable outVar = null;
    @Override
    public Variable runJFunc(int line, boolean wantOutput, String ID) {
        // ensure that only one thread is actually using the related variables
        synchronized (lock){
            // set stuff
            funcID = ID;
            ranLine = line;
            this.wantOutput = wantOutput;
            inVar = null;
            outVar = null;
            // request processing
            processed = false;
            requested = true;
            // wait for lock release (aka: the function was run by the main thread)
            while (!processed) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            return outVar;
        }
    }
    @Override
    public Variable runJFunc(int line, boolean wantOutput, String ID, Variable in) {
        synchronized (lock){
            // set stuff
            funcID = ID;
            ranLine = line;
            this.wantOutput = wantOutput;
            inVar = in;
            outVar = null;
            // request processing
            processed = false;
            requested = true;
            // wait for lock release
            while (!processed) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            return outVar;
        }
    }

    @Override
    public void addData(String data) {
        // routed through Command's buffer: these are called from the DYN thread, and the
        // SDK Telemetry object may only be touched from the OpMode loop thread.
        Command.pushTelemLine(data);
    }

    @Override
    public void update() {
        Command.updateTelem();
    }

    @Override
    public HardwareMap getHardwareMap() {
        return hardwareMap;
    }

    TimedLoopThread patherUpdateThread;
    Runnable updateFollower;
    public void linkPatherUpdateThread(TimedLoopThread followerUpdateThread, Runnable updateFollower) {
        patherUpdateThread = followerUpdateThread;
        this.updateFollower = updateFollower;
    }
    @Override
    public void updateFollower() {
        updateFollower.run();
    }
    @Override
    public void stopFollowerUpdater() {
        patherUpdateThread.stop();
    }
    @Override
    public void startFollowerUpdater() {
        patherUpdateThread.start();
    }

    private Thread DYNThread;
    @Override
    public void DYNSleep(long milliseconds){
        try {
            DYNThread.sleep(milliseconds);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    public void setDYNThread(Thread DYNThread){
        this.DYNThread = DYNThread;
    }
}