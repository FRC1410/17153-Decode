package org.firstinspires.ftc.teamcode.pedroPathing;

import com.pedropathing.Drivetrain;
import com.pedropathing.follower.Follower;
import com.pedropathing.follower.FollowerConstants;
import com.pedropathing.ftc.FollowerBuilder;
import com.pedropathing.ftc.drivetrains.Mecanum;
import com.pedropathing.ftc.drivetrains.MecanumConstants;
import com.pedropathing.ftc.localization.constants.DriveEncoderConstants;
import com.pedropathing.ftc.localization.constants.OTOSConstants;
import com.pedropathing.ftc.localization.constants.PinpointConstants;
import com.pedropathing.ftc.localization.constants.ThreeWheelConstants;
import com.pedropathing.ftc.localization.constants.ThreeWheelIMUConstants;
import com.pedropathing.ftc.localization.constants.TwoWheelConstants;
import com.pedropathing.ftc.localization.localizers.DriveEncoderLocalizer;
import com.pedropathing.ftc.localization.localizers.OTOSLocalizer;
import com.pedropathing.ftc.localization.localizers.PinpointLocalizer;
import com.pedropathing.ftc.localization.localizers.ThreeWheelIMULocalizer;
import com.pedropathing.ftc.localization.localizers.ThreeWheelLocalizer;
import com.pedropathing.ftc.localization.localizers.TwoWheelLocalizer;
import com.pedropathing.localization.Localizer;
import com.pedropathing.paths.PathConstraints;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class VFpedroFollowerBuilder extends FollowerBuilder {
    private VFpedroFollowerConstants constants;
    private PathConstraints constraints;
    private HardwareMap hardwareMap;
    private Localizer localizer;
    private Drivetrain drivetrain;

    public VFpedroFollowerBuilder(VFpedroFollowerConstants constants, HardwareMap hardwareMap) {
        super(constants, hardwareMap);
        this.hardwareMap = hardwareMap;
        this.constants = constants;
    }
    public VFpedroFollowerBuilder setLocalizer(Localizer localizer) {
        this.localizer = localizer;
        return this;
    }

    public VFpedroFollowerBuilder driveEncoderLocalizer(DriveEncoderConstants lConstants) {
        return setLocalizer(new DriveEncoderLocalizer(this.hardwareMap, lConstants));
    }

    public VFpedroFollowerBuilder OTOSLocalizer(OTOSConstants lConstants) {
        return setLocalizer(new OTOSLocalizer(this.hardwareMap, lConstants));
    }

    public VFpedroFollowerBuilder pinpointLocalizer(PinpointConstants lConstants) {
        return setLocalizer(new PinpointLocalizer(this.hardwareMap, lConstants));
    }

    public VFpedroFollowerBuilder threeWheelIMULocalizer(ThreeWheelIMUConstants lConstants) {
        return setLocalizer(new ThreeWheelIMULocalizer(this.hardwareMap, lConstants));
    }

    public VFpedroFollowerBuilder threeWheelLocalizer(ThreeWheelConstants lConstants) {
        return setLocalizer(new ThreeWheelLocalizer(this.hardwareMap, lConstants));
    }

    public VFpedroFollowerBuilder twoWheelLocalizer(TwoWheelConstants lConstants) {
        return setLocalizer(new TwoWheelLocalizer(this.hardwareMap, lConstants));
    }

    public VFpedroFollowerBuilder setDrivetrain(Drivetrain drivetrain) {
        this.drivetrain = drivetrain;
        return this;
    }
    @Override
    public VFpedroFollowerBuilder mecanumDrivetrain(MecanumConstants mecanumConstants) {
        return setDrivetrain(new Mecanum(this.hardwareMap, mecanumConstants));
    }

    public VFpedroFollowerBuilder pathConstraints(PathConstraints pathConstraints) {
        this.constraints = pathConstraints;
        PathConstraints.setDefaultConstraints(pathConstraints);
        return this;
    }
    public VFpedroFollower build() {
        VFpedroFollower follower = VFpedroFollower.create(constants, localizer, drivetrain, constraints, this.hardwareMap);
        //follower = follower.create(constants, localizer, drivetrain, constraints, this.hardwareMap);
        return follower;
    }
}
