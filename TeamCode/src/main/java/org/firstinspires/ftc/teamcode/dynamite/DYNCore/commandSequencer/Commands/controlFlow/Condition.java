package org.firstinspires.ftc.teamcode.dynamite.DYNCore.commandSequencer.Commands.controlFlow;

import org.firstinspires.ftc.teamcode.dynamite.DYNCore.commandSequencer.CommandException;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.commandSequencer.VariableManager;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.commandSequencer.variables.Variable;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.commandSequencer.variables.VariableTypes;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.commandSequencer.variables.primitives.DynBoolean;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.commandSequencer.variables.primitives.DynNumber;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.commandSequencer.variables.primitives.DynString;

public class Condition {
    private final int line;
    private static VariableManager varMan;
    public static void linkVarMan(VariableManager varMan){
        Condition.varMan = varMan;
    }

    public enum ConditionType{
        And,Or,Not,Equals,
        NotEquals,MoreThan,
        LessThan,MoreThanEq,
        LessThanEq,Constant}
    private final ConditionType type;

    private enum InTypes{bool,var,condition,num,string}
    private final Object Part1;
    private final InTypes Part1Type;

    private Object Part2;
    private InTypes Part2Type;

    // the ones with the numbers
    public Condition(int line, ConditionType opType, double in1, double in2){
        Part1 = in1;
        Part2 = in2;
        type = opType;
        Part1Type = InTypes.num;
        Part2Type = InTypes.num;
        this.line = line;
    }
    public Condition(int line, ConditionType opType, double in1, String in2){
        Part1 = in1;
        Part2 = in2;
        type = opType;
        Part1Type = InTypes.num;
        Part2Type = InTypes.var;
        this.line = line;
    }
    public Condition(int line, ConditionType opType, String in1, double in2){
        Part1 = in1;
        Part2 = in2;
        type = opType;
        Part1Type = InTypes.var;
        Part2Type = InTypes.num;
        this.line = line;
    }
    // for variable/boolean inputs
    public Condition(int line, ConditionType opType, boolean in1, boolean in2){
        Part1 = in1;
        Part2 = in2;
        type = opType;
        Part1Type = InTypes.bool;
        Part2Type = InTypes.bool;
        this.line = line;
    }
    public Condition(int line, ConditionType opType, String in1, boolean in2){
        Part1 = in1;
        Part2 = in2;
        type = opType;
        Part1Type = InTypes.var;
        Part2Type = InTypes.bool;
        this.line = line;
    }
    public Condition(int line, ConditionType opType, boolean in1, String in2){
        Part1 = in1;
        Part2 = in2;
        type = opType;
        Part1Type = InTypes.bool;
        Part2Type = InTypes.var;
        this.line = line;
    }
    public Condition(int line, ConditionType opType, String in1, String in2){
        Part1 = in1;
        Part2 = in2;
        type = opType;
        Part1Type = InTypes.var;
        Part2Type = InTypes.var;
        this.line = line;
    }
    // for condition based inputs
    public Condition(int line, ConditionType opType, Condition in1, Condition in2){
        Part1 = in1;
        Part2 = in2;
        type = opType;
        Part1Type = InTypes.condition;
        Part2Type = InTypes.condition;
        this.line = line;
    }
    public Condition(int line, ConditionType opType, boolean in1, Condition in2){
        Part1 = in1;
        Part2 = in2;
        type = opType;
        Part1Type = InTypes.bool;
        Part2Type = InTypes.condition;
        this.line = line;
    }
    public Condition(int line, ConditionType opType, Condition in1, boolean in2){
        Part1 = in1;
        Part2 = in2;
        type = opType;
        Part1Type = InTypes.condition;
        Part2Type = InTypes.bool;
        this.line = line;
    }
    public Condition(int line, ConditionType opType, String in1, Condition in2){
        Part1 = in1;
        Part2 = in2;
        type = opType;
        Part1Type = InTypes.var;
        Part2Type = InTypes.condition;
        this.line = line;
    }
    public Condition(int line, ConditionType opType, Condition in1, String in2){
        Part1 = in1;
        Part2 = in2;
        type = opType;
        Part1Type = InTypes.condition;
        Part2Type = InTypes.var;
        this.line = line;
    }
    // allows conditions to have in-built strings
    public Condition(int line, ConditionType opType, DynString in1, DynString in2){
        Part1 = in1;
        Part2 = in2;
        type = opType;
        Part1Type = InTypes.string;
        Part2Type = InTypes.string;
        this.line = line;
    }
    public Condition(int line, ConditionType opType, DynString in1, String in2){
        Part1 = in1;
        Part2 = in2;
        type = opType;
        Part1Type = InTypes.string;
        Part2Type = InTypes.var;
        this.line = line;
    }
    public Condition(int line, ConditionType opType, String in1, DynString in2){
        Part1 = in1;
        Part2 = in2;
        type = opType;
        Part1Type = InTypes.var;
        Part2Type = InTypes.string;
        this.line = line;
    }

