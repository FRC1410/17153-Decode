package org.firstinspires.ftc.teamcode.dynamite.commandSequencer.Commands.variables;

import org.firstinspires.ftc.teamcode.dynamite.commandSequencer.Commands.Command;
import org.firstinspires.ftc.teamcode.dynamite.commandSequencer.CommandException;
import org.firstinspires.ftc.teamcode.dynamite.commandSequencer.Commands.CommandType;
import org.firstinspires.ftc.teamcode.dynamite.commandSequencer.variables.Variable;
import org.firstinspires.ftc.teamcode.dynamite.commandSequencer.variables.VariableTypes;

import java.util.ArrayList;
import java.util.Map;

public class SetVar extends Command {
    private Object value;
    private final VariableTypes valueType;
    public SetVar(int line, String target, String value, boolean isVar){
        super(line, CommandType.SetVar,new String[]{target});
        if (isVar) {
            super.OutVarID = value;
            valueType = null;
        } else {
            this.value = value;
            valueType = VariableTypes.String;
        }
    }
    public SetVar(int line, String target, Map<Variable,Variable> value){
        super(line, CommandType.SetVar,new String[]{target});
        valueType = VariableTypes.Json;
        this.value = value;
    }
    public SetVar(int line, String target, ArrayList<Variable> value){
        super(line, CommandType.SetVar,new String[]{target});
        valueType = VariableTypes.List;
        this.value = value;
    }
    public SetVar(int line, String target, boolean value){
        super(line, CommandType.SetVar,new String[]{target});
        valueType = VariableTypes.Boolean;
        this.value = value;
    }
    public SetVar(int line, String target, double value){
        super(line, CommandType.SetVar,new String[]{target});
        valueType = VariableTypes.Number;
        this.value = value;
    }
    public SetVar(int line, String target, double[] value){
        super(line, CommandType.SetVar,new String[]{target});
        if (value.length == 3) valueType = VariableTypes.FieldPos;
        else if (value.length == 2) valueType = VariableTypes.FieldCord;
        else throw new CommandException(super.line,"SetVar","cannot set variable to FieldPos/Cord with invalid params");
        this.value = value;
    }

    @Override
    public void run(){
        super.run();
        // construct new var
        Variable newbie = new Variable(valueType, value);
        // apply
        getVar(InVarIDs[0]).setVariable(newbie.getClone());
    }
}
