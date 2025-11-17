package org.firstinspires.ftc.teamcode.dynamite.DynVars;

import org.firstinspires.ftc.teamcode.dynamite.DynVars.*;
public class DynVarFieldCords {
    public String name = "";
    public DynVarNumber PosX;
    public DynVarNumber PosY;
    // instantiators
    public DynVarFieldCords(String name){this.name = name;}
    public DynVarFieldCords(DynVarNumber PosX, DynVarNumber PosY, String name){this.PosX=PosX;this.PosY=PosY;this.name=name;}
    // to string
    @Override
    public String toString(){
        return "Field Cord: "+name+" X:"+PosX.toString()+" Y:"+PosY.toString();
    }
    // to prdro position (WIP)
    // to dyn field position (WIP)
    public DynVarFieldPos toFieldPos(){
        return new DynVarFieldPos(PosX, PosY, new DynVarNumber(name));
    }
    // to java string

    // to DYN string
    public DynVarString toDYNString(){return null;}
    // to list
    // equals
    public boolean equals(DynVarFieldPos in){return (in.PosX == PosX && in.PosY == PosY);}
    public boolean equals(DynVarFieldCords in){return (in.PosX == PosX && in.PosY == PosY);}

}