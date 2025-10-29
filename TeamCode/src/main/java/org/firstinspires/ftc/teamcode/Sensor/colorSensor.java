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

    public static int colourAlp;

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
        float a = colors.alpha;

        if (a < 0.01f) {
            float normalizedR = r / a;
            float normalizedG = g / a;
            float normalizedB = b / a;


            colourRed = (int)(normalizedR * 255);
            colourGre = (int)(normalizedG * 255);
            colourBlu = (int)(normalizedB * 255);
            colourAlp = (int)(a * 255);

        } else {
            colourRed = 0;
            colourGre = 0;
            colourBlu = 0;
            colourAlp = 0;

        }


        colourRed = Math.max(0, Math.min(colourRed, 255));
        colourGre = Math.max(0, Math.min(colourGre, 255));
        colourBlu = Math.max(0, Math.min(colourBlu, 255));
        colourAlp = Math.max(0, Math.min(colourAlp, 255));
    }

    public void colourData(Telemetry telemetry) {
        updateRGB();
        NormalizedRGBA colors = sensor.getNormalizedColors();
        telemetry.addData("Raw Values", "(%.3f, %.3f, %.3f, %.3f)", colors.red, colors.green, colors.blue, colors.alpha);
        telemetry.addData("RGBA", "(%d, %d, %d, %d, %d)", colourRed, colourGre, colourBlu, colourAlp);
        telemetry.update();
    }
}
