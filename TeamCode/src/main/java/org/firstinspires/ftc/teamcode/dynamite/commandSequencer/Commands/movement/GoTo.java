package org.firstinspires.ftc.teamcode.dynamite.commandSequencer.Commands.movement;

import org.firstinspires.ftc.teamcode.dynamite.commandSequencer.Commands.Command;
import org.firstinspires.ftc.teamcode.dynamite.commandSequencer.CommandException;
import org.firstinspires.ftc.teamcode.dynamite.commandSequencer.Commands.CommandType;
import org.firstinspires.ftc.teamcode.dynamite.commandSequencer.variables.Variable;
import org.firstinspires.ftc.teamcode.dynamite.commandSequencer.variables.VariableTypes;

public class GoTo extends Command {
    private boolean Literal;
    private boolean partial = false;
    private Double x = null;
    private Double y = null;
    private Double h = null;
    private String X = null;
    private String Y = null;
    private String H = null;
    private double[] pos;
    public GoTo(int line, String var){
        super(line, CommandType.GoTo,new String[]{var});
        Literal = false;
    }
    public GoTo(int line, double[] pose){
        super(line, CommandType.GoTo,new String[]{},"");
        Literal = true;
        switch (pose.length){
            case 2 -> super.InVarIDs = new String[]{
                    String.valueOf(pose[0]),
                    String.valueOf(pose[1])};
            case 3 -> super.InVarIDs = new String[]{
                    String.valueOf(pose[0]),
                    String.valueOf(pose[1]),
                    String.valueOf(pose[2])};
        }
        pos = pose.clone(); // we don't wanna touch the original (or worse, have the original touch US)
    }
    public GoTo(int line, double posX, double posY){
        super(line, CommandType.GoTo, new String[]{
                String.valueOf(posX),
                String.valueOf(posY)});
        Literal = true;
        pos = new double[]{posX,posY};
    }
    public GoTo(int line, double posX, String varY){
        super(line, CommandType.GoTo, new String[]{
                String.valueOf(posX),
                varY
        });
        partial = true;
        x = posX;
        Y = varY;
    }
    public GoTo(int line, String varX, double posY){
        super(line, CommandType.GoTo, new String[]{
                varX,
                String.valueOf(posY)
        });
        partial = true;
        X = varX;
        y = posY;
    }
    public GoTo(int line, String varX, String varY){
        super(line, CommandType.GoTo,new String[]{varX,varY});
        Literal = false;
    }
    public GoTo(int line, double x,    double y,    double h){
        super(line, CommandType.GoTo, new String[]{
                String.valueOf(x),
                String.valueOf(y),
                String.valueOf(h)});
        Literal = true;
        pos = new double[]{x,y,h};
    }
    public GoTo(int line, double posX, String varY, double h){
        super(line, CommandType.GoTo, new String[]{
                String.valueOf(posX),
                varY,
                String.valueOf(h)
        });
        partial = true;
        x = posX;
        Y = varY;
        this.h = h;
    }
    public GoTo(int line, String varX, double posY, double h){
        super(line, CommandType.GoTo, new String[]{
                varX,
                String.valueOf(posY),
                String.valueOf(h)
        });
        partial = true;
        X = varX;
        y = posY;
        this.h = h;
    }
    public GoTo(int line, String posX, String posY, double h){
        super(line, CommandType.GoTo, new String[]{
                posX, posY,
                String.valueOf(h)
        });
        partial = true;
        X = posX;
        Y = posY;
        this.h = h;
    }
    public GoTo(int line, double posX, double posY, String varH){
        super(line, CommandType.GoTo, new String[]{
                String.valueOf(posX),
                String.valueOf(posY),
                varH
        });
        partial = true;
        x = posX;
        y = posY;
        H = varH;
    }
    public GoTo(int line, double posX, String varY, String varH){
        super(line, CommandType.GoTo, new String[]{
                String.valueOf(posX),
                varY, varH
        });
        partial = true;
        x = posX;
        Y = varY;
        H = varH;
    }
    public GoTo(int line, String varX, double posY, String varH){
        super(line, CommandType.GoTo, new String[]{
                varX,
                String.valueOf(posY),
                varH
        });
        partial = true;
        X = varX;
        y = posY;
        H = varH;
    }
    public GoTo(int line, String varX, String varY, String varH){
        super(line, CommandType.GoTo,new String[]{varX,varY,varH});
        Literal = false;
    }

