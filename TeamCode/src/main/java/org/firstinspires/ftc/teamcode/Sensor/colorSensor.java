package org.firstinspires.ftc.teamcode.Sensor;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class colorSensor {
    NormalizedColorSensor sensor;
    public static double colourRed;
    public static double colourBlu;

    public static double colourGre;



    public void init(HardwareMap hardwareMap, String COLOUR_SENSOR_ID) {
        sensor = hardwareMap.get(NormalizedColorSensor.class, COLOUR_SENSOR_ID);
    }
    private int detectColour() {
        int colour = sensor.getNormalizedColors().toColor();
        return colour;
    }
    public void colourData(Telemetry telemetry) {
        telemetry.addData("Colour: ", this.detectColour());
        telemetry.update();

    }

}





