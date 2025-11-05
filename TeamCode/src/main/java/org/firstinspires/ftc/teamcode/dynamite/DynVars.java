package org.firstinspires.ftc.teamcode.dynamite;

import android.annotation.SuppressLint;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

    private final AdaptiveList indexLookup = new AdaptiveList();

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

    public void rm(int index){indexLookup.remove(index);}
}
// json
class DynVarJson{
    public String name  = "";
    // this one too =)
}

// to be used here for now for list thingy of dyn variables
class AdaptiveList<T> {
    private ArrayList<T> list = new ArrayList<>();
    public void append(T item) {list.add(item);}
    public void remove(int index) {list.remove(index);}
    public void set(int index, T item) {list.set(index, item);}
    public T get(int index) {return list.get(index);}
}