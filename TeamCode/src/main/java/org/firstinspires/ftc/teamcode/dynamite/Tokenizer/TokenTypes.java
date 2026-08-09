package org.firstinspires.ftc.teamcode.dynamite.Tokenizer;

// dis a lot a things to take care of
public enum TokenTypes {
    // single char tokens
    Lparenth, Rparenth, Comma,
    Lbracket, Rbracket,        // L/R brackets '[' + ']'
    LCbracket,RCbracket,       // L/R curly brackets '{' + '}'
    Colon,
    // math commands
    Add,Sub,Mux,Div,
    Pow,Sqrt,Sin,iSin,
    Cos,iCos,Tan,iTan,
    toRad,toDeg,
    Increment, Decrement,
    // variables
    NumberDef,BoolDef,StringDef,
    List,Json,FieldCord,
    FieldPos,
    // list/json Ops
    Get,Insert,Append,
    Remove,Set,
    // logical Ops
    Equals,NotEqual,isMore,
    isMoreEqual,isLessEqual,
    isLess,And,Or,Not,
    // movement ops
    TurnTo,GoTo,
    doBez,followSpline,
    followSplineLinear,
    followSplineSpline,
    // func/loop/if
    DefPath,Run,While,For,If,
    // telemetry
    AddData,Update,Clear,
    // literals
    Boolean,Name,Number,String,
    // random commands
    RngFloat,RngDouble,
    RngInteger,RngBoolean,
    // extra
    Start,End,PathStartPos,
    Cmd,To,MainPathFunc,Sleep
}