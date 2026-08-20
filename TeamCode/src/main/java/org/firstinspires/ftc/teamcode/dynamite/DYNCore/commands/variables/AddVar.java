package org.firstinspires.ftc.teamcode.dynamite.DYNCore.commands.variables;

import org.firstinspires.ftc.teamcode.dynamite.DYNCore.CommandException;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.commands.Command;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.commands.CommandType;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.commands.controlFlow.Condition;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.variables.complex.DynFieldCord;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.variables.complex.DynFieldPos;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.variables.complex.DynJson;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.variables.complex.DynList;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.variables.primitives.DynBoolean;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.variables.primitives.DynNumber;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.variables.primitives.DynString;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.variables.Variable;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.variables.VariableTypes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AddVar extends Command {
    private String varName;
    private final VariableTypes variableType;
    private final Object value;

    public AddVar(int line, String name, VariableTypes type, Object value){
        super(line, CommandType.AddVar, new String[]{name,type.toString(),String.valueOf(value)});
        this.varName = name;
        this.variableType = type;
        this.value = value;
    }
    public AddVar(int line, VariableTypes type, Object value){
        super(line, CommandType.AddVar, new String[]{type.toString(),String.valueOf(value)});
        this.variableType = type;
        this.value = value;
    }

    @Override
    public void run(){
        Variable newVar = null;
        switch(variableType){
            case Number -> {
                if (value instanceof Double) {
                    if (varName != null) newVar = new DynNumber((double)value, varName);
                    else newVar = new DynNumber((double) value);
                } else if (value instanceof Integer){
                    if (varName != null) newVar = new DynNumber((int)value, varName);
                    else newVar = new DynNumber((int) value);
                } else if (value instanceof Float){
                    if (varName != null) newVar = new DynNumber((float)value, varName);
                    else newVar = new DynNumber((float) value);
                } else if (value instanceof Long){
                    if (varName != null) newVar = new DynNumber((long)value, varName);
                    else newVar = new DynNumber((long) value);
                } else {
                    throw new CommandException(line, "AddVar", "Given value is not a number!");
                }
            }
            case String -> {
                if (value instanceof DynString){
                    if (varName != null) newVar = new DynString((String)((DynString)value).getValue(),varName);
                    else newVar = new DynString((String)((DynString)value).getValue());
                } else if (value instanceof String){
                    String val = (String)getVar((String)value).getValue();
                    if (varName != null) newVar = new DynString(val,varName);
                    else newVar = new DynString(val);
                } else {
                    throw new CommandException(line, "AddVar", "Given value is not a string!");
                }
            }
            case Boolean -> {
                if (value instanceof Boolean){
                    if (varName != null) newVar = new DynBoolean((boolean)value,varName);
                    else newVar = new DynBoolean((boolean)value);
                } else if (value instanceof Condition){
                    if (varName != null) newVar = new DynBoolean((Condition)value,varName);
                    else newVar = new DynBoolean((Condition)value);
                } else {
                    throw new CommandException(line, "AddVar", "Given value is not a boolean!");
                }
            }
            case List -> {
                if (value instanceof ArrayList<?>){
                    // we assume that we are given an array of objects that we convert into variables
                    // the object can be any dyn variable or a String (which represents a variable ID)
                    ArrayList<Variable> realValue = new ArrayList<>();
                    for (Object item : (ArrayList<Object>)value){
                        if (item instanceof String){
                            Variable gotten = getVar((String)item);
                            if (gotten == null) throw new CommandException(line,"AddVar","Variable "+item+" not defined!");
                            realValue.add(gotten);
                        } else if (item instanceof DynBoolean){
                            realValue.add((Variable)item);
                        } else if (item instanceof DynNumber){
                            realValue.add((Variable)item);
                        } else if (item instanceof DynString){
                            realValue.add((Variable)item);
                        }
                        // skip any invalid value
                    }
                    if (varName != null) newVar = new DynList(realValue,varName);
                    else newVar = new DynList(realValue);
                } else {
                    throw new CommandException(line, "AddVar", "Given value is not a List!");
                }
            }
            case Json -> {
                if (value instanceof Map<?,?>) {
                    // we do a similar to what is done in List data processing
                    Map<Object,Object> valueMap = ((Map<Object,Object>)value);
                    Map<Variable, Variable> realValue = new HashMap<>();
                    for (Object k : valueMap.keySet()){
                        Object v = valueMap.get(k);
                        Variable val = null;
                        if (v instanceof String){
                            val = getVar((String)v);
                            if (val == null) throw new CommandException(line,"AddVar","Variable "+v+" not defined!");
                        } else if (v instanceof DynBoolean){
                            val = (Variable)v;
                        } else if (v instanceof DynNumber){
                            val = (Variable)v;
                        } else if (v instanceof DynString){
                            val = (Variable)v;
                        }
                        if (k instanceof String){
                            Variable key = getVar((String)k);
                            if (key == null) throw new CommandException(line,"AddVar","Variable "+k+" not defined!");
                            realValue.put(key,val);
                        } else if (k instanceof DynBoolean){
                            realValue.put((Variable)k,val);
                        } else if (k instanceof DynNumber){
                            realValue.put((Variable)k,val);
                        } else if (k instanceof DynString){
                            realValue.put((Variable)k,val);
                        }
                    }
                    if (varName != null) newVar = new DynJson(realValue, varName);
                    else newVar = new DynJson(realValue);
                } else {
                    throw new CommandException(line, "AddVar", "Given value is not a json!"); // I think that its literally impossible for this to fire
                }
            }
            case FieldCord -> {
                if (value instanceof Object[]) {
                    if (((Object[])value)[0] instanceof Double){
                        if (((Object[])value)[1] instanceof Double){
                            if (varName != null) newVar = new DynFieldCord(new double[]{(double)((Object[])value)[0], (double)((Object[])value)[1]},varName);
                            else newVar = new DynFieldCord(new double[]{(double)((Object[])value)[0], (double)((Object[])value)[1]});
                        } else {
                            if (varName != null) newVar = new DynFieldCord(new Variable[]{new DynNumber((double)((Object[])value)[0]), getVar((String)((Object[])value)[1])},varName);
                            else newVar = new DynFieldCord(new Variable[]{new DynNumber((double)((Object[])value)[0]), getVar((String)((Object[])value)[1])});
                        }
                    } else{
                        if (((Object[])value)[1] instanceof Double){
                            if (varName != null) newVar = new DynFieldCord(new Variable[]{getVar((String)((Object[])value)[0]), new DynNumber((double)((Object[])value)[1])},varName);
                            else newVar = new DynFieldCord(new Variable[]{getVar((String)((Object[])value)[0]), new DynNumber((double)((Object[])value)[1])},varName);
                        } else {
                            if (varName != null) newVar = new DynFieldCord(new Variable[]{getVar((String)((Object[])value)[0]),getVar((String)((Object[])value)[1])},varName);
                            else newVar = new DynFieldCord(new Variable[]{getVar((String)((Object[])value)[0]),getVar((String)((Object[])value)[1])},varName);
                        }
                    }
                } else if (value instanceof double[]){
                    if (varName != null) newVar = new DynFieldCord((double[]) value, varName);
                    else newVar = new DynFieldCord((double[]) value);
                } else {
                    throw new CommandException(line, "AddVar", "Given value is not a field coord!");
                }
            }
            case FieldPos -> {
                if (value instanceof Object[]) {
                    if (((Object[])value)[0] instanceof Double){
                        if (((Object[])value)[1] instanceof Double){
                            if (((Object[])value)[2] instanceof Double){
                                // double, double, double
                                if (varName != null) newVar = new DynFieldPos(
                                        new double[]{
                                                (double)((Object[])value)[0],
                                                (double)((Object[])value)[1],
                                                (double)((Object[])value)[2]},
                                        varName);
                                else newVar = new DynFieldPos(
                                        new double[]{
                                                (double)((Object[])value)[0],
                                                (double)((Object[])value)[1],
                                                (double)((Object[])value)[2]});
                            } else {
                                // double, double, string
                                if (varName != null) newVar = new DynFieldPos(
                                        new Variable[]{
                                                new DynNumber((double)((Object[])value)[0]),
                                                new DynNumber((double)((Object[])value)[1]),
                                                getVar((String)((Object[])value)[2])},
                                        varName);
                                else newVar = new DynFieldPos(
                                        new Variable[]{
                                                new DynNumber((double)((Object[])value)[0]),
                                                new DynNumber((double)((Object[])value)[1]),
                                                getVar((String)((Object[])value)[2])});
                            }
                        } else {
                            if (((Object[])value)[2] instanceof Double){
                                // double, string, double
                                if (varName != null) newVar = new DynFieldPos(
                                        new Variable[]{
                                                new DynNumber((double)((Object[])value)[0]),
                                                getVar((String)((Object[])value)[1]),
                                                new DynNumber((double)((Object[])value)[2])},
                                        varName);
                                else newVar = new DynFieldPos(
                                        new Variable[]{
                                                new DynNumber((double)((Object[])value)[0]),
                                                getVar((String)((Object[])value)[1]),
                                                new DynNumber((double)((Object[])value)[2])});
                            } else {
                                // double, string, string
                                if (varName != null) newVar = new DynFieldPos(
                                        new Variable[]{
                                                new DynNumber((double)((Object[])value)[0]),
                                                getVar((String)((Object[])value)[1]),
                                                getVar((String)((Object[])value)[2])},
                                        varName);
                                else newVar = new DynFieldPos(
                                        new Variable[]{
                                                new DynNumber((double)((Object[])value)[0]),
                                                getVar((String)((Object[])value)[1]),
                                                getVar((String)((Object[])value)[2])});
                            }
                        }
                    } else {
                        if (((Object[])value)[1] instanceof Double){
                            if (((Object[])value)[2] instanceof Double){
                                // string, double, double
                                if (varName != null) newVar = new DynFieldPos(
                                        new Variable[]{
                                                getVar((String)((Object[])value)[0]),
                                                new DynNumber((double)((Object[])value)[1]),
                                                new DynNumber((double)((Object[])value)[2])},
                                        varName);
                                else newVar = new DynFieldPos(
                                        new Variable[]{
                                                getVar((String)((Object[])value)[0]),
                                                new DynNumber((double)((Object[])value)[1]),
                                                new DynNumber((double)((Object[])value)[2])});
                            } else {
                                // string, double, string
                                if (varName != null) newVar = new DynFieldPos(
                                        new Variable[]{
                                                getVar((String)((Object[])value)[0]),
                                                new DynNumber((double)((Object[])value)[1]),
                                                getVar((String)((Object[])value)[2])},
                                        varName);
                                else newVar = new DynFieldPos(
                                        new Variable[]{
                                                getVar((String)((Object[])value)[0]),
                                                new DynNumber((double)((Object[])value)[1]),
                                                getVar((String)((Object[])value)[2])});
                            }
                        } else {
                            if (((Object[])value)[2] instanceof Double){
                                // string, string, double
                                if (varName != null) newVar = new DynFieldPos(
                                        new Variable[]{
                                                getVar((String)((Object[])value)[0]),
                                                getVar((String)((Object[])value)[1]),
                                                new DynNumber((double)((Object[])value)[2])},
                                        varName);
                                else newVar = new DynFieldPos(
                                        new Variable[]{
                                                getVar((String)((Object[])value)[0]),
                                                getVar((String)((Object[])value)[1]),
                                                new DynNumber((double)((Object[])value)[2])});
                            } else {
                                // string, string, string
                                if (varName != null) newVar = new DynFieldPos(
                                        new Variable[]{
                                                getVar((String)((Object[])value)[0]),
                                                getVar((String)((Object[])value)[1]),
                                                getVar((String)((Object[])value)[2])},
                                        varName);
                                else newVar = new DynFieldPos(
                                        new Variable[]{
                                                getVar((String)((Object[])value)[0]),
                                                getVar((String)((Object[])value)[1]),
                                                getVar((String)((Object[])value)[2])});
                            }
                        }
                    }
                } else if (value instanceof double[]){
                    if (varName != null) newVar = new DynFieldPos((double[]) value, varName);
                    else newVar = new DynFieldCord((double[]) value);
                } else {
                    throw new CommandException(line, "AddVar", "Given value is not a field pos!");
                }
            }
            default -> throw new CommandException(line, "AddVar", "Unknown variable type: "+variableType);
        }
        registerVar(newVar); // we just gotta hope and pray to god that the sun doesn't blast an unlucky robot controller and uppin deletes the type enum.
    }
}
