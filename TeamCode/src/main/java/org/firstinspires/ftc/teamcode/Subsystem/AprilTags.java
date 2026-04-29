package org.firstinspires.ftc.teamcode.Subsystem;

import static org.firstinspires.ftc.teamcode.Util.Constants.CAM_OFFSET_X;
import static org.firstinspires.ftc.teamcode.Util.Constants.CAM_OFFSET_Y;
import static org.firstinspires.ftc.teamcode.Util.Constants.TAG_WIDTH;
import static org.firstinspires.ftc.teamcode.Util.Constants.TERMINAL_ANGLE_VECTOR;

import android.util.Size;

import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.Util.AprilTagData;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagLibrary;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import java.util.ArrayList;
import java.util.List;

// comment from EonCuber28: thanks Calstar9000 for getting the inital config done here, ill take it from here.
public class AprilTags {
    private final AprilTagData tagFieldData = new AprilTagData();
    public VisionPortal vision_portal;
    public AprilTagProcessor april_tag;
    private ArrayList<AprilTagDetection> detections;

    public AprilTags(HardwareMap hardwareMapCool) {
        // NOTE: The values/configs here are very finicky, don't change them unless absolutely necessary
        // comment form EonCuber28: ok 👍 :) (as of Mar 25 2026, i have changed some things due to a guide i found)
        try {
            // this defines all of the wanted keys for us
            AprilTagLibrary.Builder tagLib = new AprilTagLibrary.Builder();
            for (int tagID : tagFieldData.getKeys()){
                tagLib.addTag(tagID, "TagID"+tagID, TAG_WIDTH, DistanceUnit.INCH);
            }
            // This is the camera data, and where we get said data.
            this.april_tag = new AprilTagProcessor.Builder()
                    .setTagLibrary(tagLib.build())
                    .setTagFamily(AprilTagProcessor.TagFamily.TAG_36h11)
                    .setDrawAxes(true)
                    .setDrawCubeProjection(true)
                    .setDrawTagOutline(true)
                    .setDrawTagID(true)
                    .setOutputUnits(DistanceUnit.INCH, AngleUnit.RADIANS) // in radians to work better with java trig stuff
                    .build();
            // This is the connection to the physical camera, including the id
            VisionPortal.Builder vision_portal_builder = new VisionPortal.Builder();
            vision_portal_builder.setCamera(hardwareMapCool.get(WebcamName.class, "Webcam 1"));
            vision_portal_builder.setCameraResolution(new Size(640, 480));
            vision_portal_builder.addProcessor(april_tag);

            vision_portal = vision_portal_builder.build();
            update();
        } catch (Exception e) {
            throw new RuntimeException("Camera init failed: " + e.getMessage());
        }
    }

    /**
     * Gets tag data on the distance to the tag, the angle, and the ID of the tag
     * @return Range, Bearing, Tag ID in a double[]
     */
    public double[] getTagData() {
        // Return detection count as first value for debugging
        if (detections.isEmpty()) {
            return new double[]{-999, 0, 0}; // -999 = no detections at all
        }

        AprilTagDetection detection = detections.get(0);
        double id = detection.id;
        double range = 0;
        double bearing = 0;

        if (detection.ftcPose != null) {
            range = detection.ftcPose.range;
            bearing = detection.ftcPose.bearing;
        } else {
            range = -888; // -888 = detection exists but no pose
            bearing = -888;
        }

        return new double[]{range, bearing, id};
    }
    public double[] getRobotPosFromTag(double[] tagFieldPos, double[] relTagPos, double tagYaw){
        double tagHeading = tagFieldPos[2];

        // rotate the apriltag relative position into the field position, this part makes the offset
        double fieldDX = relTagPos[0]*Math.cos(tagHeading) - relTagPos[1]*Math.sin(tagHeading);
        double fieldDY = relTagPos[0]*Math.sin(tagHeading) + relTagPos[1]*Math.cos(tagHeading);

        double[] estPos = {
                tagFieldPos[0]+fieldDX, // this part applies the offset
                tagFieldPos[1]+fieldDY,
                tagYaw+tagHeading};

        return estPos;
    }

    private double[] shiftPosFromCamOffset(double[] ogPos){
        if (CAM_OFFSET_Y == 0 && CAM_OFFSET_X == 0) return ogPos.clone(); // idk just felt like it
        // this is the complicated part where we start our toes a little bit more into vector math (scary ik)
        // the first step is to calculate the robot facing vector, we just gotta rotate the zero vector by the robot angle
        double[] facingVect = {
                TERMINAL_ANGLE_VECTOR[0]*Math.cos(ogPos[2])- TERMINAL_ANGLE_VECTOR[1]*Math.sin(ogPos[2]),
                TERMINAL_ANGLE_VECTOR[0]*Math.sin(ogPos[2])+ TERMINAL_ANGLE_VECTOR[1]*Math.cos(ogPos[2])};
        double length = Math.sqrt(facingVect[0]*facingVect[0]+facingVect[1]*facingVect[1]); // simple pythagoras (not so scary)
        // we calculate F hat and P hat, as our robot local space matrix constructors, we just gonna normalise the vector just in case.
        double[] F_hat = {
                facingVect[0]/length,
                facingVect[1]/length};
        double[] P_hat = {
                -F_hat[1],
                F_hat[0]};
        // now we calculate the world based offset
        double[] worldOffset = {
                CAM_OFFSET_X*F_hat[0]+CAM_OFFSET_Y*P_hat[0],
                CAM_OFFSET_X*F_hat[1]+CAM_OFFSET_Y*P_hat[1]};
        // and apply the offset to a copy of input (better for adaptability)
        double[] out = ogPos.clone();
        out[0] += worldOffset[0];
        out[1] += worldOffset[1];
        return out;
    }

    public double[] getRobotPos(){
        if (detections.isEmpty()) return new double[]{-1,-1,-1};

        double sumX = 0, sumY = 0;
        double sumSin = 0, sumCos = 0;
        int validCount = 0;

        for (AprilTagDetection tagData : detections){
            if (tagData != null && tagData.ftcPose != null) {
                // get tagFieldPos
                double[] tagFieldPos = tagFieldData.getID2Tag(tagData.id);
                if (tagFieldPos == null) continue; // counter other null pointer shenanigans.
                // calculate pos
                double[] estPosFromTag = getRobotPosFromTag(
                        tagFieldPos,
                        new double[]{tagData.ftcPose.x, tagData.ftcPose.y},
                        tagData.ftcPose.yaw);
                // add to average
                sumX += estPosFromTag[0];
                sumY += estPosFromTag[1];

                sumSin += Math.sin(estPosFromTag[2]); // this calculates the angles that dont loose effectivness around averaging edge cases (especialy with radians)
                sumCos += Math.cos(estPosFromTag[2]); // and easaly allows us to get accurate averages of the angles from the different positions
                validCount++;
            }
        }

        if (validCount == 0) return new double[]{-1,-1,-1};

        // calculate average
        double[] estPos = {
                sumX/validCount,
                sumY/validCount,
                Math.atan2(sumSin/validCount,sumCos/validCount) // funny circular mean math to counteract common radian angle based edge cases.
        };
        return shiftPosFromCamOffset(estPos);
    }
    public Pose getPedroPose(){
        double[] roboPos = getRobotPos();
        return new Pose(roboPos[0], roboPos[1], roboPos[2]);
    }

    public void update(){
        this.detections = april_tag.getDetections();
    }

    public ArrayList<AprilTagDetection> getDetections(){
        return this.detections;
    }
}
