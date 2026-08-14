package org.firstinspires.ftc.teamcode.dynamite.DYNCore.commandSequencer.Commands;

public enum CommandType {
    // control flow
    For,If,While,
    // Function
    jFunc, RunPath, DynPath,
    // math
    //arithmetic
    Add,Decrement,Div,
    Increment,Mux,Pow,
    Sqrt,Sub,
    //trig
    Cos,iCos,iSin,iTan,
    Sin,Tan,toDeg,toRad,
    // movement
    SplineTo,BezTo,
    GoTo,TurnTo,
    SetStartPose,
    // random
    RngBoolean,RngDouble,
    RngFloat,RngInteger,
    // telemetry
    AddData,Clear,Update,
    // variables
    SetVar,AddVar,Set,
    Remove,Append,Get,
    Insert,
    // extra
    Sleep,Quit,HardQuit,
    Count
}
