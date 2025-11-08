package org.firstinspires.ftc.teamcode.dynamite;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/*
 THE BASIC PROCESS FOR DYNAMITE SYSTEM
 1. parse into commands
 2. process variables
 3. process loops
 4. process functions
 5. convert into pedroPath
*/

public class DynParser {
    private Telemetry telemetry;
    // the following variables are here to allow us to index them into a json
    private final Consumer<String[]> VarDec = line -> ParseVarDec(line);
    private final Consumer<String[]> MathOp = line -> ParseMathOp(line);
    private final Consumer<String[]> MoveOp = line -> ParseMoveOp(line);
    private final Consumer<String[]> SoEDec = line -> ParseSoEstatements(line);
    private final Consumer<String[]> Teleme = line -> ParseTelemetry(line);
    private final BiConsumer<String[], Integer> doLoop = (line, lineIndex) -> ParseLoop(line, lineIndex);
    private final BiConsumer<String[], Integer> doFunc = (line, lineIndex) -> ParseFunc(line, lineIndex);
    public String configName = "";
    public InputStream is;
    public BufferedReader reader;
    // this keeps track of lines of which a loop/function start command is placed
    private final List<Integer> funcLoopDepthData = new ArrayList<>();

    private final JSONObject dynFuncLookup = new JSONObject();
    public void init(Telemetry telemetry){
        this.telemetry = telemetry;
        try {
            RAWinit();
        } catch (JSONException e) {
            // output to telemetry
            this.telemetry.addData("ERROR PROCESSING JSON FOR DYN:",e);
            this.telemetry.update();
            throw new RuntimeException(e);
        }
    }
    private void RAWinit() throws JSONException {
        makeFuncLookup();
        try {
            is = hardwareMap.appContext.getAssets().open(configName);
            reader = new BufferedReader(new InputStreamReader(is));
            int lineIndex = 1;
            String line;
            String[] lineData;
            while ((line = reader.readLine())!=null){
                lineData = splitLine(line);
                if (lineData[0] == "while" || lineData[0] == "for" || lineData[0] == "def_path"){
                    BiConsumer<String[],Integer> func = (BiConsumer<String[],Integer>) dynFuncLookup.get(lineData[0]);
                    func.accept(lineData, lineIndex);
                } else {
                    Consumer<String[]> func = (Consumer<String[]>) dynFuncLookup.get(lineData[0]);
                    func.accept(lineData);
                }
                lineIndex++;
            }
            reader.close();
        } catch (IOException | JSONException e){
            telemetry.addData("IOException when reading .dyn files",e);
            telemetry.update();
        }
    }
    private void makeFuncLookup() throws JSONException {
        // variables
        dynFuncLookup.put("FieldCord", VarDec);
        dynFuncLookup.put("FieldPos", VarDec);
        dynFuncLookup.put("Num", VarDec);
        dynFuncLookup.put("Bool", VarDec);
        dynFuncLookup.put("String", VarDec);
        dynFuncLookup.put("List", VarDec);
        dynFuncLookup.put("Json", VarDec);
        // math
        dynFuncLookup.put("SUB", MathOp);
        dynFuncLookup.put("MUX", MathOp);
        dynFuncLookup.put("DIV", MathOp);
        dynFuncLookup.put("POW", MathOp);
        dynFuncLookup.put("SQR", MathOp);
        dynFuncLookup.put("SIN", MathOp);
        dynFuncLookup.put("invSIN", MathOp);
        dynFuncLookup.put("COS", MathOp);
        dynFuncLookup.put("invCOS", MathOp);
        dynFuncLookup.put("TAN", MathOp);
        dynFuncLookup.put("invTAN", MathOp);
        // movement commands
        dynFuncLookup.put("turnTo", MoveOp);
        dynFuncLookup.put("goTo", MoveOp);
        dynFuncLookup.put("followBezier", MoveOp);
        // start/end declarations
        dynFuncLookup.put("PathStartPoint", SoEDec);
        dynFuncLookup.put("autoPath", SoEDec);
        // telemetry
        dynFuncLookup.put("output2telem", Teleme);
        // loop
        dynFuncLookup.put("While", doLoop);
        dynFuncLookup.put("for", doLoop);
        // function
        dynFuncLookup.put("def_path", doFunc);
    }
    private String[] splitLine(String Line){
        List<String> out = new ArrayList<>();
        String temp = "";
        for (int i = 0; i < Line.length(); i++){
            char x = Line.charAt(i);
            if (x == ' '){
                out.add(temp);
            } else {
                temp = temp+x;
            }
        }
        String[] out2 = out.toArray(new String[0]);
        return out2;
    }
    private void ParseVarDec(String[] line){

    }
    private void ParseMathOp(String[] line){

    }
    private void ParseMoveOp(String[] line){

    }
    private void ParseSoEstatements(String[] line){

    }
    private void ParseTelemetry(String[] line) {
        telemetry.addData("DYN telemetry", line[1]);
    }
    
    private void ParseLoop(String[] line, int lineIndex){
        // process while loop
        if (line[1] == "while"){
        }
        // process for loop
        else if (line[1] == "for"){
        }
    }
    private void ParseFunc(String[] line, int lineIndex) {

    }
}