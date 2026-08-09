package org.firstinspires.ftc.teamcode.dynamite.commandSequencer.variables.complex;

import org.SquidSquad.commandSequencer.variables.Variable;
import org.SquidSquad.commandSequencer.variables.VariableTypes;

public class DynFieldPos extends Variable {
    public DynFieldPos(Variable[] pose){
        super(VariableTypes.FieldPos,pose);
    }
    public DynFieldPos(Variable[] pose, String name){
        super(VariableTypes.FieldPos,pose,name);
    }
    public DynFieldPos(Variable x, Variable y, Variable h){
        super(VariableTypes.FieldPos,new Variable[]{x,y,h});
    }
    public DynFieldPos(Variable x, Variable y, Variable h, String name){
        super(VariableTypes.FieldPos,new Variable[]{x,y,h},name);
    }
    public DynFieldPos(double[] pose){
        super(VariableTypes.FieldPos,null);
        if (pose.length == 3){
            value = new Variable[]{
                new Variable(VariableTypes.Number, pose[0]),
                new Variable(VariableTypes.Number, pose[1]),
                new Variable(VariableTypes.Number, pose[2])};
        } else {
            StringBuilder givenPose = new StringBuilder("[");
            for (int i = 0; i < pose.length; i++){
                givenPose.append(pose[i]);
                if (i != pose.length-1) givenPose.append(", ");
            }
            givenPose.append("]");
            String givenPoseStr = givenPose.toString();
            throwErr("FieldPos Variable declaration", new Object[]{givenPoseStr}, "given raw pose length is not 3, it is: "+pose.length);
        }
    }
    public DynFieldPos(double[] pose, String name){
        super(VariableTypes.FieldPos,null, name);
        if (pose.length == 3){
            value = new Variable[]{
                new Variable(VariableTypes.Number, pose[0]),
                new Variable(VariableTypes.Number, pose[1]),
                new Variable(VariableTypes.Number, pose[2])};
        } else {
            StringBuilder givenPose = new StringBuilder("[");
            for (int i = 0; i < pose.length; i++){
                givenPose.append(pose[i]);
                if (i != pose.length-1) givenPose.append(", ");
            }
            givenPose.append("]");
            String givenPoseStr = givenPose.toString();
            throwErr("FieldPos Variable declaration", new Object[]{givenPoseStr}, "given raw pose length is not 3, it is: "+pose.length);
        }
    }
    public DynFieldPos(double x, double y, double h){
        super(VariableTypes.FieldPos,new Variable[]{
                new Variable(VariableTypes.Number,x),
                new Variable(VariableTypes.Number,y),
                new Variable(VariableTypes.Number,h)
        });
    }
    public DynFieldPos(double x, double y, double h, String name){
        super(VariableTypes.FieldPos,new Variable[]{
                new Variable(VariableTypes.Number,x),
                new Variable(VariableTypes.Number,y),
                new Variable(VariableTypes.Number,h)
        },name);
    }

    @Override
    public String getTelemetryData(){
        Variable[] dimensions = (Variable[])value;
        String X = dimensions[0].getTelemetryData();
        String Y = dimensions[1].getTelemetryData();
        String H = dimensions[2].getTelemetryData();
        return "(X:"+X+", Y:"+Y+", H:"+H+")";
    }

    private void catchIncompatible(String method, Variable in) {
        if (!(in.getType() == VariableTypes.Number || in.getType() == VariableTypes.FieldCord)) {
            throwErr(method, new Object[]{in}, "Cannot use Variable type: " + in.getType());
        }
    }

    // math ops
    @Override
    public void Add(Variable in){
        catchIncompatible("Add", in);
        ((Variable[])value)[0].Add(in);
        ((Variable[])value)[1].Add(in);
        ((Variable[])value)[2].Add(in);
    }
    @Override
    public void Sub(Variable in){
        catchIncompatible("Sub", in);
        ((Variable[])value)[0].Sub(in);
        ((Variable[])value)[1].Sub(in);
        ((Variable[])value)[2].Sub(in);
    }
    @Override
    public void Mux(Variable in){
        catchIncompatible("Mux", in);
        ((Variable[])value)[0].Mux(in);
        ((Variable[])value)[1].Mux(in);
        ((Variable[])value)[2].Mux(in);
    }
    @Override
    public void Div(Variable in){
        catchIncompatible("Div", in);
        ((Variable[])value)[0].Div(in);
        ((Variable[])value)[1].Div(in);
        ((Variable[])value)[2].Div(in);
    }
    @Override
    public void Pow(Variable in){
        catchIncompatible("Pow", in);
        ((Variable[])value)[0].Pow(in);
        ((Variable[])value)[1].Pow(in);
        ((Variable[])value)[2].Pow(in);
    }

