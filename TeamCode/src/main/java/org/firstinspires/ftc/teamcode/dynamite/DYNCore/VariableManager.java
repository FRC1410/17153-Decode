package org.firstinspires.ftc.teamcode.dynamite.DYNCore;

import org.firstinspires.ftc.teamcode.dynamite.DYNCore.variables.Variable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class VariableManager {
    private final ArrayList<Variable> allVars = new ArrayList<>(); // this is dedicated to containing all created literals.
    private final ArrayList<String> registeredIDs = new ArrayList<>();
    private final Map<String, Variable> VarIdMap = new HashMap<>();

    public VariableManager(){
        Variable.registerVarSettersGetters(this::setVar, this::registerVar);
    }

    private void setVar(Variable target, Variable value){
        int targetIndex = allVars.indexOf(target);
        if (targetIndex == -1){
            //System.out.println("WARNING!! DYN could not find registered var: "+target.toString()+"!!");
            allVars.add(target);
            targetIndex = allVars.indexOf(target);
            allVars.set(targetIndex, value);
        } else {
            allVars.set(targetIndex, value);
        }
        if (!target.isLiteral()) VarIdMap.put(target.getName(),value); // fully ID->var replace the target value
    }

    public Variable getVar(String ID){
        if (registeredIDs.contains(ID)) return VarIdMap.get(ID);
        return null;
    }
    public void registerVar(Variable var){
        if (!allVars.contains(var)) {
            if (!var.isLiteral()) {
                registeredIDs.add(var.getName());
                VarIdMap.put(var.getName(), var);
            }
            allVars.add(var);
        }
    }

    public String toString(){
        StringBuilder debug = new StringBuilder("Variable manager dump:\n    Linked vars:");
        for (String id : registeredIDs){
            Variable val = VarIdMap.get(id);
            debug.append("\n        ");
            debug.append(id);
            debug.append(" -> ");
            debug.append(val.toString());
        }
        debug.append("\n    Unlinked vars:");
        for (Variable var : allVars){
            if (!var.isLiteral()){
                debug.append("\n        ");
                debug.append(var);
            }
        }
        return debug.toString();
    }
}
