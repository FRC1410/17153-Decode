package org.firstinspires.ftc.teamcode.Sensor;

import static org.firstinspires.ftc.teamcode.Util.Constants.*;

import android.graphics.Color;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;
import com.qualcomm.robotcore.hardware.SwitchableLight;
import com.qualcomm.hardware.rev.RevColorSensorV3;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class colorSensor {
    private NormalizedColorSensor sensor;
    private NormalizedColorSensor sensor2;

    private int colourRed;
    private int colourBlu;
    private int colourGre;
    private int colourAlp;

    private float[] hsvValues = new float[3];

    private float gain = 2.0f;

    public void init(HardwareMap hardwareMap, String COLOUR_SENSOR_ID, String COLOUR_SENSOR_ID_2) {
        sensor = hardwareMap.get(NormalizedColorSensor.class, COLOUR_SENSOR_ID);
        sensor2 = hardwareMap.get(NormalizedColorSensor.class, COLOUR_SENSOR_ID_2);

        sensor.setGain(gain);
        sensor2.setGain(gain);

        if (sensor instanceof SwitchableLight) {
            ((SwitchableLight) sensor).enableLight(true);
        }
        if (sensor2 instanceof SwitchableLight) {
            ((SwitchableLight) sensor2).enableLight(true);
        }
    }

    public void setGain(float newGain) {
        if (newGain >= 1.0f) {
            this.gain = newGain;
            sensor.setGain(gain);
            sensor2.setGain(gain);
        }
    }

    public float getGain() {
        return gain;
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

        if (a > 0.01f) {
            float normalizedR = r / a;
            float normalizedG = g / a;
            float normalizedB = b / a;

            colourRed = (int)(normalizedR * 255);
            colourGre = (int)(normalizedG * 255);
            colourBlu = (int)(normalizedB * 255);
            colourAlp = (int)(a * 255);
        } else {

            colourRed = (int)(r * 255);
            colourGre = (int)(g * 255);
            colourBlu = (int)(b * 255);
            colourAlp = (int)(a * 255);
        }

        colourRed = Math.max(0, Math.min(colourRed, 255));
        colourGre = Math.max(0, Math.min(colourGre, 255));
        colourBlu = Math.max(0, Math.min(colourBlu, 255));
        colourAlp = Math.max(0, Math.min(colourAlp, 255));


        Color.RGBToHSV(colourRed, colourGre, colourBlu, hsvValues);
    }

    public boolean isPurple() {
        updateRGB();
        return ((colourRed > PURPLE_LOWER_RED) && (colourRed < PURPLE_UPPER_RED))
                && ((colourGre > PURPLE_LOWER_GREEN) && (colourGre < PURPLE_UPPER_GREEN))
                && ((colourBlu > PURPLE_LOWER_BLUE) && (colourBlu < PURPLE_UPPER_BLUE));
    }

    public boolean isGreen() {
        updateRGB();
        return ((colourRed > GREEN_LOWER_RED) && (colourRed < GREEN_UPPER_RED))
                && ((colourGre > GREEN_LOWER_GREEN) && (colourGre < GREEN_UPPER_GREEN))
                && ((colourBlu > GREEN_LOWER_BLUE) && (colourBlu < GREEN_UPPER_BLUE));
    }

    public boolean isPurpleHSV() {
        updateRGB();
        float hue = hsvValues[0];
        float saturation = hsvValues[1];

        return (saturation > 0.3f) && ((hue > 270 && hue < 330) || (hue < 30));
    }

    public boolean isGreenHSV() {
        updateRGB();
        float hue = hsvValues[0];
        float saturation = hsvValues[1];

        return (saturation > 0.3f) && (hue > 90 && hue < 150);
    }

    public void colourData(Telemetry telemetry) {
        updateRGB();
        NormalizedRGBA colors = sensor.getNormalizedColors();
        NormalizedRGBA colors2 = sensor2.getNormalizedColors();

        telemetry.addData("Gain", "%.2f", gain);
        telemetry.addLine();
        telemetry.addData("Sensor 1 (R,G,B,A)", "%.3f, %.3f, %.3f, %.3f",
                colors.red, colors.green, colors.blue, colors.alpha);
        telemetry.addData("Sensor 2 (R,G,B,A)", "%.3f, %.3f, %.3f, %.3f",
                colors2.red, colors2.green, colors2.blue, colors2.alpha);
        telemetry.addLine();
        telemetry.addData("Averaged RGB", "(%d, %d, %d)", colourRed, colourGre, colourBlu);
        telemetry.addData("Alpha", "%d", colourAlp);
        telemetry.addLine();
        telemetry.addData("HSV (H,S,V)", "%.1f°, %.2f, %.2f",
                hsvValues[0], hsvValues[1], hsvValues[2]);
        telemetry.addLine();
        telemetry.addData("Is Purple (RGB)", isPurple());
        telemetry.addData("Is Green (RGB)", isGreen());
        telemetry.addData("Is Purple (HSV)", isPurpleHSV());
        telemetry.addData("Is Green (HSV)", isGreenHSV());
        telemetry.update();
    }


    public int getColourRed() { return colourRed; }
    public int getColourGreen() { return colourGre; }
    public int getColourBlue() { return colourBlu; }
    public int getColourAlpha() { return colourAlp; }

    public float getHue() { return hsvValues[0]; }
    public float getSaturation() { return hsvValues[1]; }
    public float getValue() { return hsvValues[2]; }
}