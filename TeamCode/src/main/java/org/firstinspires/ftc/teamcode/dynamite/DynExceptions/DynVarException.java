package org.firstinspires.ftc.teamcode.dynamite.DynExceptions;

import org.firstinspires.ftc.teamcode.dynamite.DynVar.DynVar;

public class DynVarException extends RuntimeException{
    public DynVarException(String VarID, DynVar VarVal, String reason){
        super("Error processing org.firstinspires.ftc.teamcode.dynamite.DynVar.DynVar function: "+reason+" ID: "+VarID+" Value: "+VarVal.toString());
    }
    public DynVarException(String VarID1, DynVar VarVal1, String VarID2, DynVar VarVal2, String reason){
        super("Error processing org.firstinspires.ftc.teamcode.dynamite.DynVar.DynVar function interaction: "+reason+" ID0: "+VarID1+" Value0: "+VarVal1.toString()+" ID1: "+VarID2+" Value1: "+VarVal2.toString());
    }
}
