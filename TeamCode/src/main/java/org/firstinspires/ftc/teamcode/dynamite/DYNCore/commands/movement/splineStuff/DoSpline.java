package org.firstinspires.ftc.teamcode.dynamite.DYNCore.commands.movement.splineStuff;

import org.firstinspires.ftc.teamcode.dynamite.DYNCore.CommandException;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.commands.Command;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.commands.CommandType;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.variables.Variable;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.variables.VariableTypes;

public class DoSpline extends Command {
    private final SplineType type;
    private boolean Literal = false;
    private String endTanID;
    private Double endTanVal;
    public DoSpline(int line, String Coord, String endTan, SplineType type){
        super (line, CommandType.SplineTo, new String[]{Coord, endTan});
        this.type = type;
        endTanID = endTan;
    }
    public DoSpline(int line, String Coord, double endTan, SplineType type){
        super (line, CommandType.SplineTo, new String[]{Coord, String.valueOf(endTan)});
        this.type = type;
        Literal = true;
        endTanVal = endTan;
    }
    public DoSpline(int line, String Coord, SplineType type){
        super (line, CommandType.SplineTo, new String[]{Coord});
        this.type = type;
    }

    public DoSpline(int line, String Coord, String endTan){
        super (line, CommandType.SplineTo, new String[]{Coord, endTan});
        this.type = SplineType.Normal;
        endTanID = endTan;
    }
    public DoSpline(int line, String Coord, double endTan){
        super (line, CommandType.SplineTo, new String[]{Coord, String.valueOf(endTan)});
        this.type = SplineType.Normal;
        Literal = true;
        endTanVal = endTan;
    }
    public DoSpline(int line, String Coord){
        super (line, CommandType.SplineTo, new String[]{Coord});
        this.type = SplineType.Normal;
    }

    @Override
    public void run(){
        super.run();
        double[] end;
        if (getVar(InVarIDs[0]).getType() == VariableTypes.FieldCord){
            end = new double[]{
                    (double)(((Variable[])getVar(InVarIDs[0]).getValue())[0]).getValue(),
                    (double)(((Variable[])getVar(InVarIDs[0]).getValue())[1]).getValue()};
        } else if (getVar(InVarIDs[0]).getType() == VariableTypes.FieldPos){
            end = new double[]{
                    (double)(((Variable[])getVar(InVarIDs[0]).getValue())[0]).getValue(),
                    (double)(((Variable[])getVar(InVarIDs[0]).getValue())[1]).getValue(),
                    (double)(((Variable[])getVar(InVarIDs[0]).getValue())[2]).getValue()};
        } else {
            throw new CommandException(line,"DoSpline","Was given non FieldPos/FieldCoord Variable: "+InVarIDs[0]);
        }

        if (Literal){
            doSpline(end,endTanVal,type);
        } else {
            if (endTanID != null){
                Variable tan = getVar(endTanID);
                if (tan.getType() == VariableTypes.Number){
                    doSpline(end,(double)tan.getValue(),type);
                } else {
                    throw new CommandException(line,"DoSpline","Given end tangent variable "+endTanID+" is not a Number!");
                }
            } else {
                doSpline(end,type);
            }
        }
    }
}
