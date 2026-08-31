package org.firstinspires.ftc.teamcode.dynamite.FTCInterface;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.dynamite.DYNCore.variables.Variable;

// is what the main DYN interpreter calls to make the robot do stuff
public interface FTCInterface {
    void setStartPos(int line, double[] pos);
    void runGeneralMove(int line, GeneralMovement move);

    Variable runJFunc(int line, boolean wantOutput, String ID);
    Variable runJFunc(int line, boolean wantOutput, String ID, Variable in);

    void addData(String data);
    void update();

    HardwareMap getHardwareMap();

    void DYNSleep(long milliseconds);
}
