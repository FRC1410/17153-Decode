package org.firstinspires.ftc.teamcode.dynamite;

import java.util.List;

public class DynVars {
    // field cords
    public List<DynVarFieldCords> fieldCords;
    // field positions
    public List<DynVarFieldPos> fieldPoses;
    // numbers
    public List<DynVarNumber> numbers;
    // booleans
    public List<DynVarBoolean> booleans;
    // strings
    public List<DynVarString> strings;
    // lists
    public List<DynVarList> lists;
    // json
    public List<DynVarJson> jsons;
}

// field cords
class DynVarFieldCords{
    public String name = "";
    public DynVarNumber PosX;
    public DynVarNumber PosY;
}
// field position
class DynVarFieldPos{
    public String name = "";
    public DynVarNumber PosX;
    public DynVarNumber PosY;
    public DynVarNumber Heading;
}
// number
class DynVarNumber{
    public String name  = "";
    public double value = 0;
}
// boolean
class DynVarBoolean{
    public String name  = "";
    public boolean value = true;
}
// string
class DynVarString{
    public String name  = "";
    public String value = "";
}
// list
class DynVarList{
    public String name  = "";
    // this ones gonna be fun :)
}
// json
class DynVarJson{
    public String name  = "";
    // this one too =)
}