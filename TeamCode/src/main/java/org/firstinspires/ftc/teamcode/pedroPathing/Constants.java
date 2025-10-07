package org.firstinspires.ftc.teamcode.pedroPathing;

import static org.firstinspires.ftc.teamcode.Util.IDs.BACK_LEFT_MOTOR_ID;
import static org.firstinspires.ftc.teamcode.Util.IDs.BACK_RIGHT_MOTOR_ID;
import static org.firstinspires.ftc.teamcode.Util.IDs.FRONT_LEFT_MOTOR_ID;
import static org.firstinspires.ftc.teamcode.Util.IDs.FRONT_RIGHT_MOTOR_ID;

import com.pedropathing.follower.Follower;
import com.pedropathing.follower.FollowerConstants;
import com.pedropathing.ftc.FollowerBuilder;
import com.pedropathing.ftc.localization.Encoder;
import com.pedropathing.ftc.localization.constants.DriveEncoderConstants;
import com.pedropathing.paths.PathConstraints;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Constants {
    public static DriveEncoderConstants localizerConstants = new DriveEncoderConstants()
            .rightFrontMotorName(FRONT_RIGHT_MOTOR_ID)
            .rightRearMotorName(BACK_RIGHT_MOTOR_ID)
            .leftRearMotorName(BACK_LEFT_MOTOR_ID)
            .leftFrontMotorName(FRONT_LEFT_MOTOR_ID)
            .leftFrontEncoderDirection(Encoder.FORWARD)
            .leftRearEncoderDirection(Encoder.FORWARD)
            .rightFrontEncoderDirection(Encoder.FORWARD)
            .rightRearEncoderDirection(Encoder.FORWARD);
    public static FollowerConstants followerConstants = new FollowerConstants();

    public static PathConstraints pathConstraints = new PathConstraints(0.99, 100, 1, 1);

    public static Follower createFollower(HardwareMap hardwareMap) {
        return new FollowerBuilder(followerConstants, hardwareMap)
                .pathConstraints(pathConstraints)
                .build();
    }
}
