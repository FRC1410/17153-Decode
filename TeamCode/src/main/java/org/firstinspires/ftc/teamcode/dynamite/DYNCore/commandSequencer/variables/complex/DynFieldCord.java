package org.firstinspires.ftc.teamcode.dynamite.DYNCore.commandSequencer.variables.complex;

import org.firstinspires.ftc.teamcode.dynamite.DYNCore.commandSequencer.variables.Variable;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.commandSequencer.variables.VariableException;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.commandSequencer.variables.VariableTypes;

public class DynFieldCord extends Variable {
    public DynFieldCord(Variable[] coord) {
        super(VariableTypes.FieldCord, coord);
        for (Variable var : coord){
            VariableTypes varType = var.getType();
            if (varType != VariableTypes.Number){
                throwErr("VarInit",coord,"Cannot use non Number as part of a coordinate!");
            }
        }
    }

    public DynFieldCord(Variable x, Variable y) {
        super(VariableTypes.FieldCord, new Variable[]{x, y});
        Variable[] coord = {x,y};
        for (Variable var : coord){
            VariableTypes varType = var.getType();
            if (varType != VariableTypes.Number){
                throwErr("VarInit",coord,"Cannot use non Number as part of a coordinate!");
            }
        }
    }

    public DynFieldCord(Variable[] coord, String name) {
        super(VariableTypes.FieldCord, coord, name);
        for (Variable var : coord){
            VariableTypes varType = var.getType();
            if (varType != VariableTypes.Number){
                throwErr("VarInit",coord,"Cannot use non Number as part of a coordinate!");
            }
        }
    }

    public DynFieldCord(Variable x, Variable y, String name) {
        super(VariableTypes.FieldCord, new Variable[]{x, y}, name);
        Variable[] coord = {x,y};
        for (Variable var : coord){
            VariableTypes varType = var.getType();
            if (varType != VariableTypes.Number){
                throwErr("VarInit",coord,"Cannot use non Number as part of a coordinate!");
            }
        }
    }

    public DynFieldCord(double x, double y) {
        super(VariableTypes.FieldCord, new Variable[]{new Variable(VariableTypes.Number, x), new Variable(VariableTypes.Number, y)});
        registerVar(((Variable[])value)[0]);
        registerVar(((Variable[])value)[1]);
    }

    public DynFieldCord(double x, double y, String name) {
        super(VariableTypes.FieldCord, new Variable[]{new Variable(VariableTypes.Number, x), new Variable(VariableTypes.Number, y)}, name);
        registerVar(((Variable[])value)[0]);
        registerVar(((Variable[])value)[1]);
    }
    public DynFieldCord(double[] coord){
        super(VariableTypes.FieldCord, coord);
        if (coord.length == 2){
            value = new Variable[]{
                    new Variable(VariableTypes.Number, coord[0]),
                    new Variable(VariableTypes.Number, coord[1])};
            registerVar(((Variable[])value)[0]);
            registerVar(((Variable[])value)[1]);
        } else {
            Double[] stupid = new Double[coord.length];
            for (int i = 0; i < coord.length; i++) stupid[i] = coord[i];
            throwErr("dynFieldCord", stupid, "given coord not proper length of 2, instead: "+coord.length);
        }
    }
    public DynFieldCord(double[] coord, String name){
        super(VariableTypes.FieldCord, coord, name);
        if (coord.length == 2){
            value = new Variable[]{
                    new Variable(VariableTypes.Number, coord[0]),
                    new Variable(VariableTypes.Number, coord[1])};
            registerVar(((Variable[])value)[0]);
            registerVar(((Variable[])value)[1]);
        } else {
            Double[] stupid = new Double[coord.length];
            for (int i = 0; i < coord.length; i++) stupid[i] = coord[i];
            throwErr("dynFieldCord", stupid, "given coord not proper length of 2, instead: "+coord.length);
        }
    }

    @Override
    public String getTelemetryData(){
        Variable[] dimensions = (Variable[])value;
        String X = dimensions[0].getTelemetryData();
        String Y = dimensions[1].getTelemetryData();
        return "(X:"+X+", Y:"+Y+")";
    }

    private void catchIncompatible(String method, Variable in1, Variable in2) {
        if (!(in1.getType() == VariableTypes.FieldCord && in2.getType() == VariableTypes.FieldCord)) {
            throwErr(method, new Object[]{in1, in2}, "Cannot use Variable types: " + in1.getType() + ", " + in2.getType());
        }
    }

