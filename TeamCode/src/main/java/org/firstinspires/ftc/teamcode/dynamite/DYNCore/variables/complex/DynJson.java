package org.firstinspires.ftc.teamcode.dynamite.DYNCore.variables.complex;

import org.firstinspires.ftc.teamcode.dynamite.DYNCore.variables.Variable;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.variables.VariableTypes;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.variables.primitives.DynNumber;

import java.util.Map;

public class DynJson extends Variable {
    public DynJson(Map<Variable,Variable> leJson){
        super(VariableTypes.Json, leJson);
    }
    public DynJson(Map<Variable,Variable> leJson, String name){
        super(VariableTypes.Json, leJson, name);
    }

    @Override
    public String getTelemetryData(){
        StringBuilder out = new StringBuilder("{");
        Map<Variable,Variable> map = (Map<Variable, Variable>)value;
        Variable[] keySet = map.keySet().toArray(new Variable[0]);
        for (int i = 0; i < keySet.length; i++){
            out.append(keySet[i].getTelemetryData());
            out.append(": ");
            out.append(map.get(keySet[i]).getTelemetryData());
            if (i < keySet.length-1){
                out.append(", ");
            }
        }
        out.append("}");
        return out.toString();
    }
    // logic Ops
    @Override
    public boolean equals(Variable in){
        if (in.getType() != VariableTypes.Json) return false;
        // key check
        Map<Variable,Variable> inMap = (Map<Variable,Variable>)in;
        Map<Variable,Variable> map = (Map<Variable, Variable>)value;
        for (Variable key : map.keySet()){
            boolean cont = false;
            for (Variable key2 : inMap.keySet()){
                if (key.equals(key2)){
                    cont = true;
                }
            }
            if (!cont){
                return false;
            }
        }
        // value check
        for (Variable key : map.keySet()){
            if (!inMap.get(key).equals(map.get(key))){
                return false;
            }
        }
        return true;
    }
    // json/list shenanigans
    @Override
    public void append(Variable in, Variable id){
        ((Map<Variable, Variable>)value).put(id,in);
    }
    @Override
    public void remove(Variable id){
        ((Map<Variable, Variable>)value).remove(id);
    }
    @Override
    public void set(Variable id, Variable in){
        ((Map<Variable, Variable>)value).put(id,in);
    }
    @Override
    public void set(int index, Variable in){
        set(new DynNumber(index),in);
    }
    @Override
    public Variable getFromID(Variable id){
        Map<Variable,Variable> map = (Map<Variable, Variable>)value;
        for (Variable key : map.keySet()){
            if (key.equals(id)){
                return map.get(key);
            }
        }
        throwErr("getFromID", new Object[]{id}, "This Json does not contain given key!");
        return null;
    }
    @Override
    public void setToRemove(Variable list, int index){
        setVariable(list.getFromIndex(index).getClone());
        list.remove(index);
    }
    @Override
    public void setToRemove(Variable json, Variable id){
        setVariable(json.getFromID(id).getClone());
        json.remove(id);
    }
    @Override
    public void set2get(Variable in, int index){
        if (in.getType() == VariableTypes.Json){
            setVariable(in.getFromID(in).getClone());
        } else {
            throwErr("set2get", new Object[]{in}, "Given Json is not a Json, it is: "+in.getType());
        }
    }
    @Override
    public void set2get(Variable in, Variable id){
        if (in.getType() == VariableTypes.Json){
            setVariable(in.getFromID(in).getClone());
        } else {
            throwErr("set2get", new Object[]{in,id}, "Given Json is not a Json, it is: "+in.getType());
        }
    }
    @Override
    public void insertVar(Variable in, int index){
        insertVar(in, new DynNumber(index));
    }
    @Override
    public void insertVar(Variable in, Variable id){
        set(id,in);
    }
}