    public Condition(int line, ConditionType opType, boolean in){
        Part1 = in;
        type = opType;
        Part1Type = InTypes.bool;
        this.line = line;
    }
    public Condition(int line, ConditionType opType, double in){
        Part1 = in;
        type = opType;
        Part1Type = InTypes.num;
        this.line = line;
    }
    public Condition(int line, ConditionType opType, String in){
        Part1 = in;
        type = opType;
        Part1Type = InTypes.var;
        this.line = line;
    }
    public Condition(int line, ConditionType opType, Condition in){
        Part1 = in;
        Part1Type = InTypes.condition;
        type = opType;
        this.line = line;
    }

    public Condition(int line, boolean in){
        Part1 = in;
        Part1Type = InTypes.bool;
        type = ConditionType.Constant;
        this.line = line;
    }
    public Condition(int line, String in){
        Part1 = in;
        Part1Type = InTypes.var;
        type = ConditionType.Constant;
        this.line = line;
    }

    // process parts
    public boolean getResult(){ // (look at previous commits for context) I'm not sure if there was an issue with those ops, but I'm just gonna redo the whole system.
        Boolean result = null;
        switch (type){
            case LessThanEq -> {
                if (typeMatch(InTypes.var,InTypes.var)){
                    Variable pt1 = varMan.getVar((String)Part1);
                    if (pt1 == null) throw new ConditionException(line,"Variable "+Part1+" not defined!");
                    Variable pt2 = varMan.getVar((String)Part2);
                    if (pt2 == null) throw new ConditionException(line,"Variable "+Part2+" not defined!");
                    result = pt1.lessThanEq(pt2);
                }
                else if (typeMatch(InTypes.var,InTypes.num)){
                    Variable pt1 = varMan.getVar((String)Part1);
                    if (pt1 == null) throw new ConditionException(line,"Variable "+Part1+" not defined!");
                    Variable pt2 = new DynNumber((double)Part2);
                    result = pt1.lessThanEq(pt2);
                }
                else if (typeMatch(InTypes.num,InTypes.var)){
                    Variable pt1 = new DynNumber((double)Part1);
                    Variable pt2 = varMan.getVar((String)Part2);
                    if (pt2 == null) throw new ConditionException(line,"Variable "+Part2+" not defined!");
                    result = pt1.lessThanEq(pt2);
                }
                else if (typeMatch(InTypes.num,InTypes.num)){
                    double pt1 = (double)Part1;
                    double pt2 = (double)Part2;
                    result = pt1<=pt2;
                }
            }
            case MoreThanEq -> {
                if (typeMatch(InTypes.var,InTypes.var)){
                    Variable pt1 = varMan.getVar((String)Part1);
                    if (pt1 == null) throw new ConditionException(line,"Variable "+Part1+" not defined!");
                    Variable pt2 = varMan.getVar((String)Part2);
                    if (pt2 == null) throw new ConditionException(line,"Variable "+Part2+" not defined!");
                    result = pt1.moreThanEq(pt2);
                }
                else if (typeMatch(InTypes.var,InTypes.num)){
                    Variable pt1 = varMan.getVar((String)Part1);
                    if (pt1 == null) throw new ConditionException(line,"Variable "+Part1+" not defined!");
                    Variable pt2 = new DynNumber((double)Part2);
                    result = pt1.moreThanEq(pt2);
                }
                else if (typeMatch(InTypes.num,InTypes.var)){
                    Variable pt1 = new DynNumber((double)Part1);
                    Variable pt2 = varMan.getVar((String)Part2);
                    if (pt2 == null) throw new ConditionException(line,"Variable "+Part2+" not defined!");
                    result = pt1.moreThanEq(pt2);
                }
                else if (typeMatch(InTypes.num,InTypes.num)){
                    double pt1 = (double)Part1;
                    double pt2 = (double)Part2;
                    result = pt1>=pt2;
                }
            }
            case NotEquals -> {
                if (typeMatch(InTypes.condition,InTypes.condition)){
                    boolean pt1 = ((Condition)Part1).getResult();
                    boolean pt2 = ((Condition)Part2).getResult();
                    result = pt1!=pt2;
                }
                else if (typeMatch(InTypes.condition,InTypes.bool)){
                    boolean pt1 = ((Condition)Part1).getResult();
                    boolean pt2 = (boolean)Part2;
                    result = pt1!=pt2;
                }
                else if (typeMatch(InTypes.condition,InTypes.var)){
                    Variable pt1 = new DynBoolean(((Condition)Part1).getResult());
                    Variable pt2 = varMan.getVar((String)Part2);
                    if (pt2 == null) throw new ConditionException(line,"Variable "+Part2+" not defined!");
                    result = !pt1.equals(pt2);
                }

                else if (typeMatch(InTypes.bool,InTypes.condition)){
                    boolean pt1 = (boolean)Part1;
                    boolean pt2 = ((Condition)Part2).getResult();
                    result = pt1!=pt2;
                }
                else if (typeMatch(InTypes.bool,InTypes.bool)){
                    boolean pt1 = (boolean)Part1;
                    boolean pt2 = (boolean)Part2;
                    result = pt1!=pt2;
                }
                else if (typeMatch(InTypes.bool,InTypes.var)){
                    Variable pt1 = new DynBoolean((boolean)Part1);
                    Variable pt2 = varMan.getVar((String)Part2);
                    if (pt2 == null) throw new ConditionException(line,"Variable "+Part2+" not defined!");
                    result = !pt1.equals(pt2);
                }

                else if (typeMatch(InTypes.var,InTypes.condition)){
                    Variable pt1 = varMan.getVar((String)Part1);
                    if (pt1 == null) throw new ConditionException(line,"Variable "+Part1+" not defined!");
                    Variable pt2 = new DynBoolean(((Condition)Part2).getResult());
                    result = !pt1.equals(pt2);
                }
                else if (typeMatch(InTypes.var,InTypes.bool)){
                    Variable pt1 = varMan.getVar((String)Part1);
                    if (pt1 == null) throw new ConditionException(line,"Variable "+Part1+" not defined!");
                    Variable pt2 = new DynBoolean((boolean)Part2);
                    result = !pt1.equals(pt2);
                }
                else if (typeMatch(InTypes.var,InTypes.var)){
                    Variable pt1 = varMan.getVar((String)Part1);
                    if (pt1 == null) throw new ConditionException(line,"Variable "+Part1+" not defined!");
                    Variable pt2 = varMan.getVar((String)Part2);
                    if (pt2 == null) throw new ConditionException(line,"Variable "+Part2+" not defined!");
                    result = !pt1.equals(pt2);
                }
                else if (typeMatch(InTypes.var,InTypes.num)){
                    Variable pt1 = varMan.getVar((String)Part1);
                    if (pt1 == null) throw new ConditionException(line,"Variable "+Part1+" not defined!");
                    Variable pt2 = new DynNumber((double)Part2);
                    result = !pt1.equals(pt2);
                }

                else if (typeMatch(InTypes.num,InTypes.var)){
                    Variable pt1 = new DynNumber((double)Part1);
                    Variable pt2 = varMan.getVar((String)Part2);
                    if (pt2 == null) throw new ConditionException(line,"Variable "+Part2+" not defined!");
                    result = !pt1.equals(pt2);
                }
                else if (typeMatch(InTypes.num,InTypes.num)){
                    double pt1 = (double)Part1;
                    double pt2 = (double)Part2;
                    result = pt1!=pt2;
                }

                else if (typeMatch(InTypes.string,InTypes.string)){
                    DynString pt1 = (DynString)Part1;
                    DynString pt2 = (DynString)Part2;
                    result = !pt1.equals(pt2);
                }
                else if (typeMatch(InTypes.var,InTypes.string)){
                    Variable pt1 = varMan.getVar((String)Part1);
                    if (pt1 == null) throw new ConditionException(line,"Variable "+Part1+" not defined!");
                    DynString pt2 = (DynString)Part2;
                    result = !pt1.equals(pt2);
                }
                else if (typeMatch(InTypes.string,InTypes.var)){
                    DynString pt1 = (DynString)Part1;
                    Variable pt2 = varMan.getVar((String)Part2);
                    if (pt2 == null) throw new ConditionException(line,"Variable "+Part2+" not defined!");
                    result = !pt1.equals(pt2);
                }
            }
            case LessThan -> {
                if (typeMatch(InTypes.var,InTypes.var)){
                    Variable pt1 = varMan.getVar((String)Part1);
                    if (pt1 == null) throw new ConditionException(line,"Variable "+Part1+" not defined!");
                    Variable pt2 = varMan.getVar((String)Part2);
                    if (pt2 == null) throw new ConditionException(line,"Variable "+Part2+" not defined!");
                    result = pt1.lessThan(pt2);
                }
                else if (typeMatch(InTypes.var,InTypes.num)){
                    Variable pt1 = varMan.getVar((String)Part1);
                    if (pt1 == null) throw new ConditionException(line,"Variable "+Part1+" not defined!");
                    Variable pt2 = new DynNumber((double)Part2);
                    result = pt1.lessThan(pt2);
                }
                else if (typeMatch(InTypes.num,InTypes.var)){
                    Variable pt1 = new DynNumber((double)Part1);
                    Variable pt2 = varMan.getVar((String)Part2);
                    if (pt2 == null) throw new ConditionException(line,"Variable "+Part2+" not defined!");
                    result = pt1.lessThan(pt2);
                }
                else if (typeMatch(InTypes.num,InTypes.num)){
                    double pt1 = (double)Part1;
                    double pt2 = (double)Part2;
                    result = pt1<pt2;
                }
            }
            case MoreThan -> {
                if (typeMatch(InTypes.var,InTypes.var)){
                    Variable pt1 = varMan.getVar((String)Part1);
                    if (pt1 == null) throw new ConditionException(line,"Variable "+Part1+" not defined!");
                    Variable pt2 = varMan.getVar((String)Part2);
                    if (pt2 == null) throw new ConditionException(line,"Variable "+Part2+" not defined!");
                    result = pt1.moreThan(pt2);
                }
                else if (typeMatch(InTypes.var,InTypes.num)){
                    Variable pt1 = varMan.getVar((String)Part1);
                    if (pt1 == null) throw new ConditionException(line,"Variable "+Part1+" not defined!");
                    Variable pt2 = new DynNumber((double)Part2);
                    result = pt1.moreThan(pt2);
                }
                else if (typeMatch(InTypes.num,InTypes.var)){
                    Variable pt1 = new DynNumber((double)Part1);
                    Variable pt2 = varMan.getVar((String)Part2);
                    if (pt2 == null) throw new ConditionException(line,"Variable "+Part2+" not defined!");
                    result = pt1.moreThan(pt2);
                }
                else if (typeMatch(InTypes.num,InTypes.num)){
                    double pt1 = (double)Part1;
                    double pt2 = (double)Part2;
                    result = pt1>pt2;
                }
            }
            case Constant -> {
                switch (Part1Type){
                    case condition -> result = ((Condition)Part1).getResult();
                    case bool -> result = (boolean)Part1;
                    case var -> {
                        Variable value = varMan.getVar((String)Part1);
                        if (value == null) throw new ConditionException(line,"Variable "+Part1+" not defined!");
                        if (value.getType() == VariableTypes.Boolean){
                            result = (boolean)(value.getValue());
                        } else {
                            throw new ConditionException(line,"Variable "+Part1+" is not a boolean!");
                        }
                    }
                }
            }
            case Equals -> {
                if (typeMatch(InTypes.condition,InTypes.condition)){
                    boolean pt1 = ((Condition)Part1).getResult();
                    boolean pt2 = ((Condition)Part2).getResult();
                    result = pt1==pt2;
                }
                else if (typeMatch(InTypes.condition,InTypes.bool)){
                    boolean pt1 = ((Condition)Part1).getResult();
                    boolean pt2 = (boolean)Part2;
                    result = pt1==pt2;
                }
                else if (typeMatch(InTypes.condition,InTypes.var)){
                    Variable pt1 = new DynBoolean(((Condition)Part1).getResult());
                    Variable pt2 = varMan.getVar((String)Part2);
                    if (pt2 == null) throw new ConditionException(line,"Variable "+Part2+" not defined!");
                    result = pt1.equals(pt2);
                }

                else if (typeMatch(InTypes.bool,InTypes.condition)){
                    boolean pt1 = (boolean)Part1;
                    boolean pt2 = ((Condition)Part2).getResult();
                    result = pt1==pt2;
                }
                else if (typeMatch(InTypes.bool,InTypes.bool)){
                    boolean pt1 = (boolean)Part1;
                    boolean pt2 = (boolean)Part2;
                    result = pt1==pt2;
                }
                else if (typeMatch(InTypes.bool,InTypes.var)){
                    Variable pt1 = new DynBoolean((boolean)Part1);
                    Variable pt2 = varMan.getVar((String)Part2);
                    if (pt2 == null) throw new ConditionException(line,"Variable "+Part2+" not defined!");
                    result = pt1.equals(pt2);
                }

                else if (typeMatch(InTypes.var,InTypes.condition)){
                    Variable pt1 = varMan.getVar((String)Part1);
                    if (pt1 == null) throw new ConditionException(line,"Variable "+Part1+" not defined!");
                    Variable pt2 = new DynBoolean(((Condition)Part2).getResult());
                    result = pt1.equals(pt2);
                }
                else if (typeMatch(InTypes.var,InTypes.bool)){
                    Variable pt1 = varMan.getVar((String)Part1);
                    if (pt1 == null) throw new ConditionException(line,"Variable "+Part1+" not defined!");
                    Variable pt2 = new DynBoolean((boolean)Part2);
                    result = pt1.equals(pt2);
                }
                else if (typeMatch(InTypes.var,InTypes.var)){
                    Variable pt1 = varMan.getVar((String)Part1);
                    if (pt1 == null) throw new ConditionException(line,"Variable "+Part1+" not defined!");
                    Variable pt2 = varMan.getVar((String)Part2);
                    if (pt2 == null) throw new ConditionException(line,"Variable "+Part2+" not defined!");
                    result = pt1.equals(pt2);
                }
                else if (typeMatch(InTypes.var,InTypes.num)){
                    Variable pt1 = varMan.getVar((String)Part1);
                    if (pt1 == null) throw new ConditionException(line,"Variable "+Part1+" not defined!");
                    Variable pt2 = new DynNumber((double)Part2);
                    result = pt1.equals(pt2);
                }

                else if (typeMatch(InTypes.num,InTypes.var)){
                    Variable pt1 = new DynNumber((double)Part1);
                    Variable pt2 = varMan.getVar((String)Part2);
                    if (pt2 == null) throw new ConditionException(line,"Variable "+Part2+" not defined!");
                    result = pt1.equals(pt2);
                }
                else if (typeMatch(InTypes.num,InTypes.num)){
                    double pt1 = (double)Part1;
                    double pt2 = (double)Part2;
                    result = pt1==pt2;
                }

                else if (typeMatch(InTypes.string,InTypes.string)){
                    DynString pt1 = (DynString)Part1;
                    DynString pt2 = (DynString)Part2;
                    result = pt1.equals(pt2);
                }
                else if (typeMatch(InTypes.var,InTypes.string)){
                    Variable pt1 = varMan.getVar((String)Part1);
                    if (pt1 == null) throw new ConditionException(line,"Variable "+Part1+" not defined!");
                    DynString pt2 = (DynString)Part2;
                    result = pt1.equals(pt2);
                }
                else if (typeMatch(InTypes.string,InTypes.var)){
                    DynString pt1 = (DynString)Part1;
                    Variable pt2 = varMan.getVar((String)Part2);
                    if (pt2 == null) throw new ConditionException(line,"Variable "+Part2+" not defined!");
                    result = pt1.equals(pt2);
                }
            }
            case And -> {
                if (typeMatch(InTypes.condition,InTypes.condition)){
                    boolean pt1 = ((Condition)Part1).getResult();
                    boolean pt2 = ((Condition)Part2).getResult();
                    result = pt1&&pt2;
                }
                else if (typeMatch(InTypes.condition,InTypes.bool)){
                    boolean pt1 = ((Condition)Part1).getResult();
                    boolean pt2 = (boolean)Part2;
                    result = pt1&&pt2;
                }
                else if (typeMatch(InTypes.condition,InTypes.var)){
                    Variable pt1 = new DynBoolean(((Condition)Part1).getResult());
                    Variable pt2 = varMan.getVar((String)Part2);
                    if (pt2 == null) throw new ConditionException(line,"Variable "+Part2+" not defined!");
                    result = pt1.and(pt2);
                }

                else if (typeMatch(InTypes.bool,InTypes.condition)){
                    boolean pt1 = (boolean)Part1;
                    boolean pt2 = ((Condition)Part2).getResult();
                    result = pt1&&pt2;
                }
                else if (typeMatch(InTypes.bool,InTypes.bool)){
                    boolean pt1 = (boolean)Part1;
                    boolean pt2 = (boolean)Part2;
                    result = pt1&&pt2;
                }
                else if (typeMatch(InTypes.bool,InTypes.var)){
                    Variable pt1 = new DynBoolean((boolean)Part1);
                    Variable pt2 = varMan.getVar((String)Part2);
                    if (pt2 == null) throw new ConditionException(line,"Variable "+Part2+" not defined!");
                    result = pt1.and(pt2);
                }

                else if (typeMatch(InTypes.var,InTypes.condition)){
                    Variable pt1 = varMan.getVar((String)Part1);
                    if (pt1 == null) throw new ConditionException(line,"Variable "+Part1+" not defined!");
                    Variable pt2 = new DynBoolean(((Condition)Part2).getResult());
                    result = pt1.and(pt2);
                }
                else if (typeMatch(InTypes.var,InTypes.bool)){
                    Variable pt1 = varMan.getVar((String)Part1);
                    if (pt1 == null) throw new ConditionException(line,"Variable "+Part1+" not defined!");
                    Variable pt2 = new DynBoolean((boolean)Part2);
                    result = pt1.and(pt2);
                }
            }
            case Not -> {
                switch (Part1Type){
                    case condition -> result = !(((Condition)Part1).getResult());
                    case bool -> result = !((boolean)Part1);
                    case var -> {
                        Variable val = varMan.getVar((String)Part1);
                        if (val == null) throw new ConditionException(line,"Variable "+Part1+" not defined!");
                        result = val.not();
                    }
                }
            }
            case Or -> {
                if (typeMatch(InTypes.condition,InTypes.condition)){
                    boolean pt1 = ((Condition)Part1).getResult();
                    boolean pt2 = ((Condition)Part2).getResult();
                    result = pt1||pt2;
                }
                else if (typeMatch(InTypes.condition,InTypes.bool)){
                    boolean pt1 = ((Condition)Part1).getResult();
                    boolean pt2 = (boolean)Part2;
                    result = pt1||pt2;
                }
                else if (typeMatch(InTypes.condition,InTypes.var)){
                    Variable pt1 = new DynBoolean(((Condition)Part1).getResult());
                    Variable pt2 = varMan.getVar((String)Part2);
                    if (pt2 == null) throw new ConditionException(line,"Variable "+Part2+" not defined!");
                    result = pt1.or(pt2);
                }

                else if (typeMatch(InTypes.bool,InTypes.condition)){
                    boolean pt1 = (boolean)Part1;
                    boolean pt2 = ((Condition)Part2).getResult();
                    result = pt1||pt2;
                }
                else if (typeMatch(InTypes.bool,InTypes.bool)){
                    boolean pt1 = (boolean)Part1;
                    boolean pt2 = (boolean)Part2;
                    result = pt1||pt2;
                }
                else if (typeMatch(InTypes.bool,InTypes.var)){
                    Variable pt1 = new DynBoolean((boolean)Part1);
                    Variable pt2 = varMan.getVar((String)Part2);
                    if (pt2 == null) throw new ConditionException(line,"Variable "+Part2+" not defined!");
                    result = pt1.or(pt2);
                }

                else if (typeMatch(InTypes.var,InTypes.condition)){
                    Variable pt1 = varMan.getVar((String)Part1);
                    if (pt1 == null) throw new ConditionException(line,"Variable "+Part1+" not defined!");
                    Variable pt2 = new DynBoolean(((Condition)Part2).getResult());
                    result = pt1.or(pt2);
                }
                else if (typeMatch(InTypes.var,InTypes.bool)){
                    Variable pt1 = varMan.getVar((String)Part1);
                    if (pt1 == null) throw new ConditionException(line,"Variable "+Part1+" not defined!");
                    Variable pt2 = new DynBoolean((boolean)Part2);
                    result = pt1.or(pt2);
                }
            }
        }
        if (result == null){
            // it's weird... but I like it
            String opType;
            String part1type;
            String part2type;
            switch (type){
                case MoreThan -> opType = "More Than";
                case Constant -> opType = "Constant";
                case Equals -> opType = "Equals";
                case And -> opType = "And";
                case Or -> opType = "Or";
                case LessThan -> opType = "Less Than";
                case NotEquals -> opType = "Not Equals";
                default -> opType = "null (BAD)";
            }
            switch (Part1Type){
                case condition -> part1type = "Condition";
                case var -> part1type = "Variable";
                case bool -> part1type = "Boolean";
                default -> part1type = "Null (BAD)";
            }
            switch (Part2Type){
                case bool -> part2type = "Boolean";
                case var -> part2type = "Variable";
                case condition -> part2type = "Condition";
                default -> part2type = "Null (OK)";
            }
            throw new ConditionException(line,"Unable to determine operation "+opType+" on type "+part1type+" and "+part2type);
        }
        return result;
    }

