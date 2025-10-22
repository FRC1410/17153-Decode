package org.firstinspires.ftc.teamcode.Subsystem;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;
        private NormalizedColorSensor colorSensor;


}
public void init(HardwareMap hardwareMap, String deviceName) {
    colorSensor = hardwareMap.get(NormalizedColorSensor.class, deviceName);
}
