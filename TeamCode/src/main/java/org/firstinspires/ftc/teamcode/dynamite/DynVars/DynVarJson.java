package org.firstinspires.ftc.teamcode.dynamite.DynVars;

import java.util.HashMap;
import java.util.Map;

public class DynVarJson {
    public String name  = "";
    // the json object
    private final Map<String, Object> data = new HashMap<>();
    // instantiators
    public DynVarJson(String name){this.name = name;}
    // add key with value to object
    public void add(String key, DynVarFieldCords value){data.put(key, value);}
    public void add(String key, DynVarFieldPos value){data.put(key, value);}
    public void add(String key, DynVarNumber value){data.put(key, value);}
    public void add(String key, DynVarBoolean value){data.put(key, value);}
    public void add(String key, DynVarString value){data.put(key, value);}
    public void add(String key, DynVarList value){data.put(key, value);}
    public void add(String key, DynVarJson value){data.put(key, value);}
    // set key to value in json
    public void set(String key, DynVarFieldCords value){data.put(key, value);}
    public void set(String key, DynVarFieldPos value){data.put(key, value);}
    public void set(String key, DynVarNumber value){data.put(key, value);}
    public void set(String key, DynVarBoolean value){data.put(key, value);}
    public void set(String key, DynVarString value){data.put(key, value);}
    public void set(String key, DynVarList value){data.put(key, value);}
    public void set(String key, DynVarJson value){data.put(key, value);}
    // get object of key
    public <T> T get(String key){return (T) data.get(key);}
    // remove key and its object
    public void remove(String key){data.remove(key);}
}
