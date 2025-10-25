package org.firstinspires.ftc.teamcode.Sensor;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class colorSensor {
    NormalizedColorSensor sensor;
    public static double colourRed;
    public static double colourBlu;

    public static double colourGre;



    public void init(HardwareMap hardwareMap, String COLOUR_SENSOR_ID) {
        sensor = hardwareMap.get(NormalizedColorSensor.class, COLOUR_SENSOR_ID);
    }
    public int detectColour() {
        int colour = sensor.getNormalizedColors().toColor();
        return colour;

    }
    public void updateRGB() {
        NormalizedRGBA colors = sensor.getNormalizedColors();
        colourRed = (int)(colors.red * 255);
        colourGre = (int)(colors.green * 255);
        colourBlu = (int)(colors.blue * 255);
    }

    public void colourData(Telemetry telemetry) {
        updateRGB();
        telemetry.addData("RGB", "(%d, %d, %d)", colourRed, colourGre, colourBlu);
        telemetry.update();

    }

}





