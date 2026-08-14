package org.firstinspires.ftc.teamcode.dynamite;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.commandSequencer.Commands.Command;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.commandSequencer.variables.Variable;
import org.firstinspires.ftc.teamcode.dynamite.FTCInterface.FTCInterface;
import org.firstinspires.ftc.teamcode.dynamite.FTCInterface.GeneralMovement;

import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class PPInterface implements FTCInterface {
    private final HardwareMap hardwareMap;
    private final Telemetry telemetry;
    private final Follower pather;
    private boolean hasStartPosBeenSet;

    public PPInterface(Follower pather, HardwareMap hardwareMap, Telemetry telemetry) {
        this.hardwareMap = hardwareMap;
        this.telemetry = telemetry;
        this.pather = pather;
    }

    @Override
    public void setStartPos(double[] pos) {
        if (!hasStartPosBeenSet) {
            hasStartPosBeenSet = true;
            if (pos.length == 2) {
                pather.setStartingPose(new Pose(pos[0], pos[1]));
            } else if (pos.length == 3) {
                pather.setStartingPose(new Pose(pos[0], pos[1], pos[2]));
            }
        } else {
            // TODO: add catch logic to convert this stuff into comprehensible DYN errors
            throw new RuntimeException("Cannot set start pos when its already been set!");
        }
    }

    @Override
    public void runGeneralMove(GeneralMovement move) {
        if (hasStartPosBeenSet) {
            switch (move.type) {
                // TODO: these three
                case Bezier -> {}
                case TurnTo -> {}
                case GoTo -> {}
                default -> throw new RuntimeException("Pedro Pathing does not support this kind of movement!");
            }
        } else {
            // TODO: here too
            throw new RuntimeException("Cannot move robot until start pose has been set!");
        }
    }

    @Override
    public Variable runJFunc(int line, boolean wantOutput, String ID) {
        if (wantOutput){
            if (supplierJFuncs.containsKey(ID)){
                return supplierJFuncs.get(ID).get();
            } else {
                throw new RuntimeException("No available function with ID: " + ID);
            }
        } else {
            if (runnableJFuncs.containsKey(ID)){
                runnableJFuncs.get(ID).run();
                return null;
            } else {
                throw new RuntimeException("No available function with ID: " + ID);
            }
        }
    }
    @Override
    public Variable runJFunc(int line, boolean wantOutput, String ID, Variable in) {
        if (wantOutput){
            if (functionJFuncs.containsKey(ID)){
                return functionJFuncs.get(ID).apply(in);
            } else {
                throw new RuntimeException("No available function with ID: " + ID);
            }
        } else {
            if (consumerJFuncs.containsKey(ID)){
                consumerJFuncs.get(ID).accept(in);
                return null;
            } else {
                throw new RuntimeException("No available function with ID: " + ID);
            }
        }
    }

    private Map<String, Function<Variable, Variable>> functionJFuncs;
    private Map<String, Consumer<Variable>> consumerJFuncs;
    private Map<String, Supplier<Variable>> supplierJFuncs;
    private Map<String, Runnable> runnableJFuncs;

    public void linkJFuncs(
            Map<String, Function<Variable, Variable>> functions,
            Map<String, Consumer<Variable>> consumers,
            Map<String, Supplier<Variable>> suppliers,
            Map<String, Runnable> runnables) {
        functionJFuncs = functions;
        consumerJFuncs = consumers;
        supplierJFuncs = suppliers;
        runnableJFuncs = runnables;
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