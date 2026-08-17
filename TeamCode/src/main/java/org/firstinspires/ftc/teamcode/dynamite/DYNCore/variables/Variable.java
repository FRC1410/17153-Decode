package org.firstinspires.ftc.teamcode.dynamite.DYNCore.variables;

import org.firstinspires.ftc.teamcode.dynamite.DYNCore.variables.complex.DynFieldCord;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.variables.complex.DynFieldPos;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.variables.complex.DynJson;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.variables.complex.DynList;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.variables.primitives.DynBoolean;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.variables.primitives.DynNumber;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.variables.primitives.DynString;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class Variable {
    // gotta do this dumb thing to make it work
    private static BiConsumer<Variable,Variable> setVar;
    private static Consumer<Variable> registerVar;
    public static void registerVarSettersGetters(BiConsumer<Variable,Variable> method, Consumer<Variable> method2){
        setVar = method;
        registerVar = method2;
    }
    public void setVariable(Variable in){
        // Build a new instance of in's own class carrying THIS variable's identity
        // (name + literal flag). Cloning `in` verbatim produced a nameless literal, which
        // broke the ID binding in VariableManager and froze the variable after one write.
        setVar.accept(this, cloneAs(in, this.name, this.literal));
    }
    public void registerVar(Variable in){
        registerVar.accept(in);
    }

    private final VariableTypes type;
    protected Object value;
    private final boolean literal;
    private final String name;

    public Variable(VariableTypes type, Object value, String name){
        this.type = type;
        this.value = value;
        this.name = name;
        literal = false;
    }
    public Variable(VariableTypes type, Object value){
        this.type = type;
        this.value = value;
        this.name = "Literal";
        literal = true;
    }

    public String getName(){
        return name;
    }
    public boolean isLiteral(){
        return literal;
    }
    public Object getValue(){
        return value;
    }
    public VariableTypes getType(){
        return type;
    }
    public String getTelemetryData(){
        throwErr("getTelemetryData",new Object[0],"Method not implemented for variable "+type);
        return null;
    }

    public String toString(){
        // type: value
        String out = "";
        if (this.literal){
            out = "Literal:";
        }
        switch (this.type){
            case Number -> out = out+"Number:";
            case String -> out = out+"String:";
            case Boolean -> out = out+"Boolean:";
            case List -> out = out+"List:";
            case Json -> out = out+"Json:";
            case FieldCord -> out = out+"FieldCord:";
            case FieldPos -> out = out+"FieldPos:";
            default -> out = out+"NULL (XTRA BAD):";
        }
        out = out+value;
        return out;
    }
    public String valueToString(){
        return value.toString();
    }
    
    // overridable operations
    public void Add(Variable in1, Variable in2){throwErr("Add",new Object[]{in1,in2},"invalid method for var "+type);}
    public void Add(Variable in){throwErr("Add",new Object[]{in},"invalid method for var "+type);}
    
    public void Sub(Variable in1, Variable in2){throwErr("Sub",new Object[]{in1,in2},"invalid method for var "+type);}
    public void Sub(Variable in){throwErr("Sub",new Object[]{in},"invalid method for var "+type);}
    
    public void Mux(Variable in1, Variable in2){throwErr("Mux",new Object[]{in1,in2},"invalid method for var "+type);}
    public void Mux(Variable in){throwErr("Mux",new Object[]{in},"invalid method for var "+type);}
    
    public void Div(Variable in1, Variable in2){throwErr("Div",new Object[]{in1,in2},"invalid method for var "+type);}
    public void Div(Variable in){throwErr("Div",new Object[]{in},"invalid method for var "+type);}
    
    public void Pow(Variable in1, Variable in2){throwErr("Pow",new Object[]{in1,in2},"invalid method for var "+type);}
    public void Pow(Variable in){throwErr("Pow",new Object[]{in},"invalid method for var "+type);}
    
    public void Sqrt(Variable in){throwErr("Sqrt",new Object[]{in},"invalid method for var "+type);}
    public void Sqrt(){throwErr("Sqrt", new Object[]{}, "invalid method for var "+type);}
    
    public void Sin(Variable in){throwErr("Sin",new Object[]{in},"invalid method for var "+type);}
    public void Sin(){throwErr("Sin", new Object[]{}, "invalid method for var "+type);}
    
    public void iSin(Variable in){throwErr("iSin",new Object[]{in},"invalid method for var "+type);}
    public void iSin(){throwErr("iSin", new Object[]{}, "invalid method for var "+type);}
    
    public void Cos(Variable in){throwErr("Cos",new Object[]{in},"invalid method for var "+type);}
    public void Cos(){throwErr("Cos", new Object[]{}, "invalid method for var "+type);}
    
    public void iCos(Variable in){throwErr("iCos",new Object[]{in},"invalid method for var "+type);}
    public void iCos(){throwErr("iCos", new Object[]{}, "invalid method for var "+type);}

    public void Tan(Variable in){throwErr("Tan",new Object[]{in},"invalid method for var "+type);}
    public void Tan(){throwErr("Tan", new Object[]{}, "invalid method for var "+type);}

    public void iTan(Variable in){throwErr("iTan",new Object[]{in},"invalid method for var "+type);}
    public void iTan(){throwErr("iTan", new Object[]{}, "invalid method for var "+type);}

    public void toDeg(Variable in){throwErr("toDeg",new Object[]{in},"invalid method for var "+type);}
    public void toDeg(){throwErr("toDeg", new Object[]{}, "invalid method for var "+type);}

    public void toRad(Variable in){throwErr("toRad",new Object[]{in},"invalid method for var "+type);}
    public void toRad(){throwErr("toRad",new Object[]{},"invalid method for var "+type);}
    
    public void Inc(){throwErr("Increment",new Object[]{},"invalid method for var "+type);}
    public void Dec(){throwErr("Decrement",new Object[]{},"invalid method for var "+type);}

    public boolean equals(Variable in){throwErr("equals",new Object[]{in},"invalid method for var "+type); return false;}
    public boolean and(Variable in){throwErr("And",new Object[]{in},"invalid method for var "+type); return false;}
    public boolean or(Variable in){throwErr("Or",new Object[]{in},"invalid method for var "+type); return false;}
    public boolean not(){throwErr("Not",new Object[]{this},"invalid method for var "+type); return false;}

    public boolean lessThan(Variable in){throwErr("Less Than (<)",new Object[]{in},"invalid method for var "+type); return false;}
    public boolean moreThan(Variable in){throwErr("More Than (>)",new Object[]{in},"invalid method for var "+type); return false;}

    public boolean lessThanEq(Variable in){throwErr("More Than Eqauls (>=)",new Object[]{in},"invalid method for var "+type); return false;}
    public boolean moreThanEq(Variable in){throwErr("More Than Equals (>=)",new Object[]{in},"invalid method for var "+type); return false;}
    // json/list ops (ts so cooked) executed as if THIS VAR is the out (if not that then we are the given list/json)
    //get
    // take json/listVar id/index outVar(this)
    public void set2get(Variable in, int index){throwErr("Get",new Object[]{in,index},"invalid method for var "+type);}
    public void set2get(Variable in, Variable id){throwErr("Get",new Object[]{in,id},"invalid method for var "+type);}
    // helper functions (can be overrided) for lists and arrays
    public Variable getFromID(Variable id){throwErr("Get", new Object[]{id}, "method not overridden"); return null;}
    public Variable getFromIndex(int index){throwErr("Get",new Object[]{index},"invalid method for var "+type); return null;}
    //insert
    // take inVar id/index targetList/json
    public void insertVar(Variable in, int index){throwErr("Insert",new Object[]{in,index},"invalid method for var "+type);}
    public void insertVar(Variable in, Variable id){throwErr("Insert",new Object[]{in,id},"invalid method for var "+type);}
    //append
    // take inVar listVar
    // take inVar id listVar
    public void append(Variable in){throwErr("Append",new Object[]{in},"invalid method for var "+type);}
    public void append(Variable in, Variable id){throwErr("Append",new Object[]{in,id},"invalid method for var "+type);}
    //remove
    // take index/id list/jsonVar
    public void remove(int index){throwErr("Remove",new Object[]{index},"invalid method for var "+type);}
    public void remove(Variable id){throwErr("Remove",new Object[]{id},"invalid method for var "+type);}
    // take index/id list/jsonVar outVar
    public void setToRemove(Variable list, int index){throwErr("Set",new Object[]{list,index},"invalid method for var "+type);}
    public void setToRemove(Variable json, Variable id){throwErr("Set",new Object[]{json,id},"invalid method for var "+type);}
    //set
    // take id/index inVar
    public void set(int index, Variable in){throwErr("Set",new Object[]{index,in},"invalid method for var "+type);}
    public void set(Variable id, Variable in){throwErr("Set",new Object[]{id,in},"invalid method for var "+type);}

    // setters
    // setters
    /**
     * Builds a new Variable of the SAME CONCRETE CLASS as `src`, holding a copy of its value,
     * but stamped with the supplied identity. Single point of subclass selection, so a type
     * change on assignment produces a genuine class swap rather than a mislabelled object.
     */
    public static Variable cloneAs(Variable src, String newName, boolean asLiteral){
        switch (src.getType()){
            case Number -> {
                double v = (double)src.getValue();
                return asLiteral ? new DynNumber(v) : new DynNumber(v,newName);
            }
            case Boolean -> {
                boolean v = (boolean)src.getValue();
                return asLiteral ? new DynBoolean(v) : new DynBoolean(v,newName);
            }
            case String -> {
                String v = (String)src.getValue();
                return asLiteral ? new DynString(v) : new DynString(v,newName);
            }
            case FieldCord -> {
                Variable[] v = copyElements((Variable[])src.getValue());
                return asLiteral ? new DynFieldCord(v) : new DynFieldCord(v,newName);
            }
            case FieldPos -> {
                Variable[] v = copyElements((Variable[])src.getValue());
                return asLiteral ? new DynFieldPos(v) : new DynFieldPos(v,newName);
            }
            case List -> {
                ArrayList<Variable> v = copyElements((ArrayList<Variable>)src.getValue());
                return asLiteral ? new DynList(v) : new DynList(v,newName);
            }
            case Json -> {
                Map<Variable,Variable> v = copyElements((Map<Variable,Variable>)src.getValue());
                return asLiteral ? new DynJson(v) : new DynJson(v,newName);
            }
            default -> throw new VariableException(
                    "cloneAs", src.toString(), "Cannot clone variable of type "+src.getType());
        }
    }

    // Element copying keeps the original semantics: literals are copied (no identity worth
    // sharing), named variables are shared by reference so they stay aliased.
    private static Variable[] copyElements(Variable[] in){
        Variable[] out = new Variable[in.length];
        for (int i = 0; i < in.length; i++){
            out[i] = in[i].isLiteral() ? in[i].getClone() : in[i];
        }
        return out;
    }
    private static ArrayList<Variable> copyElements(ArrayList<Variable> in){
        ArrayList<Variable> out = new ArrayList<>(in.size());
        for (Variable var : in){
            out.add(var.isLiteral() ? var.getClone() : var);
        }
        return out;
    }
    private static Map<Variable,Variable> copyElements(Map<Variable,Variable> in){
        Map<Variable,Variable> out = new HashMap<>();
        for (Map.Entry<Variable,Variable> entry : in.entrySet()){
            Variable val = entry.getValue();
            out.put(entry.getKey(), val.isLiteral() ? val.getClone() : val);
        }
        return out;
    }

    public Variable getClone(Variable toClone){
        return cloneAs(toClone, toClone.getName(), toClone.isLiteral());
    }
    public Variable getClone(){
        return getClone(this);
    }

    public void setValue(ArrayList<Variable> in){
        Variable newVal;
        if (literal) newVal = new DynList(in);
        else newVal = new DynList(in,name);
        setVariable(newVal);
    }
    public void setValue(Map<Variable,Variable> in){
        Variable newVal;
        if (literal) newVal = new DynJson(in);
        else newVal = new DynJson(in,name);
        setVariable(newVal);
    }
    public void setValue(String in){
        Variable newVal;
        if (literal) newVal = new DynString(in);
        else newVal = new DynString(in,name);
        setVariable(newVal);
    }
    public void setValue(boolean in){
        Variable newVal;
        if (literal) newVal = new DynBoolean(in);
        else newVal = new DynBoolean(in,name);
        setVariable(newVal);
    }
    public void setValue(double in){
        Variable newVal;
        if (literal) newVal = new DynNumber(in);
        else newVal = new DynNumber(in,name);
        setVariable(newVal);
    }
    public void setValue(double[] in){
        if (in.length == 2) {
            Variable newVal;
            if (literal) newVal = new DynFieldCord(in);
            else newVal = new DynFieldCord(in,name);
            setVariable(newVal);
        } else if (in.length == 3){
            Variable newVal;
            if (literal) newVal = new DynFieldPos(in);
            else newVal = new DynFieldPos(in,name);
            setVariable(newVal);
        }
    }

    // kinda a thing i made to express to DYN programmers if they are doing a bad to thing to a var. (should primarily trigger for null type variables, which should never happen)
    protected void throwErr(String method, Object[] vars, String reason){
        StringBuilder involvedVars = new StringBuilder();
        for (int i = 0; i < vars.length; i++){
            involvedVars.append(vars[i].toString());
            if (!(i == vars.length-1)){
                involvedVars.append(", ");
            }
        }
        throw new VariableException(method, involvedVars.toString(), reason);
    }
}
