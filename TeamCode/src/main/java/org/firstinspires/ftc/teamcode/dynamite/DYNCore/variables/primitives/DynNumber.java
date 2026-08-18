package org.firstinspires.ftc.teamcode.dynamite.DYNCore.variables.primitives;

import org.firstinspires.ftc.teamcode.dynamite.DYNCore.variables.Variable;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.variables.VariableTypes;
// I have no idea why I need to import this
import java.lang.String;

public class DynNumber extends Variable { // imma start calling the variable classes: cast-mageddons
    public DynNumber(int value){
        super(VariableTypes.Number, (double) value);
    }
    public DynNumber(long value){
        super(VariableTypes.Number, (double) value);
    }
    public DynNumber(float value){
        super(VariableTypes.Number, (double) value);
    }
    public DynNumber(double value){
        super(VariableTypes.Number, value);
    }

    public DynNumber(int value, String name){
        super(VariableTypes.Number, (double) value, name);
    }
    public DynNumber(long value, String name){
        super(VariableTypes.Number, (double) value, name);
    }
    public DynNumber(float value, String name){
        super(VariableTypes.Number, (double) value, name);
    }
    public DynNumber(double value, String name){
        super(VariableTypes.Number, value, name);
    }

    @Override
    public String getTelemetryData(){
        double d = (double)value;
        boolean isWhole = d == Math.floor(d) && !Double.isInfinite(d);
        return isWhole ? String.valueOf((long) d) : String.valueOf(d);
    }

    private void catchIncompatible(String method, Variable in1, Variable in2){
        if (in1.getType() != VariableTypes.Number || in2.getType() != VariableTypes.Number){
            throwErr(method, new Object[]{in1,in2}, "Cannot use variable types: "+in1.getType()+", "+in2.getType());
        }
    }
    private void catchIncompatible(String method, Variable in){
        if (in.getType() != VariableTypes.Number){
            throwErr(method, new Object[]{in, this}, "Cannot use variable type: "+ in.getType());
        }
    }

    // math ops
    @Override
    public void Add(Variable in1, Variable in2){
        catchIncompatible("Add",in1,in2);
        value = (double)in1.getValue() + (double)in2.getValue();
    }
    @Override
    public void Add(Variable in){
        catchIncompatible("Add", in);
        value = (double)value + (double)in.getValue(); // ok java
    }

    @Override
    public void Sub(Variable in1, Variable in2){
        catchIncompatible("Sub", in1, in2);
        value = (double)in1.getValue() - (double)in2.getValue();
    }
    @Override
    public void Sub(Variable in){
        catchIncompatible("Sub", in);
        value = (double)value - (double)in.getValue();
    }

    @Override
    public void Mux(Variable in1, Variable in2){
        catchIncompatible("Mux", in1, in2);
        value = (double)in1.getValue() * (double)in2.getValue();
    }
    @Override
    public void Mux(Variable in){
        catchIncompatible("Mux", in);
        value = (double)value * (double)in.getValue();
    }

    @Override
    public void Div(Variable in1, Variable in2){
        catchIncompatible("Div", in1, in2);
        if ((double)in2.getValue() == 0) throwErr("Div",new Object[]{in1,in2}, "Div by zero!");
        value = (double)in1.getValue() / (double)in2.getValue();
    }
    @Override
    public void Div(Variable in){
        catchIncompatible("Div", in);
        if ((double)in.getValue() == 0) throwErr("Div",new Object[]{in,this}, "Div by zero!");
        value = (double)value / (double)in.getValue();
    }

    @Override
    public void Pow(Variable in1, Variable in2){
        catchIncompatible("Pow", in1, in2);
        value = Math.pow((double)in1.getValue(), (double)in2.getValue());
    }
    @Override
    public void Pow(Variable in){
        catchIncompatible("Pow", in);
        value = Math.pow((double)value, (double)in.getValue());
    }

    @Override
    public void Sqrt(Variable in){
        catchIncompatible("Sqrt", in);
        if ((double)in.getValue() < 0) throwErr("Sqrt", new Object[]{in},"Square root of negative!");
        value = Math.sqrt((double)in.getValue());
    }
    @Override
    public void Sqrt(){
        if ((double)value < 0) throwErr("Sqrt", new Object[]{this},"Square root of negative!");
        value = Math.sqrt((double)value);
    }

    @Override
    public void Sin(Variable in){
        catchIncompatible("Sin", in);
        if (processInRad){
            value = Math.sin((double) in.getValue());
        } else {
            value = Math.sin(Math.toRadians((double)in.getValue()));
        }
    }
    @Override
    public void Sin(){
        if (processInRad) {
            value = Math.sin((double) value);
        } else {
            value = Math.sin(Math.toRadians((double)value));
        }
    }

