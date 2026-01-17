package org.firstinspires.ftc.teamcode.dynamite.DynVar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DynVarBuffer {
    private Map<String, DynVar> varBuffer = new HashMap<>();

    public DynVarBuffer(Map<String, DynVar> varBuffer){
        this.varBuffer = varBuffer;
    }
    public DynVarBuffer(String[] IDs){
        for (int i = 0; i < IDs.length; i++){
            varBuffer.put(IDs[i], new DynVar(IDs[i]));
        }
    }
    public DynVarBuffer(ArrayList<String> IDs){
        for (int i = 1; i < IDs.toArray().length; i++){
            varBuffer.put(IDs.get(i-1), new DynVar(IDs.get(i-1)));
        }
    }

    public DynVar getVar(String ID){
        return varBuffer.get(ID); // I know this works
    }
    public void setVar(String ID, DynVar Value){
        varBuffer.put(ID, Value); // I think this works (maybe)
    }
}
