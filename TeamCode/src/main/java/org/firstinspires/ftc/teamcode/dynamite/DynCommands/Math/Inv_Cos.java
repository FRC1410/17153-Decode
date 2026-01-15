package org.firstinspires.ftc.teamcode.dynamite.DynCommands.Math;

import org.firstinspires.ftc.teamcode.dynamite.DynCommands.DynCommand;
import org.firstinspires.ftc.teamcode.dynamite.DynVar.DynVar;
import org.firstinspires.ftc.teamcode.dynamite.DynExceptions.DynVarException;
import org.firstinspires.ftc.teamcode.dynamite.DynExceptions.DynAutoStepException;

import java.util.function.BiConsumer;
import java.util.function.Function;

public class Inv_Cos implements DynCommand {
    private String Value1ID;
    private String outID;

    private BiConsumer<String, DynVar> setVar;
    private Function<String, DynVar> getVar;

    public Inv_Cos(String InID1, String OutID){
        Value1ID = InID1;
        outID = OutID;
    }
    public Inv_Cos(String IOID){
        Value1ID = IOID;
        outID = IOID;
    }

    @Override
    public void init(BiConsumer<String, DynVar> SetVarFunc, Function<String, DynVar> GetVarFunc){
        getVar = GetVarFunc;
        setVar = SetVarFunc;
    }

    @Override
    public void run(){
        DynVar v1 = getVar.apply(Value1ID);
        DynVar out = getVar.apply(outID);
        if (out == null) {
            try {
                out = new DynVar("Number", outID, 0.0);
                setVar.accept(outID, out);
            } catch (Exception e) {
                throw new DynAutoStepException("org.firstinspires.ftc.teamcode.dynamite.DynCommands.Math.Inv_Cos: failed to create output variable");
            }
        }
        out.setToIcs(v1);
    }

    @Override
    public String getDescription() {
        return "InvCos(" + Value1ID + " -> " + outID + ")";
    }
}