    @Override
    public void run(){
        super.run();
        double[] start = getBotPos.get();
        double[] end;
        if (partial) {
            if (H != null || h != null){
                end = new double[3];
                if (H != null) {
                    end[2] = h;
                }
            } else {
                end = new double[2];
            }
            if (X == null) {
                end[0] = x;
            } else {
                Variable varX = getVar(X);
                if (varX.getType() == VariableTypes.Number){
                    end[0] = (double)varX.getValue();
                } else {
                    throw new CommandException(line,"GoTo","Expected number variable, got:"+varX.getType());
                }
            }
            if (Y == null) {
                end[1] = y;
            } else {
                Variable varY = getVar(Y);
                if (varY.getType() == VariableTypes.Number){
                    end[0] = (double)varY.getValue();
                } else {
                    throw new CommandException(line,"GoTo","Expected number variable, got:"+varY.getType());
                }
            }
            moveSE(start, end);
        } else if (Literal){
            switch (pos.length){
                case 2 -> {
                    end = new double[]{
                            pos[0],
                            pos[1],
                            start[2]
                    }; // allocate even MORE RAM!!!!
                    moveSE(start, end);
                }
                case 3 -> {
                    end = pos;  // we already control it, so no need to copy it.
                    moveSE(start, end);
                }
            }
        } else {
            switch (InVarIDs.length){
                case 1 -> {
                    if (getVar(InVarIDs[0]).getType() == VariableTypes.FieldCord){
                        end = new double[]{
                                (double)(((Variable[])getVar(InVarIDs[0]).getValue())[0].getValue()),
                                (double)(((Variable[])getVar(InVarIDs[0]).getValue())[1].getValue()),
                                start[2]
                        };
                    } else if(getVar(InVarIDs[0]).getType() == VariableTypes.FieldPos){
                        end = new double[]{
                                (double)(((Variable[])getVar(InVarIDs[0]).getValue())[0].getValue()),
                                (double)(((Variable[])getVar(InVarIDs[0]).getValue())[1].getValue()),
                                (double)(((Variable[])getVar(InVarIDs[0]).getValue())[2].getValue())
                        };
                    } else {
                        throw new CommandException(line, "GoTo", "Cannot use variable type: "+getVar(InVarIDs[0]).getValue().toString()+" as valid field position");
                    }
                    moveSE(start, end);
                }
                case 2 -> {
                    if (getVar(InVarIDs[0]).getType() == VariableTypes.Number && getVar(InVarIDs[1]).getType() == VariableTypes.Number){
                        end = new double[]{
                                (double)(((Variable[])getVar(InVarIDs[0]).getValue())[0].getValue()),
                                (double)(((Variable[])getVar(InVarIDs[0]).getValue())[1].getValue()),
                                start[2]
                        };
                        moveSE(start, end);
                    } else {
                        String type1 = getVar(InVarIDs[0]).getType().toString();
                        String type2 = getVar(InVarIDs[1]).getType().toString();
                        throw new CommandException(line, "GoTo", "Cannot use variable types "+type1+", "+type2+" as values for coordinates");
                    }
                }
                case 3 -> {
                    if (getVar(InVarIDs[0]).getType() == VariableTypes.Number && getVar(InVarIDs[1]).getType() == VariableTypes.Number && getVar(InVarIDs[2]).getType() == VariableTypes.Number){
                        end = pos;
                        moveSE(start, end);
                    } else {
                        String type1 = getVar(InVarIDs[0]).getType().toString();
                        String type2 = getVar(InVarIDs[1]).getType().toString();
                        String type3 = getVar(InVarIDs[2]).getType().toString();
                        throw new CommandException(line, "GoTo", "Cannot use variable types "+type1+", "+type2+", "+type3+" as values for coordinates");
                    }
                }
            }
        }
    }
    private void moveSE(double[] start, double[] end){ // this feels way too simple compared to SplineTo
        moveTo.accept(end);
        if (isLinearFollower) waitForIdle();
    }
    private void waitForIdle(){
        while (!isIdle.get()){
            long start = System.currentTimeMillis();

            updateFollower.run();
            updateLoop.run();

            long diff = System.currentTimeMillis() - start;
            long sleep = 10-diff;
            if (sleep > 0) {
                try {
                    Thread.sleep(sleep);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