    @Override
    public void iSin(Variable in){
        catchIncompatible("iSin", in);
        value = Math.asin((double) in.getValue());
        if (!processInRad) value = Math.toDegrees((double)value);
    }
    @Override
    public void iSin(){
        value = Math.asin((double) value);
        if (!processInRad) value = Math.toDegrees((double)value);
    }

    @Override
    public void Cos(Variable in){
        catchIncompatible("Cos", in);
        if (processInRad){
            value = Math.cos((double) in.getValue());
        } else {
            value = Math.cos(Math.toRadians((double)in.getValue()));
        }
    }
    @Override
    public void Cos(){
        if (processInRad) {
            value = Math.cos((double) value);
        } else {
            value = Math.cos(Math.toRadians((double)value));
        }
    }

    @Override
    public void iCos(Variable in){
        catchIncompatible("iCos", in);
        value = Math.acos((double) in.getValue());
        if (!processInRad) value = Math.toDegrees((double)value);
    }
    @Override
    public void iCos(){
        value = Math.acos((double) value);
        if (!processInRad) value = Math.toDegrees((double)value);
    }

    @Override
    public void Tan(Variable in){
        catchIncompatible("Tan", in);
        if (processInRad){
            value = Math.tan((double) in.getValue());
        } else {
            value = Math.tan(Math.toRadians((double)in.getValue()));
        }
    }
    @Override
    public void Tan(){
        if (processInRad) {
            value = Math.tan((double) value);
        } else {
            value = Math.tan(Math.toRadians((double)value));
        }
    }

    @Override
    public void iTan(Variable in){
        catchIncompatible("iTan", in);
        value = Math.atan((double) in.getValue());
        if (!processInRad) value = Math.toDegrees((double)value);
    }
    @Override
    public void iTan(){
        value = Math.atan((double) value);
        if (!processInRad) value = Math.toDegrees((double)value);
    }

    @Override
    public void toDeg(Variable in){
        catchIncompatible("toDeg", in);
        value = Math.toDegrees((double)in.getValue());
    }
    @Override
    public void toDeg(){
        value = Math.toDegrees((double)value);
    }

    @Override
    public void toRad(Variable in){
        catchIncompatible("toRad", in);
        value = Math.toRadians((double)in.getValue());
    }
    @Override
    public void toRad(){
        value = Math.toRadians((double)value);
    }

    @Override
    public void Inc(){
        value = (double)value + 1;
    }
    @Override
    public void Dec(){
        value = (double)value - 1;
    }
    // logic ops
    @Override
    public boolean equals(Variable in){
        switch (in.getType()){ // after some thinking, these are the only variables types that could equal a number.
            case String -> {
                // integer check
                if (String.valueOf(value).endsWith(".0")){
                    String thisValue = String.valueOf(value).replace(".0","");
                    return String.valueOf(in.getValue()).equals(thisValue);
                } else {
                    return String.valueOf(in.getValue()).equals(value);
                }
            }
            case Number -> {
                return ((double)in.getValue() == (double)value);
            }
        }
        return false;
    }

    @Override
    public boolean lessThan(Variable in){
        catchIncompatible("Less Than", in);
        return ((double)value < (double)in.getValue());
    }
    @Override
    public boolean moreThan(Variable in){
        catchIncompatible("More Than", in);
        return ((double)value > (double)in.getValue());
    }

    @Override
    public boolean moreThanEq(Variable in){
        catchIncompatible("More Than Equals", in);
        return ((double)value >= (double)in.getValue());
    }
    @Override
    public boolean lessThanEq(Variable in){
        catchIncompatible("Less Than Equals", in);
        return ((double)value <= (double)in.getValue());
    }
    // from list/json ops
    // reference comment below
    @Override
    public void set2get(Variable in, int index){
        if (in.getType() == VariableTypes.List){
            setVariable(in.getFromIndex(index));
        } else {
            throwErr("set2get", new Object[]{in,index}, "Given list is not a list, it is: "+in.getType());
        }
    }
    @Override
    public void set2get(Variable in, Variable id){
        if (in.getType() == VariableTypes.Json){
            setVariable(in.getFromID(id));
        } else if (in.getType() == VariableTypes.List){
            if (id.getType() == VariableTypes.Number){
                set2get(in,(int)Math.floor((double)id.getValue()));
            } else {
                throwErr("set2get", new Object[]{in,id}, "Cannot use non numbers as list indices!");
            }
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