    @Override
    public void Sqrt(Variable in){
        catchIncompatible("Sqrt", in);
        ((Variable[])value)[0].Sqrt(in);
        ((Variable[])value)[1].Sqrt(in);
        ((Variable[])value)[2].Sqrt(in);
    }
    @Override
    public void Sqrt(){
        ((Variable[])value)[0].Sqrt();
        ((Variable[])value)[1].Sqrt();
        ((Variable[])value)[2].Sqrt();
    }

    @Override
    public void Sin(Variable in){
        catchIncompatible("Sin", in);
        ((Variable[])value)[0].Sin(in);
        ((Variable[])value)[1].Sin(in);
        ((Variable[])value)[2].Sin(in);
    }
    @Override
    public void Sin(){
        ((Variable[])value)[0].Sin();
        ((Variable[])value)[1].Sin();
        ((Variable[])value)[2].Sin();
    }

    @Override
    public void iSin(Variable in){
        catchIncompatible("iSin", in);
        ((Variable[])value)[0].iSin(in);
        ((Variable[])value)[1].iSin(in);
        ((Variable[])value)[2].iSin(in);
    }
    @Override
    public void iSin(){
        ((Variable[])value)[0].iSin();
        ((Variable[])value)[1].iSin();
        ((Variable[])value)[2].iSin();
    }

    @Override
    public void Cos(Variable in){
        catchIncompatible("Cos", in);
        ((Variable[])value)[0].Cos(in);
        ((Variable[])value)[1].Cos(in);
        ((Variable[])value)[2].Cos(in);
    }
    @Override
    public void Cos(){
        ((Variable[])value)[0].Cos();
        ((Variable[])value)[1].Cos();
        ((Variable[])value)[2].Cos();
    }

    @Override
    public void iCos(Variable in){
        catchIncompatible("iCos", in);
        ((Variable[])value)[0].iCos(in);
        ((Variable[])value)[1].iCos(in);
        ((Variable[])value)[2].iCos(in);
    }
    @Override
    public void iCos(){
        ((Variable[])value)[0].iCos();
        ((Variable[])value)[1].iCos();
        ((Variable[])value)[2].iCos();
    }

    @Override
    public void Tan(Variable in){
        catchIncompatible("Tan", in);
        ((Variable[])value)[0].Tan(in);
        ((Variable[])value)[1].Tan(in);
        ((Variable[])value)[2].Tan(in);
    }
    @Override
    public void Tan(){
        ((Variable[])value)[0].Tan();
        ((Variable[])value)[1].Tan();
        ((Variable[])value)[2].Tan();
    }

    @Override
    public void iTan(Variable in){
        catchIncompatible("iTan", in);
        ((Variable[])value)[0].iTan(in);
        ((Variable[])value)[1].iTan(in);
        ((Variable[])value)[2].iTan(in);
    }
    @Override
    public void iTan(){
        ((Variable[])value)[0].iTan();
        ((Variable[])value)[1].iTan();
        ((Variable[])value)[2].iTan();
    }

    @Override
    public void toDeg(Variable in){
        catchIncompatible("toDeg", in);
        ((Variable[])value)[0].toDeg(in);
        ((Variable[])value)[1].toDeg(in);
        ((Variable[])value)[2].toDeg(in);
    }
    @Override
    public void toDeg(){
        ((Variable[])value)[0].Sqrt();
        ((Variable[])value)[1].Sqrt();
        ((Variable[])value)[2].Sqrt();
    }

    @Override
    public void toRad(Variable in){
        catchIncompatible("toRad", in);
        ((Variable[])value)[0].toRad(in);
        ((Variable[])value)[1].toRad(in);
        ((Variable[])value)[2].toRad(in);
    }
    @Override
    public void toRad(){
        ((Variable[])value)[0].toRad();
        ((Variable[])value)[1].toRad();
        ((Variable[])value)[2].toRad();
    }

