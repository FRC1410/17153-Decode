package org.firstinspires.ftc.teamcode.dynamite.commandSequencer.variables.complex;

import org.SquidSquad.commandSequencer.variables.Variable;
import org.SquidSquad.commandSequencer.variables.VariableTypes;

import java.util.ArrayList;

public class DynList extends Variable {
    public DynList(ArrayList<Variable> leList){
        super(VariableTypes.List, leList);
    }
    public DynList(ArrayList<Variable> leList, String name){
        super(VariableTypes.List, leList, name);
    }

    @Override
    public String getTelemetryData(){
        StringBuilder out = new StringBuilder("[");
        ArrayList<Variable> thisList = (ArrayList<Variable>)value;
        for (int i = 0; i < thisList.size(); i++){
            Variable val = thisList.get(i);
            out.append(val.getTelemetryData());
            if (i < thisList.size()-1) {
                out.append(", ");
            }
        }
        out.append("]");
        return out.toString();
    }

    // logic Ops
    @Override
    public boolean equals(Variable in){
        if (in.getType() == VariableTypes.List){
            ArrayList<Variable> inList = (ArrayList<Variable>)in.getValue();
            ArrayList<Variable> thisList = (ArrayList<Variable>)value;
            if (inList.size() == thisList.size()){
                for (int i = 0; i < inList.size(); i++){
                    if (!inList.get(i).equals(thisList.get(i))){
                        return false; // this is when I reject "never nester" mentality.
                    }
                }
                return true;
            }
        }
        return false;
    }
    // json/list shenanigans
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
    public void set(int index, Variable in){
        ((ArrayList<Variable>)value).set(index, in);
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
    public Variable getFromIndex(int index){
        ArrayList<Variable> thisList = (ArrayList<Variable>)value;
        if (index < thisList.size() && index >= 0){
            return thisList.get(index);
        }
        throwErr("getFormIndex", new Object[]{index}, "Out of given list boundries index: "+index);
        return null;
    }
    @Override
    public void insertVar(Variable in, int index){
        ((ArrayList<Variable>)value).add(index,in);
    }
    @Override
    public void insertVar(Variable in, Variable index){
        if (index.getType() == VariableTypes.Number){
            int idx = (int)Math.floor((double)index.getValue());
            insertVar(in,idx);
        } else {
            throwErr("insert",new Object[]{in,index},"Cannot use non number as index!");
        }
    }
    @Override
    public void append(Variable in){
        ((ArrayList<Variable>)value).add(in);
    }
    @Override
    public void remove(int index){
        ((ArrayList<Variable>)value).remove(index);
    }
    @Override
    public void remove(Variable index){
        if (index.getType() == VariableTypes.Number){
            int idx = (int)Math.floor((double)index.getValue());
            remove(idx);
        } else {
            super.remove(index);
        }
    }
}
