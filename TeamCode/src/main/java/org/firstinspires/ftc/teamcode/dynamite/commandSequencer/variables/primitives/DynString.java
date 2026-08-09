package org.firstinspires.ftc.teamcode.dynamite.commandSequencer.variables.primitives;

import org.SquidSquad.commandSequencer.variables.Variable;
import org.SquidSquad.commandSequencer.variables.VariableTypes;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

public class DynString extends Variable {
    public DynString(String value){
        super(VariableTypes.String, value);
    }
    public DynString(String value, String name){
        super(VariableTypes.String, value, name);
    }

    @Override
    public String getTelemetryData(){
        return String.valueOf(value);
    }

    // logic Ops
    @Override
    public boolean equals(Variable in) {
        switch (in.getType()){
            case String -> {
                return String.valueOf(in.getValue()).equals(String.valueOf(value));
            }
            case FieldCord -> {
                // previously called ini, but that sounded like annie
                Variable[] inVal = (Variable[]) in.getValue();
                double x = (double) inVal[0].getValue();
                double y = (double) inVal[1].getValue();
                String cord = "("+x+", "+y+")";
                return (cord.equals(String.valueOf(value)));
            }
            case FieldPos -> {
                Variable[] inVal = (Variable[]) in.getValue();
                double x = (double) inVal[0].getValue();
                double y = (double) inVal[1].getValue();
                double h = (double) inVal[2].getValue();
                String cord = "("+x+", "+y+", "+h+")";
                return (cord.equals(String.valueOf(value)));
            }
            case Boolean -> {
                boolean inVal = (boolean) in.getValue();
                return String.valueOf(inVal).equals(String.valueOf(value));
            }
            case Number -> {
                double inVal = (double) in.getValue();
                String num = String.valueOf(inVal);
                if (num.endsWith(".0")) num = num.replace(".0","");
                return (num.equals(String.valueOf(value)));
            }
            case Json -> {
                Map<Variable,Variable> inVal = (Map<Variable, Variable>)in.getValue();
                Set<Variable> inKeys = inVal.keySet();
                StringBuilder json = new StringBuilder("{");
                int i = 0;
                for (Variable key : inKeys){
                    json.append(key.valueToString());
                    json.append(":");
                    json.append(inVal.get(key).valueToString());
                    if (i != inKeys.size()-1) json.append(", ");
                    i++;
                }
                json.append("}");
                String inString = json.toString();
                return (inString.equals(String.valueOf(value)));
            }
            case List -> {
                ArrayList<Variable> inVal = (ArrayList<Variable>) in.getValue();
                StringBuilder list = new StringBuilder("[");
                int i = 0;
                for (Variable val : inVal){
                    list.append(val.valueToString());
                    if (i != inVal.size()-1) list.append(", ");
                    i++;
                }
                list.append("]");
                String inString = list.toString();
                return (inString.equals(String.valueOf(value)));
            }
        }
        return false;
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
            setVariable(in.getFromID(id));
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
    @Override
    public void set(int index, Variable in){
        if (index == ((String)value).length()) {
            throwErr("set", new Object[]{index}, "Given index out of range!");
        }
        String newVal = in.valueToString();
        String thisVal = (String)value;
        String start = thisVal.substring(0,index);
        String end = thisVal.substring(index+1);
        value = start+newVal+end;
    }
}
