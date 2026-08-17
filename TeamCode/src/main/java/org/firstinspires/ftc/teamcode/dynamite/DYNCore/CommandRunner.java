package org.firstinspires.ftc.teamcode.dynamite.DYNCore;

import org.firstinspires.ftc.teamcode.dynamite.DYNCore.commands.Command;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.commands.controlFlow.Condition;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.commands.function.DynPath;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.commandConstructor.CommandConstructor;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.variables.Variable;

import java.util.Map;

public class CommandRunner {
    private final CommandConstructor constructor;
    private final Map<String,DynPath> mp;
    private VariableManager varMan;
    public CommandRunner(CommandConstructor constructor){
        this.constructor = constructor;
        mp = constructor.getFuncIDmap();
    }

    public void linkUpCommand(){
        // link all the command things
        varMan = new VariableManager();
        Condition.linkVarMan(varMan);
        Command.linkVarMan(varMan);
        Command.registerPathRunner(this::runPath,this::pathExists);
    }

    public void registerVar(Variable var){
        varMan.registerVar(var);
    }
    public void runPath(String pathID){
        mp.get(pathID).run();
    }
    public boolean pathExists(String pathID){
        DynPath path = mp.get(pathID);
        return (path!=null);
    }

    public String run(){
        if (mp.keySet().contains(constructor.getMainFuncName())) {
            mp.get(constructor.getMainFuncName()).run();
            if (Command.hasStopped()) {
                return Command.getExitCode();
            } else {
                return "0";
            }
        } else {
            throw new RuntimeException("Unknown function "+constructor.getMainFuncName()+"!");
        }
    }
}
