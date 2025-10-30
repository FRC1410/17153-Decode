package org.firstinspires.ftc.teamcode.dynamite;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;
import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class dynParser {
    public String configName = "";
    public InputStream is;
    public BufferedReader reader;
    public void init(){
        try {
            is = hardwareMap.appContext.getAssets().open(configName);
            reader = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = reader.readLine())!=null){

            }
            reader.close();
        } catch (IOException e){
            telemetry.addData("IOException when reading .dyn files",e);
        }
    }
    private void ParseLine(String line){

    }
    private void ParseVarDec(String line){

    }
    private void ParseMathOp(String line){

    }
    private void ParseMoveOp(String line){

    }
}