    private boolean typeMatch(InTypes type1, InTypes type2){
        return (type1 == Part1Type)&&(type2 == Part2Type);
    }

    public String toString(){
        StringBuilder out = new StringBuilder("(");
        switch (Part1Type){
            case condition -> out.append(Part1.toString());
            case bool -> out.append((boolean)Part1);
            case var -> out.append("Var:").append((String)Part1);
            case num -> out.append((double)Part1);
            default -> out.append("UNKNOWN");
        }
        out.append(" ");
        // `case null` requires Java 21; this module compiles at 17, so the null
        // check is hoisted out (a switch on a null enum would otherwise NPE).
        if (type == null) {
            out.append("NULL");
        } else switch (type){
            case Or -> out.append("OR");
            case And -> out.append("AND");
            case Not -> out.append("NOT");
            case Equals -> out.append("EQUALS");
            case Constant -> out.append("CONSTANT");
            case LessThan -> out.append("LESS-THAN");
            case MoreThan -> out.append("MROE-THAN");
            case NotEquals -> out.append("NOT-EQUALS");
            case LessThanEq -> out.append("LESS-EQUALS-THAN");
            case MoreThanEq -> out.append("MORE-EQUALS-THAN");
            default -> out.append("NONE");
        }
        out.append(" ");
        if (Part2Type == null) {
            out.append("NULL");
        } else switch (Part2Type){
            case condition -> out.append(Part2.toString());
            case bool -> out.append((boolean)Part2);
            case var -> out.append("Var:").append((String)Part2);
            case num -> out.append((double)Part2);
            default -> out.append("UNKNOWN");
        }
        out.append(")");
        return out.toString();
    }
}

class ConditionException extends CommandException {
    public ConditionException(int line, String message){
        super(line,"Condition",message);
    }
}