    private void catchIncompatible(String method, Variable in) {
        if (!(in.getType() == VariableTypes.Number || in.getType() == VariableTypes.FieldCord)) {
            throwErr(method, new Object[]{in}, "Cannot use Variable type: " + in.getType());
        }
    }

    // math ops
    @Override
    public void Add(Variable in1, Variable in2) {
        catchIncompatible("Add", in1, in2);
        // take x/y values from both, add then, and then set this variable to the result
        Variable[] val1 = (Variable[]) in1.getValue();
        Variable[] val2 = (Variable[]) in2.getValue();

        ((Variable[]) value)[0].Add(val1[0], val2[0]);
        ((Variable[]) value)[1].Add(val1[1], val2[1]);
    }

    @Override
    public void Add(Variable in) {
        catchIncompatible("Add", in);
        switch (in.getType()) {
            case Number -> {
                ((Variable[]) value)[0].Add(in);
                ((Variable[]) value)[1].Add(in);
            }
            case FieldCord -> {
                ((Variable[]) value)[0].Add(((Variable[]) in.getValue())[0]);
                ((Variable[]) value)[1].Add(((Variable[]) in.getValue())[1]);
            }
        }
    }

    @Override
    public void Sub(Variable in1, Variable in2) {
        catchIncompatible("Sub", in1, in2);
        Variable[] val1 = (Variable[]) in1.getValue();
        Variable[] val2 = (Variable[]) in2.getValue();

        ((Variable[]) value)[0].Sub(val1[0], val2[0]);
        ((Variable[]) value)[1].Sub(val1[1], val2[1]);
    }

    @Override
    public void Sub(Variable in) {
        catchIncompatible("Sub", in);
        switch (in.getType()) {
            case Number -> {
                ((Variable[]) value)[0].Sub(in);
                ((Variable[]) value)[1].Sub(in);
            }
            case FieldCord -> {
                ((Variable[]) value)[0].Sub(((Variable[]) in.getValue())[0]);
                ((Variable[]) value)[1].Sub(((Variable[]) in.getValue())[1]);
            }
        }
    }

    @Override
    public void Mux(Variable in1, Variable in2) {
        catchIncompatible("Mux", in1, in2);
        Variable[] val1 = (Variable[]) in1.getValue();
        Variable[] val2 = (Variable[]) in2.getValue();

        ((Variable[]) value)[0].Mux(val1[0], val2[0]);
        ((Variable[]) value)[1].Mux(val1[1], val2[1]);
    }

    @Override
    public void Mux(Variable in) {
        catchIncompatible("Mux", in);
        switch (in.getType()) {
            case Number -> {
                ((Variable[]) value)[0].Mux(in);
                ((Variable[]) value)[1].Mux(in);
            }
            case FieldCord -> {
                ((Variable[]) value)[0].Mux(((Variable[]) in.getValue())[0]);
                ((Variable[]) value)[1].Mux(((Variable[]) in.getValue())[1]);
            }
        }
    }

    @Override
    public void Div(Variable in1, Variable in2) {
        catchIncompatible("Div", in1, in2);
        Variable[] val1 = (Variable[]) in1.getValue();
        Variable[] val2 = (Variable[]) in2.getValue();

        ((Variable[]) value)[0].Div(val1[0], val2[0]);
        ((Variable[]) value)[1].Div(val1[1], val2[1]);
    }

    @Override
    public void Div(Variable in) {
        catchIncompatible("Div", in);
        switch (in.getType()) {
            case Number -> {
                ((Variable[]) value)[0].Div(in);
                ((Variable[]) value)[1].Div(in);
            }
            case FieldCord -> {
                ((Variable[]) value)[0].Div(((Variable[]) in.getValue())[0]);
                ((Variable[]) value)[1].Div(((Variable[]) in.getValue())[1]);
            }
        }
    }

    @Override
    public void Pow(Variable in1, Variable in2) {
        catchIncompatible("Pow", in1, in2);
        Variable[] val1 = (Variable[]) in1.getValue();
        Variable[] val2 = (Variable[]) in2.getValue();

        ((Variable[]) value)[0].Pow(val1[0], val2[0]);
        ((Variable[]) value)[1].Pow(val1[1], val2[1]);
    }

