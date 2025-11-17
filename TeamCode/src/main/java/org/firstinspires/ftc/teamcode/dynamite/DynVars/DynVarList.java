package org.firstinspires.ftc.teamcode.dynamite.DynVars;

import java.util.ArrayList;

public class DynVarList {
    public String name  = "";

    private final AdaptiveList indexLookup = new AdaptiveList();

    // instantiators
    public DynVarList(String name){this.name = name;}

    public <T> T get(int index){return (T) indexLookup.get(index);}

    public void append(DynVarFieldCords value){indexLookup.append(value);}
    public void append(DynVarFieldPos value){indexLookup.append(value);}
    public void append(DynVarNumber value){indexLookup.append(value);}
    public void append(DynVarBoolean value){indexLookup.append(value);}
    public void append(DynVarString value){indexLookup.append(value);}
    public void append(DynVarList value){indexLookup.append(value);}

    public void set(int index, DynVarFieldCords value){indexLookup.set(index, value);}
    public void set(int index, DynVarFieldPos value){indexLookup.set(index, value);}
    public void set(int index, DynVarNumber value){indexLookup.set(index, value);}
    public void set(int index, DynVarBoolean value){indexLookup.set(index, value);}
    public void set(int index, DynVarString value){indexLookup.set(index, value);}
    public void set(int index, DynVarList value){indexLookup.set(index, value);}
    public void set(int index, DynVarJson value){indexLookup.set(index, value);}

    public void rm(int index){indexLookup.remove(index);}
}
class AdaptiveList<T> {
    private ArrayList<T> list = new ArrayList<>();
    public void append(T item) {list.add(item);}
    public void remove(int index) {list.remove(index);}
    public void set(int index, T item) {list.set(index, item);}
    public T get(int index) {return list.get(index);}
}