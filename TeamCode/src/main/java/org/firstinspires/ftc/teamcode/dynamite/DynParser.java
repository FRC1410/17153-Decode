package org.firstinspires.ftc.teamcode.dynamite;//import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;

//import org.firstinspires.ftc.robotcore.external.Telemetry;
import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.lang.reflect.Array;
import java.nio.charset.StandardCharsets;
import java.util.*;
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
    private final boolean debug = true;
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
    // wierd calstar stuff
    private final Map<String, Object[]> variables = new HashMap<>();
    // this keeps track of lines of which a loop/function start command is placed
    private final List<Integer> funcLoopDepthData = new ArrayList<>();
    private boolean lineParseLock = false;
    private final JSONObject dynFuncLookup = new JSONObject();
    public void init(Telemetry telemetry, String configName){
        this.telemetry = telemetry;
        try {
            try {
                // non FTC
                RAWinit(configName);
            } catch (Exception e) {
                // FTC ready
                FTCinit();
            }
        } catch (JSONException e) {
            // output to telemetry
            this.telemetry.addData("ERROR PROCESSING JSON FOR DYN:",e);
            this.telemetry.update();
            throw new RuntimeException(e);
        }
    }
    private void RAWinit(String configName){
        try (BufferedReader reader1 = new BufferedReader(null)){//FileReader("/path/to/dyn/files/"+configName+".dyn", StandardCharsets.UTF_8))){
            // first pass on the file to parse the lines, and seprate by spaces
            List<String[]> parsedLines = new ArrayList<>();
            String line;
            System.out.println();
            while ((line = reader1.readLine()) != null){
                Array lineComponents = parseLine(line);
                //parsedLines.add(lineComponents);
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }
    private Array parseLine(String line){
        // clean up the string and remove extra spaces
        line = line.trim().replaceAll("\\s+"," ");
        // split the line into its sectors using the spaces, parenthesis, and square brackets
        for (int x = 0; x < line.length(); x++){
            char currentChar = line.charAt(x);
            switch (currentChar){
                case '(':{
                    // start new touple thingy
                }
                case ')':{
                    // end touple thing
                    // add to outside string
                }
                case ' ':{
                    // start new sector in current list
                }
                default: {
                    // add to current list
                }
            }
        }
        return null;
    }
    private void FTCinit() throws JSONException {
        makeFuncLookup();
        try {
            is = (InputStream) hardwareMap.appContext.getAssets().open(configName);
            reader = new BufferedReader(new InputStreamReader(is));
            int lineIndex = 1;
            String line;
            String[] lineData;
            while ((line = reader.readLine())!=null){
                lineData = splitLine(line);
                if (Objects.equals(lineData[0], "while") || Objects.equals(lineData[0], "for") || Objects.equals(lineData[0], "def_path")){
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
    public void addCommand(Consumer<String[]> func, String command) throws JSONException {
        dynFuncLookup.put(command, func);
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
        switch (line[0]) {
            case "Num":
                this.variables.put(line[1], new Object[]{line[2], double.class});
            case "Bool":
                this.variables.put(line[1], new Object[]{line[2], boolean.class});
            case "String":
                this.variables.put(line[1], new Object[]{line[2], String.class});
            case "List":
                String[] lis = Arrays.stream(line).skip(2).toArray(String[]::new);
                String[] nLis = new String[line.length - 2];
                for (String s : lis) {
                    String cleanedString = s
                            .replace("[", "")
                            .replace("]", "")
                            .strip()
                            .replace(",", "");
                    for (int i = 0; i < 3; i++) {
                        Object o = nLis[i];
                        if (o == null) {
                            o = cleanedString;
                            break;
                        }
                    }
                }
                this.variables.put(line[1], new Object[]{nLis, double.class});
            case "Json":
                String[] jLis = Arrays.stream(line).skip(2).toArray(String[]::new);
                String[] jNLis = new String[line.length - 2];
                for (String s : jLis) {
                    String cleanedString = s
                            .replace("{", "")
                            .replace("}", "")
                            .strip()
                            .replace(",", "");
                    for (int i = 0; i < 3; i++) {
                        Object o = jNLis[i];
                        if (o == null) {
                            String[] input = s
                                    .replace("\"", "")
                                    .split(":");
                            o=input;
                            break;
                        }
                    }
                }
                this.variables.put(line[1], new Object[]{jNLis, JSONArray.class});
        }
    }
    private void ParseMathOp(String[] line){

    }
    private void ParseMoveOp(String[] line){

    }
    private void ParseSoEstatements(String[] line){

    }
    private void ParseTelemetry(String[] line) {telemetry.addData("DYN telemetry", line[1]);}
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
class parenthesis{;
    public void add(Object thing){}
}
class squareBrackets{
    public void add(Object thing){}
}