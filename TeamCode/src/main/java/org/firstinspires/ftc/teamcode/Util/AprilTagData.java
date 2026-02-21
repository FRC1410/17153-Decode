package org.firstinspires.ftc.teamcode.Util;

import java.util.HashMap;
import java.util.Map;

public class AprilTagData {
    private Map<String,AprilTagFieldPos> ID2Pos = new HashMap<>();
    public AprilTagData(){
        // this is where we set all of the field positions...
        // for all of the aprilTags positions and normal vectors
        ID2Pos.put("24",new AprilTagFieldPos(127.5,130.5, Math.toRadians(220))); // red goal apriltag (these took way too much work to get)
        ID2Pos.put("20",new AprilTagFieldPos(16.5,130.5, Math.toRadians(312.5))); // blue goal apriltag
        ID2Pos.put("23",new AprilTagFieldPos(72,144, Math.toRadians(270))); // PPG apriltag
        ID2Pos.put("22",new AprilTagFieldPos(72,144, Math.toRadians(270))); // PGP apriltag
        ID2Pos.put("21",new AprilTagFieldPos(72,144, Math.toRadians(270))); // GPP apriltag
    }
    public double[] getID2Tag(int ID){
        return ID2Pos.get(String.valueOf(ID)).toRAW();
    }
    public double[] getID2Tag(String ID){
        return ID2Pos.get(ID).toRAW();
    }
}

class AprilTagFieldPos{
    private double Xpos;
    private double Ypos;
    private double Heading;
    public AprilTagFieldPos(double x,double y,double h){
        Xpos = x;
        Ypos = y;
        Heading = h;
    }
    public double[] toRAW(){
        return new double[] {Xpos,Ypos,Heading};
    }
}