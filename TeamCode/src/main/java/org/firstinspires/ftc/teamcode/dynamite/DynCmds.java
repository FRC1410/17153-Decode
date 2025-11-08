package org.firstinspires.ftc.teamcode.dynamite;

import java.util.ArrayList;
import java.util.Objects;

public class DynCmds {
    public String DynID;
    public ArrayList<Object> configSequence;
}

class DefineVar{
    String varName;
    String varValue;
}
class MathOp{
    String MathOp;
    String VarIn1;
    String VarIn2;
    String VarOut;
}
class MovementOp{
    String type;
    String[] Poses;
}
class StartDeclare{
    String name;
}
class EndDeclare{
    String functionEndingName;
}
class ForLoop{

}
class WhileLoop{}
class PathFunc{}