    @Override
    public void Pow(Variable in) {
        catchIncompatible("Pow", in);
        switch (in.getType()) {
            case Number -> {
                ((Variable[]) value)[0].Pow(in);
                ((Variable[]) value)[1].Pow(in);
            }
            case FieldCord -> {
                ((Variable[]) value)[0].Pow(((Variable[]) in.getValue())[0]);
                ((Variable[]) value)[1].Pow(((Variable[]) in.getValue())[1]);
            }
        }
    }

    @Override
    public void Sqrt(Variable in) {
        catchIncompatible("Sqrt", in);
        switch (in.getType()) {
            case Number -> {
                ((Variable[]) value)[0].Sqrt(in);
                ((Variable[]) value)[1].Sqrt(in);
            }
            case FieldCord -> {
                ((Variable[]) value)[0].Sqrt(((Variable[]) in.getValue())[0]);
                ((Variable[]) value)[1].Sqrt(((Variable[]) in.getValue())[1]);
            }
        }
    }

    @Override
    public void Sqrt() {
        ((Variable[]) value)[0].Sqrt();
        ((Variable[]) value)[1].Sqrt();
    }

    @Override
    public void Sin(Variable in) {
        catchIncompatible("Sin", in);
        switch (in.getType()) {
            case Number -> {
                ((Variable[]) value)[0].Sin(in);
                ((Variable[]) value)[1].Sin(in);
            }
            case FieldCord -> {
                ((Variable[]) value)[0].Sin(((Variable[]) in.getValue())[0]);
                ((Variable[]) value)[1].Sin(((Variable[]) in.getValue())[1]);
            }
        }
    }

    @Override
    public void Sin() {
        ((Variable[]) value)[0].Sin();
        ((Variable[]) value)[1].Sin();
    }

    @Override
    public void iSin(Variable in) {
        catchIncompatible("iSin", in);
        switch (in.getType()) {
            case Number -> {
                ((Variable[]) value)[0].iSin(in);
                ((Variable[]) value)[1].iSin(in);
            }
            case FieldCord -> {
                ((Variable[]) value)[0].iSin(((Variable[]) in.getValue())[0]);
                ((Variable[]) value)[1].iSin(((Variable[]) in.getValue())[1]);
            }
        }
    }

    @Override
    public void iSin() {
        ((Variable[]) value)[0].iSin();
        ((Variable[]) value)[1].iSin();
    }

    @Override
    public void Cos(Variable in) {
        catchIncompatible("Cos", in);
        switch (in.getType()) {
            case Number -> {
                ((Variable[]) value)[0].Cos(in);
                ((Variable[]) value)[1].Cos(in);
            }
            case FieldCord -> {
                ((Variable[]) value)[0].Cos(((Variable[]) in.getValue())[0]);
                ((Variable[]) value)[1].Cos(((Variable[]) in.getValue())[1]);
            }
        }
    }

    @Override
    public void Cos() {
        ((Variable[]) value)[0].Cos();
        ((Variable[]) value)[1].Cos();
    }

    @Override
    public void iCos(Variable in) {
        catchIncompatible("iCos", in);
        switch (in.getType()) {
            case Number -> {
                ((Variable[]) value)[0].iCos(in);
                ((Variable[]) value)[1].iCos(in);
            }
            case FieldCord -> {
                ((Variable[]) value)[0].iCos(((Variable[]) in.getValue())[0]);
                ((Variable[]) value)[1].iCos(((Variable[]) in.getValue())[1]);
            }
        }
    }

    @Override
    public void iCos() {
        ((Variable[]) value)[0].iCos();
        ((Variable[]) value)[1].iCos();
    }

    @Override
    public void Tan(Variable in) {
        catchIncompatible("Tan", in);
        switch (in.getType()) {
            case Number -> {
                ((Variable[]) value)[0].Tan(in);
                ((Variable[]) value)[1].Tan(in);
            }
            case FieldCord -> {
                ((Variable[]) value)[0].Tan(((Variable[]) in.getValue())[0]);
                ((Variable[]) value)[1].Tan(((Variable[]) in.getValue())[1]);
            }
        }
    }

    @Override
    public void Tan() {
        ((Variable[]) value)[0].Tan();
        ((Variable[]) value)[1].Tan();
    }

