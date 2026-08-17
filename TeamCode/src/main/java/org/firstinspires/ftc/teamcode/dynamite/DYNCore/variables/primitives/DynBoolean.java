package org.firstinspires.ftc.teamcode.dynamite.DYNCore.variables.primitives;

import org.firstinspires.ftc.teamcode.dynamite.DYNCore.commands.controlFlow.Condition;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.variables.Variable;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.variables.VariableTypes;

public class DynBoolean extends Variable {
    private Condition con = null;

    public DynBoolean(boolean state){
        super(VariableTypes.Boolean, state);
    }
    public DynBoolean(boolean state, String name){
        super(VariableTypes.Boolean,state,name);
    }
    public DynBoolean(Condition state, String name){
        super(VariableTypes.Boolean, state, name);
        con = state;
    }
    public DynBoolean(Condition state){
        super(VariableTypes.Boolean, state);
        con = state;
    }

    @Override
    public String getTelemetryData(){
        return String.valueOf(getValue());
    }

    private void catchIncompatible(Variable in, String operation){
        if (in.getType() != VariableTypes.Boolean){
            throwErr(operation,new Object[]{in}, "Cannot use variable type: "+in.getType());
        }
    }

    private boolean getState(){
        if (con == null) return (boolean)value;
        else return con.getResult();
    }
    public Object getValue(){
        if (con == null) return super.getValue();
        else return con.getResult();
    }

    // logic Ops
    @Override
    public boolean equals(Variable in){
        switch (in.getType()){
            case Boolean -> {
                return ((boolean)in.getValue() == getState());
            }
            case String -> {
                return String.valueOf(in.getValue()).equals(String.valueOf(getState()));
            }
        }
        return false;
    }
    @Override
    public boolean and(Variable in){
        catchIncompatible(in, "And");
        return ((boolean)in.getValue() && getState());
    }
    @Override
    public boolean or(Variable in){
        catchIncompatible(in, "Or");
        return ((boolean)in.getValue() || getState());
    }
    @Override
    public boolean not(){
        return !getState();
    }
    // json/list shenanigans
    @Override
    public void set2get(Variable in, int index){
        if (in.getType() == VariableTypes.Json){
            setVariable(in.getFromID(in));
        } else {
            throwErr("set2get", new Object[]{in}, "Given Json is not a Json, it is: "+in.getType());
        }
    }
    @Override
    public void set2get(Variable in, Variable id){
        if (in.getType() == VariableTypes.Json){
            setVariable(in.getFromID(in));
        } else {
            throwErr("set2get", new Object[]{in,id}, "Given Json is not a Json, it is: "+in.getType());
        }
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
}
