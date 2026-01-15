package org.firstinspires.ftc.teamcode.dynamite.DynCommands.Math;

import org.firstinspires.ftc.teamcode.dynamite.DynCommands.DynCommand;
import org.firstinspires.ftc.teamcode.dynamite.DynVar.DynVar;
import org.firstinspires.ftc.teamcode.dynamite.DynExceptions.DynVarException;
import org.firstinspires.ftc.teamcode.dynamite.DynExceptions.DynAutoStepException;

import java.util.function.BiConsumer;
import java.util.function.Function;

public class Sub implements DynCommand {
    private String Value1ID;
    private String Value2ID;
    private String outID;

    private BiConsumer<String, DynVar> setVar;
    private Function<String, DynVar> getVar;

    public Sub(String InID1, String InID2, String OutID){
        Value1ID = InID1;
        Value2ID = InID2;
        outID = OutID;
    }
    public Sub(String IOID, String InID){
        Value1ID = IOID;
        Value2ID = InID;
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
        DynVar v2 = getVar.apply(Value2ID);
        DynVar out = getVar.apply(outID);
        if (out == null) {
            try {
                out = new DynVar("Number", outID, 0.0);
                setVar.accept(outID, out);
            } catch (Exception e) {
                throw new DynAutoStepException("org.firstinspires.ftc.teamcode.dynamite.DynCommands.Math.Sub: failed to create output variable");
            }
        }
        out.setToSub(v1, v2);
    }

    @Override
    public String getDescription() {
        return "org.firstinspires.ftc.teamcode.dynamite.DynCommands.Math.Sub(" + Value1ID + " - " + Value2ID + " -> " + outID + ")";
    }
}