    @Override
    public void iTan(Variable in) {
        catchIncompatible("iTan", in);
        switch (in.getType()) {
            case Number -> {
                ((Variable[]) value)[0].iTan(in);
                ((Variable[]) value)[1].iTan(in);
            }
            case FieldCord -> {
                ((Variable[]) value)[0].iTan(((Variable[]) in.getValue())[0]);
                ((Variable[]) value)[1].iTan(((Variable[]) in.getValue())[1]);
            }
        }
    }

    @Override
    public void iTan() {
        ((Variable[]) value)[0].iTan();
        ((Variable[]) value)[1].iTan();
    }

    @Override
    public void toDeg(Variable in) {
        catchIncompatible("toDeg", in);
        switch (in.getType()) {
            case Number -> {
                ((Variable[]) value)[0].toDeg(in);
                ((Variable[]) value)[1].toDeg(in);
            }
            case FieldCord -> {
                ((Variable[]) value)[0].toDeg(((Variable[]) in.getValue())[0]);
                ((Variable[]) value)[1].toDeg(((Variable[]) in.getValue())[1]);
            }
        }
    }

    @Override
    public void toDeg() {
        ((Variable[]) value)[0].toDeg();
        ((Variable[]) value)[1].toDeg();
    }

    @Override
    public void toRad(Variable in) {
        catchIncompatible("toRad", in);
        switch (in.getType()) {
            case Number -> {
                ((Variable[]) value)[0].toRad(in);
                ((Variable[]) value)[1].toRad(in);
            }
            case FieldCord -> {
                ((Variable[]) value)[0].toRad(((Variable[]) in.getValue())[0]);
                ((Variable[]) value)[1].toRad(((Variable[]) in.getValue())[1]);
            }
        }
    }

    @Override
    public void toRad() {
        ((Variable[]) value)[0].toRad();
        ((Variable[]) value)[1].toRad();
    }

    // logic Ops
    @Override
    public boolean equals(Variable in) {
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
            case FieldCord, FieldPos -> {
                Variable[] thisPos = (Variable[]) value;
                Variable[] givenPos = (Variable[]) in.getValue();
                double[] thisPosVal = {(double) thisPos[0].getValue(), (double) thisPos[1].getValue()};
                double[] givenPosVal = {(double) givenPos[0].getValue(), (double) givenPos[1].getValue()};
                return ((thisPosVal[0] == givenPosVal[0]) && (thisPosVal[1] == givenPosVal[1]));
            }
        }
        return false;
    }
    // json/list shenanigans
    @Override
    public void set2get(Variable in, int index){
        try {
            setVariable(in.getFromIndex(index).getClone());
        } catch (VariableException e) {
            throwErr("Set 2 get", new Object[]{in}, "Given List is not a List, it is: "+in.getType());
        }
    }
    @Override
    public void set2get(Variable in, Variable id){
        try {
            setVariable(in.getFromID(id).getClone());
        } catch (VariableException e) {
            throwErr("Set 2 get", new Object[]{in,id}, "Given Json is not a Json, it is: "+in.getType());
        }
    }

    @Override
    public void set(int index, Variable in){
        switch (index){
            case 1 -> ((Variable[])value)[0].setVariable(in);
            case 2 -> ((Variable[])value)[1].setVariable(in);
        }
        throwErr("set", new Object[]{in}, "Field Coord only accepts index 1 and 2 as valid. was given: "+index);
    }
    @Override
    public void set(Variable id, Variable in){
        switch (id.valueToString()){
            case "X","x" -> ((Variable[])value)[0].setVariable(in);
            case "Y","y" -> ((Variable[])value)[1].setVariable(in);
        }
        throwErr("set", new Object[]{in}, "Field Coord only accepts ids X and Y as valid.");
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
    public Variable getFromID(Variable id){
        switch (String.valueOf(id.getValue())){
            case "X","x" -> { // java really wants these in a block... for..... some........ reason
                return ((Variable[])value)[0];
            }
            case "Y","y" -> {
                return ((Variable[])value)[1];
            }
        }
        throwErr("getFromID", new Object[]{id}, "Field only accept X or Y as valid ID, was given: "+id.getValue());
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
        }
        throwErr("getFromIndex", new Object[]{}, "Field only accept 0 or 1 as valid indices, was given: "+index);
        return null; //ah yeaassss, we must return AFTER throwing an error. how splendid.
    }
}
