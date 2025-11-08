package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Util.DriverUtil.Rumbler;
import org.firstinspires.ftc.teamcode.Util.Toggle;

@TeleOp
public class Robot extends OpMode {
    private final Rumbler rumbler = new Rumbler();
    private final Toggle drivetrainToggle = new Toggle();

    private boolean lastA = false;
    private boolean lastB = false;
    private boolean lastX = false;
    private boolean lastY = false;

    @Override
    public void init() {
    }

    @Override
    public void start() {
        rumbler.startMatchTimer();
    }

    public void getTelemetry() {
        telemetry.update();
    }

    @Override
    public void loop() {
        boolean currentA = gamepad1.a;
        boolean currentB = gamepad1.b;
        boolean currentX = gamepad1.x;
        boolean currentY = gamepad1.y;

        rumbler.halftimeRumble(gamepad1);

        if (currentA && !lastA) {
            rumbler.rumbleBlip(gamepad1);
        }

        if (currentB && !lastB) {
            rumbler.rumbleBlips(gamepad1, 3);
        }

        if (currentX && !lastX) {
            rumbler.rumbleLeft(gamepad1,500);
        }

        if (currentY && !lastY) {
            rumbler.rumbleRight(gamepad1, 500);
        }

        lastA = currentA;
        lastB = currentB;
        lastX = currentX;
        lastY = currentY;

        getTelemetry();
    }
}