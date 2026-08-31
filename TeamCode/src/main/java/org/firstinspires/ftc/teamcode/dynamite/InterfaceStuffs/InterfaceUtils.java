package org.firstinspires.ftc.teamcode.dynamite.InterfaceStuffs;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;

import org.firstinspires.ftc.teamcode.dynamite.DYNCore.CommandException;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.variables.Variable;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.variables.VariableTypes;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.variables.complex.DynFieldPos;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.variables.primitives.DynBoolean;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.variables.primitives.DynNumber;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.variables.primitives.DynString;
import org.firstinspires.ftc.teamcode.dynamite.FTCInterface.DYNInterpreter;
import org.firstinspires.ftc.teamcode.dynamite.PPInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class InterfaceUtils {
    // follower utils
    private double followerUpdateRate = 50.0;
    double getFollowerUpdateRate(){
        return followerUpdateRate;
    }
    public void setFollowerUpdateRate(double newRate){
        followerUpdateRate = newRate;
    }

    // thread priority utils
    private int DynThreadPriority = Thread.MIN_PRIORITY;
    private int FollowerThreadPriority = Thread.currentThread().getPriority()+1;
    int getDynThreadPriority() {
        return DynThreadPriority;
    }
    int getFollowerThreadPriority() {
        return FollowerThreadPriority;
    }
    public void setDynThreadPriority(int priority){
        DynThreadPriority = priority;
    }
    public void setFollowerThreadPriority(int priority){
        FollowerThreadPriority = priority;
    }

    // jFunc utils
    private PPInterface ppInterface;
    private DYNInterpreter interpreter;
    void registerDYNstuffs(PPInterface ppInterface, DYNInterpreter interpreter){
        this.ppInterface = ppInterface;
        this.interpreter = interpreter;
    }
    private final Map<String, Function<Variable,Variable>> functionJFuncs = new HashMap<>();
    private final Map<String, Consumer<Variable>> consumerJFuncs = new HashMap<>();
    private final Map<String, Supplier<Variable>> supplierJFuncs = new HashMap<>();
    private final Map<String,Runnable> runnableJFuncs = new HashMap<>();
    public final void registerJFunc(String ID, Function<Variable,Variable> func){
        functionJFuncs.put(ID,func);
    }
    public final void registerJFunc(String ID, Consumer<Variable> func){
        consumerJFuncs.put(ID,func);
    }
    public final void registerJFunc(String ID, Supplier<Variable> func){
        supplierJFuncs.put(ID,func);
    }
    public final void registerJFunc(String ID, Runnable func){
        runnableJFuncs.put(ID,func);
    }
    void processJFuncCalls() {
        // this ensures that only one thread is touching this handshake process at a time.
        synchronized (ppInterface.lock){
            if (ppInterface.requested){
                ppInterface.requested = false;
                if (ppInterface.wantOutput){
                    if (ppInterface.inVar == null){
                        // search suppliers
                        if (supplierJFuncs.containsKey(ppInterface.funcID)){
                            ppInterface.outVar = supplierJFuncs.get(ppInterface.funcID).get();
                            if (ppInterface.outVar == null) throw new CommandException(ppInterface.ranLine,"jFunc","Called function returned null!");
                            // normalize state
                            ppInterface.funcID = null;
                            ppInterface.inVar = null;
                            // notify DYN thread
                            ppInterface.processed = true;
                            ppInterface.lock.notify();
                        } else {
                            throw new CommandException(ppInterface.ranLine,"Move robot","Unknown jFunc ID: "+ppInterface.funcID);
                        }
                    } else {
                        // search functions
                        if (functionJFuncs.containsKey(ppInterface.funcID)){
                            ppInterface.outVar = functionJFuncs.get(ppInterface.funcID).apply(ppInterface.inVar);
                            // normalize state
                            ppInterface.funcID = null;
                            ppInterface.inVar = null;
                            // notify DYN thread
                            ppInterface.processed = true;
                            ppInterface.lock.notify();
                        } else {
                            throw new CommandException(ppInterface.ranLine,"Move robot","Unknown jFunc ID: "+ppInterface.funcID);
                        }
                    }
                }
                else {
                    if (ppInterface.inVar == null) {
                        // search runnables
                        if (runnableJFuncs.containsKey(ppInterface.funcID)){
                            runnableJFuncs.get(ppInterface.funcID).run();
                            // normalize state
                            ppInterface.funcID = null;
                            ppInterface.inVar = null;
                            ppInterface.outVar = null;
                            // notify DYN thread
                            ppInterface.processed = true;
                            ppInterface.lock.notify();
                        } else {
                            throw new CommandException(ppInterface.ranLine,"Move robot","Unknown jFunc ID: "+ppInterface.funcID);
                        }
                    } else {
                        // search consumers
                        if (consumerJFuncs.containsKey(ppInterface.funcID)){
                            consumerJFuncs.get(ppInterface.funcID).accept(ppInterface.inVar);
                            // normalize state
                            ppInterface.funcID = null;
                            ppInterface.inVar = null;
                            ppInterface.outVar = null;
                            // notify DYN thread
                            ppInterface.processed = true;
                            ppInterface.lock.notify();
                        } else {
                            throw new CommandException(ppInterface.ranLine,"Move robot","Unknown jFunc ID: "+ppInterface.funcID);
                        }
                    }
                }
            }
        }
    }

    // variable stuffs
    public boolean isVariableFieldCoord(Variable var){
        return var.getType() == VariableTypes.FieldCord;
    }
    public boolean isVariableBoolean(Variable var){
        return var.getType() == VariableTypes.Boolean;
    }
    public boolean isVariableFieldPose(Variable var){
        return var.getType() == VariableTypes.FieldPos;
    }
    public boolean isVariableString(Variable var){
        return var.getType() == VariableTypes.String;
    }
    public boolean isVariableNumber(Variable var){
        return var.getType() == VariableTypes.Number;
    }
    public boolean isVariableList(Variable var){
        return var.getType() == VariableTypes.List;
    }
    public boolean isVariableJson(Variable var){
        return var.getType() == VariableTypes.Json;
    }
    // variable value extractors
    public Map<Variable,Variable> getJsonFromVariable(Variable var){
        if (isVariableJson(var)){
            return (Map<Variable,Variable>)var.getValue();
        } else {
            throw new RuntimeException("Variable "+var.getTelemetryData()+" is not a Json!");
        }
    }
    public ArrayList<Variable> getListFromVariable(Variable var){
        if (isVariableList(var)){
            return (ArrayList<Variable>)var.getValue();
        } else {
            throw new RuntimeException("Variable "+var.getTelemetryData()+" is not a List!");
        }
    }
    public boolean getBooleanFromVariable(Variable var){
        if (isVariableBoolean(var)){
            return (boolean)var.getValue();
        } else {
            throw new RuntimeException("Variable "+var.getTelemetryData()+" is not a Boolean!");
        }
    }
    public double getNumberFromVariable(Variable var){
        if (isVariableNumber(var)){
            return (double)var.getValue();
        } else {
            throw new RuntimeException("Variable "+var.getTelemetryData()+" is not a Number!");
        }
    }
    public String getStringFromVariable(Variable var){
        if (isVariableString(var)){
            return (String)var.getValue();
        } else {
            throw new RuntimeException("Variable "+var.getTelemetryData()+" is not a String!");
        }
    }
    public Pose getFieldPosFromVariable(Variable var){
        if (isVariableFieldCoord(var)) {
            Variable[] coords = (Variable[]) var.getValue();
            return new Pose(
                    (double) coords[0].getValue(),
                    (double) coords[1].getValue());
        } else if (isVariableFieldPose(var)){
            Variable[] pose = (Variable[])var.getValue();
            return new Pose(
                    (double)pose[0].getValue(),
                    (double)pose[1].getValue(),
                    (double)pose[2].getValue());
        } else {
            throw new RuntimeException("Variable "+var.getTelemetryData()+" is not a Field Position/Coordinate!");
        }
    }
    // Variable makers
    public final Variable makeBooleanVar(boolean value){
        Variable var = new DynBoolean(value);
        interpreter.registerVar(var);
        return var;
    }
    public final Variable makeNumberVar(double value){
        Variable var = new DynNumber(value);
        interpreter.registerVar(var);
        return var;
    }
    public final Variable makeStringVar(String value){
        Variable var = new DynString(value);
        interpreter.registerVar(var);
        return var;
    }
    public final Variable makePoseVar(Pose value){
        Variable var = new DynFieldPos(value.getX(),value.getY(),value.getHeading());
        interpreter.registerVar(var);
        return var;
    }
    public final Variable makeVarCopy(Variable var){
        Variable val = var.getClone();
        interpreter.registerVar(var);
        return val;
    }
}
