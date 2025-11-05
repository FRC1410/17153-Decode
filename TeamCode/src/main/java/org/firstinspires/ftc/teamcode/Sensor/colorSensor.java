package org.firstinspires.ftc.teamcode.Sensor;

import static org.firstinspires.ftc.teamcode.Util.Constants.*;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;
import com.qualcomm.hardware.rev.RevColorSensorV3;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class colorSensor {
    NormalizedColorSensor sensor;
    NormalizedColorSensor sensor2;
    public static int colourRed;
    public static int colourBlu;
    public static int colourGre;
    public static int colourAlp;

    public void init(HardwareMap hardwareMap, String COLOUR_SENSOR_ID, String COLOUR_SENSOR_ID_2) {
        sensor = hardwareMap.get(NormalizedColorSensor.class, COLOUR_SENSOR_ID);
        sensor2 = hardwareMap.get(NormalizedColorSensor.class, COLOUR_SENSOR_ID_2);
        try {
            RevColorSensorV3 revSensor = hardwareMap.get(RevColorSensorV3.class, COLOUR_SENSOR_ID);
            revSensor.enableLed(true);
        } catch (Exception e) {

        }
        try {
            RevColorSensorV3 revSensor2 = hardwareMap.get(RevColorSensorV3.class, COLOUR_SENSOR_ID_2);
            revSensor2.enableLed(true);
        } catch (Exception e) {

        }
    }

    public int detectColour() {
        updateRGB();
        if (colourRed > colourGre && colourRed > colourBlu) return 1;
        if (colourGre > colourRed && colourGre > colourBlu) return 2;
        if (colourBlu > colourRed && colourBlu > colourGre) return 3;
        return 0;
    }

    public void updateRGB() {
        NormalizedRGBA colors = sensor.getNormalizedColors();
        NormalizedRGBA colors2 = sensor2.getNormalizedColors();

        float r = (colors.red + colors2.red) / 2;
        float g = (colors.green + colors2.green) / 2;
        float b = (colors.blue + colors2.blue) / 2;
        float a = (colors.alpha + colors2.alpha) / 2;

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

    public boolean isPurple(){
        if (((colourRed > PURPLE_LOWER_RED) && (colourRed < PURPLE_UPPER_RED)) && ((colourGre > PURPLE_LOWER_GREEN) && (colourGre < PURPLE_UPPER_GREEN)) && ((colourBlu > PURPLE_LOWER_BLUE) && (colourBlu < PURPLE_UPPER_BLUE))){
            return true;
        }
        return false;
    }

    public boolean isGreen(){
        if (((colourRed > GREEN_LOWER_RED) && (colourRed < GREEN_UPPER_RED)) && ((colourGre > GREEN_LOWER_GREEN) && (colourGre < GREEN_UPPER_GREEN)) && ((colourBlu > GREEN_LOWER_BLUE) && (colourBlu < GREEN_UPPER_BLUE))){
            return true;
        }
        return false;
    }


    public void colourData(Telemetry telemetry) {
        updateRGB();
        NormalizedRGBA colors = sensor.getNormalizedColors();
        NormalizedRGBA colors2 = sensor2.getNormalizedColors();
        telemetry.addData("Raw Values Sensor 1", "(%.3f, %.3f, %.3f, %.3f)", colors.red, colors.green, colors.blue, colors.alpha);
        telemetry.addData("Raw Values Sensor 2", "(%.3f, %.3f, %.3f, %.3f)", colors2.red, colors2.green, colors2.blue, colors2.alpha);
        telemetry.addData("RGB", "(%d, %d, %d)", colourRed, colourGre, colourBlu);
        telemetry.addData("Alpha: ", colourAlp);
        telemetry.addData("IsPurple: ", isPurple());
        telemetry.addData("IsGreen: ", isGreen());
        telemetry.update();
    }
}