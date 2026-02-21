package org.firstinspires.ftc.teamcode.Subsystem;

import static org.firstinspires.ftc.teamcode.Util.Constants.CAM_OFFSET_X;
import static org.firstinspires.ftc.teamcode.Util.Constants.CAM_OFFSET_Y;
import static org.firstinspires.ftc.teamcode.Util.Constants.ZERO_ANGLE_VECTOR;

import android.util.Size;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.Util.AprilTagData;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import java.util.List;

// comment from EonCuber28: thanks Calstar9000 for getting the inital config done here, ill take it from here.
public class AprilTags {
    private AprilTagData tagFieldData = new AprilTagData();
    public VisionPortal vision_portal;
    public AprilTagProcessor april_tag;

    public AprilTags(HardwareMap hardwareMapCool) {
        // NOTE: The values/configs here are very finicky, don't change them unless absolutely necessary
        // comment form EonCuber28: ok 👍 :)
        try {
            // This is the camera data, and where we get said data.
            this.april_tag = new AprilTagProcessor.Builder()
                    .setTagFamily(AprilTagProcessor.TagFamily.TAG_36h11)
                    .setDrawAxes(true)
                    .setDrawCubeProjection(true)
                    .setDrawTagOutline(true)
                    .setDrawTagID(true)
                    .setOutputUnits(DistanceUnit.INCH, AngleUnit.DEGREES)
                    .build();

            // This is the connection to the physical camera, including the id
            this.vision_portal = new VisionPortal.Builder()
                    .setCamera(hardwareMapCool.get(WebcamName.class, "Webcam 1"))
                    .addProcessor(this.april_tag)
                    .setCameraResolution(new Size(1280, 800))
                    .enableLiveView(true)
                    .setAutoStopLiveView(false)
                    .build();
        } catch (Exception e) {
            throw new RuntimeException("Camera init failed: " + e.getMessage());
        }
    }

    /**
     * Gets tag data on the distance to the tag, the angle, and the ID of the tag
     * @return Range, Bearing, Tag ID in a double[]
     */
    public double[] getTagData() {
        List<AprilTagDetection> detections = this.april_tag.getDetections();

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
    public double[] getRobotPosFromTag(double[] tagDat, double[] relTagPos, double tagYaw){
        double[] estPos = new double[3];
        // so its deacently simple to get the field position from the tag data
        estPos[0] = tagDat[0]+relTagPos[0];
        estPos[1] = tagDat[1]+relTagPos[1];
        // but to get the angle fit for your pathing system (for us its pedro), you gotta know the "zero" angle vector
        // then using some simple vector math, you can get the robot angle. all of the major parts are pre-calculated, so there is not much to see here.
        estPos[2] = tagYaw+tagDat[2];
        return estPos;
    }

    private double[] shiftPosFromCamOffset(double[] ogPos){
        // this is the complicated part where we start our toes a little bit more into vector math (scary ik)
        // the first step is to calculate the robot facing vector, we just gotta rotate the zero vector by the robot angle
        double[] facingVect = {
                ZERO_ANGLE_VECTOR[0]*Math.cos(ogPos[2])-ZERO_ANGLE_VECTOR[1]*Math.sin(ogPos[2]),
                ZERO_ANGLE_VECTOR[0]*Math.sin(ogPos[2])+ZERO_ANGLE_VECTOR[1]*Math.cos(ogPos[2])};
        double length = Math.sqrt(facingVect[0]*facingVect[0]+facingVect[1]*facingVect[1]); // simple pythagoras
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
        // and apply the offset
        ogPos[0] -= worldOffset[0];
        ogPos[1] -= worldOffset[1];
        return ogPos;
    }

    public double[] getRobotPos(){
        List<AprilTagDetection> detections = this.april_tag.getDetections();
        if (detections.isEmpty()) return null;
        double[] estPos = new double[3];
        for (AprilTagDetection tagData : detections){
            // calculate pos
            double[] estPosFromTag = getRobotPosFromTag(
                    tagFieldData.getID2Tag(tagData.id),
                    new double[]{tagData.ftcPose.x, tagData.ftcPose.y},
                    tagData.ftcPose.yaw);
            // add to average
            estPos[0] += estPosFromTag[0];
            estPos[1] += estPosFromTag[1];
            estPos[2] += estPosFromTag[2];
        }
        // calculate average
        estPos[0] = estPos[0]/detections.size();
        estPos[1] = estPos[1]/detections.size();
        estPos[2] = estPos[2]/detections.size();
        return shiftPosFromCamOffset(estPos);
    }
}
