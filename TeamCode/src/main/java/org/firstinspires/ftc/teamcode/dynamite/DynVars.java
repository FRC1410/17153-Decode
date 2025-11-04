package org.firstinspires.ftc.teamcode.dynamite;

import java.util.ArrayList;
import java.util.List;

public class DynVars {
    // field cords
    public List<DynVarFieldCords> fieldCords = new ArrayList<>();
    // field positions
    public List<DynVarFieldPos> fieldPoses = new ArrayList<>();
    // numbers
    public List<DynVarNumber> numbers = new ArrayList<>();
    // booleans
    public List<DynVarBoolean> booleans = new ArrayList<>();
    // strings
    public List<DynVarString> strings = new ArrayList<>();
    // lists
    public List<DynVarList> lists = new ArrayList<>();
    // json
    public List<DynVarJson> jsons = new ArrayList<>();
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
    private List<DynVarFieldCords> FieldCords;
    private List<DynVarFieldPos> FieldPoses;
    private List<DynVarNumber> Numbers;
    private List<DynVarBoolean> Booleans;
    private List<DynVarString> Strings;
    private List<DynVarList> Lists;
}
// json
class DynVarJson{
    public String name  = "";
    // this one too =)
}