    @Override
    public void Inc(){
        ((Variable[])value)[0].Inc();
        ((Variable[])value)[1].Inc();
        ((Variable[])value)[2].Inc();
    }
    @Override
    public void Dec(){
        ((Variable[])value)[0].Dec();
        ((Variable[])value)[1].Dec();
        ((Variable[])value)[2].Dec();
    }
    // logic Ops
    @Override
    public boolean equals(Variable in){
        switch (in.getType()) {
            case String -> {
                String inVal = String.valueOf(in.getValue());
                Variable[] thisPos = (Variable[]) value;
                String thisVal = "(" + thisPos[0].getValue() + ", " + thisPos[1].getValue() + ")";
                return (inVal.equals(thisVal));
            }
            case Number -> {
                Variable[] thisPos = (Variable[]) value;
                double thisDist = Math.sqrt(Math.pow((double) thisPos[1].getValue(), 2) + Math.pow((double) thisPos[1].getValue(), 2));
                double givenVal = (double) in.getValue();
                return (thisDist == givenVal);
            }
            case FieldCord -> {
                Variable[] thisPos = (Variable[]) value;
                Variable[] givenPos = (Variable[]) in.getValue();
                double[] thisPosVal = {
                        (double) thisPos[0].getValue(),
                        (double) thisPos[1].getValue()};
                double[] givenPosVal = {
                        (double) givenPos[0].getValue(),
                        (double) givenPos[1].getValue()};
                return ((thisPosVal[0] == givenPosVal[0]) && (thisPosVal[1] == givenPosVal[1]));
            }
            case FieldPos -> {
                Variable[] thisPos = (Variable[])value;
                Variable[] givenPos = (Variable[])in.getValue();
                double[] thisPosVal = {
                        (double)thisPos[0].getValue(),
                        (double)thisPos[2].getValue(),
                        (double)thisPos[1].getValue()};
                double[] givenPosVal = {
                        (double)givenPos[0].getValue(),
                        (double)givenPos[2].getValue(),
                        (double)givenPos[1].getValue()};
                return ((thisPosVal[0] == givenPosVal[0]) && (thisPosVal[1] == givenPosVal[1]) && (thisPosVal[2] == givenPosVal[2]));
            }
        }
        return false;
    }
    // json/list shenanigans
    @Override
    public void set2get(Variable in, int index){
        if (in.getType() == VariableTypes.Json){
            setVariable(in.getFromIndex(index).getClone());
        } else {
            throwErr("set2get", new Object[]{in}, "Given Json is not a Json, it is: "+in.getType());
        }
    }
    @Override
    public void set2get(Variable in, Variable id){
        if (in.getType() == VariableTypes.Json){
            setVariable(in.getFromID(id).getClone());
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
        switch (index){
            case 1 -> ((Variable[])value)[0].setVariable(in);
            case 2 -> ((Variable[])value)[1].setVariable(in);
            case 3 -> ((Variable[])value)[2].setVariable(in);
        }
        throwErr("set", new Object[]{in}, "Field Pos only accepts index 1, 2, and 3 as valid. was given: "+index);
    }
    @Override
    public void set(Variable id, Variable in){
        switch (id.valueToString()){
            case "X","x" -> ((Variable[])value)[0].setVariable(in);
            case "Y","y" -> ((Variable[])value)[1].setVariable(in);
            case "H","h" -> ((Variable[])value)[2].setVariable(in);
        }
        throwErr("set", new Object[]{in}, "Field Pos only accepts ids X, Y, and H as valid.");
    }

    @Override
    public Variable getFromID(Variable id){
        switch (String.valueOf(id.getValue())){
            case "X","x" -> {
                return ((Variable[])value)[0];
            }
            case "Y","y" -> {
                return ((Variable[])value)[1];
            }
            case "H","h" -> {
                return ((Variable[])value)[2];
            }
        }
        throwErr("getFromID", new Object[]{id}, "Field only accept X,Y, or H as valid ID, was given: "+id.getValue());
        return null;
    }
    @Override
    public Variable getFromIndex(int index){
        switch (index){
            case 0 -> {
                return ((Variable[])value)[0];
            }
            case 1 -> {
                return ((Variable[])value)[1];
            }
            case 2 -> {
                return ((Variable[])value)[2];
            }
        }
        throwErr("getFromIndex", new Object[]{}, "Field only accept 0,1 or 2 as valid indices, was given: "+index);
        return null;
    }
}
