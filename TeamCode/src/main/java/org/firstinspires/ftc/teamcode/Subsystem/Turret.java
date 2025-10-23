package org.firstinspires.ftc.teamcode.Subsystem;
import static com.qualcomm.robotcore.hardware.DcMotorSimple.Direction.FORWARD;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Range;
import static org.firstinspires.ftc.teamcode.Util.IDs.*;
import static org.firstinspires.ftc.teamcode.Util.Constants.*;
import static org.firstinspires.ftc.teamcode.Util.Tuning.*;
import org.firstinspires.ftc.teamcode.Util.PIDController;

public class Turret {

    DcMotorEx turretMotor;
    private aprilTags aprilTagSystem;
    private int targetAprilTagID = 22;
    private PIDController trackingPID;


    public void init(HardwareMap hardwareMap) {
        this.turretMotor = hardwareMap.get(DcMotorEx.class, TURRET_MOTOR_ID);
        this.turretMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        this.turretMotor.setDirection(FORWARD);
        this.turretMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        this.turretMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        this.aprilTagSystem = new aprilTags(hardwareMap);
        this.trackingPID = new PIDController(TURRET_P, TURRET_I, TURRET_D);
    }

    public void setTargetAprilTagID(int id) {
        this.targetAprilTagID = id;
    }
    public boolean trackAprilTag() {
        double[] tagData = aprilTagSystem.getTagData();

        double range = tagData[0];
        double bearing = tagData[1];
        double detectedID = tagData[2];

        if (range == -999) {
            stopMotor();
            return false;
        }

        if (range == -888 || bearing == -888) {
            stopMotor();
            return false;
        }

        if ((int)detectedID != targetAprilTagID) {
            stopMotor();
            return false;
        }

        double normalizedBearing = normalizeAngle(bearing);

        if (Math.abs(normalizedBearing) < ACCEPTED_BEARING) {
            stopMotor();
            return true;
        }

        double pidOutput = trackingPID.calculate(0, normalizedBearing);
        double power = -pidOutput;

        if (Math.abs(power) > 0 && Math.abs(power) < MIN_TRACKING_POWER) {
            power = Math.signum(power) * MIN_TRACKING_POWER;
        }

        power = Range.clip(power, -MAX_TRACKING_POWER, MAX_TRACKING_POWER);

        setPower(power);
        return true;
    }
    private double normalizeAngle(double angle) {
        while (angle > 180) {
            angle -= 360;
        }
        while (angle < -180) {
            angle += 360;
        }
        return angle;
    }

    public double getTagBearing() {
        double[] tagData = aprilTagSystem.getTagData();
        if (tagData[0] == -999 || tagData[0] == -888) {
            return 999;
        }
        return tagData[1];
    }

    public double getTagRange() {
        double[] tagData = aprilTagSystem.getTagData();
        return tagData[0];
    }

    public int getDetectedTagID() {
        double[] tagData = aprilTagSystem.getTagData();
        if (tagData[0] == -999 || tagData[0] == -888) {
            return -999;
        }
        return (int)tagData[2];
    }

    public void setPower(double power) {
        turretMotor.setPower(power);
    }
    public void stopMotor() {
        turretMotor.setPower(0);
    }
    public int getCurrentPosition() {
        return turretMotor.getCurrentPosition();
    }
    public double getCurrentAngle() {
        return getCurrentPosition() / TURRET_TICKS_PER_DEGREE;
    }

    public void rotate(double power) {
        setPower(Range.clip(power, -1.0, 1.0));
    }

    public boolean hasValidTarget() {
        double[] tagData = aprilTagSystem.getTagData();
        double range = tagData[0];
        double bearing = tagData[1];
        double detectedID = tagData[2];

        return range != -999 && range != -888 && bearing != -888
                && (int)detectedID == targetAprilTagID;
    }
}