package org.firstinspires.ftc.teamcode.dynamite.DynCommands.Math;

import org.firstinspires.ftc.teamcode.dynamite.DynCommands.DynCommand;
import org.firstinspires.ftc.teamcode.dynamite.DynVar.DynVar;
import org.firstinspires.ftc.teamcode.dynamite.DynExceptions.DynVarException;
import org.firstinspires.ftc.teamcode.dynamite.DynExceptions.DynAutoStepException;

import java.util.function.BiConsumer;
import java.util.function.Function;

public class Cos implements DynCommand {
    private String Value1ID;
    private String outID;

    private BiConsumer<String, DynVar> setVar;
    private Function<String, DynVar> getVar;

    public Cos(String InID1, String OutID){
        Value1ID = InID1;
        outID = OutID;
    }
    public Cos(String IOID){
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
                throw new DynAutoStepException("org.firstinspires.ftc.teamcode.dynamite.DynCommands.Math.Cos: failed to create output variable");
            }
        }
        out.setToCos(v1);
    }

    @Override
    public String getDescription() {
        return "org.firstinspires.ftc.teamcode.dynamite.DynCommands.Math.Cos(" + Value1ID + " -> " + outID + ")";
    }
}
