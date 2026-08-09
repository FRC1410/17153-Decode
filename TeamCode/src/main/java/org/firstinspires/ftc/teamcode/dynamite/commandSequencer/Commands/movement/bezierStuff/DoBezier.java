package org.firstinspires.ftc.teamcode.dynamite.commandSequencer.Commands.movement.bezierStuff;

import org.firstinspires.ftc.teamcode.dynamite.commandSequencer.CommandException;
import org.firstinspires.ftc.teamcode.dynamite.commandSequencer.Commands.Command;
import org.firstinspires.ftc.teamcode.dynamite.commandSequencer.Commands.CommandType;
import org.firstinspires.ftc.teamcode.dynamite.commandSequencer.variables.Variable;
import org.firstinspires.ftc.teamcode.dynamite.commandSequencer.variables.VariableTypes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DoBezier extends Command {
    private boolean hybrid = false;
    private Object[] points = null;
    public DoBezier(int line, String midPose, String endPose) {
        super(line, CommandType.BezTo, new String[]{midPose,endPose});
    }
    public DoBezier(int line, String[] midPoses, String endPose) {
        super(line, CommandType.BezTo, constructPoses(midPoses, endPose));
    }
    public DoBezier(int line, double[] midPose, String endPose){
        super(line, CommandType.BezTo, constructPoses(midPose,endPose));
    }
    public DoBezier(int line, double[][] midPoses, String endPose){
        super(line, CommandType.BezTo, constructPoses(midPoses,endPose));
    }
    public DoBezier(int line, String midPose, double[] endPose){
        super(line, CommandType.BezTo, constructPoses(midPose,endPose));
    }
    public DoBezier(int line, String[] midPose, double[] endPose){
        super(line, CommandType.BezTo, constructPoses(midPose,endPose));
    }
    public DoBezier(int line, double[] midPose, double[] endPose){
        super(line, CommandType.BezTo, constructPoses(midPose,endPose));
    }
    public DoBezier(int line, double[][] midPose, double[] endPose){
        super(line, CommandType.BezTo, constructPoses(midPose,endPose));
    }
    public DoBezier(int line, Object[][] points){
        super(line, CommandType.BezTo, constructPoses(points));
        hybrid = true;
        this.points = points;
    }

    private static String[] constructPoses(String[] midPoses, String endPos){
        ArrayList<String> poses = new ArrayList<>(Arrays.asList(midPoses));
        poses.add(endPos);
        return poses.toArray(new String[0]);
    }
    private static String[] constructPoses(double[] midPose, String endPose){
        ArrayList<String> poses = new ArrayList<>();
        if (midPose.length == 2){
            poses.add("[X:"+midPose[0]+",X:"+midPose[1]+"]");
        } else if (midPose.length == 3){
            poses.add("[X:"+midPose[0]+",Y:"+midPose[1]+",H:"+midPose[2]+"]");
        }
        poses.add(endPose);
        return poses.toArray(new String[0]);
    }
    private static String[] constructPoses(double[] midPose, double[] endPose){
        ArrayList<String> poses = new ArrayList<>();
        if (midPose.length == 2){
            poses.add("[X:"+midPose[0]+",X:"+midPose[1]+"]");
        } else if (midPose.length == 3){
            poses.add("[X:"+midPose[0]+",Y:"+midPose[1]+",H:"+midPose[2]+"]");
        }
        if (endPose.length == 2){
            poses.add("[X:"+endPose[0]+",X:"+endPose[1]+"]");
        } else if (endPose.length == 3){
            poses.add("[X:"+endPose[0]+",Y:"+endPose[1]+",H:"+endPose[2]+"]");
        }
        return poses.toArray(new String[0]);
    }
    private static String[] constructPoses(String[] midPoses, double[] endPose){
        ArrayList<String> poses = new ArrayList<>(Arrays.asList(midPoses));
        if (endPose.length == 2){
            poses.add("[X:"+endPose[0]+",X:"+endPose[1]+"]");
        } else if (endPose.length == 3){
            poses.add("[X:"+endPose[0]+",Y:"+endPose[1]+",H:"+endPose[2]+"]");
        }
        return poses.toArray(new String[0]);
    }
    private static String[] constructPoses(double[][] midPoses, double[] endPose){
        ArrayList<String> poses = new ArrayList<>();
        for (double[] midPose : midPoses){
            if (midPose.length == 2){
                poses.add("[X:"+midPose[0]+",X:"+midPose[1]+"]");
            } else if (midPose.length == 3){
                poses.add("[X:"+midPose[0]+",Y:"+midPose[1]+",H:"+midPose[2]+"]");
            }
        }
        if (endPose.length == 2){
            poses.add("[X:"+endPose[0]+",X:"+endPose[1]+"]");
        } else if (endPose.length == 3){
            poses.add("[X:"+endPose[0]+",Y:"+endPose[1]+",H:"+endPose[2]+"]");
        }
        return poses.toArray(new String[0]);
    }
    private static String[] constructPoses(double[][] midPoses, String endPose){
        ArrayList<String> poses = new ArrayList<>();
        for (double[] midPose : midPoses){
            if (midPose.length == 2){
                poses.add("[X:"+midPose[0]+",X:"+midPose[1]+"]");
            } else if (midPose.length == 3){
                poses.add("[X:"+midPose[0]+",Y:"+midPose[1]+",H:"+midPose[2]+"]");
            }
        }
        poses.add(endPose);
        return poses.toArray(new String[0]);
    }
    private static String[] constructPoses(String midPose, double[] endPose){
        ArrayList<String> poses = new ArrayList<>();
        poses.add(midPose);
        if (endPose.length == 2){
            poses.add("[X:"+endPose[0]+",X:"+endPose[1]+"]");
        } else if (endPose.length == 3){
            poses.add("[X:"+endPose[0]+",Y:"+endPose[1]+",H:"+endPose[2]+"]");
        }
        return poses.toArray(new String[0]);
    }
    private static String[] constructPoses(Object[][] points){
        ArrayList<String> pointies = new ArrayList<>();
        for (Object[] point : points){
            if (point.length == 2){
                pointies.add("[X:"+point[0]+",X:"+point[1]+"]");
            } else if (point.length == 3){
                pointies.add("[X:"+point[0]+",Y:"+point[1]+",H:"+point[2]+"]");
            }
        }
        return pointies.toArray(new String[0]);
    }

    @Override
    public void run(){
        super.run();
        ArrayList<double[]> points = new ArrayList<>();
        if (hybrid){
            for (Object[] point : (Object[][])this.points){
                if (point.length == 2){
                    Object x = point[0];
                    Object y = point[1];
                    if (x instanceof Double || x instanceof Integer || x instanceof Long || x instanceof Float || x instanceof Byte || x instanceof Short){
                        if (y instanceof Double || y instanceof Integer || y instanceof Long || y instanceof Float || y instanceof Byte || y instanceof Short){
                            points.add(new double[]{
                                    (double)x,
                                    (double)y});
                        } else {
                            Variable val2 = getVar((String)y);
                            if (val2.getType() != VariableTypes.Number) throw new CommandException(line,"DoBezier","Cannot use non number variable '"+y+"' for path coordinate!");
                            points.add(new double[]{
                                    (double)x,
                                    (double)val2.getValue()});
                        }
                    } else {
                        if (y instanceof Double || y instanceof Integer || y instanceof Long || y instanceof Float || y instanceof Byte || y instanceof Short){
                            Variable val1 = getVar((String)x);
                            if (val1.getType() != VariableTypes.Number) throw new CommandException(line,"DoBezier","Cannot use non number variable '"+x+"' for path coordinate!");
                            points.add(new double[]{
                                    (double)val1.getValue(),
                                    (double)y});
                        } else {
                            Variable val1 = getVar((String)x);
                            if (val1.getType() != VariableTypes.Number) throw new CommandException(line,"DoBezier","Cannot use non number variable '"+x+"' for path coordinate!");
                            Variable val2 = getVar((String)y);
                            if (val2.getType() != VariableTypes.Number) throw new CommandException(line,"DoBezier","Cannot use non number variable '"+y+"' for path coordinate!");
                            points.add(new double[]{
                                    (double)val1.getValue(),
                                    (double)val2.getValue()});

                        }
                    }
                } else if (point.length == 3){
                    Object x = point[0];
                    Object y = point[1];
                    Object h = point[2];
                    if (x instanceof Double || x instanceof Integer || x instanceof Long || x instanceof Float || x instanceof Byte || x instanceof Short){
                        if (y instanceof Double || y instanceof Integer || y instanceof Long || y instanceof Float || y instanceof Byte || y instanceof Short){
                            if (h instanceof Double || h instanceof Integer || h instanceof Long || h instanceof Float || h instanceof Byte || h instanceof Short){
                                points.add(new double[]{
                                        (double)x,
                                        (double)y,
                                        (double)h});
                            } else {
                                Variable val3 = getVar((String)h);
                                if (val3.getType() != VariableTypes.Number) throw new CommandException(line,"DoBezier","Cannot use non number variable '"+h+"' for path coordinate!");
                                points.add(new double[]{
                                        (double)x,
                                        (double)y,
                                        (double)val3.getValue()});
                            }
                        } else {
                            if (h instanceof Double || h instanceof Integer || h instanceof Long || h instanceof Float || h instanceof Byte || h instanceof Short){
                                Variable val2 = getVar((String)y);
                                if (val2.getType() != VariableTypes.Number) throw new CommandException(line,"DoBezier","Cannot use non number variable '"+y+"' for path coordinate!");
                                points.add(new double[]{
                                        (double)x,
                                        (double)val2.getValue(),
                                        (double)h});
                            } else {
                                Variable val2 = getVar((String)y);
                                if (val2.getType() != VariableTypes.Number) throw new CommandException(line,"DoBezier","Cannot use non number variable '"+y+"' for path coordinate!");
                                Variable val3 = getVar((String)h);
                                if (val3.getType() != VariableTypes.Number) throw new CommandException(line,"DoBezier","Cannot use non number variable '"+h+"' for path coordinate!");
                                points.add(new double[]{
                                        (double)x,
                                        (double)val2.getValue(),
                                        (double)val3.getValue()});
                            }
                        }
                    } else {
                        if (y instanceof Double || y instanceof Integer || y instanceof Long || y instanceof Float || y instanceof Byte || y instanceof Short){
                            if (h instanceof Double || h instanceof Integer || h instanceof Long || h instanceof Float || h instanceof Byte || h instanceof Short){
                                Variable val1 = getVar((String)x);
                                if (val1.getType() != VariableTypes.Number) throw new CommandException(line,"DoBezier","Cannot use non number variable '"+x+"' for path coordinate!");
                                points.add(new double[]{
                                        (double)val1.getValue(),
                                        (double)y,
                                        (double)h});
                            } else {
                                Variable val1 = getVar((String)x);
                                if (val1.getType() != VariableTypes.Number) throw new CommandException(line,"DoBezier","Cannot use non number variable '"+x+"' for path coordinate!");
                                Variable val3 = getVar((String)h);
                                if (val3.getType() != VariableTypes.Number) throw new CommandException(line,"DoBezier","Cannot use non number variable '"+h+"' for path coordinate!");
                                points.add(new double[]{
                                        (double)val1.getValue(),
                                        (double)y,
                                        (double)val3.getValue()});
                            }
                        } else {
                            if (h instanceof Double || h instanceof Integer || h instanceof Long || h instanceof Float || h instanceof Byte || h instanceof Short){
                                Variable val1 = getVar((String)x);
                                if (val1.getType() != VariableTypes.Number) throw new CommandException(line,"DoBezier","Cannot use non number variable '"+x+"' for path coordinate!");
                                Variable val2 = getVar((String)y);
                                if (val2.getType() != VariableTypes.Number) throw new CommandException(line,"DoBezier","Cannot use non number variable '"+y+"' for path coordinate!");
                                points.add(new double[]{
                                        (double)val1.getValue(),
                                        (double)val2.getValue(),
                                        (double)h});
                            } else {
                                Variable val1 = getVar((String)x);
                                if (val1.getType() != VariableTypes.Number) throw new CommandException(line,"DoBezier","Cannot use non number variable '"+x+"' for path coordinate!");
                                Variable val2 = getVar((String)y);
                                if (val2.getType() != VariableTypes.Number) throw new CommandException(line,"DoBezier","Cannot use non number variable '"+y+"' for path coordinate!");
                                Variable val3 = getVar((String)h);
                                if (val3.getType() != VariableTypes.Number) throw new CommandException(line,"DoBezier","Cannot use non number variable '"+h+"' for path coordinate!");
                                points.add(new double[]{
                                        (double)val1.getValue(),
                                        (double)val2.getValue(),
                                        (double)val3.getValue()});
                            }
                        }
                    }
                }
            }
        } else {
            for (String id : getInVarIDs()) {
                Variable point = getVar(id);
                if (point.getType() == VariableTypes.FieldCord || point.getType() == VariableTypes.FieldPos) {
                    double[] pos = (double[]) point.getValue();
                    points.add(pos);
                } else {
                    throw new CommandException(line, "doBezier", "Given path point: " + point.getName() + " is not FieldCord/FieldPos");
                }
            }
        }
    }
}
