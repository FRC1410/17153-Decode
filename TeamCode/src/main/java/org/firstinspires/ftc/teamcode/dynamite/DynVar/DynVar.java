package org.firstinspires.ftc.teamcode.dynamite.DynVar;

import org.firstinspires.ftc.teamcode.dynamite.DynExceptions.DynVarException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DynVar {
    private enum VarType{
        NUMBER,
        BOOLEAN,
        STRING,
        LIST,
        JSON,
        FIELD_CORD,
        FIELD_POS
    }
    public VarType type;
    private Object value;
    private String ID;

    // instatiators
    public DynVar(String setType, String ID, double val) throws Exception {
        value = val;
        this.ID = ID;
        switch (setType){
            case "Number": type = VarType.NUMBER; break;
            case "Boolean": type = VarType.BOOLEAN; break;
                case "String": type = VarType.STRING; break;
                case "List": type = VarType.LIST; break;
                case "Json": type = VarType.JSON; break;
                case "Field coordinates": type = VarType.FIELD_CORD; break;
                case "Field cords": type = VarType.FIELD_CORD; break;
                case "Field position": type = VarType.FIELD_POS; break;
                case "Field pos": type = VarType.FIELD_POS; break;
                default: throw new Exception("cannot create DYN variable type: "+setType);
        }
    }
    public DynVar(String setType, String ID, int val) throws Exception {
        value = (double)val;
        this.ID = ID;
        switch (setType){
            case "Number": type = VarType.NUMBER; break;
            case "Boolean": type = VarType.BOOLEAN; break;
            case "String": type = VarType.STRING; break;
            case "List": type = VarType.LIST; break;
            case "Json": type = VarType.JSON; break;
            case "Field coordinates": type = VarType.FIELD_CORD; break;
            case "Field cords": type = VarType.FIELD_CORD; break;
            case "Field position": type = VarType.FIELD_POS; break;
            case "Field pos": type = VarType.FIELD_POS; break;
            default: throw new Exception("cannot create DYN variable type: "+setType);
        }
    }
    public DynVar(String setType, String ID, float val) throws Exception {
        value = (double)val;
        this.ID = ID;
        switch (setType){
            case "Number": type = VarType.NUMBER; break;
            case "Boolean": type = VarType.BOOLEAN; break;
            case "String": type = VarType.STRING; break;
            case "List": type = VarType.LIST; break;
            case "Json": type = VarType.JSON; break;
            case "Field coordinates": type = VarType.FIELD_CORD; break;
            case "Field cords": type = VarType.FIELD_CORD; break;
            case "Field position": type = VarType.FIELD_POS; break;
            case "Field pos": type = VarType.FIELD_POS; break;
            default: throw new Exception("cannot create DYN variable type: "+setType);
        }
    }
    public DynVar(String setType, String ID, long val) throws Exception {
        value = (double)val;
        this.ID = ID;
        switch (setType){
            case "Number": type = VarType.NUMBER; break;
            case "Boolean": type = VarType.BOOLEAN; break;
            case "String": type = VarType.STRING; break;
            case "List": type = VarType.LIST; break;
            case "Json": type = VarType.JSON; break;
            case "Field coordinates": type = VarType.FIELD_CORD; break;
            case "Field cords": type = VarType.FIELD_CORD; break;
            case "Field position": type = VarType.FIELD_POS; break;
            case "Field pos": type = VarType.FIELD_POS; break;
            default: throw new Exception("cannot create DYN variable type: "+setType);
        }
    }
    public DynVar(String setType, String ID, boolean val) throws Exception {
        value = val;
        this.ID = ID;
        switch (setType){
            case "Number": type = VarType.NUMBER; break;
            case "Boolean": type = VarType.BOOLEAN; break;
            case "String": type = VarType.STRING; break;
            case "List": type = VarType.LIST; break;
            case "Json": type = VarType.JSON; break;
            case "Field coordinates": type = VarType.FIELD_CORD; break;
            case "Field cords": type = VarType.FIELD_CORD; break;
            case "Field position": type = VarType.FIELD_POS; break;
            case "Field pos": type = VarType.FIELD_POS; break;
            default: throw new Exception("cannot create DYN variable type: "+setType);
        }
    }
    public DynVar(String setType, String ID, String val) throws Exception {
        value = val;
        this.ID = ID;
        switch (setType){
            case "Number": type = VarType.NUMBER; break;
            case "Boolean": type = VarType.BOOLEAN; break;
            case "String": type = VarType.STRING; break;
            case "List": type = VarType.LIST; break;
            case "Json": type = VarType.JSON; break;
            case "Field coordinates": type = VarType.FIELD_CORD; break;
            case "Field cords": type = VarType.FIELD_CORD; break;
            case "Field position": type = VarType.FIELD_POS; break;
            case "Field pos": type = VarType.FIELD_POS; break;
            default: throw new Exception("cannot create DYN variable type: "+setType);
        }
    }
    public DynVar(String setType, String ID, ArrayList<DynVar> val) throws Exception {
        value = val;
        this.ID = ID;
        switch (setType){
            case "Number": type = VarType.NUMBER; break;
            case "Boolean": type = VarType.BOOLEAN; break;
            case "String": type = VarType.STRING; break;
            case "List": type = VarType.LIST; break;
            case "Json": type = VarType.JSON; break;
            case "Field coordinates": type = VarType.FIELD_CORD; break;
            case "Field cords": type = VarType.FIELD_CORD; break;
            case "Field position": type = VarType.FIELD_POS; break;
            case "Field pos": type = VarType.FIELD_POS; break;
            default: throw new Exception("cannot create DYN variable type: "+setType);
        }
    }
    public DynVar(String setType, String ID, Map<DynVar, DynVar> val) throws Exception {
        value = val;
        this.ID = ID;
        switch (setType){
            case "Number": type = VarType.NUMBER; break;
            case "Boolean": type = VarType.BOOLEAN; break;
            case "String": type = VarType.STRING; break;
            case "List": type = VarType.LIST; break;
            case "Json": type = VarType.JSON; break;
            case "Field coordinates": type = VarType.FIELD_CORD; break;
            case "Field cords": type = VarType.FIELD_CORD; break;
            case "Field position": type = VarType.FIELD_POS; break;
            case "Field pos": type = VarType.FIELD_POS; break;
            default: throw new Exception("cannot create DYN variable type: "+setType);
        }
    }
    public DynVar(String setType, String ID, double[] val) throws Exception {
        value = val; // this will deal with any Field cord, or pos
        this.ID = ID;
        switch (setType){
            case "Number": type = VarType.NUMBER; break;
            case "Boolean": type = VarType.BOOLEAN; break;
            case "String": type = VarType.STRING; break;
            case "List": type = VarType.LIST; break;
            case "Json": type = VarType.JSON; break;
            case "Field coordinates": type = VarType.FIELD_CORD; break;
            case "Field cords": type = VarType.FIELD_CORD; break;
            case "Field position": type = VarType.FIELD_POS; break;
            case "Field pos": type = VarType.FIELD_POS; break;
            default: throw new Exception("cannot create DYN variable type: "+setType);
        }
    }
    public DynVar(String setType, String ID, Object val) throws Exception {
        value = val;
        this.ID = ID;
        switch (setType) {
            case "Number": type = VarType.NUMBER;break;
            case "Boolean": type = VarType.BOOLEAN;break;
            case "String": type = VarType.STRING;break;
            case "List": type = VarType.LIST;break;
            case "Json": type = VarType.JSON;break;
            case "Field coordinates": type = VarType.FIELD_CORD;break;
            case "Field cords": type = VarType.FIELD_CORD;break;
            case "Field position": type = VarType.FIELD_POS;break;
            case "Field pos": type = VarType.FIELD_POS;break;
            default: throw new Exception("cannot create DYN variable type: " + setType);
        }
    }

    public DynVar(String ID){this.ID = ID;} //used in tandem with init method to allow the use of temporary empty variables in a buffer before a script is run

    // logical ops
    public boolean equals(DynVar IN){
        return (IN.getValue() == value);
    }
    public boolean isTrue(){
        if (type == VarType.BOOLEAN){
            return ((boolean)value == true);
        } else {
            throw new DynVarException(ID, this, "org.firstinspires.ftc.teamcode.dynamite.DynVar.DynVar is not a Boolean for OP: isTrue");
        }
    }
    public boolean isFalse(){
        if (type == VarType.BOOLEAN){
            return ((boolean)value == false);
        } else {
            throw new DynVarException(ID, this, "org.firstinspires.ftc.teamcode.dynamite.DynVar.DynVar is not a Boolean for OP: isFalse");
        }
    }
    public boolean isMore(DynVar IN){
        if (IN.type == VarType.NUMBER && type == VarType.NUMBER){
            // dyn numbers are always doubles in java land.
            return ((double)IN.getValue() > (double)value);
        } else {
            throw new DynVarException(ID, this, IN.getID(), IN, "org.firstinspires.ftc.teamcode.dynamite.DynVar.DynVar Values Do Not Match NUMBER Type in OP: isMore");
        }
    }
    public boolean isLess(DynVar IN){
        if (IN.type == VarType.NUMBER && type == VarType.NUMBER){
            // dyn numbers are always doubles in java land.
            return ((double)IN.getValue() < (double)value);
        } else {
            throw new DynVarException(ID, this, IN.getID(), IN, "org.firstinspires.ftc.teamcode.dynamite.DynVar.DynVar Values Do Not Match NUMBER Type in OP: isLess");
        }
    }
    public boolean isMoreEq(DynVar IN){
        if (IN.type == VarType.NUMBER && type == VarType.NUMBER){
            // dyn numbers are always doubles in java land.
            return ((double)IN.getValue() >= (double)value);
        } else {
            throw new DynVarException(ID, this, IN.getID(), IN, "org.firstinspires.ftc.teamcode.dynamite.DynVar.DynVar Values Do Not Match NUMBER Type in OP: isMoreEq");
        }
    }
    public boolean isLessEq(DynVar IN){
        if (IN.type == VarType.NUMBER && type == VarType.NUMBER){
            // dyn numbers are always doubles in java land.
            return ((double)IN.getValue() <= (double)value);
        } else {
            throw new DynVarException(ID, this, IN.getID(), IN, "org.firstinspires.ftc.teamcode.dynamite.DynVar.DynVar Values Do Not Match NUMBER Type in OP: isLessEq");
        }
    }

    // math ops
    public void setToAdd(DynVar IN){
        if (IN.type == VarType.NUMBER && type == VarType.NUMBER){
            value = (double)value+(double)IN.getValue();
        } else if (IN.type == VarType.STRING && type == VarType.NUMBER) {
            value = (double)value+(String)IN.getValue();
            type = VarType.STRING;
        } else if (IN.type == VarType.STRING && type == VarType.STRING) {
            value = value+(String)IN.getValue();
        } else if (IN.type == VarType.NUMBER && type == VarType.STRING) {
            value = (String)value+(double)IN.getValue();
        } else {
            throw new DynVarException(ID, this, IN.getID(), IN, "Dyn Values Do Not Match NUMBER Type in OP: setToAdd");
        }
    }
    public void setToAdd(DynVar IN1, DynVar IN2){
        if (IN1.type == VarType.NUMBER && IN2.type == VarType.NUMBER) {
            value = (double)IN1.getValue()+(double)IN2.getValue();
        } else if (IN1.type == VarType.NUMBER && IN2.type == VarType.STRING) {
            type = VarType.STRING;
            value = (double)IN1.getValue()+(String)IN2.getValue();
        } else if (IN1.type == VarType.STRING && IN2.type == VarType.NUMBER){
            type = VarType.STRING;
            value = (String)IN1.getValue()+(double)IN2.getValue();
        } else if (IN1.type == VarType.STRING && IN2.type == VarType.STRING) {
            type = VarType.STRING;
            value = IN1.getValue()+(String)IN2.getValue();
        } else {
            throw new DynVarException(IN1.getID(), IN1, IN2.getID(), IN2, "Dyn Values Do Not Match NUMBER Type in OP: setToAdd");
        }
    }
    public void setToSub(DynVar IN){
        // important the IN variable is the first value in the equasion
        if (IN.type == VarType.NUMBER && type == VarType.NUMBER){
            value = (double)IN.getValue()-(double)value;
        } else {
            throw new DynVarException(ID, this, IN.getID(), IN, "Dyn Values Do Not Match NUMBER Type in OP: setToSub");
        }
    }
    public void setToSub(DynVar IN1, DynVar IN2){
        if (IN1.type == VarType.NUMBER && IN2.type == VarType.NUMBER) {
            value = (double)IN1.getValue()-(double)IN2.getValue();
        } else {
            throw new DynVarException(IN1.getID(), IN1, IN2.getID(), IN2, "Dyn Values Do Not Match NUMBER Type in OP: settoSub");
        }
    }
    public void setToMux(DynVar IN){
        if (IN.type == VarType.NUMBER && type == VarType.NUMBER){
            value = (double)IN.getValue()*(double)value;
        } else {
            throw new DynVarException(ID, this, IN.getID(), IN, "Dyn Values Do Not Match NUMBER Type in OP: setToMux");
        }
    }
    public void setToMux(DynVar IN1, DynVar IN2){
        if (IN1.type == VarType.NUMBER && IN2.type == VarType.NUMBER) {
            value = (double)IN1.getValue()*(double)IN2.getValue();
        } else {
            throw new DynVarException(IN1.getID(), IN1, IN2.getID(), IN2, "Dyn Values Do Not Match NUMBER Type in OP: setToMux");
        }
    }
    public void setToDiv(DynVar IN){
        // the in value is the numerator
        if (IN.type == VarType.NUMBER && type == VarType.NUMBER){
            value = (double)IN.getValue()/(double)value;
        } else {
            throw new DynVarException(ID, this, IN.getID(), IN, "Dyn Values Do Not Match NUMBER Type in OP: setToDiv");
        }
    }
    public void setToDiv(DynVar IN1, DynVar IN2){
        if (IN1.type == VarType.NUMBER && IN2.type == VarType.NUMBER) {
            value = (double)IN1.getValue()/(double)IN2.getValue();
        } else {
            throw new DynVarException(IN1.getID(), IN1, IN2.getID(), IN2, "Dyn Values Do Not Match NUMBER Type in OP: setToDiv");
        }
    }
    public void setToPow(DynVar IN){
        if (IN.type == VarType.NUMBER && type == VarType.NUMBER){
            value = Math.pow((double)value, (double)IN.getValue());
        } else {
            throw new DynVarException(ID, this, IN.getID(), IN, "Dyn Values Do Not Match NUMBER Type in OP: setToPow");
        }
    }
    public void setToPow(DynVar IN1, DynVar IN2){
        if (IN1.type == VarType.NUMBER && IN2.type == VarType.NUMBER) {
            value = Math.pow((double)IN1.getValue(),(double)IN2.getValue());
        } else {
            throw new DynVarException(IN1.getID(), IN1, IN2.getID(), IN2, "Dyn Values Do Not Match NUMBER Type in OP: setToPow");
        }
    }
    public void setToSqr(DynVar IN){
        if (IN.type == VarType.NUMBER && type == VarType.NUMBER){
            value = Math.sqrt((double)IN.getValue());
        } else {
            throw new DynVarException(ID, this, IN.getID(), IN, "Dyn Values Do Not Match NUMBER Type in OP: setToSqr");
        }
    }
    public void setToSqr(){
        if (type == VarType.NUMBER) {
            value = Math.sqrt((double)value);
        } else {
            throw new DynVarException(ID, this, "Dyn Values Do Not Match NUMBER Type in OP: setToSqr");
        }
    }
    public void setToSin(DynVar IN){
        if (IN.type == VarType.NUMBER && type == VarType.NUMBER){
            value = Math.sin((double)IN.getValue());
        } else {
            throw new DynVarException(ID, this, IN.getID(), IN, "Dyn Values Do Not Match NUMBER Type in OP: setToSin");
        }
    }
    public void setToSin(){
        if (type == VarType.NUMBER) {
            value = Math.sin((double)value);
        } else {
            throw new DynVarException(ID, this, "Dyn Values Do Not Match NUMBER Type in OP: setToSin");
        }
    }
    public void setToIsn(DynVar IN){
        if (IN.type == VarType.NUMBER && type == VarType.NUMBER){
            value = 1/Math.sin((double)IN.getValue());
        } else {
            throw new DynVarException(ID, this, IN.getID(), IN, "Dyn Values Do Not Match NUMBER Type in OP: setToIsn");
        }
    }
    public void setToIsn(){
        if (type == VarType.NUMBER) {
            value = 1/Math.sin((double)value);
        } else {
            throw new DynVarException(ID, this, "Dyn Values Do Not Match NUMBER Type in OP: setToIsn");
        }
    }
    public void setToCos(DynVar IN){
        if (IN.type == VarType.NUMBER && type == VarType.NUMBER){
            value = Math.cos((double)IN.getValue());
        } else {
            throw new DynVarException(ID, this, IN.getID(), IN, "Dyn Values Do Not Match NUMBER Type in OP: setToCos");
        }
    }
    public void setToCos(){
        if (type == VarType.NUMBER) {
            value = Math.cos((double)value);
        } else {
            throw new DynVarException(ID, this, "Dyn Values Do Not Match NUMBER Type in OP: setToCos");
        }
    }
    public void setToIcs(DynVar IN){
        if (IN.type == VarType.NUMBER && type == VarType.NUMBER){
            value = 1/Math.cos((double)IN.getValue());
        } else {
            throw new DynVarException(ID, this, IN.getID(), IN, "1 or more DynVars are not NUMBERs in OP: setToIcs");
        }
    }
    public void setToIcs(){
        if (type == VarType.NUMBER) {
            value = 1/Math.cos((double)value);
        } else {
            throw new DynVarException(ID, this, "Dyn Type Is A non-NUMBER Type in OP: setToIcs");
        }
    }
    public void setToTan(DynVar IN){
        if (IN.type == VarType.NUMBER && type == VarType.NUMBER){
            value = Math.tan((double)IN.getValue());
        } else {
            throw new DynVarException(ID, this, IN.getID(), IN, "Dyn Values Do Not Match NUMBER Type in OP: setToTan");
        }
    }
    public void setToTan(){
        if (type == VarType.NUMBER) {
            value = Math.tan((double)value);
        } else {
            throw new DynVarException(ID, this, "Dyn Values Do Not Match NUMBER Type in OP: setToTan");
        }
    }
    public void setToItn(DynVar IN){
        if (IN.type == VarType.NUMBER && type == VarType.NUMBER){
            value = 1/Math.tan((double)IN.getValue());
        } else {
            throw new DynVarException(ID, this, IN.getID(), IN, "Dyn Values Do Not Match NUMBER Type in OP: setToItn");
        }
    }
    public void setToItn(){
        if (type == VarType.NUMBER) {
            value = 1/Math.tan((double)value);
        } else {
            throw new DynVarException(ID, this, "Dyn type is a non NUMBER type in OP: setToItn");
        }
    }
    public void setToRadianConvert(DynVar IN){
        if (IN.type == VarType.NUMBER){
            value = Math.toRadians((double)IN.getValue());
            type = VarType.NUMBER;
        } else {
            throw new DynVarException(IN.getID(), IN, "Dyn var type not compatible with number based action in op: setToRadianConvert");
        }
    }
    public void setToRadianConvert(){
        if (type == VarType.NUMBER){
            value = Math.toRadians((double)value);
            type = VarType.NUMBER;
        } else {
            throw new DynVarException(ID, this, "Dyn var type not compatible with number based action in op: setToRadianConvert");
        }
    }
    public void setToDegreeConvert(DynVar IN){
        if (IN.type == VarType.NUMBER){
            value = Math.toDegrees((double)IN.getValue());
            type = VarType.NUMBER;
        } else {
            throw new DynVarException(IN.getID(), IN, "Dyn var type not compatible with number based action in op: setToDegreeConvert");
        }
    }
    public void setToDegreeConvert(){
        if (type == VarType.NUMBER){
            value = Math.toDegrees((double)value);
            type = VarType.NUMBER;
        } else {
            throw new DynVarException(ID, this, "Dyn var type not compatible with number based action in op: setToDegreeConvert");
        }
    }

    // general
    public void init(String setType, String ID, double val) throws Exception {
        value = val;
        this.ID = ID;
        switch (setType){
            case "Number": type = VarType.NUMBER; break;
            case "Boolean": type = VarType.BOOLEAN; break;
            case "String": type = VarType.STRING; break;
            case "List": type = VarType.LIST; break;
            case "Json": type = VarType.JSON; break;
            case "Field coordinates": type = VarType.FIELD_CORD; break;
            case "Field cords": type = VarType.FIELD_CORD; break;
            case "Field position": type = VarType.FIELD_POS; break;
            case "Field pos": type = VarType.FIELD_POS; break;
            default: throw new Exception("cannot create DYN variable type: "+setType);
        }
    }
    public void init(String setType, String ID, int val) throws Exception {
        value = (double)val;
        this.ID = ID;
        switch (setType){
            case "Number": type = VarType.NUMBER; break;
            case "Boolean": type = VarType.BOOLEAN; break;
            case "String": type = VarType.STRING; break;
            case "List": type = VarType.LIST; break;
            case "Json": type = VarType.JSON; break;
            case "Field coordinates": type = VarType.FIELD_CORD; break;
            case "Field cords": type = VarType.FIELD_CORD; break;
            case "Field position": type = VarType.FIELD_POS; break;
            case "Field pos": type = VarType.FIELD_POS; break;
            default: throw new Exception("cannot create DYN variable type: "+setType);
        }
    }
    public void init(String setType, String ID, float val) throws Exception {
        value = (double)val;
        this.ID = ID;
        switch (setType){
            case "Number": type = VarType.NUMBER; break;
            case "Boolean": type = VarType.BOOLEAN; break;
            case "String": type = VarType.STRING; break;
            case "List": type = VarType.LIST; break;
            case "Json": type = VarType.JSON; break;
            case "Field coordinates": type = VarType.FIELD_CORD; break;
            case "Field cords": type = VarType.FIELD_CORD; break;
            case "Field position": type = VarType.FIELD_POS; break;
            case "Field pos": type = VarType.FIELD_POS; break;
            default: throw new Exception("cannot create DYN variable type: "+setType);
        }
    }
    public void init(String setType, String ID, long val) throws Exception {
        value = (double)val;
        this.ID = ID;
        switch (setType){
            case "Number": type = VarType.NUMBER; break;
            case "Boolean": type = VarType.BOOLEAN; break;
            case "String": type = VarType.STRING; break;
            case "List": type = VarType.LIST; break;
            case "Json": type = VarType.JSON; break;
            case "Field coordinates": type = VarType.FIELD_CORD; break;
            case "Field cords": type = VarType.FIELD_CORD; break;
            case "Field position": type = VarType.FIELD_POS; break;
            case "Field pos": type = VarType.FIELD_POS; break;
            default: throw new Exception("cannot create DYN variable type: "+setType);
        }
    }
    public void init(String setType, String ID, boolean val) throws Exception {
        value = val;
        this.ID = ID;
        switch (setType){
            case "Number": type = VarType.NUMBER; break;
            case "Boolean": type = VarType.BOOLEAN; break;
            case "String": type = VarType.STRING; break;
            case "List": type = VarType.LIST; break;
            case "Json": type = VarType.JSON; break;
            case "Field coordinates": type = VarType.FIELD_CORD; break;
            case "Field cords": type = VarType.FIELD_CORD; break;
            case "Field position": type = VarType.FIELD_POS; break;
            case "Field pos": type = VarType.FIELD_POS; break;
            default: throw new Exception("cannot create DYN variable type: "+setType);
        }
    }
    public void init(String setType, String ID, String val) throws Exception {
        value = val;
        this.ID = ID;
        switch (setType){
            case "Number": type = VarType.NUMBER; break;
            case "Boolean": type = VarType.BOOLEAN; break;
            case "String": type = VarType.STRING; break;
            case "List": type = VarType.LIST; break;
            case "Json": type = VarType.JSON; break;
            case "Field coordinates": type = VarType.FIELD_CORD; break;
            case "Field cords": type = VarType.FIELD_CORD; break;
            case "Field position": type = VarType.FIELD_POS; break;
            case "Field pos": type = VarType.FIELD_POS; break;
            default: throw new Exception("cannot create DYN variable type: "+setType);
        }
    }
    public void init(String setType, String ID, ArrayList<DynVar> val) throws Exception {
        value = val;
        this.ID = ID;
        switch (setType){
            case "Number": type = VarType.NUMBER; break;
            case "Boolean": type = VarType.BOOLEAN; break;
            case "String": type = VarType.STRING; break;
            case "List": type = VarType.LIST; break;
            case "Json": type = VarType.JSON; break;
            case "Field coordinates": type = VarType.FIELD_CORD; break;
            case "Field cords": type = VarType.FIELD_CORD; break;
            case "Field position": type = VarType.FIELD_POS; break;
            case "Field pos": type = VarType.FIELD_POS; break;
            default: throw new Exception("cannot create DYN variable type: "+setType);
        }
    }
    public void init(String setType, String ID, Map<DynVar, DynVar> val) throws Exception {
        value = val;
        this.ID = ID;
        switch (setType){
            case "Number": type = VarType.NUMBER; break;
            case "Boolean": type = VarType.BOOLEAN; break;
            case "String": type = VarType.STRING; break;
            case "List": type = VarType.LIST; break;
            case "Json": type = VarType.JSON; break;
            case "Field coordinates": type = VarType.FIELD_CORD; break;
            case "Field cords": type = VarType.FIELD_CORD; break;
            case "Field position": type = VarType.FIELD_POS; break;
            case "Field pos": type = VarType.FIELD_POS; break;
            default: throw new Exception("cannot create DYN variable type: "+setType);
        }
    }
    public void init(String setType, String ID, double[] val) throws Exception {
        value = val; // this will deal with any Field cord, or pos
        this.ID = ID;
        switch (setType){
            case "Number": type = VarType.NUMBER; break;
            case "Boolean": type = VarType.BOOLEAN; break;
            case "String": type = VarType.STRING; break;
            case "List": type = VarType.LIST; break;
            case "Json": type = VarType.JSON; break;
            case "Field coordinates": type = VarType.FIELD_CORD; break;
            case "Field cords": type = VarType.FIELD_CORD; break;
            case "Field position": type = VarType.FIELD_POS; break;
            case "Field pos": type = VarType.FIELD_POS; break;
            default: throw new Exception("cannot create DYN variable type: "+setType);
        }
    }
    public void init(String setType, String ID, Object val) throws Exception {
        value = val;
        this.ID = ID;
        switch (setType){
            case "Number": type = VarType.NUMBER; break;
            case "Boolean": type = VarType.BOOLEAN; break;
            case "String": type = VarType.STRING; break;
            case "List": type = VarType.LIST; break;
            case "Json": type = VarType.JSON; break;
            case "Field coordinates": type = VarType.FIELD_CORD; break;
            case "Field cords": type = VarType.FIELD_CORD; break;
            case "Field position": type = VarType.FIELD_POS; break;
            case "Field pos": type = VarType.FIELD_POS; break;
            default: throw new Exception("cannot create DYN variable type: "+setType);
        }
    }

    public Object getValue(){return value;}
    public String getID(){return ID;}

    /**
     * Set the value of this variable.
     */
    public void setValue(Object newValue) {
        this.value = newValue;
    }

    /**
     * Get this variable's value as a double.
     * Works for NUMBER type.
     */
    public double asDouble() {
        if (type == VarType.NUMBER) {
            return (double) value;
        }
        throw new DynVarException(ID, this, "Cannot convert to double, type is: " + type);
    }

    /**
     * Get this variable's value as a boolean.
     * Works for BOOLEAN type.
     */
    public boolean asBoolean() {
        if (type == VarType.BOOLEAN) {
            return (boolean) value;
        }
        throw new DynVarException(ID, this, "Cannot convert to boolean, type is: " + type);
    }

    /**
     * Get this variable's value as a String.
     */
    public String asString() {
        if (type == VarType.STRING) {
            return (String) value;
        }
        return String.valueOf(value);
    }

    /**
     * Get this variable's value as a double array.
     * Works for FIELD_CORD and FIELD_POS types.
     */
    public double[] asDoubleArray() {
        if (type == VarType.FIELD_CORD || type == VarType.FIELD_POS) {
            return (double[]) value;
        }
        throw new DynVarException(ID, this, "Cannot convert to double[], type is: " + type);
    }

    public String toString(){ //used mainly for debug purposes
        String out;
        switch (type){
            case NUMBER: return "NUMBER " + (double)value;
            case BOOLEAN: return "BOOLEAN " + (boolean)value;
            case STRING: return "STRING " + value;
            case LIST:
                out = "LIST [";
                for (int i = 0; i < ((ArrayList<DynVar>)value).toArray().length; i++){
                    out = out + ((ArrayList<DynVar>)value).get(i).toString();
                    if (i < ((ArrayList<DynVar>)value).toArray().length-1){out = out+", ";}
                }
                return out+"]";
            case JSON:
                out = "JSON {";
                int i = 0;
                for (Map.Entry<DynVar, DynVar> entry : ((HashMap<DynVar, DynVar>) value).entrySet()){
                    out += "org.firstinspires.ftc.teamcode.dynamite.DynVar.DynVar: " + i + " Value: " + entry.getValue();
                    if (i < ((HashMap<DynVar, DynVar>) value).size()-1){out += ", ";}
                    i ++;
                }
                return out+"}";
            case FIELD_CORD: return "FIELD_CORD X: "+((double[])value)[0]+" Y: "+((double[])value)[1];
            case FIELD_POS: return "FIELD_POS X: "+((double[])value)[0]+" Y: "+((double[])value)[1]+" H: "+((double[])value)[2];
            default: throw new RuntimeException("WARNING: NO TYPE ASSIGNED TO org.firstinspires.ftc.teamcode.dynamite.DynVar.DynVar ID: "+ID+"!");
        }
    }

    /**
     * Format for DYN telemetry output with shorthand type.
     * Example: "NUM varName 3.0"
     */
    public String toTelemetryString() {
        String id = ID == null ? "" : ID;
        switch (type) {
            case NUMBER:
                return "NUM " + id + " " + (double) value;
            case BOOLEAN:
                return "BOOL " + id + " " + (boolean) value;
            case STRING:
                return "STR " + id + " " + value;
            case LIST:
                return "LIST " + id + " " + toString();
            case JSON:
                return "JSON " + id + " " + toString();
            case FIELD_CORD:
                return "FC " + id + " " + ((double[]) value)[0] + " " + ((double[]) value)[1];
            case FIELD_POS:
                return "FP " + id + " " + ((double[]) value)[0] + " " + ((double[]) value)[1] + " " + ((double[]) value)[2];
            default:
                return toString();
        }
    }

    // custom funciton interfaces
    public <T> T toJava(){
        return (T)value;
    }
}
