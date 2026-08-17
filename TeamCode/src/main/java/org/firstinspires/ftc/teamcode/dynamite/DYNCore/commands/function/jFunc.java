package org.firstinspires.ftc.teamcode.dynamite.DYNCore.commands.function;

import org.firstinspires.ftc.teamcode.dynamite.DYNCore.commands.Command;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.commands.CommandType;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.variables.primitives.DynNumber;

public class jFunc extends Command {
    public enum funcType{
        I,IO,O,S // for input only, Input and output, output only, and static (no args)
    }
    private final funcType type;
    private final String inFuncID;
    private String inID = "";
    private String outID = "";
    public jFunc(int line, String inFuncID){
        super(line, CommandType.jFunc,"");
        type = funcType.S;
        this.inFuncID = inFuncID;
    }
    public jFunc(int line, String inFuncID, String Var, boolean IeO){
        super(line,CommandType.jFunc,"");
        this.inFuncID = inFuncID;
        if (IeO){
            super.InVarIDs = new String[]{Var};
            type = funcType.I;
            inID = Var;
        } else {
            super.OutVarID = Var;
            type = funcType.O;
            outID = Var;
        }
    }
    public jFunc(int line, String inFuncID, String inVar, String outVar){
        super(line,CommandType.jFunc,new String[]{inVar},outVar);
        this.inFuncID = inFuncID;
        type = funcType.IO;
        inID = inVar;
        outID = outVar;
    }

    @Override
    public void run(){
        super.run();
        switch (type) {
            case S -> runJFunc(false,inFuncID);
            case I -> runJFunc(false,inFuncID,getVar(inID));
            case O -> {
                if (!varExists(outID)){
                    registerVar(new DynNumber(0,outID));
                }
                getVar(outID).setVariable(runJFunc(true, inFuncID));
            }
            case IO -> {
                if (!varExists(outID)){
                    registerVar(new DynNumber(0,outID));
                }
                getVar(outID).setVariable(runJFunc(true, inFuncID, getVar(inID)));
            }
        }
    }
}
