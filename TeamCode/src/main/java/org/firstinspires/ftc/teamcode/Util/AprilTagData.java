package org.firstinspires.ftc.teamcode.Util;

import java.util.HashMap;
import java.util.Map;

public class AprilTagData {
    private Map<Integer,AprilTagFieldPos> ID2Pos = new HashMap<>();
    public AprilTagData(){
        // this is where we set all of the field positions...
        // for all of the aprilTags positions and normal vectors
        ID2Pos.put(24,new AprilTagFieldPos(127.5,130.5, Math.toRadians(220))); // red goal apriltag (the goal angles took way too much work to get)
        ID2Pos.put(20,new AprilTagFieldPos(16.5,130.5, Math.toRadians(312.5))); // blue goal apriltag
        ID2Pos.put(23,new AprilTagFieldPos(72,144, Math.toRadians(270))); // PPG apriltag
        ID2Pos.put(22,new AprilTagFieldPos(72,144, Math.toRadians(270))); // PGP apriltag
        ID2Pos.put(21,new AprilTagFieldPos(72,144, Math.toRadians(270))); // GPP apriltag
        //ID2Pos.put(18,new AprilTagFieldPos(0,0,0)); // test apriltag
    }
    public double[] getID2Tag(int ID){
        AprilTagFieldPos data = ID2Pos.get(ID);
        if (data != null) return data.toRAW(); // avoids previous possible null pointer issues
        return new double[]{-1,-1,-1};
    }
    public int[] getKeys(){ // going forward is one thing, going back is whole other thing
        Integer[] keys = ID2Pos.keySet().toArray(new Integer[0]);
        int[] KEYS = new int[ID2Pos.size()];
        int i = 0;
        for (Integer keyley : keys){
            KEYS[i] = keyley;
            i++;
        }
        return KEYS;
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