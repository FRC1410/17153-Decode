package org.firstinspires.ftc.teamcode.dynamite.DynVars;

public class DynVarFieldPos {
    public String name = "";
    public DynVarNumber PosX;
    public DynVarNumber PosY;
    public DynVarNumber Heading;
    public DynVarFieldPos(){
        PosX = new DynVarNumber(name);
        PosY = new DynVarNumber(name);
        Heading = new DynVarNumber(name);
    }
    public DynVarFieldPos(DynVarNumber PosX, DynVarNumber PosY, DynVarNumber Heading){
        this.PosX = PosX;
        this.PosY = PosY;
        this.Heading = Heading;
    }
    // to dyn field cord (WIP)
    // to string (WIP)
    // pedro position (WIP)
}
