package org.firstinspires.ftc.teamcode.Subsystem;
import static com.qualcomm.robotcore.hardware.DcMotorSimple.Direction.FORWARD;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Range;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import static org.firstinspires.ftc.teamcode.Util.IDs.*;
import static org.firstinspires.ftc.teamcode.Util.Constants.*;
import static org.firstinspires.ftc.teamcode.Util.Tuning.*;
import org.firstinspires.ftc.teamcode.Util.PIDController;

public class Turret {

    DcMotor turretMotor;
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
    public boolean trackAprilTag(Telemetry telemetry) {
        double[] tagData = aprilTagSystem.getTagData();

        double range = tagData[0];
        double bearing = tagData[1];
        double detectedID = tagData[2];

        telemetry.addData("Motor Power", "%.4f", turretMotor.getPower());
        telemetry.addData("Position", getCurrentPosition());
        telemetry.addData("Angle", "%.2f°", getCurrentAngle());
        telemetry.addData("Target Tag ID", targetAprilTagID);

        if (range == -999) {
            telemetry.addData("Status", "No tags detected");
            stopMotor();
            return false;
        }

        if (range == -888 || bearing == -888) {
            telemetry.addData("Status", "Error reading tag data");
            stopMotor();
            return false;
        }

        if ((int)detectedID != targetAprilTagID) {
            telemetry.addData("Status", "Wrong tag detected");
            telemetry.addData("Detected Tag ID", (int)detectedID);
            stopMotor();
            return false;
        }

        double normalizedBearing = normalizeAngle(bearing);

        telemetry.addData("Status", "Tracking");
        telemetry.addData("Detected Tag ID", (int)detectedID);
        telemetry.addData("Range", "%.2f", range);
        telemetry.addData("Bearing", "%.2f°", bearing);
        telemetry.addData("Normalized Bearing", "%.2f°", normalizedBearing);

        if (Math.abs(normalizedBearing) < ACCEPTED_BEARING) {
            telemetry.addData("Lock Status", "LOCKED ON TARGET");
            stopMotor();
            return true;
        }

        double pidOutput = trackingPID.calculate(0, normalizedBearing);
        double rawPower = -pidOutput;

        // Collapse tiny values to exact zero to avoid confusion
        if (Math.abs(rawPower) < 1e-6) {
            rawPower = 0.0;
        }

        double power = rawPower;

        // Enforce minimum tracking power - if we need to move, give it at least MIN_TRACKING_POWER
        if (Math.abs(power) > 0 && Math.abs(power) < MIN_TRACKING_POWER) {
            power = Math.signum(power) * MIN_TRACKING_POWER;
        }

        double beforeClip = power;
        power = Range.clip(power, -MAX_TRACKING_POWER, MAX_TRACKING_POWER);

        telemetry.addData("Lock Status", "Adjusting...");
        telemetry.addData("PID Output", "%.6f", pidOutput);
        telemetry.addData("Raw Power", "%.6f", rawPower);
        telemetry.addData("Power (w/ MIN)", "%.6f", beforeClip);
        telemetry.addData("Applied Power", "%.4f", power);
        telemetry.addData("MIN_TRACKING_POWER", "%.2f", MIN_TRACKING_POWER);

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