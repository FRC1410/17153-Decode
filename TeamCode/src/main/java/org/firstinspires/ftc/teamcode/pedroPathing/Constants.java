package org.firstinspires.ftc.teamcode.pedroPathing;

import static org.firstinspires.ftc.teamcode.Util.IDs.BACK_LEFT_MOTOR_ID;
import static org.firstinspires.ftc.teamcode.Util.IDs.BACK_RIGHT_MOTOR_ID;
import static org.firstinspires.ftc.teamcode.Util.IDs.FRONT_LEFT_MOTOR_ID;
import static org.firstinspires.ftc.teamcode.Util.IDs.FRONT_RIGHT_MOTOR_ID;

import com.pedropathing.control.FilteredPIDFCoefficients;
import com.pedropathing.control.PIDFCoefficients;
import com.pedropathing.follower.Follower;
import com.pedropathing.follower.FollowerConstants;
import com.pedropathing.ftc.FollowerBuilder;
import com.pedropathing.ftc.drivetrains.MecanumConstants;
import com.pedropathing.ftc.localization.Encoder;
import com.pedropathing.ftc.localization.constants.DriveEncoderConstants;
import com.pedropathing.paths.PathConstraints;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Constants {
    public static MecanumConstants driveConstants = new MecanumConstants()
            .rightFrontMotorName(FRONT_RIGHT_MOTOR_ID)
            .rightRearMotorName(BACK_RIGHT_MOTOR_ID)
            .leftRearMotorName(BACK_LEFT_MOTOR_ID)
            .leftFrontMotorName(FRONT_LEFT_MOTOR_ID)
            .leftFrontMotorDirection(DcMotorSimple.Direction.REVERSE)
            .leftRearMotorDirection(DcMotorSimple.Direction.REVERSE)
            .rightFrontMotorDirection(DcMotorSimple.Direction.FORWARD)
            .rightRearMotorDirection(DcMotorSimple.Direction.REVERSE);
    public static DriveEncoderConstants localizerConstants = new DriveEncoderConstants()
            .rightFrontMotorName(FRONT_RIGHT_MOTOR_ID)
            .rightRearMotorName(BACK_RIGHT_MOTOR_ID)
            .leftRearMotorName(BACK_LEFT_MOTOR_ID)
            .leftFrontMotorName(FRONT_LEFT_MOTOR_ID)
            .leftFrontEncoderDirection(Encoder.REVERSE)
            .leftRearEncoderDirection(Encoder.REVERSE)
            .rightFrontEncoderDirection(Encoder.FORWARD)
            .rightRearEncoderDirection(Encoder.REVERSE)
            .robotWidth(16.5)
            .robotLength(17)
            .forwardTicksToInches(0.004880721498724319)
            .strafeTicksToInches(0.0046481890386523665)
            .turnTicksToInches(0.01119074420705601);
    public static FollowerConstants followerConstants = new FollowerConstants()
            .mass(7)
            .forwardZeroPowerAcceleration(-25.9346931313679598)
            .lateralZeroPowerAcceleration(-67.342491844080064)
            .translationalPIDFCoefficients(new PIDFCoefficients(
                    0.035,
                    0.008,
                    0,
                    0.02
            ))
            .headingPIDFCoefficients(new PIDFCoefficients(
                    0.8,
                    0,
                    0,
                    0.01
            ))
            .drivePIDFCoefficients(new FilteredPIDFCoefficients(
                    0.1,
                    0,
                    0.00035,
                    0.6,
                    0.015
            ))
            .centripetalScaling(0.0005);

    public static PathConstraints pathConstraints = new PathConstraints(
            0.995,
            0.1,
            0.1,
            0.009,
            50,
            1.25,
            10,
            1);

    public static Follower createFollower(HardwareMap hardwareMap) {
        return new FollowerBuilder(followerConstants, hardwareMap)
                .mecanumDrivetrain(driveConstants)
                .driveEncoderLocalizer(localizerConstants)
                .pathConstraints(pathConstraints)
                .build();
    }
}
