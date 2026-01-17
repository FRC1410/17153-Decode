package org.firstinspires.ftc.teamcode.Util;

public class Inputlimiter {
    private int currentstep;
    private int stepsPerUpdate = 5;
    private boolean CurrentState;
    private boolean NextState;
    public boolean getState(boolean stateIn){
        currentstep++;
        if (currentstep%stepsPerUpdate == 0){CurrentState = NextState;}
        NextState = stateIn;
        return CurrentState;
    }

}
