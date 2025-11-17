package org.firstinspires.ftc.teamcode.dynamite.DynVars;

public class DynVarNumber {
    public String name  = "";
    public double value;
    // instantiators
    public DynVarNumber(double in, String Name){value = in;name = Name;}
    public DynVarNumber(float in, String Name){value = in;name = Name;}
    public DynVarNumber(int in, String Name){value = in;name = Name;}
    public DynVarNumber(String Name){value = 0;name = Name;}
    // to dyn string (WIP)
    public DynVarString toDynStr(){
        return null;//new DynVarString();
    }
    // to java number (WIP)
    public double toNum(){return value;}
    // to java string
    @Override
    public String toString(){
        return "DYNnum: "+name+" value:"+value;
    }
    // set to add (WIP)
}
