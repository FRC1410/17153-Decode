package org.firstinspires.ftc.teamcode.Sensor;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;
import com.qualcomm.hardware.rev.RevColorSensorV3;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class colorSensor {
    NormalizedColorSensor sensor;
    public static int colourRed;
    public static int colourBlu;
    public static int colourGre;

    public void init(HardwareMap hardwareMap, String COLOUR_SENSOR_ID) {
        sensor = hardwareMap.get(NormalizedColorSensor.class, COLOUR_SENSOR_ID);
        try {
            RevColorSensorV3 revSensor = hardwareMap.get(RevColorSensorV3.class, COLOUR_SENSOR_ID);
            revSensor.enableLed(true);
        } catch (Exception e) {

        }
    }

    public int detectColour() {
        updateRGB();
        if (colourRed > colourGre && colourRed > colourBlu) return 1; // RED
        if (colourGre > colourRed && colourGre > colourBlu) return 2; // GREEN
        if (colourBlu > colourRed && colourBlu > colourGre) return 3; // BLUE
        return 0;
    }

    public void updateRGB() {
        NormalizedRGBA colors = sensor.getNormalizedColors();
        float r = colors.red;
        float g = colors.green;
        float b = colors.blue;


        float max = Math.max(r, Math.max(g, b));

        if (max < 0.01f) {

            colourRed = (int)(r * 2550);
            colourGre = (int)(g * 1275); //default 2550, changed to deescalate green. 1275 is 255 * 5
            colourBlu = (int)(b * 2550);
        } else {

            float normalizedR = r / max;
            float normalizedG = g / max;
            float normalizedB = b / max;


            colourRed = (int)(normalizedR * 255);
            colourGre = (int)(normalizedG * 255);
            colourBlu = (int)(normalizedB * 255);
        }


        colourRed = Math.min(colourRed, 255);
        colourGre = Math.min(colourGre, 255);
        colourBlu = Math.min(colourBlu, 255);
    }

    public void colourData(Telemetry telemetry) {
        updateRGB();
        NormalizedRGBA colors = sensor.getNormalizedColors();
        telemetry.addData("Raw Values", "(%.3f, %.3f, %.3f)", colors.red, colors.green, colors.blue);
        telemetry.addData("RGB", "(%d, %d, %d)", colourRed, colourGre, colourBlu);
        telemetry.update();
    }
}
