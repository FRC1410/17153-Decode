// Call me Linus Torvalds the way I be hatin' on these indentations.
package org.firstinspires.ftc.teamcode.dynamite.commandSequencer;

import org.firstinspires.ftc.teamcode.dynamite.commandSequencer.Commands.Command;
import org.firstinspires.ftc.teamcode.dynamite.commandSequencer.Commands.util.Sleep;
import org.firstinspires.ftc.teamcode.dynamite.commandSequencer.Commands.controlFlow.Condition;
import org.firstinspires.ftc.teamcode.dynamite.commandSequencer.Commands.controlFlow.For;
import org.firstinspires.ftc.teamcode.dynamite.commandSequencer.Commands.controlFlow.If;
import org.firstinspires.ftc.teamcode.dynamite.commandSequencer.Commands.controlFlow.While;
import org.firstinspires.ftc.teamcode.dynamite.commandSequencer.Commands.function.DynPath;
import org.firstinspires.ftc.teamcode.dynamite.commandSequencer.Commands.function.RunPath;
import org.firstinspires.ftc.teamcode.dynamite.commandSequencer.Commands.math.arithmetic.*;
import org.firstinspires.ftc.teamcode.dynamite.commandSequencer.Commands.math.trig.*;
import org.firstinspires.ftc.teamcode.dynamite.commandSequencer.Commands.movement.bezierStuff.DoBezier;
import org.firstinspires.ftc.teamcode.dynamite.commandSequencer.Commands.movement.GoTo;
import org.firstinspires.ftc.teamcode.dynamite.commandSequencer.Commands.movement.splineStuff.DoSpline;
import org.firstinspires.ftc.teamcode.dynamite.commandSequencer.Commands.movement.splineStuff.DoSplineLinear;
import org.firstinspires.ftc.teamcode.dynamite.commandSequencer.Commands.movement.splineStuff.DoSplineSpline;
import org.firstinspires.ftc.teamcode.dynamite.commandSequencer.Commands.movement.TurnTo;
import org.firstinspires.ftc.teamcode.dynamite.commandSequencer.Commands.random.RngBoolean;
import org.firstinspires.ftc.teamcode.dynamite.commandSequencer.Commands.random.RngDouble;
import org.firstinspires.ftc.teamcode.dynamite.commandSequencer.Commands.random.RngFloat;
import org.firstinspires.ftc.teamcode.dynamite.commandSequencer.Commands.random.RngInteger;
import org.firstinspires.ftc.teamcode.dynamite.commandSequencer.Commands.telemetry.AddData;
import org.firstinspires.ftc.teamcode.dynamite.commandSequencer.Commands.telemetry.Clear;
import org.firstinspires.ftc.teamcode.dynamite.commandSequencer.Commands.telemetry.Update;
import org.firstinspires.ftc.teamcode.dynamite.commandSequencer.Commands.variables.*;
import org.firstinspires.ftc.teamcode.dynamite.commandSequencer.variables.VariableTypes;
import org.firstinspires.ftc.teamcode.dynamite.commandSequencer.variables.primitives.DynBoolean;
import org.firstinspires.ftc.teamcode.dynamite.commandSequencer.variables.primitives.DynNumber;
import org.firstinspires.ftc.teamcode.dynamite.commandSequencer.variables.primitives.DynString;
import org.firstinspires.ftc.teamcode.dynamite.Tokenizer.Token;
import org.firstinspires.ftc.teamcode.dynamite.Tokenizer.TokenTypes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.firstinspires.ftc.teamcode.dynamite.Tokenizer.TokenTypes.*;

// TODO: convert all executed exceptions in here into normal "Exceptions" or at least extensions of it.
// TODO: separate the method calls in delegateToken to separate classes
// TODO: annotate all 'i' modifiers
// something to note about this process is that most of the errors
// that we throw here are going to be the most common errors
// for a prog to see while developing with dyn.
// Also there is an abhorrent amount of """magic numbers"" in this process.
// and I am sorry for that. I do not see an effective way that would not
// require the re-construction of the language to solve this.
// DO YOUR BEST TO NOT TOUCH THE I VALUE MODIFIERS
public class CommandConstructor {
    private String mainFuncName = "Main";
    private String[] ogFileLines;
    private Token[] givenTokens;
    private final ArrayList<Command> depthTracker;
    private final ArrayList<Command> finalCommands;
    private final Map<String, DynPath> funcIDmap;
    private int i;

    public CommandConstructor(String ogFile){
        i = 0;
        depthTracker = new ArrayList<>();
        finalCommands = new ArrayList<>();
        funcIDmap = new HashMap<>();
    }

    public String getMainFuncName(){
        return mainFuncName;
    }
    public Map<String,DynPath> getFuncIDmap(){
        return funcIDmap;
    }

    public void processTokens(Token[] tokenstream){
        givenTokens = tokenstream;
        while (true){
            if (i <= tokenstream.length-1) {
                delegateToken();
            } else {
                break;
            }
        }
    }
    private void delegateToken() {
        Token current = givenTokens[i];
        // we only care about the "starters" of the token stream
        switch (current.type()) {
            case Add, Sub, Mux, Div,
                 Pow, Sqrt, Sin, iSin,
                 Cos, iCos, Tan, iTan,
                 toRad, toDeg,
                 Increment, Decrement -> processMathOp();
            case NumberDef, BoolDef, StringDef, List,
                 Json, FieldCord, FieldPos -> processVariable();
            case Get, Insert, Append,
                 Remove, Set -> processListJsonOp();
            case TurnTo, GoTo,
                 doBez,followSpline,
                 followSplineLinear,
                 followSplineSpline-> processMoveOp();
            case DefPath, While, For, If -> processFuncLoopIf();
            case AddData, Update, Clear -> processTelemetry();
            case RngBoolean, RngDouble,
                 RngInteger, RngFloat -> processRandomOp();
            case PathStartPos -> processPathStart();
            case Cmd -> processCommand();
            case MainPathFunc -> processMainPathFunc();
            case Run -> processRun();
            case End -> processEnd();
            case Sleep -> processExtras();
            default -> {
                String tokenVal;
                if (current.getValue() == null){
                    tokenVal = current.type().toString();
                } else {
                    tokenVal = java.lang.String.valueOf(current.getValue());
                }
                throwError("Unknown keyword: " + tokenVal);
            }
        }
    }

    private void processExtras(){
        if (nextIsType(Name)){
            i++;
            String name = (String)givenTokens[i].getValue();
            addCommand(new Sleep(getLine(),name));
            i++;
        } else if (nextIsType(Number)){
            i++;
            double time = (double)givenTokens[i].getValue();
            addCommand(new Sleep(getLine(),time));
            i++;
        } else {
            i++;
            throwError("Expected name/number | Got: "+givenTokens[i].type());
        }
    }
    private void processRun(){
        int line = givenTokens[i].getLine();
        i++;
        if (givenTokens[i].type() == Name){
            String pathID = java.lang.String.valueOf(givenTokens[i].getValue());
            addCommand(new RunPath(line,pathID));
        } else {
            throwError("Cannot use non-name input for Run Command!");
        }
        i++;
    }
    private void processEnd(){
        i++;
        // ArrayList.getLast()/removeLast() are Java 21 SequencedCollection methods and
        // are absent from android.jar; indexed access is the portable equivalent.
        if (depthTracker.size() == 1) {
            finalCommands.add(depthTracker.remove(depthTracker.size() - 1));
        } else if (!depthTracker.isEmpty()){
            Command currentDepth = depthTracker.get(depthTracker.size() - 1);
            depthTracker.remove(depthTracker.size() - 1);
            depthTracker.get(depthTracker.size() - 1).addCommand(currentDepth);
        }
    }
    private void processMathOp(){
        Token current = givenTokens[i];
        switch (current.type()){
            // 1-2 IO ops
            case Sqrt -> {
                if (nextIsType(Name,To,Name)){
                    i++;
                    Token name0 = givenTokens[i];
                    Token name1 = givenTokens[i+2];
                    addCommand(new Sqrt(name0.getLine(), (String)name0.getValue(),(String)name1.getValue()));
                    i+=3;
                } else if (nextIsType(Number,To,Name)){
                    i++;
                    Token number = givenTokens[i];
                    Token name = givenTokens[i+2];
                    addCommand(new Sqrt(number.getLine(), (double)number.getValue(),(String)name.getValue()));
                    i+=3;
                } else if (nextIsType(Name)){
                    i++;
                    Token name = givenTokens[i];
                    addCommand(new Sqrt(name.getLine(), (String)name.getValue()));
                    i++;
                } else {
                    if (nextIsType(Name) || nextIsType(Number)){
                        i++;
                        if (nextIsType(To)){
                            i++;
                            if (!(nextIsType(Name)||nextIsType(Number))){
                                i++;
                                throwError("Expected name/number | Got: "+givenTokens[i].type());
                            }
                        } else {
                            i++;
                            throwError("Expected \"to\" | Got: "+givenTokens[i].type());
                        }
                    } else {
                        i++;
                        throwError("Expected name/number | Got: "+givenTokens[i].type());
                    }
                }
            }
            case Sin -> {
                if (nextIsType(Name,To,Name)){
                    i++;
                    Token name0 = givenTokens[i];
                    Token name1 = givenTokens[i+2];
                    addCommand(new Sin(name0.getLine(), (String)name0.getValue(),(String)name1.getValue()));
                    i+=3;
                } else if (nextIsType(Number,To,Name)){
                    i++;
                    Token number = givenTokens[i];
                    Token name = givenTokens[i+2];
                    addCommand(new Sin(number.getLine(), (double)number.getValue(),(String)name.getValue()));
                    i+=3;
                } else if (nextIsType(Name)){
                    i++;
                    Token name = givenTokens[i];
                    addCommand(new Sin(name.getLine(), (String)name.getValue()));
                    i++;
                } else {
                    if (nextIsType(Name) || nextIsType(Number)){
                        i++;
                        if (nextIsType(To)){
                            i++;
                            if (!(nextIsType(Name)||nextIsType(Number))){
                                i++;
                                throwError("Expected name/number | Got: "+givenTokens[i].type());
                            }
                        } else {
                            i++;
                            throwError("Expected \"to\" | Got: "+givenTokens[i].type());
                        }
                    } else {
                        i++;
                        throwError("Expected name/number | Got: "+givenTokens[i].type());
                    }
                }}
            case iSin -> {
                if (nextIsType(Name,To,Name)){
                    i++;
                    Token name0 = givenTokens[i];
                    Token name1 = givenTokens[i+2];
                    addCommand(new iSin(name0.getLine(), (String)name0.getValue(),(String)name1.getValue()));
                    i+=3;
                } else if (nextIsType(Number,To,Name)){
                    i++;
                    Token number = givenTokens[i];
                    Token name = givenTokens[i+2];
                    addCommand(new iSin(number.getLine(), (double)number.getValue(),(String)name.getValue()));
                    i+=3;
                } else if (nextIsType(Name)){
                    i++;
                    Token name = givenTokens[i];
                    addCommand(new iSin(name.getLine(), (String)name.getValue()));
                    i++;
                } else {
                    if (nextIsType(Name) || nextIsType(Number)){
                        i++;
                        if (nextIsType(To)){
                            i++;
                            if (!(nextIsType(Name)||nextIsType(Number))){
                                i++;
                                throwError("Expected name/number | Got: "+givenTokens[i].type());
                            }
                        } else {
                            i++;
                            throwError("Expected \"to\" | Got: "+givenTokens[i].type());
                        }
                    } else {
                        i++;
                        throwError("Expected name/number | Got: "+givenTokens[i].type());
                    }
                }
            }
            case Cos -> {
                if (nextIsType(Name,To,Name)){
                    i++;
                    Token name0 = givenTokens[i];
                    Token name1 = givenTokens[i+2];
                    addCommand(new Cos(name0.getLine(), (String)name0.getValue(),(String)name1.getValue()));
                    i+=3;
                } else if (nextIsType(Number,To,Name)){
                    i++;
                    Token number = givenTokens[i];
                    Token name = givenTokens[i+2];
                    addCommand(new Cos(number.getLine(), (double)number.getValue(),(String)name.getValue()));
                    i+=3;
                } else if (nextIsType(Name)){
                    i++;
                    Token name = givenTokens[i];
                    addCommand(new Cos(name.getLine(), (String)name.getValue()));
                    i++;
                } else {
                    if (nextIsType(Name) || nextIsType(Number)){
                        i++;
                        if (nextIsType(To)){
                            i++;
                            if (!(nextIsType(Name)||nextIsType(Number))){
                                i++;
                                throwError("Expected name/number | Got: "+givenTokens[i].type());
                            }
                        } else {
                            i++;
                            throwError("Expected \"to\" | Got: "+givenTokens[i].type());
                        }
                    } else {
                        i++;
                        throwError("Expected name/number | Got: "+givenTokens[i].type());
                    }
                }
            }
            case iCos -> {
                if (nextIsType(Name,To,Name)){
                    i++;
                    Token name0 = givenTokens[i];
                    Token name1 = givenTokens[i+2];
                    addCommand(new iCos(name0.getLine(), (String)name0.getValue(),(String)name1.getValue()));
                    i+=3;
                } else if (nextIsType(Number,To,Name)){
                    i++;
                    Token number = givenTokens[i];
                    Token name = givenTokens[i+2];
                    addCommand(new iCos(number.getLine(), (double)number.getValue(),(String)name.getValue()));
                    i+=3;
                } else if (nextIsType(Name)){
                    i++;
                    Token name = givenTokens[i];
                    addCommand(new iCos(name.getLine(), (String)name.getValue()));
                    i++;
                } else {
                    if (nextIsType(Name) || nextIsType(Number)){
                        i++;
                        if (nextIsType(To)){
                            i++;
                            if (!(nextIsType(Name)||nextIsType(Number))){
                                i++;
                                throwError("Expected name/number | Got: "+givenTokens[i].type());
                            }
                        } else {
                            i++;
                            throwError("Expected \"to\" | Got: "+givenTokens[i].type());
                        }
                    } else {
                        i++;
                        throwError("Expected name/number | Got: "+givenTokens[i].type());
                    }
                }
            }
            case Tan -> {
                if (nextIsType(Name,To,Name)){
                    i++;
                    Token name0 = givenTokens[i];
                    Token name1 = givenTokens[i+2];
                    addCommand(new Tan(name0.getLine(), (String)name0.getValue(),(String)name1.getValue()));
                    i+=3;
                } else if (nextIsType(Number,To,Name)){
                    i++;
                    Token number = givenTokens[i];
                    Token name = givenTokens[i+2];
                    addCommand(new Tan(number.getLine(), (double)number.getValue(),(String)name.getValue()));
                    i+=3;
                } else if (nextIsType(Name)){
                    i++;
                    Token name = givenTokens[i];
                    addCommand(new Tan(name.getLine(), (String)name.getValue()));
                    i++;
                } else {
                    if (nextIsType(Name) || nextIsType(Number)){
                        i++;
                        if (nextIsType(To)){
                            i++;
                            if (!(nextIsType(Name)||nextIsType(Number))){
                                i++;
                                throwError("Expected name/number | Got: "+givenTokens[i].type());
                            }
                        } else {
                            i++;
                            throwError("Expected \"to\" | Got: "+givenTokens[i].type());
                        }
                    } else {
                        i++;
                        throwError("Expected name/number | Got: "+givenTokens[i].type());
                    }
                }
            }
            case iTan -> {
                if (nextIsType(Name,To,Name)){
                    i++;
                    Token name0 = givenTokens[i];
                    Token name1 = givenTokens[i+2];
                    addCommand(new iTan(name0.getLine(), (String)name0.getValue(),(String)name1.getValue()));
                    i+=3;
                } else if (nextIsType(Number,To,Name)){
                    i++;
                    Token number = givenTokens[i];
                    Token name = givenTokens[i+2];
                    addCommand(new iTan(number.getLine(), (double)number.getValue(),(String)name.getValue()));
                    i+=3;
                } else if (nextIsType(Name)){
                    i++;
                    Token name = givenTokens[i];
                    addCommand(new iTan(name.getLine(), (String)name.getValue()));
                    i++;
                } else {
                    if (nextIsType(Name) || nextIsType(Number)){
                        i++;
                        if (nextIsType(To)){
                            i++;
                            if (!(nextIsType(Name)||nextIsType(Number))){
                                i++;
                                throwError("Expected name/number | Got: "+givenTokens[i].type());
                            }
                        } else {
                            i++;
                            throwError("Expected \"to\" | Got: "+givenTokens[i].type());
                        }
                    } else {
                        i++;
                        throwError("Expected name/number | Got: "+givenTokens[i].type());
                    }
                }
            }
            case Decrement -> {
                if (nextIsType(Name)){
                    i++;
                    Token name = givenTokens[i];
                    addCommand(new Decrement(name.getLine(), (String)name.getValue()));
                    i++;
                } else {
                    i++;
                    throwError("Expected name | Got: "+givenTokens[i].type());
                }
            }
            case Increment -> {
                if (nextIsType(Name)){
                    i++;
                    Token name = givenTokens[i];
                    addCommand(new Increment(name.getLine(), (String)name.getValue()));
                    i++;
                } else {
                    i++;
                    throwError("Expected name | Got: "+givenTokens[i].type());
                }
            }
            case toRad -> {
                if (nextIsType(Name)){
                    i++;
                    Token name = givenTokens[i];
                    addCommand(new ToRad(name.getLine(), (String)name.getValue()));
                    i++;
                } else {
                    i++;
                    throwError("Expected name | Got: "+givenTokens[i].type());
                }
            }
            case toDeg -> {
                if (nextIsType(Name)){
                    i++;
                    Token name = givenTokens[i];
                    addCommand(new ToDeg(name.getLine(), (String)name.getValue()));
                    i++;
                } else {
                    i++;
                    throwError("Expected name | Got: "+givenTokens[i].type());
                }
            }
            // 2-3 IO ops
            case Pow -> {
                if (nextIsType(Name,Name,To,Name)){
                    i++;
                    Token name0 = givenTokens[i];
                    Token name1 = givenTokens[i+1];
                    Token out = givenTokens[i+3];
                    addCommand(new Pow(name0.getLine(),
                            (String)name0.getValue(),
                            (String)name1.getValue(),
                            (String)out.getValue()));
                    i+=4;
                } else if (nextIsType(Name,Number,To,Name)){
                    i++;
                    Token name = givenTokens[i];
                    Token number = givenTokens[i+1];
                    Token out = givenTokens[i+3];
                    addCommand(new Pow(name.getLine(),
                            (String)name.getValue(),
                            (double)number.getValue(),
                            (String)out.getValue()));
                    i+=4;
                } else if (nextIsType(Number,Name,To,Name)){
                    i++;
                    Token number = givenTokens[i];
                    Token name = givenTokens[i+1];
                    Token out = givenTokens[i+3];
                    addCommand(new Pow(number.getLine(),
                            (double)number.getValue(),
                            (String)name.getValue(),
                            (String)out.getValue()));
                    i+=4;
                } else if (nextIsType(Number,Number,To,Name)){
                    i++;
                    Token number0 = givenTokens[i];
                    Token number1 = givenTokens[i+1];
                    Token out = givenTokens[i+3];
                    addCommand(new Pow(number0.getLine(),
                            (double)number0.getValue(),
                            (double)number1.getValue(),
                            (String)out.getValue()));
                    i+=4;
                } else if (nextIsType(Name,Number)){
                    i++;
                    Token name = givenTokens[i];
                    Token number = givenTokens[i+1];
                    addCommand(new Pow(name.getLine(),
                            (String)name.getValue(),
                            (double)number.getValue()));
                    i+=2;
                } else if (nextIsType(Number,Name)){
                    i++;
                    Token number = givenTokens[i];
                    Token name = givenTokens[i+1];
                    addCommand(new Pow(number.getLine(),
                            (double)number.getValue(),
                            (String)name.getValue()));
                    i+=2;
                } else if (nextIsType(Name,Name)){
                    i++;
                    Token name0 = givenTokens[i];
                    Token name1 = givenTokens[i+1];
                    addCommand(new Pow(name0.getLine(),
                            (String)name0.getValue(),
                            (String)name1.getValue()));
                    i+=2;
                } else {
                    if (nextIsType(Name)||nextIsType(Number)){
                        i++;
                        if (nextIsType(Name)||nextIsType(Number)){
                            i++;
                            if (nextIsType(To)){
                                i++;
                                if (!(nextIsType(Name)||nextIsType(Number))){
                                    i++;
                                    throwError("Expected name/number | Got: "+givenTokens[i].type());
                                }
                            } else {
                                i++;
                                throwError("Expected \"to\" | Got: "+givenTokens[i].type());
                            }
                        } else {
                            i++;
                            throwError("Expected name/number | Got: "+givenTokens[i].type());
                        }
                    } else {
                        i++;
                        throwError("Expected name/number | Got: "+givenTokens[i].type());
                    }
                }
            }
            case Div -> {
                if (nextIsType(Name,Name,To,Name)){
                    i++;
                    Token name0 = givenTokens[i];
                    Token name1 = givenTokens[i+1];
                    Token out = givenTokens[i+3];
                    addCommand(new Div(name0.getLine(),
                            (String)name0.getValue(),
                            (String)name1.getValue(),
                            (String)out.getValue()));
                    i+=4;
                } else if (nextIsType(Name,Number,To,Name)){
                    i++;
                    Token name = givenTokens[i];
                    Token number = givenTokens[i+1];
                    Token out = givenTokens[i+3];
                    addCommand(new Div(name.getLine(),
                            (String)name.getValue(),
                            (double)number.getValue(),
                            (String)out.getValue()));
                    i+=4;
                } else if (nextIsType(Number,Name,To,Name)){
                    i++;
                    Token number = givenTokens[i];
                    Token name = givenTokens[i+1];
                    Token out = givenTokens[i+3];
                    addCommand(new Div(number.getLine(),
                            (double)number.getValue(),
                            (String)name.getValue(),
                            (String)out.getValue()));
                    i+=4;
                } else if (nextIsType(Number,Number,To,Name)){
                    i++;
                    Token number0 = givenTokens[i];
                    Token number1 = givenTokens[i+1];
                    Token out = givenTokens[i+3];
                    addCommand(new Div(number0.getLine(),
                            (double)number0.getValue(),
                            (double)number1.getValue(),
                            (String)out.getValue()));
                    i+=4;
                } else if (nextIsType(Name,Number)){
                    i++;
                    Token name = givenTokens[i];
                    Token number = givenTokens[i+1];
                    addCommand(new Div(name.getLine(),
                            (String)name.getValue(),
                            (double)number.getValue()));
                    i+=2;
                } else if (nextIsType(Number,Name)){
                    i++;
                    Token number = givenTokens[i];
                    Token name = givenTokens[i+1];
                    addCommand(new Div(number.getLine(),
                            (double)number.getValue(),
                            (String)name.getValue()));
                    i+=2;
                } else if (nextIsType(Name,Name)){
                    i++;
                    Token name0 = givenTokens[i];
                    Token name1 = givenTokens[i+1];
                    addCommand(new Div(name0.getLine(),
                            (String)name0.getValue(),
                            (String)name1.getValue()));
                    i+=2;
                } else {
                    if (nextIsType(Name)||nextIsType(Number)){
                        i++;
                        if (nextIsType(Name)||nextIsType(Number)){
                            i++;
                            if (nextIsType(To)){
                                i++;
                                if (!(nextIsType(Name)||nextIsType(Number))){
                                    i++;
                                    throwError("Expected name/number | Got: "+givenTokens[i].type());
                                }
                            } else {
                                i++;
                                throwError("Expected \"to\" | Got: "+givenTokens[i].type());
                            }
                        } else {
                            i++;
                            throwError("Expected name/number | Got: "+givenTokens[i].type());
                        }
                    } else {
                        i++;
                        throwError("Expected name/number | Got: "+givenTokens[i].type());
                    }
                }
            }
            case Mux -> {
                if (nextIsType(Name,Name,To,Name)){
                    i++;
                    Token name0 = givenTokens[i];
                    Token name1 = givenTokens[i+1];
                    Token out = givenTokens[i+3];
                    addCommand(new Mux(name0.getLine(),
                            (String)name0.getValue(),
                            (String)name1.getValue(),
                            (String)out.getValue()));
                    i+=4;
                } else if (nextIsType(Name,Number,To,Name)){
                    i++;
                    Token name = givenTokens[i];
                    Token number = givenTokens[i+1];
                    Token out = givenTokens[i+3];
                    addCommand(new Mux(name.getLine(),
                            (String)name.getValue(),
                            (double)number.getValue(),
                            (String)out.getValue()));
                    i+=4;
                } else if (nextIsType(Number,Name,To,Name)){
                    i++;
                    Token number = givenTokens[i];
                    Token name = givenTokens[i+1];
                    Token out = givenTokens[i+3];
                    addCommand(new Mux(number.getLine(),
                            (double)number.getValue(),
                            (String)name.getValue(),
                            (String)out.getValue()));
                    i+=4;
                } else if (nextIsType(Number,Number,To,Name)){
                    i++;
                    Token number0 = givenTokens[i];
                    Token number1 = givenTokens[i+1];
                    Token out = givenTokens[i+3];
                    addCommand(new Mux(number0.getLine(),
                            (double)number0.getValue(),
                            (double)number1.getValue(),
                            (String)out.getValue()));
                    i+=4;
                } else if (nextIsType(Name,Number)){
                    i++;
                    Token name = givenTokens[i];
                    Token number = givenTokens[i+1];
                    addCommand(new Mux(name.getLine(),
                            (String)name.getValue(),
                            (double)number.getValue()));
                    i+=2;
                } else if (nextIsType(Number,Name)){
                    i++;
                    Token number = givenTokens[i];
                    Token name = givenTokens[i+1];
                    addCommand(new Mux(number.getLine(),
                            (double)number.getValue(),
                            (String)name.getValue()));
                    i+=2;
                } else if (nextIsType(Name,Name)){
                    i++;
                    Token name0 = givenTokens[i];
                    Token name1 = givenTokens[i+1];
                    addCommand(new Mux(name0.getLine(),
                            (String)name0.getValue(),
                            (String)name1.getValue()));
                    i+=2;
                } else {
                    if (nextIsType(Name)||nextIsType(Number)){
                        i++;
                        if (nextIsType(Name)||nextIsType(Number)){
                            i++;
                            if (nextIsType(To)){
                                i++;
                                if (!(nextIsType(Name)||nextIsType(Number))){
                                    i++;
                                    throwError("Expected name/number | Got: "+givenTokens[i].type());
                                }
                            } else {
                                i++;
                                throwError("Expected \"to\" | Got: "+givenTokens[i].type());
                            }
                        } else {
                            i++;
                            throwError("Expected name/number | Got: "+givenTokens[i].type());
                        }
                    } else {
                        i++;
                        throwError("Expected name/number | Got: "+givenTokens[i].type());
                    }
                }
            }
            case Sub -> {
                if (nextIsType(Name,Name,To,Name)){
                    i++;
                    Token name0 = givenTokens[i];
                    Token name1 = givenTokens[i+1];
                    Token out = givenTokens[i+3];
                    addCommand(new Sub(name0.getLine(),
                            (String)name0.getValue(),
                            (String)name1.getValue(),
                            (String)out.getValue()));
                    i+=4;
                } else if (nextIsType(Name,Number,To,Name)){
                    i++;
                    Token name = givenTokens[i];
                    Token number = givenTokens[i+1];
                    Token out = givenTokens[i+3];
                    addCommand(new Sub(name.getLine(),
                            (String)name.getValue(),
                            (double)number.getValue(),
                            (String)out.getValue()));
                    i+=4;
                } else if (nextIsType(Number,Name,To,Name)){
                    i++;
                    Token number = givenTokens[i];
                    Token name = givenTokens[i+1];
                    Token out = givenTokens[i+3];
                    addCommand(new Sub(number.getLine(),
                            (double)number.getValue(),
                            (String)name.getValue(),
                            (String)out.getValue()));
                    i+=4;
                } else if (nextIsType(Number,Number,To,Name)){
                    i++;
                    Token number0 = givenTokens[i];
                    Token number1 = givenTokens[i+1];
                    Token out = givenTokens[i+3];
                    addCommand(new Sub(number0.getLine(),
                            (double)number0.getValue(),
                            (double)number1.getValue(),
                            (String)out.getValue()));
                    i+=4;
                } else if (nextIsType(Name,Number)){
                    i++;
                    Token name = givenTokens[i];
                    Token number = givenTokens[i+1];
                    addCommand(new Sub(name.getLine(),
                            (String)name.getValue(),
                            (double)number.getValue()));
                    i+=2;
                } else if (nextIsType(Number,Name)){
                    i++;
                    Token number = givenTokens[i];
                    Token name = givenTokens[i+1];
                    addCommand(new Sub(number.getLine(),
                            (double)number.getValue(),
                            (String)name.getValue()));
                    i+=2;
                } else if (nextIsType(Name,Name)){
                    i++;
                    Token name0 = givenTokens[i];
                    Token name1 = givenTokens[i+1];
                    addCommand(new Sub(name0.getLine(),
                            (String)name0.getValue(),
                            (String)name1.getValue()));
                    i+=2;
                } else {
                    if (nextIsType(Name)||nextIsType(Number)){
                        i++;
                        if (nextIsType(Name)||nextIsType(Number)){
                            i++;
                            if (nextIsType(To)){
                                i++;
                                if (!(nextIsType(Name)||nextIsType(Number))){
                                    i++;
                                    throwError("Expected name/number | Got: "+givenTokens[i].type());
                                }
                            } else {
                                i++;
                                throwError("Expected \"to\" | Got: "+givenTokens[i].type());
                            }
                        } else {
                            i++;
                            throwError("Expected name/number | Got: "+givenTokens[i].type());
                        }
                    } else {
                        i++;
                        throwError("Expected name/number | Got: "+givenTokens[i].type());
                    }
                }
            }
            case Add -> {
                if (nextIsType(Name,Name,To,Name)){
                    i++;
                    Token name0 = givenTokens[i];
                    Token name1 = givenTokens[i+1];
                    Token out = givenTokens[i+3];
                    addCommand(new Add(name0.getLine(),
                            (String)name0.getValue(),
                            (String)name1.getValue(),
                            (String)out.getValue()));
                    i+=4;
                } else if (nextIsType(Name,Number,To,Name)){
                    i++;
                    Token name = givenTokens[i];
                    Token number = givenTokens[i+1];
                    Token out = givenTokens[i+3];
                    addCommand(new Add(name.getLine(),
                            (String)name.getValue(),
                            (double)number.getValue(),
                            (String)out.getValue()));
                    i+=4;
                } else if (nextIsType(Number,Name,To,Name)){
                    i++;
                    Token number = givenTokens[i];
                    Token name = givenTokens[i+1];
                    Token out = givenTokens[i+3];
                    addCommand(new Add(number.getLine(),
                            (double)number.getValue(),
                            (String)name.getValue(),
                            (String)out.getValue()));
                    i+=4;
                } else if (nextIsType(Number,Number,To,Name)){
                    i++;
                    Token number0 = givenTokens[i];
                    Token number1 = givenTokens[i+1];
                    Token out = givenTokens[i+3];
                    addCommand(new Add(number0.getLine(),
                            (double)number0.getValue(),
                            (double)number1.getValue(),
                            (String)out.getValue()));
                    i+=4;
                } else if (nextIsType(Name,Number)){
                    i++;
                    Token name = givenTokens[i];
                    Token number = givenTokens[i+1];
                    addCommand(new Add(name.getLine(),
                            (String)name.getValue(),
                            (double)number.getValue()));
                    i+=2;
                } else if (nextIsType(Number,Name)){
                    i++;
                    Token number = givenTokens[i];
                    Token name = givenTokens[i+1];
                    addCommand(new Add(number.getLine(),
                            (double)number.getValue(),
                            (String)name.getValue()));
                    i+=2;
                } else if (nextIsType(Name,Name)){
                    i++;
                    Token name0 = givenTokens[i];
                    Token name1 = givenTokens[i+1];
                    addCommand(new Add(name0.getLine(),
                            (String)name0.getValue(),
                            (String)name1.getValue()));
                    i+=2;
                } else {
                    if (nextIsType(Name)||nextIsType(Number)){
                        i++;
                        if (nextIsType(Name)||nextIsType(Number)){
                            i++;
                            if (nextIsType(To)){
                                i++;
                                if (!(nextIsType(Name)||nextIsType(Number))){
                                    i++;
                                    throwError("Expected name/number | Got: "+givenTokens[i].type());
                                }
                            } else {
                                i++;
                                throwError("Expected \"to\" | Got: "+givenTokens[i].type());
                            }
                        } else {
                            i++;
                            throwError("Expected name/number | Got: "+givenTokens[i].type());
                        }
                    } else {
                        i++;
                        throwError("Expected name/number | Got: "+givenTokens[i].type());
                    }
                }
            }
        }
    }
    private void processMoveOp(){
        Token current = givenTokens[i];
        switch (current.type()){
            case TurnTo -> {
                i++;
                // check next token for name/literal
                if(nextIsType(Name)) {
                    i++;
                    // construct command
                    addCommand(new TurnTo(getLine(),(String)givenTokens[i].getValue()));
                    i++;
                } else if (nextIsType(Number)){
                    i++;
                    // ensure it's a double
                    if (givenTokens[i].getValue() instanceof Double){
                        addCommand(new TurnTo(getLine(),(double)givenTokens[i].getValue()));
                        i++;
                    } else {
                        throwError("Expected a number! got: " + givenTokens[i].getValue());
                    }
                } else {
                    throwError("Expected name/literal! got: " + givenTokens[i].getValue());
                }
                break;
            }
            case GoTo -> {
                if (nextIsType(Name)){
                    i++;
                    Token name = givenTokens[i];
                    addCommand(new GoTo(name.getLine(),(String)name.getValue()));
                    i++;
                }
                else if (nextIsType(Lparenth,Name,Comma,Name,Rparenth)){
                    i++;
                    Token name0 = givenTokens[i];
                    Token name1 = givenTokens[i+2];
                    addCommand(new GoTo(name0.getLine(),(String)name0.getValue(),(String)name1.getValue()));
                    i+=3;
                }
                else if (nextIsType(Lparenth,Name,Comma,Number,Rparenth)){
                    i++;
                    Token name = givenTokens[i];
                    Token number = givenTokens[i+2];
                    addCommand(new GoTo(name.getLine(),(String)name.getValue(),(double)number.getValue()));
                    i+=3;
                }
                else if (nextIsType(Lparenth,Number,Comma,Name,Rparenth)){
                    i++;
                    Token number = givenTokens[i];
                    Token name = givenTokens[i+2];
                    addCommand(new GoTo(number.getLine(),(double)number.getValue(),(String)name.getValue()));
                    i+=3;
                }
                else if (nextIsType(Lparenth,Number,Comma,Number,Rparenth)){
                    i++;
                    Token number0 = givenTokens[i];
                    Token number1 = givenTokens[i+2];
                    addCommand(new GoTo(number0.getLine(),(double)number0.getValue(),(double)number1.getValue()));
                    i+=3;
                }
                else if (nextIsType(Lparenth,Name,Comma,Name,Comma,Name,Rparenth)){
                    i++;
                    Token name0 = givenTokens[i];
                    Token name1 = givenTokens[i+2];
                    Token name2 = givenTokens[i+4];
                    addCommand(new GoTo(name0.getLine(),(String)name0.getValue(),(String)name1.getValue(),(String)name2.getValue()));
                    i+=5;
                }
                else if (nextIsType(Lparenth,Name,Comma,Name,Comma,Number,Rparenth)){
                    i++;
                    Token name0 = givenTokens[i];
                    Token name1 = givenTokens[i+2];
                    Token number = givenTokens[i+4];
                    addCommand(new GoTo(name0.getLine(),(String)name0.getValue(),(String)name1.getValue(),(double)number.getValue()));
                    i+=5;
                }
                else if (nextIsType(Lparenth,Name,Comma,Number,Comma,Name,Rparenth)){
                    i++;
                    Token name0 = givenTokens[i];
                    Token number = givenTokens[i+2];
                    Token name1 = givenTokens[i+4];
                    addCommand(new GoTo(name0.getLine(),(String)name0.getValue(),(double)number.getValue(),(String)name1.getValue()));
                    i+=5;
                }
                else if (nextIsType(Lparenth,Number,Comma,Name,Comma,Name,Rparenth)){
                    i++;
                    Token number = givenTokens[i];
                    Token name0 = givenTokens[i+2];
                    Token name1 = givenTokens[i+4];
                    addCommand(new GoTo(number.getLine(),(double)number.getValue(),(String)name0.getValue(),(String)name1.getValue()));
                    i+=5;
                }
                else if (nextIsType(Lparenth,Name,Comma,Number,Comma,Number,Rparenth)){
                    i++;
                    Token name = givenTokens[i];
                    Token number0 = givenTokens[i+2];
                    Token number1 = givenTokens[i+4];
                    addCommand(new GoTo(name.getLine(),(String)name.getValue(),(double)number0.getValue(),(double)number1.getValue()));
                    i+=5;
                }
                else if (nextIsType(Lparenth,Number,Comma,Number,Comma,Name,Rparenth)){
                    i++;
                    Token number0 = givenTokens[i];
                    Token number1 = givenTokens[i+2];
                    Token name = givenTokens[i+4];
                    addCommand(new GoTo(number0.getLine(),(double)number0.getValue(),(double)number1.getValue(),(String)name.getValue()));
                    i+=5;
                }
                else if (nextIsType(Lparenth,Number,Comma,Name,Comma,Number,Rparenth)){
                    i++;
                    Token number0 = givenTokens[i];
                    Token name = givenTokens[i+2];
                    Token number1 = givenTokens[i+4];
                    addCommand(new GoTo(number0.getLine(),(double)number0.getValue(),(String)name.getValue(),(double)number1.getValue()));
                    i+=5;
                }
                else if (nextIsType(Lparenth,Number,Comma,Number,Comma,Number,Rparenth)){
                    i++;
                    Token number0 = givenTokens[i];
                    Token number1 = givenTokens[i+2];
                    Token number2 = givenTokens[i+4];
                    addCommand(new GoTo(number0.getLine(),(double)number0.getValue(),(double)number1.getValue(),(double)number2.getValue()));
                    i+=5;
                }
                else {
                    // step token by token to find issue
                    if (nextIsType(Name) || nextIsType(Lparenth)){
                        i++;
                        if (nextIsType(Name) ||  nextIsType(Number)){
                            i++;
                            if (nextIsType(Comma)){
                                i++;
                                if (nextIsType(Name) || nextIsType(Number)){
                                    i++;
                                    if (nextIsType(Comma) || nextIsType(Rparenth)){
                                        i++;
                                        if (nextIsType(Name) || nextIsType(Number)){
                                            i++;
                                            if (nextIsType(Rparenth)){
                                                i++;
                                                throwError("Expected \")\" | Got: "+givenTokens[i].type());
                                            }
                                        } else {
                                            i++;
                                            throwError("Expected name/number | Got: "+givenTokens[i].type());
                                        }
                                    } else {
                                        i++;
                                        throwError("Expected comma/\")\" | Got: "+givenTokens[i].type());
                                    }
                                } else {
                                    i++;
                                    throwError("Expected Name/Number | Got: "+givenTokens[i].type());
                                }
                            } else {
                                i++;
                                throwError("Expected comma | Got: "+givenTokens[i].type());
                            }
                        } else {
                            i++;
                            throwError("Expected name/number | Got: "+givenTokens[i].type());
                        }
                    } else {
                        i++;
                        throwError("Expected name/\"(\" | Got: "+givenTokens[i].type());
                    }
                }
                break;
            }
            case doBez -> {
                // TODO: compact this mess
                // this... this is bad

                // pos1 to pos2
                if (nextIsType(Name, To, Name)) {
                    i++;
                    Token name0 = givenTokens[i];
                    Token name1 = givenTokens[i+2];
                    addCommand(new DoBezier(name0.getLine(),(String)name0.getValue(),(String)name1.getValue()));
                    i+=3;
                }
                // (x,y) to (x,y)
                else if (nextIsType(Lparenth,Name,Comma,Name,Rparenth,To,Lparenth,Name,Comma,Name,Rparenth)){
                    Object[] pt1 = getNextTouple();
                    i+=6;
                    Object[] pt2 = getNextTouple();
                    addCommand(new DoBezier(givenTokens[i].getLine(),new Object[][]{pt1,pt2}));
                    i+=6;
                }
                else if (nextIsType(Lparenth,Name,Comma,Name,Rparenth,To,Lparenth,Name,Comma,Number,Rparenth)){
                    Object[] pt1 = getNextTouple();
                    i+=6;
                    Object[] pt2 = getNextTouple();
                    addCommand(new DoBezier(givenTokens[i].getLine(),new Object[][]{pt1,pt2}));
                    i+=6;
                }
                else if (nextIsType(Lparenth,Name,Comma,Name,Rparenth,To,Lparenth,Number,Comma,Name,Rparenth)){
                    Object[] pt1 = getNextTouple();
                    i+=6;
                    Object[] pt2 = getNextTouple();
                    addCommand(new DoBezier(givenTokens[i].getLine(),new Object[][]{pt1,pt2}));
                    i+=6;
                }
                else if (nextIsType(Lparenth,Name,Comma,Number,Rparenth,To,Lparenth,Name,Comma,Name,Rparenth)){
                    Object[] pt1 = getNextTouple();
                    i+=6;
                    Object[] pt2 = getNextTouple();
                    addCommand(new DoBezier(givenTokens[i].getLine(),new Object[][]{pt1,pt2}));
                    i+=6;
                }
                else if (nextIsType(Lparenth,Number,Comma,Name,Rparenth,To,Lparenth,Name,Comma,Name,Rparenth)){
                    Object[] pt1 = getNextTouple();
                    i+=6;
                    Object[] pt2 = getNextTouple();
                    addCommand(new DoBezier(givenTokens[i].getLine(),new Object[][]{pt1,pt2}));
                    i+=6;
                }
                else if (nextIsType(Lparenth,Name,Comma,Name,Rparenth,To,Lparenth,Number,Comma,Number,Rparenth)){
                    Object[] pt1 = getNextTouple();
                    i+=6;
                    Object[] pt2 = getNextTouple();
                    addCommand(new DoBezier(givenTokens[i].getLine(),new Object[][]{pt1,pt2}));
                    i+=6;
                }
                else if (nextIsType(Lparenth,Name,Comma,Number,Rparenth,To,Lparenth,Number,Comma,Name,Rparenth)){
                    Object[] pt1 = getNextTouple();
                    i+=6;
                    Object[] pt2 = getNextTouple();
                    addCommand(new DoBezier(givenTokens[i].getLine(),new Object[][]{pt1,pt2}));
                    i+=6;
                }
                else if (nextIsType(Lparenth,Number,Comma,Number,Rparenth,To,Lparenth,Name,Comma,Name,Rparenth)){
                    Object[] pt1 = getNextTouple();
                    i+=6;
                    Object[] pt2 = getNextTouple();
                    addCommand(new DoBezier(givenTokens[i].getLine(),new Object[][]{pt1,pt2}));
                    i+=6;
                }
                else if (nextIsType(Lparenth,Number,Comma,Name,Rparenth,To,Lparenth,Name,Comma,Number,Rparenth)){
                    Object[] pt1 = getNextTouple();
                    i+=6;
                    Object[] pt2 = getNextTouple();
                    addCommand(new DoBezier(givenTokens[i].getLine(),new Object[][]{pt1,pt2}));
                    i+=6;
                }
                else if (nextIsType(Lparenth,Name,Comma,Number,Rparenth,To,Lparenth,Number,Comma,Number,Rparenth)){
                    Object[] pt1 = getNextTouple();
                    i+=6;
                    Object[] pt2 = getNextTouple();
                    addCommand(new DoBezier(givenTokens[i].getLine(),new Object[][]{pt1,pt2}));
                    i+=6;
                }
                else if (nextIsType(Lparenth,Number,Comma,Number,Rparenth,To,Lparenth,Number,Comma,Name,Rparenth)){
                    Object[] pt1 = getNextTouple();
                    i+=6;
                    Object[] pt2 = getNextTouple();
                    addCommand(new DoBezier(givenTokens[i].getLine(),new Object[][]{pt1,pt2}));
                    i+=6;
                }
                else if (nextIsType(Lparenth,Number,Comma,Number,Rparenth,To,Lparenth,Name,Comma,Number,Rparenth)){
                    Object[] pt1 = getNextTouple();
                    i+=6;
                    Object[] pt2 = getNextTouple();
                    addCommand(new DoBezier(givenTokens[i].getLine(),new Object[][]{pt1,pt2}));
                    i+=6;
                }
                else if (nextIsType(Lparenth,Number,Comma,Name,Rparenth,To,Lparenth,Number,Comma,Number,Rparenth)){
                    Object[] pt1 = getNextTouple();
                    i+=6;
                    Object[] pt2 = getNextTouple();
                    addCommand(new DoBezier(givenTokens[i].getLine(),new Object[][]{pt1,pt2}));
                    i+=6;
                }
                else if (nextIsType(Lparenth,Number,Comma,Number,Rparenth,To,Lparenth,Number,Comma,Number,Rparenth)){
                    Object[] pt1 = getNextTouple();
                    i+=6;
                    Object[] pt2 = getNextTouple();
                    addCommand(new DoBezier(givenTokens[i].getLine(),new Object[][]{pt1,pt2}));
                    i+=6;
                }
                // (x,y) to (x,y,h)
                else if (nextIsType(Lparenth,Name,Comma,Name,Rparenth,To,Lparenth,Name,Comma,Name,Comma,Name,Rparenth)){
                    Object[] pt1 = getNextTouple();
                    i+=8;
                    Object[] pt2 = getNextTouple();
                    addCommand(new DoBezier(givenTokens[i].getLine(),new Object[][]{pt1,pt2}));
                    i+=6;
                }
                else if (nextIsType(Lparenth,Name,Comma,Name,Rparenth,To,Lparenth,Name,Comma,Name,Comma,Number,Rparenth)){
                    Object[] pt1 = getNextTouple();
                    i+=8;
                    Object[] pt2 = getNextTouple();
                    addCommand(new DoBezier(givenTokens[i].getLine(),new Object[][]{pt1,pt2}));
                    i+=6;
                }
                else if (nextIsType(Lparenth,Name,Comma,Name,Rparenth,To,Lparenth,Name,Comma,Number,Comma,Name,Rparenth)){
                    Object[] pt1 = getNextTouple();
                    i+=8;
                    Object[] pt2 = getNextTouple();
                    addCommand(new DoBezier(givenTokens[i].getLine(),new Object[][]{pt1,pt2}));
                    i+=6;
                }
                else if (nextIsType(Lparenth,Name,Comma,Name,Rparenth,To,Lparenth,Number,Comma,Name,Comma,Name,Rparenth)){
                    Object[] pt1 = getNextTouple();
                    i+=8;
                    Object[] pt2 = getNextTouple();
                    addCommand(new DoBezier(givenTokens[i].getLine(),new Object[][]{pt1,pt2}));
                    i+=6;
                }
                else if (nextIsType(Lparenth,Name,Comma,Number,Rparenth,To,Lparenth,Name,Comma,Name,Comma,Name,Rparenth)){
                    Object[] pt1 = getNextTouple();
                    i+=8;
                    Object[] pt2 = getNextTouple();
                    addCommand(new DoBezier(givenTokens[i].getLine(),new Object[][]{pt1,pt2}));
                    i+=6;
                }
                else if (nextIsType(Lparenth,Number,Comma,Name,Rparenth,To,Lparenth,Name,Comma,Name,Comma,Name,Rparenth)){
                    Object[] pt1 = getNextTouple();
                    i+=8;
                    Object[] pt2 = getNextTouple();
                    addCommand(new DoBezier(givenTokens[i].getLine(),new Object[][]{pt1,pt2}));
                    i+=6;
                }
                else if (nextIsType(Lparenth,Name,Comma,Name,Rparenth,To,Lparenth,Name,Comma,Number,Comma,Number,Rparenth)){
                    Object[] pt1 = getNextTouple();
                    i+=8;
                    Object[] pt2 = getNextTouple();
                    addCommand(new DoBezier(givenTokens[i].getLine(),new Object[][]{pt1,pt2}));
                    i+=6;
                }
                else if (nextIsType(Lparenth,Name,Comma,Name,Rparenth,To,Lparenth,Number,Comma,Number,Comma,Name,Rparenth)){
                    Object[] pt1 = getNextTouple();
                    i+=8;
                    Object[] pt2 = getNextTouple();
                    addCommand(new DoBezier(givenTokens[i].getLine(),new Object[][]{pt1,pt2}));
                    i+=6;
                }
                else if (nextIsType(Lparenth,Name,Comma,Number,Rparenth,To,Lparenth,Number,Comma,Name,Comma,Name,Rparenth)){
                    Object[] pt1 = getNextTouple();
                    i+=8;
                    Object[] pt2 = getNextTouple();
                    addCommand(new DoBezier(givenTokens[i].getLine(),new Object[][]{pt1,pt2}));
                    i+=6;
                }
                else if (nextIsType(Lparenth,Number,Comma,Number,Rparenth,To,Lparenth,Name,Comma,Name,Comma,Name,Rparenth)){
                    Object[] pt1 = getNextTouple();
                    i+=8;
                    Object[] pt2 = getNextTouple();
                    addCommand(new DoBezier(givenTokens[i].getLine(),new Object[][]{pt1,pt2}));
                    i+=6;
                }
                else if (nextIsType(Lparenth,Number,Comma,Number,Rparenth,To,Lparenth,Name,Comma,Name,Comma,Name,Rparenth)){
                    Object[] pt1 = getNextTouple();
                    i+=8;
                    Object[] pt2 = getNextTouple();
                    addCommand(new DoBezier(givenTokens[i].getLine(),new Object[][]{pt1,pt2}));
                    i+=6;
                }
                else if (nextIsType(Lparenth,Name,Comma,Name,Rparenth,To,Lparenth,Number,Comma,Number,Comma,Number,Rparenth)){
                    Object[] pt1 = getNextTouple();
                    i+=8;
                    Object[] pt2 = getNextTouple();
                    addCommand(new DoBezier(givenTokens[i].getLine(),new Object[][]{pt1,pt2}));
                    i+=6;
                }
                else if (nextIsType(Lparenth,Name,Comma,Number,Rparenth,To,Lparenth,Number,Comma,Number,Comma,Name,Rparenth)){
                    Object[] pt1 = getNextTouple();
                    i+=8;
                    Object[] pt2 = getNextTouple();
                    addCommand(new DoBezier(givenTokens[i].getLine(),new Object[][]{pt1,pt2}));
                    i+=6;
                }
                else if (nextIsType(Lparenth,Number,Comma,Number,Rparenth,To,Lparenth,Number,Comma,Name,Comma,Name,Rparenth)){
                    Object[] pt1 = getNextTouple();
                    i+=8;
                    Object[] pt2 = getNextTouple();
                    addCommand(new DoBezier(givenTokens[i].getLine(),new Object[][]{pt1,pt2}));
                    i+=6;
                }
                else if (nextIsType(Lparenth,Number,Comma,Number,Rparenth,To,Lparenth,Name,Comma,Name,Comma,Number,Rparenth)){
                    Object[] pt1 = getNextTouple();
                    i+=8;
                    Object[] pt2 = getNextTouple();
                    addCommand(new DoBezier(givenTokens[i].getLine(),new Object[][]{pt1,pt2}));
                    i+=6;
                }
                else if (nextIsType(Lparenth,Name,Comma,Number,Rparenth,To,Lparenth,Number,Comma,Number,Comma,Number,Rparenth)){
                    Object[] pt1 = getNextTouple();
                    i+=8;
                    Object[] pt2 = getNextTouple();
                    addCommand(new DoBezier(givenTokens[i].getLine(),new Object[][]{pt1,pt2}));
                    i+=6;
                }
                else if (nextIsType(Lparenth,Number,Comma,Number,Rparenth,To,Lparenth,Number,Comma,Number,Comma,Name,Rparenth)){
                    Object[] pt1 = getNextTouple();
                    i+=8;
                    Object[] pt2 = getNextTouple();
                    addCommand(new DoBezier(givenTokens[i].getLine(),new Object[][]{pt1,pt2}));
                    i+=6;
                }
                else if (nextIsType(Lparenth,Number,Comma,Number,Rparenth,To,Lparenth,Number,Comma,Name,Comma,Number,Rparenth)){
                    Object[] pt1 = getNextTouple();
                    i+=8;
                    Object[] pt2 = getNextTouple();
                    addCommand(new DoBezier(givenTokens[i].getLine(),new Object[][]{pt1,pt2}));
                    i+=6;
                }
                else if (nextIsType(Lparenth,Number,Comma,Number,Rparenth,To,Lparenth,Number,Comma,Name,Comma,Number,Rparenth)){
                    Object[] pt1 = getNextTouple();
                    i+=8;
                    Object[] pt2 = getNextTouple();
                    addCommand(new DoBezier(givenTokens[i].getLine(),new Object[][]{pt1,pt2}));
                    i+=6;
                }
                else if (nextIsType(Lparenth,Number,Comma,Number,Rparenth,To,Lparenth,Name,Comma,Number,Comma,Number,Rparenth)){
                    Object[] pt1 = getNextTouple();
                    i+=8;
                    Object[] pt2 = getNextTouple();
                    addCommand(new DoBezier(givenTokens[i].getLine(),new Object[][]{pt1,pt2}));
                    i+=6;
                }
                else if (nextIsType(Lparenth,Number,Comma,Name,Rparenth,To,Lparenth,Number,Comma,Number,Comma,Number,Rparenth)){
                    Object[] pt1 = getNextTouple();
                    i+=8;
                    Object[] pt2 = getNextTouple();
                    addCommand(new DoBezier(givenTokens[i].getLine(),new Object[][]{pt1,pt2}));
                    i+=6;
                }
                else if (nextIsType(Lparenth,Number,Comma,Number,Rparenth,To,Lparenth,Number,Comma,Number,Comma,Number,Rparenth)){
                    Object[] pt1 = getNextTouple();
                    i+=6;
                    Object[] pt2 = getNextTouple();
                    addCommand(new DoBezier(givenTokens[i].getLine(),new Object[][]{pt1,pt2}));
                    i+=8;
                }
                // (x,y,h) to (x,y)
                else if (nextIsType(Lparenth,Name,Comma,Name,Comma,Name,Rparenth,To,Lparenth,Name,Comma,Name,Rparenth)){
                    Object[] pt1 = getNextTouple();
                    i+=8;
                    Object[] pt2 = getNextTouple();
                    addCommand(new DoBezier(givenTokens[i].getLine(),new Object[][]{pt1,pt2}));
                    i+=6;
                }
                else if (nextIsType(Lparenth,Name,Comma,Name,Comma,Name,Rparenth,To,Lparenth,Name,Comma,Number,Rparenth)){
                    Object[] pt1 = getNextTouple();
                    i+=8;
                    Object[] pt2 = getNextTouple();
                    addCommand(new DoBezier(givenTokens[i].getLine(),new Object[][]{pt1,pt2}));
                    i+=6;
                }
                else if (nextIsType(Lparenth,Name,Comma,Name,Comma,Name,Rparenth,To,Lparenth,Number,Comma,Name,Rparenth)){
                    Object[] pt1 = getNextTouple();
                    i+=8;
                    Object[] pt2 = getNextTouple();
                    addCommand(new DoBezier(givenTokens[i].getLine(),new Object[][]{pt1,pt2}));
                    i+=6;
                }
                else if (nextIsType(Lparenth,Name,Comma,Name,Comma,Number,Rparenth,To,Lparenth,Name,Comma,Name,Rparenth)){
                    Object[] pt1 = getNextTouple();
                    i+=8;
                    Object[] pt2 = getNextTouple();
                    addCommand(new DoBezier(givenTokens[i].getLine(),new Object[][]{pt1,pt2}));
                    i+=6;
                }
                else if (nextIsType(Lparenth,Name,Comma,Number,Comma,Name,Rparenth,To,Lparenth,Name,Comma,Name,Rparenth)){
                    Object[] pt1 = getNextTouple();
                    i+=8;
                    Object[] pt2 = getNextTouple();
                    addCommand(new DoBezier(givenTokens[i].getLine(),new Object[][]{pt1,pt2}));
                    i+=6;
                }
                else if (nextIsType(Lparenth,Number,Comma,Name,Comma,Name,Rparenth,To,Lparenth,Name,Comma,Name,Rparenth)){
                    Object[] pt1 = getNextTouple();
                    i+=8;
                    Object[] pt2 = getNextTouple();
                    addCommand(new DoBezier(givenTokens[i].getLine(),new Object[][]{pt1,pt2}));
                    i+=6;
                }
                else if (nextIsType(Lparenth,Name,Comma,Name,Comma,Name,Rparenth,To,Lparenth,Number,Comma,Number,Rparenth)){
                    Object[] pt1 = getNextTouple();
                    i+=8;
                    Object[] pt2 = getNextTouple();
                    addCommand(new DoBezier(givenTokens[i].getLine(),new Object[][]{pt1,pt2}));
                    i+=6;
                }
                else if (nextIsType(Lparenth,Name,Comma,Name,Comma,Number,Rparenth,To,Lparenth,Number,Comma,Name,Rparenth)){
                    Object[] pt1 = getNextTouple();
                    i+=8;
                    Object[] pt2 = getNextTouple();
                    addCommand(new DoBezier(givenTokens[i].getLine(),new Object[][]{pt1,pt2}));
                    i+=6;
                }
                else if (nextIsType(Lparenth,Name,Comma,Number,Comma,Number,Rparenth,To,Lparenth,Name,Comma,Name,Rparenth)){
                    Object[] pt1 = getNextTouple();
                    i+=8;
                    Object[] pt2 = getNextTouple();
                    addCommand(new DoBezier(givenTokens[i].getLine(),new Object[][]{pt1,pt2}));
                    i+=6;
                }
                else if (nextIsType(Lparenth,Number,Comma,Number,Comma,Name,Rparenth,To,Lparenth,Name,Comma,Name,Rparenth)){
                    Object[] pt1 = getNextTouple();
                    i+=8;
                    Object[] pt2 = getNextTouple();
                    addCommand(new DoBezier(givenTokens[i].getLine(),new Object[][]{pt1,pt2}));
                    i+=6;
                }
                else if (nextIsType(Lparenth,Number,Comma,Name,Comma,Name,Rparenth,To,Lparenth,Name,Comma,Number,Rparenth)){
                    Object[] pt1 = getNextTouple();
                    i+=8;
                    Object[] pt2 = getNextTouple();
                    addCommand(new DoBezier(givenTokens[i].getLine(),new Object[][]{pt1,pt2}));
                    i+=6;
                }
                else if (nextIsType(Lparenth,Name,Comma,Name,Comma,Number,Rparenth,To,Lparenth,Number,Comma,Number,Rparenth)){
                    Object[] pt1 = getNextTouple();
                    i+=8;
                    Object[] pt2 = getNextTouple();
                    addCommand(new DoBezier(givenTokens[i].getLine(),new Object[][]{pt1,pt2}));
                    i+=6;
                }
                else if (nextIsType(Lparenth,Name,Comma,Number,Comma,Number,Rparenth,To,Lparenth,Number,Comma,Name,Rparenth)){
                    Object[] pt1 = getNextTouple();
                    i+=8;
                    Object[] pt2 = getNextTouple();
                    addCommand(new DoBezier(givenTokens[i].getLine(),new Object[][]{pt1,pt2}));
                    i+=6;
                }
                else if (nextIsType(Lparenth,Number,Comma,Number,Comma,Number,Rparenth,To,Lparenth,Name,Comma,Name,Rparenth)){
                    Object[] pt1 = getNextTouple();
                    i+=8;
                    Object[] pt2 = getNextTouple();
                    addCommand(new DoBezier(givenTokens[i].getLine(),new Object[][]{pt1,pt2}));
                    i+=6;
                }
                else if (nextIsType(Lparenth,Number,Comma,Number,Comma,Name,Rparenth,To,Lparenth,Name,Comma,Number,Rparenth)){
                    Object[] pt1 = getNextTouple();
                    i+=8;
                    Object[] pt2 = getNextTouple();
                    addCommand(new DoBezier(givenTokens[i].getLine(),new Object[][]{pt1,pt2}));
                    i+=6;
                }
                else if (nextIsType(Lparenth,Number,Comma,Name,Comma,Name,Rparenth,To,Lparenth,Number,Comma,Number,Rparenth)){
                    Object[] pt1 = getNextTouple();
                    i+=8;
                    Object[] pt2 = getNextTouple();
                    addCommand(new DoBezier(givenTokens[i].getLine(),new Object[][]{pt1,pt2}));
                    i+=6;
                }
                else if (nextIsType(Lparenth,Name,Comma,Number,Comma,Number,Rparenth,To,Lparenth,Number,Comma,Number,Rparenth)){
                    Object[] pt1 = getNextTouple();
                    i+=8;
                    Object[] pt2 = getNextTouple();
                    addCommand(new DoBezier(givenTokens[i].getLine(),new Object[][]{pt1,pt2}));
                    i+=6;
                }
                else if (nextIsType(Lparenth,Number,Comma,Number,Comma,Number,Rparenth,To,Lparenth,Number,Comma,Name,Rparenth)){
                    Object[] pt1 = getNextTouple();
                    i+=8;
                    Object[] pt2 = getNextTouple();
                    addCommand(new DoBezier(givenTokens[i].getLine(),new Object[][]{pt1,pt2}));
                    i+=6;
                }
                else if (nextIsType(Lparenth,Number,Comma,Number,Comma,Number,Rparenth,To,Lparenth,Name,Comma,Number,Rparenth)){
                    Object[] pt1 = getNextTouple();
                    i+=8;
                    Object[] pt2 = getNextTouple();
                    addCommand(new DoBezier(givenTokens[i].getLine(),new Object[][]{pt1,pt2}));
                    i+=6;
                }
                else if (nextIsType(Lparenth,Number,Comma,Number,Comma,Name,Rparenth,To,Lparenth,Number,Comma,Number,Rparenth)){
                    Object[] pt1 = getNextTouple();
                    i+=8;
                    Object[] pt2 = getNextTouple();
                    addCommand(new DoBezier(givenTokens[i].getLine(),new Object[][]{pt1,pt2}));
                    i+=6;
                }
                else if (nextIsType(Lparenth,Number,Comma,Name,Comma,Number,Rparenth,To,Lparenth,Number,Comma,Number,Rparenth)){
                    Object[] pt1 = getNextTouple();
                    i+=8;
                    Object[] pt2 = getNextTouple();
                    addCommand(new DoBezier(givenTokens[i].getLine(),new Object[][]{pt1,pt2}));
                    i+=6;
                }
                else if (nextIsType(Lparenth,Number,Comma,Number,Comma,Number,Rparenth,To,Lparenth,Number,Comma,Number,Rparenth)){
                    Object[] pt1 = getNextTouple();
                    i+=8;
                    Object[] pt2 = getNextTouple();
                    addCommand(new DoBezier(givenTokens[i].getLine(),new Object[][]{pt1,pt2}));
                    i+=6;
                }
                // (x,y,h) to (x,y,h)
                else if (nextIsType(Lparenth,Name,Comma,Name,Comma,Name,Rparenth,To,Lparenth,Name,Comma,Name,Comma,Name,Rparenth)){
                    Object[] pt1 = getNextTouple();
                    i+=8;
                    Object[] pt2 = getNextTouple();
                    addCommand(new DoBezier(givenTokens[i].getLine(),new Object[][]{pt1,pt2}));
                    i+=8;
                }
                else if (nextIsType(Lparenth,Name,Comma,Name,Comma,Name,Rparenth,To,Lparenth,Name,Comma,Name,Comma,Number,Rparenth)){
                    Object[] pt1 = getNextTouple();
                    i+=8;
                    Object[] pt2 = getNextTouple();
                    addCommand(new DoBezier(givenTokens[i].getLine(),new Object[][]{pt1,pt2}));
                    i+=8;
                }
                else if (nextIsType(Lparenth,Name,Comma,Name,Comma,Name,Rparenth,To,Lparenth,Name,Comma,Number,Comma,Name,Rparenth)){
                    Object[] pt1 = getNextTouple();
                    i+=8;
                    Object[] pt2 = getNextTouple();
                    addCommand(new DoBezier(givenTokens[i].getLine(),new Object[][]{pt1,pt2}));
                    i+=8;
                }
                else if (nextIsType(Lparenth,Name,Comma,Name,Comma,Name,Rparenth,To,Lparenth,Number,Comma,Name,Comma,Name,Rparenth)){
                    Object[] pt1 = getNextTouple();
                    i+=8;
                    Object[] pt2 = getNextTouple();
                    addCommand(new DoBezier(givenTokens[i].getLine(),new Object[][]{pt1,pt2}));
                    i+=8;
                }
                else if (nextIsType(Lparenth,Name,Comma,Name,Comma,Number,Rparenth,To,Lparenth,Name,Comma,Name,Comma,Name,Rparenth)){
                    Object[] pt1 = getNextTouple();
                    i+=8;
                    Object[] pt2 = getNextTouple();
                    addCommand(new DoBezier(givenTokens[i].getLine(),new Object[][]{pt1,pt2}));
                    i+=8;
                }
                else if (nextIsType(Lparenth,Name,Comma,Number,Comma,Name,Rparenth,To,Lparenth,Name,Comma,Name,Comma,Name,Rparenth)){
                    Object[] pt1 = getNextTouple();
                    i+=8;
                    Object[] pt2 = getNextTouple();
                    addCommand(new DoBezier(givenTokens[i].getLine(),new Object[][]{pt1,pt2}));
                    i+=8;
                }
                else if (nextIsType(Lparenth,Number,Comma,Name,Comma,Name,Rparenth,To,Lparenth,Name,Comma,Name,Comma,Name,Rparenth)){
                    Object[] pt1 = getNextTouple();
                    i+=8;
                    Object[] pt2 = getNextTouple();
                    addCommand(new DoBezier(givenTokens[i].getLine(),new Object[][]{pt1,pt2}));
                    i+=8;
                }
                else if (nextIsType(Lparenth,Name,Comma,Name,Comma,Name,Rparenth,To,Lparenth,Name,Comma,Number,Comma,Number,Rparenth)){
                    Object[] pt1 = getNextTouple();
                    i+=8;
                    Object[] pt2 = getNextTouple();
                    addCommand(new DoBezier(givenTokens[i].getLine(),new Object[][]{pt1,pt2}));
                    i+=8;
                }
                else if (nextIsType(Lparenth,Name,Comma,Name,Comma,Name,Rparenth,To,Lparenth,Number,Comma,Number,Comma,Name,Rparenth)){
                    Object[] pt1 = getNextTouple();
                    i+=8;
                    Object[] pt2 = getNextTouple();
                    addCommand(new DoBezier(givenTokens[i].getLine(),new Object[][]{pt1,pt2}));
                    i+=8;
                }
                else if (nextIsType(Lparenth,Name,Comma,Name,Comma,Number,Rparenth,To,Lparenth,Number,Comma,Name,Comma,Name,Rparenth)){
                    Object[] pt1 = getNextTouple();
                    i+=8;
                    Object[] pt2 = getNextTouple();
                    addCommand(new DoBezier(givenTokens[i].getLine(),new Object[][]{pt1,pt2}));
                    i+=8;
                }
                else if (nextIsType(Lparenth,Name,Comma,Number,Comma,Number,Rparenth,To,Lparenth,Name,Comma,Name,Comma,Name,Rparenth)){
                    Object[] pt1 = getNextTouple();
                    i+=8;
                    Object[] pt2 = getNextTouple();
                    addCommand(new DoBezier(givenTokens[i].getLine(),new Object[][]{pt1,pt2}));
                    i+=8;
                }
                else if (nextIsType(Lparenth,Number,Comma,Number,Comma,Name,Rparenth,To,Lparenth,Name,Comma,Name,Comma,Name,Rparenth)){
                    Object[] pt1 = getNextTouple();
                    i+=8;
                    Object[] pt2 = getNextTouple();
                    addCommand(new DoBezier(givenTokens[i].getLine(),new Object[][]{pt1,pt2}));
                    i+=8;
                }
                else if (nextIsType(Lparenth,Number,Comma,Name,Comma,Name,Rparenth,To,Lparenth,Name,Comma,Name,Comma,Number,Rparenth)){
                    Object[] pt1 = getNextTouple();
                    i+=8;
                    Object[] pt2 = getNextTouple();
                    addCommand(new DoBezier(givenTokens[i].getLine(),new Object[][]{pt1,pt2}));
                    i+=8;
                }
                else if (nextIsType(Lparenth,Name,Comma,Name,Comma,Name,Rparenth,To,Lparenth,Number,Comma,Number,Comma,Number,Rparenth)){
                    Object[] pt1 = getNextTouple();
                    i+=8;
                    Object[] pt2 = getNextTouple();
                    addCommand(new DoBezier(givenTokens[i].getLine(),new Object[][]{pt1,pt2}));
                    i+=8;
                }
                else if (nextIsType(Lparenth,Name,Comma,Name,Comma,Number,Rparenth,To,Lparenth,Number,Comma,Number,Comma,Name,Rparenth)){
                    Object[] pt1 = getNextTouple();
                    i+=8;
                    Object[] pt2 = getNextTouple();
                    addCommand(new DoBezier(givenTokens[i].getLine(),new Object[][]{pt1,pt2}));
                    i+=8;
                }
                else if (nextIsType(Lparenth,Name,Comma,Number,Comma,Number,Rparenth,To,Lparenth,Number,Comma,Name,Comma,Name,Rparenth)){
                    Object[] pt1 = getNextTouple();
                    i+=8;
                    Object[] pt2 = getNextTouple();
                    addCommand(new DoBezier(givenTokens[i].getLine(),new Object[][]{pt1,pt2}));
                    i+=8;
                }
                else if (nextIsType(Lparenth,Number,Comma,Number,Comma,Number,Rparenth,To,Lparenth,Name,Comma,Name,Comma,Name,Rparenth)){
                    Object[] pt1 = getNextTouple();
                    i+=8;
                    Object[] pt2 = getNextTouple();
                    addCommand(new DoBezier(givenTokens[i].getLine(),new Object[][]{pt1,pt2}));
                    i+=8;
                }
                else if (nextIsType(Lparenth,Number,Comma,Number,Comma,Name,Rparenth,To,Lparenth,Name,Comma,Name,Comma,Number,Rparenth)){
                    Object[] pt1 = getNextTouple();
                    i+=8;
                    Object[] pt2 = getNextTouple();
                    addCommand(new DoBezier(givenTokens[i].getLine(),new Object[][]{pt1,pt2}));
                    i+=8;
                }
                else if (nextIsType(Lparenth,Number,Comma,Name,Comma,Name,Rparenth,To,Lparenth,Name,Comma,Number,Comma,Number,Rparenth)){
                    Object[] pt1 = getNextTouple();
                    i+=8;
                    Object[] pt2 = getNextTouple();
                    addCommand(new DoBezier(givenTokens[i].getLine(),new Object[][]{pt1,pt2}));
                    i+=8;
                }
                else if (nextIsType(Lparenth,Name,Comma,Name,Comma,Number,Rparenth,To,Lparenth,Number,Comma,Number,Comma,Number,Rparenth)){
                    Object[] pt1 = getNextTouple();
                    i+=8;
                    Object[] pt2 = getNextTouple();
                    addCommand(new DoBezier(givenTokens[i].getLine(),new Object[][]{pt1,pt2}));
                    i+=8;
                }
                else if (nextIsType(Lparenth,Name,Comma,Number,Comma,Number,Rparenth,To,Lparenth,Number,Comma,Number,Comma,Name,Rparenth)){
                    Object[] pt1 = getNextTouple();
                    i+=8;
                    Object[] pt2 = getNextTouple();
                    addCommand(new DoBezier(givenTokens[i].getLine(),new Object[][]{pt1,pt2}));
                    i+=8;
                }
                else if (nextIsType(Lparenth,Number,Comma,Number,Comma,Number,Rparenth,To,Lparenth,Number,Comma,Name,Comma,Name,Rparenth)){
                    Object[] pt1 = getNextTouple();
                    i+=8;
                    Object[] pt2 = getNextTouple();
                    addCommand(new DoBezier(givenTokens[i].getLine(),new Object[][]{pt1,pt2}));
                    i+=8;
                }
                else if (nextIsType(Lparenth,Number,Comma,Number,Comma,Number,Rparenth,To,Lparenth,Name,Comma,Name,Comma,Number,Rparenth)){
                    Object[] pt1 = getNextTouple();
                    i+=8;
                    Object[] pt2 = getNextTouple();
                    addCommand(new DoBezier(givenTokens[i].getLine(),new Object[][]{pt1,pt2}));
                    i+=8;
                }
                else if (nextIsType(Lparenth,Number,Comma,Number,Comma,Name,Rparenth,To,Lparenth,Name,Comma,Number,Comma,Number,Rparenth)){
                    Object[] pt1 = getNextTouple();
                    i+=8;
                    Object[] pt2 = getNextTouple();
                    addCommand(new DoBezier(givenTokens[i].getLine(),new Object[][]{pt1,pt2}));
                    i+=8;
                }
                else if (nextIsType(Lparenth,Number,Comma,Name,Comma,Name,Rparenth,To,Lparenth,Number,Comma,Number,Comma,Number,Rparenth)){
                    Object[] pt1 = getNextTouple();
                    i+=8;
                    Object[] pt2 = getNextTouple();
                    addCommand(new DoBezier(givenTokens[i].getLine(),new Object[][]{pt1,pt2}));
                    i+=8;
                }
                else if (nextIsType(Lparenth,Name,Comma,Number,Comma,Number,Rparenth,To,Lparenth,Number,Comma,Number,Comma,Number,Rparenth)){
                    Object[] pt1 = getNextTouple();
                    i+=8;
                    Object[] pt2 = getNextTouple();
                    addCommand(new DoBezier(givenTokens[i].getLine(),new Object[][]{pt1,pt2}));
                    i+=8;
                }
                else if (nextIsType(Lparenth,Number,Comma,Number,Comma,Number,Rparenth,To,Lparenth,Number,Comma,Number,Comma,Name,Rparenth)){
                    Object[] pt1 = getNextTouple();
                    i+=8;
                    Object[] pt2 = getNextTouple();
                    addCommand(new DoBezier(givenTokens[i].getLine(),new Object[][]{pt1,pt2}));
                    i+=8;
                }
                else if (nextIsType(Lparenth,Number,Comma,Number,Comma,Number,Rparenth,To,Lparenth,Number,Comma,Name,Comma,Number,Rparenth)){
                    Object[] pt1 = getNextTouple();
                    i+=8;
                    Object[] pt2 = getNextTouple();
                    addCommand(new DoBezier(givenTokens[i].getLine(),new Object[][]{pt1,pt2}));
                    i+=8;
                }
                else if (nextIsType(Lparenth,Number,Comma,Number,Comma,Number,Rparenth,To,Lparenth,Name,Comma,Number,Comma,Number,Rparenth)){
                    Object[] pt1 = getNextTouple();
                    i+=8;
                    Object[] pt2 = getNextTouple();
                    addCommand(new DoBezier(givenTokens[i].getLine(),new Object[][]{pt1,pt2}));
                    i+=8;
                }
                else if (nextIsType(Lparenth,Number,Comma,Number,Comma,Name,Rparenth,To,Lparenth,Number,Comma,Number,Comma,Number,Rparenth)){
                    Object[] pt1 = getNextTouple();
                    i+=8;
                    Object[] pt2 = getNextTouple();
                    addCommand(new DoBezier(givenTokens[i].getLine(),new Object[][]{pt1,pt2}));
                    i+=8;
                }
                else if (nextIsType(Lparenth,Number,Comma,Name,Comma,Number,Rparenth,To,Lparenth,Number,Comma,Number,Comma,Number,Rparenth)){
                    Object[] pt1 = getNextTouple();
                    i+=8;
                    Object[] pt2 = getNextTouple();
                    addCommand(new DoBezier(givenTokens[i].getLine(),new Object[][]{pt1,pt2}));
                    i+=8;
                }
                else if (nextIsType(Lparenth,Number,Comma,Number,Comma,Number,Rparenth,To,Lparenth,Number,Comma,Number,Comma,Number,Rparenth)){
                    Object[] pt1 = getNextTouple();
                    i+=8;
                    Object[] pt2 = getNextTouple();
                    addCommand(new DoBezier(givenTokens[i].getLine(),new Object[][]{pt1,pt2}));
                    i+=8;
                }
                else if (nextIsType(Lparenth,Lparenth)){
                    ArrayList<Object[]> points = new ArrayList<>();
                    boolean go = true;
                    int line = givenTokens[i].getLine();
                    while (go){
                        if (nextIsType(Lparenth,Name,Comma,Name,Rparenth)){
                            i+=2;
                            line = givenTokens[i].getLine();
                            Token name0 = givenTokens[i];
                            Token name1 = givenTokens[i+2];
                            points.add(new Object[]{name0.getValue(), name1.getValue()});
                            i+=3;
                        }
                        else if (nextIsType(Lparenth,Number,Comma,Name,Rparenth)){
                            i+=2;
                            line = givenTokens[i].getLine();
                            Token number = givenTokens[i];
                            Token name = givenTokens[i+2];
                            points.add(new Object[]{number.getValue(), name.getValue()});
                            i+=3;
                        }
                        else if (nextIsType(Lparenth,Name,Comma,Number,Rparenth)){
                            i+=2;
                            line = givenTokens[i].getLine();
                            Token name = givenTokens[i];
                            Token number = givenTokens[i+2];
                            points.add(new Object[]{name.getValue(), number.getValue()});
                            i+=3;
                        }
                        else if (nextIsType(Lparenth,Number,Comma,Number,Rparenth)){
                            i+=2;
                            line = givenTokens[i].getLine();
                            Token number0 = givenTokens[i];
                            Token number1 = givenTokens[i+2];
                            points.add(new Object[]{number0.getValue(), number1.getValue()});
                            i+=3;
                        }
                        else if (nextIsType(Lparenth,Name,Comma,Name,Comma,Name,Rparenth)){
                            i+=2;
                            line = givenTokens[i].getLine();
                            Token name0 = givenTokens[i];
                            Token name1 = givenTokens[i+2];
                            Token name2 = givenTokens[i+4];
                            points.add(new Object[]{name0.getValue(), name1.getValue(), name2.getValue()});
                            i+=5;
                        }
                        else if (nextIsType(Lparenth,Name,Comma,Name,Comma,Number,Rparenth)){
                            i+=2;
                            line = givenTokens[i].getLine();
                            Token name0 = givenTokens[i];
                            Token name1 = givenTokens[i+2];
                            Token number = givenTokens[i+4];
                            points.add(new Object[]{name0.getValue(), name1.getValue(), number.getValue()});
                            i+=5;
                        }
                        else if (nextIsType(Lparenth,Name,Comma,Number,Comma,Name,Rparenth)){
                            i+=2;
                            line = givenTokens[i].getLine();
                            Token name0 = givenTokens[i];
                            Token number = givenTokens[i+2];
                            Token name1 = givenTokens[i+4];
                            points.add(new Object[]{name0.getValue(), number.getValue(), name1.getValue()});
                            i+=5;
                        }
                        else if (nextIsType(Lparenth,Number,Comma,Name,Comma,Name,Rparenth)){
                            i+=2;
                            line = givenTokens[i].getLine();
                            Token number = givenTokens[i];
                            Token name0 = givenTokens[i+2];
                            Token name1 = givenTokens[i+4];
                            points.add(new Object[]{number.getValue(), name0.getValue(), name1.getValue()});
                            i+=5;
                        }
                        else if (nextIsType(Lparenth,Name,Comma,Number,Comma,Number,Rparenth)){
                            i+=2;
                            line = givenTokens[i].getLine();
                            Token name = givenTokens[i];
                            Token number0 = givenTokens[i+2];
                            Token number1 = givenTokens[i+4];
                            points.add(new Object[]{name.getValue(), number0.getValue(), number1.getValue()});
                            i+=5;
                        }
                        else if (nextIsType(Lparenth,Number,Comma,Number,Comma,Name,Rparenth)){
                            i+=2;
                            line = givenTokens[i].getLine();
                            Token number0 = givenTokens[i];
                            Token number1 = givenTokens[i+2];
                            Token name = givenTokens[i+4];
                            points.add(new Object[]{number0.getValue(), number1.getValue(), name.getValue()});
                            i+=5;
                        }
                        else if (nextIsType(Lparenth,Number,Comma,Name,Comma,Number,Rparenth)){
                            i+=2;
                            line = givenTokens[i].getLine();
                            Token number0 = givenTokens[i];
                            Token name = givenTokens[i+2];
                            Token number1 = givenTokens[i+4];
                            points.add(new Object[]{number0.getValue(), name.getValue(), number1.getValue()});
                            i+=5;
                        }
                        else if (nextIsType(Lparenth,Number,Comma,Number,Comma,Number,Rparenth)){
                            i+=2;
                            line = givenTokens[i].getLine();
                            Token number0 = givenTokens[i];
                            Token number1 = givenTokens[i+2];
                            Token number2 = givenTokens[i+4];
                            points.add(new Object[]{number0.getValue(), number1.getValue(), number2.getValue()});
                            i+=5;
                        }
                        else {
                            if (nextIsType(Lparenth)){
                                if (nextIsType(Name)||nextIsType(Number)){
                                    if (nextIsType(Comma)){
                                        if (nextIsType(Name)||nextIsType(Number)){
                                            if (nextIsType(Comma)||nextIsType(Rparenth)){
                                                if (nextIsType(Name)||nextIsType(Number)){
                                                    if (!nextIsType(Rparenth)){
                                                        i++;
                                                        throwError("Expected \")\" | Got: "+givenTokens[i].type());
                                                    }
                                                } else {
                                                    i++;
                                                    throwError("Expected name/number | Got: "+givenTokens[i].type());
                                                }
                                            } else {
                                                i++;
                                                throwError("Expected \",\"/\")\" | Got: "+givenTokens[i].type());
                                            }
                                        } else {
                                            i++;
                                            throwError("Expected name/number | Got: "+givenTokens[i].type());
                                        }
                                    } else {
                                        i++;
                                        throwError("Expected \",\" | Got: "+givenTokens[i].type());
                                    }
                                } else {
                                    i++;
                                    throwError("Expected name/number | Got: "+givenTokens[i].type());
                                }
                            } else {
                                i++;
                                throwError("Expected \"(\" | Got: "+givenTokens[i].type());
                            }
                        }
                        if (!nextIsType(Comma) || nextIsType(Rparenth)){
                            go = false;
                        }
                        i++;
                    }
                    addCommand(new DoBezier(line, points.toArray(new Object[0][0])));
                }
                else if (nextIsType(Lparenth)){
                    ArrayList<Object[]> points = new ArrayList<>();
                    boolean go = true;
                    int line = givenTokens[i].getLine();
                    while (go){
                        if (nextIsType(Name,Comma)){
                            i++;
                            line = givenTokens[i].getLine();
                            points.add(new Object[]{givenTokens[i].getValue()});
                            i++;
                        }
                        else if (nextIsType(Name,Rparenth)){
                            i++;
                            line = givenTokens[i].getLine();
                            points.add(new Object[]{givenTokens[i].getValue()});
                            i++;
                            go = false;
                        }
                        else {
                            if (nextIsType(Name)){
                                if (!nextIsType(Comma)){
                                    i++;
                                    throwError("Expected Comma | Got: "+givenTokens[i].type());
                                }
                            } else {
                                i++;
                                throwError("Expected Name | Got: "+givenTokens[i].type());
                            }
                        }
                    }
                    addCommand(new DoBezier(line, points.toArray(new Object[0][0])));
                }
                else {
                    // cases:
                    // | N | to | N
                    // | ( | NN | , | NN | , | NN | ) | to | ( | NN | , | NN | , | NN | )
                    // | ( | NN | , | NN | , | NN | ) | to | ( | NN | , | NN | ) |
                    // | ( | NN | , | NN | ) | to | ( | NN | , | NN | , | NN | ) |
                    // | ( | NN | , | NN | ) | to | ( | NN | , | NN | ) |
                    if (nextIsType(Lparenth)||nextIsType(Name)){
                        i++;
                        if (nextIsType(Name)||nextIsType(Number)||nextIsType(To)){
                            i++;
                            if (nextIsType(Comma)||nextIsType(Number)||nextIsType(Name)){
                                i++;
                                if (nextIsType(Name)||nextIsType(Comma)){
                                    i++;
                                    if (nextIsType(Name)||nextIsType(Number)){
                                        i++;
                                        if (nextIsType(Comma)||nextIsType(Rparenth)){
                                            i++;
                                            if (nextIsType(Number)||nextIsType(Name)||nextIsType(To)){
                                                i++;
                                                if (nextIsType(Lparenth)||nextIsType(Rparenth)){
                                                    i++;
                                                    if (nextIsType(To)||nextIsType(Name)||nextIsType(Number)){
                                                        i++;
                                                        if (nextIsType(Lparenth)||nextIsType(Comma)){
                                                            i++;
                                                            if (nextIsType(Name)||nextIsType(Number)){
                                                                i++;
                                                                if (nextIsType(Comma)||nextIsType((Rparenth))){
                                                                    i++;
                                                                    if (nextIsType(Name)||nextIsType(Number)){
                                                                        i++;
                                                                        if (nextIsType(Comma)||nextIsType(Rparenth)){
                                                                            i++;
                                                                            if (nextIsType(Name)||nextIsType(Number)){
                                                                                i++;
                                                                                if (!nextIsType(Rparenth)){
                                                                                 i++;
                                                                                 throwError("Expected \")\" | Got: "+givenTokens[i].type());
                                                                                }
                                                                            } else {
                                                                                i++;
                                                                                throwError("Expected name/number | Got: "+givenTokens[i].type());
                                                                            }
                                                                        } else {
                                                                            i++;
                                                                            throwError("Expected \",\" / \")\" | Got: "+givenTokens[i].type());
                                                                        }
                                                                    } else {
                                                                        i++;
                                                                        throwError("Expected name/number | Got: "+givenTokens[i].type());
                                                                    }
                                                                } else {
                                                                    i++;
                                                                    throwError("Expected \",\" / \")\" | Got: "+givenTokens[i].type());
                                                                }
                                                            } else {
                                                                i++;
                                                                throwError("Expected name/number | Got: "+givenTokens[i].type());
                                                            }
                                                        } else {
                                                            i++;
                                                            throwError("Expected \",\" / \"(\" | Got: "+givenTokens[i].type());
                                                        }
                                                    } else {
                                                        i++;
                                                        throwError("Expected \"to\"/name/number | Got: "+givenTokens[i].type());
                                                    }
                                                } else {
                                                    i++;
                                                    throwError("Expected \"(\" / \")\" | Got: "+givenTokens[i].type());
                                                }
                                            } else {
                                                i++;
                                                throwError("Expected \"to\"/name/number | Got: "+givenTokens[i].type());
                                            }
                                        } else {
                                            i++;
                                            throwError("Expected \",\" / \")\" | Got: "+givenTokens[i].type());
                                        }
                                    } else {
                                        i++;
                                        throwError("Expected name/number | Got: "+givenTokens[i].type());
                                    }
                                } else {
                                    i++;
                                    throwError("Expected name/\",\" | Got: "+givenTokens[i].type());
                                }
                            } else {
                                i++;
                                throwError("Expected name/number/\",\" | Got: "+givenTokens[i].type());
                            }
                        } else {
                            i++;
                            throwError("Expected \"to\"/name/number | Got: "+givenTokens[i].type());
                        }
                    } else {
                        i++;
                        throwError("Expected name/\"(\" | Got: "+givenTokens[i].type());
                    }
                }
                break;
            }
            default -> {
                switch (current.type()){
                    case followSpline -> {
                        if (nextIsType(Name,Number)){
                            i++;
                            Token name = givenTokens[i];
                            Token number = givenTokens[i+1];
                            addCommand(new DoSpline(name.getLine(), (String)name.getValue(),(String)number.getValue()));
                            i+=2;
                        }
                        else if (nextIsType(Name,Name)){
                            i++;
                            Token name0 = givenTokens[i];
                            Token name1 = givenTokens[i+1];
                            addCommand(new DoSpline(name0.getLine(), (String)name0.getValue(),(String)name1.getValue()));
                            i+=2;
                        }
                        else if (nextIsType(Name)){
                            i++;
                            Token name = givenTokens[i];
                            addCommand(new DoSpline(name.getLine(), (String)name.getValue()));
                            i++;
                        } else {
                            if (nextIsType(Name)){
                                if (!(nextIsType(Number) || nextIsType(Name))){
                                    throwError("Expected name/number | Got: "+givenTokens[i].type());
                                }
                            } else {
                                throwError("Expected name | Got: "+givenTokens[i].type());
                            }
                        }
                    }
                    case followSplineSpline -> {
                        if (nextIsType(Name,Number)){
                            i++;
                            Token name = givenTokens[i];
                            Token number = givenTokens[i+1];
                            addCommand(new DoSplineSpline(name.getLine(), (String)name.getValue(),(String)number.getValue()));
                            i+=2;
                        }
                        else if (nextIsType(Name,Name)){
                            i++;
                            Token name0 = givenTokens[i];
                            Token name1 = givenTokens[i+1];
                            addCommand(new DoSplineSpline(name0.getLine(), (String)name0.getValue(),(String)name1.getValue()));
                            i+=2;
                        }
                        else if (nextIsType(Name)){
                            i++;
                            Token name = givenTokens[i];
                            addCommand(new DoSplineSpline(name.getLine(), (String)name.getValue()));
                            i++;
                        } else {
                            if (nextIsType(Name)){
                                if (!(nextIsType(Number) || nextIsType(Name))){
                                    throwError("Expected name/number | Got: "+givenTokens[i].type());
                                }
                            } else {
                                throwError("Expected name | Got: "+givenTokens[i].type());
                            }
                        }
                    }
                    case followSplineLinear -> {
                        if (nextIsType(Name,Number)){
                            i++;
                            Token name = givenTokens[i];
                            Token number = givenTokens[i+1];
                            addCommand(new DoSplineLinear(name.getLine(), (String)name.getValue(),(String)number.getValue()));
                            i+=2;
                        }
                        else if (nextIsType(Name,Name)){
                            i++;
                            Token name0 = givenTokens[i];
                            Token name1 = givenTokens[i+1];
                            addCommand(new DoSplineLinear(name0.getLine(), (String)name0.getValue(),(String)name1.getValue()));
                            i+=2;
                        }
                        else if (nextIsType(Name)){
                            i++;
                            Token name = givenTokens[i];
                            addCommand(new DoSplineLinear(name.getLine(), (String)name.getValue()));
                            i++;
                        } else {
                            if (nextIsType(Name)){
                                if (!(nextIsType(Number) || nextIsType(Name))){
                                    throwError("Expected name/number | Got: "+givenTokens[i].type());
                                }
                            } else {
                                throwError("Expected name | Got: "+givenTokens[i].type());
                            }
                        }
                    }
                }
            }
        }
    }
    private void processCommand(){
        if (nextIsType(Name)){
            i++;
            Token name = givenTokens[i];
            addCommand(new RunPath(name.getLine(),(String)name.getValue()));
            i++;
        } else {
            i++;
            throwError("Expected name | Get: "+givenTokens[i].type());
        }
    }
    private void processVariable(){
        Token current = givenTokens[i];
        switch (current.type()){
            case FieldCord -> {
                if (nextIsType(Name,Lparenth,Name,Comma,Name,Rparenth)){
                    i++;
                    Token name = givenTokens[i];
                    Token x = givenTokens[i+2];
                    Token y = givenTokens[i+4];
                    addCommand(new AddVar(name.getLine(), (String)name.getValue(), VariableTypes.FieldCord, new Object[]{x.getValue(),y.getValue()}));
                    i+=6;
                }
                else if (nextIsType(Name,Lparenth,Number,Comma,Name,Rparenth)){
                    i++;
                    Token name = givenTokens[i];
                    Token x = givenTokens[i+2];
                    Token y = givenTokens[i+4];
                    addCommand(new AddVar(name.getLine(), (String)name.getValue(), VariableTypes.FieldCord, new Object[]{x.getValue(),y.getValue()}));
                    i+=6;
                }
                else if (nextIsType(Name,Lparenth,Name,Comma,Number,Rparenth)){
                    i++;
                    Token name = givenTokens[i];
                    Token x = givenTokens[i+2];
                    Token y = givenTokens[i+4];
                    addCommand(new AddVar(name.getLine(), (String)name.getValue(), VariableTypes.FieldCord, new Object[]{x.getValue(),y.getValue()}));
                    i+=6;
                }
                else if (nextIsType(Name,Lparenth,Number,Comma,Number,Rparenth)){
                    i++;
                    Token name = givenTokens[i];
                    Token x = givenTokens[i+2];
                    Token y = givenTokens[i+4];
                    addCommand(new AddVar(name.getLine(), (String)name.getValue(), VariableTypes.FieldCord, new Object[]{x.getValue(),y.getValue()}));
                    i+=6;
                }
                else {
                    if (nextIsType(Name)){
                        i++;
                        if (nextIsType(Lparenth)){
                            i++;
                            if (nextIsType(Name)||nextIsType(Number)){
                                i++;
                                if (nextIsType(Comma)){
                                    i++;
                                    if (nextIsType(Name)||nextIsType(Number)){
                                        i++;
                                        if (!nextIsType(Rparenth)){
                                            i++;
                                            throwError("Expected \")\" | Got: "+givenTokens[i].type());
                                        }
                                    } else {
                                        i++;
                                        throwError("Expected name/number | Got: "+givenTokens[i].type());
                                    }
                                } else {
                                    i++;
                                    throwError("Expected \",\" | Got: "+givenTokens[i].type());
                                }
                            } else {
                                i++;
                                throwError("Expected name/number | Got: "+givenTokens[i].type());
                            }
                        } else {
                            i++;
                            throwError("Expected \"(\" | Got: "+givenTokens[i].type());
                        }
                    } else {
                        i++;
                        throwError("Expected name | Got: "+givenTokens[i].type());
                    }
                }
            }
            case FieldPos -> {
                String name = "";
                if (nextIsType(Name)){
                    i++;
                    name = (String)givenTokens[i].getValue();
                } else {
                    i++;
                    throwError("Expected name | Got: "+givenTokens[i].type());
                }

                if (nextIsType(Lparenth,Name,Comma,Name,Comma,Name,Rparenth)){
                    i++;
                    Token in1 = givenTokens[i+1];
                    Token in2 = givenTokens[i+3];
                    Token in3 = givenTokens[i+5];
                    addCommand(new AddVar(in1.getLine(), name, VariableTypes.FieldPos, new Object[]{in1.getValue(),in2.getValue(),in3.getValue()}));
                    i+=7;
                }
                else if (nextIsType(Lparenth,Name,Comma,Name,Comma,Number,Rparenth)){
                    i++;
                    Token in1 = givenTokens[i+1];
                    Token in2 = givenTokens[i+3];
                    Token in3 = givenTokens[i+5];
                    addCommand(new AddVar(in1.getLine(), name, VariableTypes.FieldPos, new Object[]{in1.getValue(),in2.getValue(),in3.getValue()}));
                    i+=7;
                }
                else if (nextIsType(Lparenth,Name,Comma,Number,Comma,Name,Rparenth)){
                    i++;
                    Token in1 = givenTokens[i+1];
                    Token in2 = givenTokens[i+3];
                    Token in3 = givenTokens[i+5];
                    addCommand(new AddVar(in1.getLine(), name, VariableTypes.FieldPos, new Object[]{in1.getValue(),in2.getValue(),in3.getValue()}));
                    i+=7;
                }
                else if (nextIsType(Lparenth,Number,Comma,Name,Comma,Name,Rparenth)){
                    i++;
                    Token in1 = givenTokens[i+1];
                    Token in2 = givenTokens[i+3];
                    Token in3 = givenTokens[i+5];
                    addCommand(new AddVar(in1.getLine(), name, VariableTypes.FieldPos, new Object[]{in1.getValue(),in2.getValue(),in3.getValue()}));
                    i+=7;
                }
                else if (nextIsType(Lparenth,Name,Comma,Number,Comma,Number,Rparenth)){
                    i++;
                    Token in1 = givenTokens[i+1];
                    Token in2 = givenTokens[i+3];
                    Token in3 = givenTokens[i+5];
                    addCommand(new AddVar(in1.getLine(), name, VariableTypes.FieldPos, new Object[]{in1.getValue(),in2.getValue(),in3.getValue()}));
                    i+=7;
                }
                else if (nextIsType(Lparenth,Number,Comma,Number,Comma,Name,Rparenth)){
                    i++;
                    Token in1 = givenTokens[i+1];
                    Token in2 = givenTokens[i+3];
                    Token in3 = givenTokens[i+5];
                    addCommand(new AddVar(in1.getLine(), name, VariableTypes.FieldPos, new Object[]{in1.getValue(),in2.getValue(),in3.getValue()}));
                    i+=7;
                }
                else if (nextIsType(Lparenth,Number,Comma,Name,Comma,Number,Rparenth)){
                    i++;
                    Token in1 = givenTokens[i+1];
                    Token in2 = givenTokens[i+3];
                    Token in3 = givenTokens[i+5];
                    addCommand(new AddVar(in1.getLine(), name, VariableTypes.FieldPos, new Object[]{in1.getValue(),in2.getValue(),in3.getValue()}));
                    i+=7;
                }
                else if (nextIsType(Lparenth,Number,Comma,Number,Comma,Number,Rparenth)){
                    i++;
                    Token in1 = givenTokens[i+1];
                    Token in2 = givenTokens[i+3];
                    Token in3 = givenTokens[i+5];
                    addCommand(new AddVar(in1.getLine(), name, VariableTypes.FieldPos, new Object[]{in1.getValue(),in2.getValue(),in3.getValue()}));
                    i+=7;
                }
                else {
                    if (nextIsType(Lparenth)){
                        i++;
                        if (nextIsType(Name)||nextIsType(Number)){
                            i++;
                            if (nextIsType(Comma)){
                                i++;
                                if (nextIsType(Name)||nextIsType(Number)){
                                    i++;
                                    if (nextIsType(Comma)){
                                        i++;
                                        if (nextIsType(Name)||nextIsType(Number)){
                                            i++;
                                            if (!nextIsType(Rparenth)){
                                                i++;
                                                throwError("Expected \")\" | Got: "+givenTokens[i].type());
                                            }
                                        } else {
                                            i++;
                                            throwError("Expected nane/number | Got: "+givenTokens[i].type());
                                        }
                                    } else {
                                        i++;
                                        throwError("Expected \",\" | Got: "+givenTokens[i].type());
                                    }
                                } else {
                                    i++;
                                    throwError("Expected name/number | Got: "+givenTokens[i].type());
                                }
                            } else {
                                i++;
                                throwError("Expected \",\" | Got: "+givenTokens[i].type());
                            }
                        } else {
                            i++;
                            throwError("Expected name/number | Got: "+givenTokens[i].type());
                        }
                    } else {
                        i++;
                        throwError("Expected \"(\" | Got: "+givenTokens[i].type());
                    }
                }
            }
            case NumberDef -> {
                if (nextIsType(Name,Number)){
                    i++;
                    Token name = givenTokens[i];
                    Token number = givenTokens[i+1];
                    addCommand(new AddVar(name.getLine(), (String)name.getValue(), VariableTypes.Number, number.getValue()));
                    i+=2;
                } else if (nextIsType(Name)){
                    i++;
                    Token name = givenTokens[i];
                    addCommand(new AddVar(name.getLine(), (String)name.getValue(), VariableTypes.Number, 0));
                    i++;
                } else {
                    if (nextIsType(Name)){
                        i+=2;
                        throwError("Expected number | Got: "+givenTokens[i].type());
                    } else {
                        i++;
                        throwError("Expected name | Got: "+givenTokens[i].type());
                    }
                }
            }
            case StringDef -> {
                if (nextIsType(Name,String)){
                    i++;
                    Token name = givenTokens[i];
                    Token value = givenTokens[i+1];
                    addCommand(new AddVar(getLine(), (String)name.getValue(), VariableTypes.String, value.getValue()));
                    i+=2;
                } else if (nextIsType(Name)){
                    i++;
                    Token name = givenTokens[i];
                    addCommand(new AddVar(getLine(), (String)name.getValue(), VariableTypes.String, ""));
                    i++;
                } else {
                    if (nextIsType(Name)){
                        i++;
                        throwError("Expected string | Got: "+givenTokens[i].type());
                    } else {
                        i++;
                        throwError("Expected name | Got: "+givenTokens[i].type());
                    }
                }
            }
            case BoolDef -> {
                if (nextIsType(Name,Lparenth)){
                    i++;
                    Token name = givenTokens[i];
                    Condition con = processCondition();
                    addCommand(new AddVar(getLine(), (String)name.getValue(), VariableTypes.Boolean, con));
                    i++;
                }
                else if (nextIsType(Name,Boolean)){
                    i++;
                    Token name = givenTokens[i];
                    Token value = givenTokens[i+1];
                    addCommand(new AddVar(getLine(), (String)name.getValue(), VariableTypes.Boolean, value.getValue()));
                    i+=2;
                }
                else if (nextIsType(Name)){
                    i++;
                    Token name = givenTokens[i];
                    addCommand(new AddVar(getLine(), (String)name.getValue(), VariableTypes.Boolean, true));
                    i++;
                }
                else {
                    if (nextIsType(Name)){
                        i++;
                        throwError("Expected true/false/condition | Got: "+givenTokens[i].type());
                    } else {
                        i++;
                        throwError("Expected name | Got: "+givenTokens[i].type());
                    }
                }
            }
            case List -> {
                if (nextIsType(Name,Lbracket)){
                    i++;
                    Token name = givenTokens[i];
                    i++;
                    ArrayList<Object> listVals = new ArrayList<>();
                    while (!nextIsType(Rbracket)){
                        i++;
                        switch (givenTokens[i].type()){
                            case Rbracket -> {} // skips the default condition and lets the loop end naturally
                            case Boolean -> listVals.add(new DynBoolean((boolean)givenTokens[i].getValue()));
                            case String -> listVals.add(new DynString((String)givenTokens[i].getValue()));
                            case Number -> listVals.add(new DynNumber((double)givenTokens[i].getValue()));
                            case Name -> listVals.add(givenTokens[i].getValue());
                            default -> throwError("Expected boolean/string/number/name | Got: "+givenTokens[i].type());
                        }
                        if (nextIsType(Comma)){
                            i++;
                        } else if (nextIsType(Rbracket)) {
                            i++;
                            break;
                        } else {
                            i++;
                            throwError("Expected \"]\"/\",\" | Got: "+givenTokens[i].type());
                        }
                    }
                    i++;
                    addCommand(new AddVar(getLine(),(String)name.getValue(),VariableTypes.List,listVals));
                } else if (nextIsType(Name)){
                    i++;
                    Token name = givenTokens[i];
                    addCommand(new AddVar(getLine(),(String)name.getValue(),VariableTypes.List,new ArrayList<Object>()));
                    i++;
                } else {
                    if (nextIsType(Name)){
                        i++;
                        throwError("Expected \"[\" | Got: "+givenTokens[i].type());
                    } else {
                        i++;
                        throwError("Expected name | Got: "+givenTokens[i].type());
                    }
                }
            }
            case Json -> {
                if (nextIsType(Name,LCbracket)){
                    i++;
                    String name = (String)givenTokens[i].getValue();
                    Map<Object,Object> jsonKV = new HashMap<>();
                    i+=2; // step into the json tokens
                    while (!nextIsType(RCbracket)){
                        Object[] thisKV = processJsonChunk();
                        if (givenTokens[i].type() == Comma) {
                            jsonKV.put(thisKV[0],thisKV[1]);
                            i++;
                        } else if (givenTokens[i].type() == RCbracket){
                            jsonKV.put(thisKV[0],thisKV[1]);
                            i--; // backstep for proper positioning
                            break;
                        } else {
                            throwError("Expected \"}\"/\",\" | Got: "+givenTokens[i].type());
                        }
                    }
                    i+=2;// step to next command
                    addCommand(new AddVar(getLine(),name,VariableTypes.Json,jsonKV));
                } else {
                    if (nextIsType(Name)){
                        i+=2;
                        throwError("Expected \"{\" | Got: "+givenTokens[i].type());
                    } else {
                        i++;
                        throwError("Expected name | Got: "+givenTokens[i].type());
                    }
                }
            }
        }
    }
    Object[] processJsonChunk(){
        // given that i counter is placed as such:
        // ..., K:V
        //      ^
        // end with i counter right on top of the 'V'
        Token Key = givenTokens[i];
        TokenTypes keyType = Key.type();
        Token Value = givenTokens[i+2];
        TokenTypes valueType = Value.type();
        // verify validity of tokens
        if ((keyType == Name || keyType == Number || keyType == Boolean || keyType == String)
            && (valueType == Name || valueType == Number || valueType == Boolean || valueType == String)){
            Object k = null;
            switch (keyType){
                case Boolean -> k = new DynBoolean((boolean)Key.getValue());
                case Number -> k = new DynNumber((double)Key.getValue());
                case String -> k = new DynString((String)Key.getValue());
                case Name -> k = Key.getValue();
            }
            Object v = null;
            switch (valueType){
                case Boolean -> v = new DynBoolean((boolean)Value.getValue());
                case Number -> v = new DynNumber((double)Value.getValue());
                case String -> v = new DynString((String)Value.getValue());
                case Name -> v = Value.getValue();
            }
            i+=3;
            return new Object[]{k,v};
        } else {
            if (keyType == Name || keyType == Number || keyType == Boolean || keyType == String){
                throwError("Expected name/number/boolean/string | Got: "+keyType);
            } else {
                i+=2;
                throwError("Expected name/number/boolean/string | Got: "+valueType);
            }
            return null;
        }
    }
    private void processRandomOp(){
        Token current = givenTokens[i];
        switch (current.type()){
            case RngBoolean -> {
                if (nextIsType(Name)){
                    i++;
                    Token name = givenTokens[i];
                    addCommand(new RngBoolean(name.getLine(), (String)name.getValue()));
                    i++;
                } else {
                    i++;
                    throwError("Expected name | Got: "+givenTokens[i]);
                }
            }
            case RngInteger -> {
                if (nextIsType(Name,Name,To,Name)){
                    i++;
                    Token name0 = givenTokens[i];
                    Token name1 = givenTokens[i+1];
                    Token name2 = givenTokens[i+3];
                    addCommand(new RngInteger(name0.getLine(), (String)name0.getValue(),(String)name1.getValue(), (String)name2.getValue()));
                    i+=4;
                } else if (nextIsType(Number,Name,To,Name)){
                    i++;
                    Token number = givenTokens[i];
                    Token name0 = givenTokens[i+1];
                    Token name1 = givenTokens[i+3];
                    addCommand(new RngInteger(number.getLine(), (double)number.getValue(),(String)name0.getValue(), (String)name1.getValue()));
                    i+=4;
                } else if (nextIsType(Name,Number,To,Name)){
                    i++;
                    Token name0 = givenTokens[i];
                    Token number = givenTokens[i+1];
                    Token name1 = givenTokens[i+3];
                    addCommand(new RngInteger(name0.getLine(), (String)name0.getValue(),(double)number.getValue(), (String)name1.getValue()));
                    i+=4;
                } else if (nextIsType(Number,Number,To,Name)){
                    i++;
                    Token number0 = givenTokens[i];
                    Token number1 = givenTokens[i+1];
                    Token name = givenTokens[i+3];
                    addCommand(new RngInteger(number0.getLine(), (double)number0.getValue(),(double)number1.getValue(), (String)name.getValue()));
                    i+=4;
                } else {
                    if (nextIsType(Name)||nextIsType(Number)){
                        i++;
                        if (nextIsType(Name)||nextIsType(Number)){
                            i++;
                            if (nextIsType(To)){
                                i++;
                                if (!nextIsType(Name)){
                                    i++;
                                    throwError("Expected name | Got: "+givenTokens[i].type());
                                }
                            } else {
                                i++;
                                throwError("Expected \"to\" | Got: "+givenTokens[i].type());
                            }
                        } else {
                            i++;
                            throwError("Expected name/number | Got: "+givenTokens[i].type());
                        }
                    } else {
                        i++;
                        throwError("Expected name/number | Got: "+givenTokens[i].type());
                    }
                }
            }
            case RngDouble -> {
                if (nextIsType(Name,Name,To,Name)){
                    i++;
                    Token name0 = givenTokens[i];
                    Token name1 = givenTokens[i+1];
                    Token name2 = givenTokens[i+3];
                    addCommand(new RngDouble(name0.getLine(), (String)name0.getValue(),(String)name1.getValue(), (String)name2.getValue()));
                    i+=4;
                } else if (nextIsType(Number,Name,To,Name)){
                    i++;
                    Token number = givenTokens[i];
                    Token name0 = givenTokens[i+1];
                    Token name1 = givenTokens[i+3];
                    addCommand(new RngDouble(number.getLine(), (double)number.getValue(),(String)name0.getValue(), (String)name1.getValue()));
                    i+=4;
                } else if (nextIsType(Name,Number,To,Name)){
                    i++;
                    Token name0 = givenTokens[i];
                    Token number = givenTokens[i+1];
                    Token name1 = givenTokens[i+3];
                    addCommand(new RngDouble(name0.getLine(), (String)name0.getValue(),(double)number.getValue(), (String)name1.getValue()));
                    i+=4;
                } else if (nextIsType(Number,Number,To,Name)){
                    i++;
                    Token number0 = givenTokens[i];
                    Token number1 = givenTokens[i+1];
                    Token name = givenTokens[i+3];
                    addCommand(new RngDouble(number0.getLine(), (double)number0.getValue(),(double)number1.getValue(), (String)name.getValue()));
                    i+=4;
                } else {
                    if (nextIsType(Name)||nextIsType(Number)){
                        i++;
                        if (nextIsType(Name)||nextIsType(Number)){
                            i++;
                            if (nextIsType(To)){
                                i++;
                                if (!nextIsType(Name)){
                                    i++;
                                    throwError("Expected name | Got: "+givenTokens[i].type());
                                }
                            } else {
                                i++;
                                throwError("Expected \"to\" | Got: "+givenTokens[i].type());
                            }
                        } else {
                            i++;
                            throwError("Expected name/number | Got: "+givenTokens[i].type());
                        }
                    } else {
                        i++;
                        throwError("Expected name/number | Got: "+givenTokens[i].type());
                    }
                }
            }
            case RngFloat -> {
                if (nextIsType(Name)){
                    i++;
                    Token name = givenTokens[i];
                    addCommand(new RngFloat(name.getLine(), (String)name.getValue()));
                    i++;
                } else {
                    i++;
                    throwError("Expected name | Got: "+givenTokens[i].type());
                }
            }
        }
    }
    private void processPathStart(){
        // TODO: make this when we start integrating followers on FTC hardware.
        i+=2;
    }
    private void processTelemetry(){
        Token current = givenTokens[i];
        switch (current.type()){
            case AddData -> {
                if (nextIsType(Name)){
                    i++;
                    Token name = givenTokens[i];
                    addCommand(new AddData(name.getLine(), (String)name.getValue()));
                    i++;
                } else if (nextIsType(Number)||nextIsType(Boolean)||nextIsType(String)) {
                    i++;
                    Token value = givenTokens[i];
                    addCommand(new AddData(value.getLine(), value.getValue()));
                    i++;
                } else {
                    if (!(nextIsType(Name)||nextIsType(Number)||nextIsType(Boolean)||nextIsType(String))){
                        i++;
                        throwError("Expected name/number/boolean/stirng | Got: "+givenTokens[i].type());
                    }
                }
            }
            case Update -> {
                i++;
                addCommand(new Update(current.getLine()));
            }
            case Clear -> {
                i++;
                addCommand(new Clear(current.getLine()));
            }
        }
    }

    // The previous version of this function was made by Claude Sonnet 5 with all the context of DYN possible,
    // and it still messed up this task.
    // So that's why this was written by hand.
    private Condition processCondition(){
        // this functions expects the next token to be a '('
        // this functions ends with the pointer on the closing parenthesis
        Condition out = null;
        // we construct conditions with max 2 parts each, with a 1pt min.
        if (nextIsType(Lparenth)){
            i+=2;
            switch (givenTokens[i].type()){
                case Lparenth -> {
                    i--; // we do a lot of back-stepping in this process
                    Condition pt1 = processCondition();
                    i++;
                    switch (givenTokens[i].type()){
                        case Or -> {
                            i++;
                            switch (givenTokens[i].type()){
                                case Lparenth -> {
                                    i--;
                                    Condition pt2 = processCondition();
                                    out = new Condition(getLine(), Condition.ConditionType.Or,pt1,pt2);
                                    if (nextIsType(Rparenth)){
                                        i++;
                                    } else {
                                        i++;
                                        throwError("Expected \")\" | Got: "+givenTokens[i].type());
                                    }
                                }
                                case Boolean -> {
                                    boolean pt2 = (boolean)givenTokens[i].getValue();
                                    out = new Condition(getLine(), Condition.ConditionType.Or,pt1,pt2);
                                    if (nextIsType(Rparenth)){
                                        i++;
                                    } else {
                                        i++;
                                        throwError("Expected \")\" | Got: "+givenTokens[i].type());
                                    }
                                }
                                case Name -> {
                                    String pt2 = (String)givenTokens[i].getValue();
                                    out = new Condition(getLine(), Condition.ConditionType.Or,pt1,pt2);
                                    if (nextIsType(Rparenth)){
                                        i++;
                                    } else {
                                        i++;
                                        throwError("Expected \")\" | Got: "+givenTokens[i].type());
                                    }
                                }
                            }
                        }
                        case And -> {
                            i++;
                            switch (givenTokens[i].type()){
                                case Lparenth -> {
                                    i--;
                                    Condition pt2 = processCondition();
                                    out = new Condition(getLine(), Condition.ConditionType.Or,pt1,pt2);
                                    if (nextIsType(Rparenth)){
                                        i++;
                                    } else {
                                        i++;
                                        throwError("Expected \")\" | Got: "+givenTokens[i].type());
                                    }
                                }
                                case Boolean -> {
                                    boolean pt2 = (boolean)givenTokens[i].getValue();
                                    out = new Condition(getLine(), Condition.ConditionType.And,pt1,pt2);
                                    if (nextIsType(Rparenth)){
                                        i++;
                                    } else {
                                        i++;
                                        throwError("Expected \")\" | Got: "+givenTokens[i].type());
                                    }
                                }
                                case Name -> {
                                    String pt2 = (String)givenTokens[i].getValue();
                                    out = new Condition(getLine(), Condition.ConditionType.And,pt1,pt2);
                                    if (nextIsType(Rparenth)){
                                        i++;
                                    } else {
                                        i++;
                                        throwError("Expected \")\" | Got: "+givenTokens[i].type());
                                    }
                                }
                            }
                        }
                        case Equals -> {
                            i++;
                            switch (givenTokens[i].type()){
                                case Lparenth -> {
                                    i--;
                                    Condition pt2 = processCondition();
                                    out = new Condition(getLine(), Condition.ConditionType.Equals,pt1,pt2);
                                    if (nextIsType(Rparenth)){
                                        i++;
                                    } else {
                                        i++;
                                        throwError("Expected \")\" | Got: "+givenTokens[i].type());
                                    }
                                }
                                case Boolean -> {
                                    boolean pt2 = (boolean)givenTokens[i].getValue();
                                    out = new Condition(getLine(), Condition.ConditionType.Equals,pt1,pt2);
                                    if (nextIsType(Rparenth)){
                                        i++;
                                    } else {
                                        i++;
                                        throwError("Expected \")\" | Got: "+givenTokens[i].type());
                                    }
                                }
                                case Name -> {
                                    String pt2 = (String)givenTokens[i].getValue();
                                    out = new Condition(getLine(), Condition.ConditionType.Equals,pt1,pt2);
                                    if (nextIsType(Rparenth)){
                                        i++;
                                    } else {
                                        i++;
                                        throwError("Expected \")\" | Got: "+givenTokens[i].type());
                                    }
                                }
                            }
                        }
                        default -> throwError("Expected \"Or/\"And\" | Got: "+givenTokens[i].type());
                    }
                }
                case Boolean -> {
                    boolean pt1 = (boolean)givenTokens[i].getValue();
                    i++;
                    switch (givenTokens[i].type()){
                        case Or -> {
                            i++;
                            switch (givenTokens[i].type()){
                                case Lparenth -> {
                                    i--;
                                    Condition pt2 = processCondition();
                                    out = new Condition(getLine(), Condition.ConditionType.Or,pt1,pt2);
                                    if (nextIsType(Rparenth)){
                                        i++;
                                    } else {
                                        i++;
                                        throwError("Expected \")\" | Got: "+givenTokens[i].type());
                                    }
                                }
                                case Boolean -> {
                                    boolean pt2 = (boolean)givenTokens[i].getValue();
                                    out = new Condition(getLine(), Condition.ConditionType.Or,pt1,pt2);
                                    if (nextIsType(Rparenth)){
                                        i++;
                                    } else {
                                        i++;
                                        throwError("Expected \")\" | Got: "+givenTokens[i].type());
                                    }
                                }
                                case Name -> {
                                    String pt2 = (String)givenTokens[i].getValue();
                                    out = new Condition(getLine(), Condition.ConditionType.Or,pt1,pt2);
                                    if (nextIsType(Rparenth)){
                                        i++;
                                    } else {
                                        i++;
                                        throwError("Expected \")\" | Got: "+givenTokens[i].type());
                                    }
                                }
                            }
                        }
                        case And -> {
                            i++;
                            switch (givenTokens[i].type()){
                                case Lparenth -> {
                                    i--;
                                    Condition pt2 = processCondition();
                                    out = new Condition(getLine(), Condition.ConditionType.And,pt1,pt2);
                                    if (nextIsType(Rparenth)){
                                        i++;
                                    } else {
                                        i++;
                                        throwError("Expected \")\" | Got: "+givenTokens[i].type());
                                    }
                                }
                                case Boolean -> {
                                    boolean pt2 = (boolean)givenTokens[i].getValue();
                                    out = new Condition(getLine(), Condition.ConditionType.And,pt1,pt2);
                                    if (nextIsType(Rparenth)){
                                        i++;
                                    } else {
                                        i++;
                                        throwError("Expected \")\" | Got: "+givenTokens[i].type());
                                    }
                                }
                                case Name -> {
                                    String pt2 = (String)givenTokens[i].getValue();
                                    out = new Condition(getLine(), Condition.ConditionType.And,pt1,pt2);
                                    if (nextIsType(Rparenth)){
                                        i++;
                                    } else {
                                        i++;
                                        throwError("Expected \")\" | Got: "+givenTokens[i].type());
                                    }
                                }
                            }
                        }
                        case Equals -> {
                            i++;
                            switch (givenTokens[i].type()){
                                case Lparenth -> {
                                    i--;
                                    Condition pt2 = processCondition();
                                    out = new Condition(getLine(), Condition.ConditionType.Equals,pt1,pt2);
                                    if (nextIsType(Rparenth)){
                                        i++;
                                    } else {
                                        i++;
                                        throwError("Expected \")\" | Got: "+givenTokens[i].type());
                                    }
                                }
                                case Boolean -> {
                                    boolean pt2 = (boolean)givenTokens[i].getValue();
                                    out = new Condition(getLine(), Condition.ConditionType.Equals,pt1,pt2);
                                    if (nextIsType(Rparenth)){
                                        i++;
                                    } else {
                                        i++;
                                        throwError("Expected \")\" | Got: "+givenTokens[i].type());
                                    }
                                }
                                case Name -> {
                                    String pt2 = (String)givenTokens[i].getValue();
                                    out = new Condition(getLine(), Condition.ConditionType.Equals,pt1,pt2);
                                    if (nextIsType(Rparenth)){
                                        i++;
                                    } else {
                                        i++;
                                        throwError("Expected \")\" | Got: "+givenTokens[i].type());
                                    }
                                }
                            }
                        }
                        default -> throwError("Expected \"Or/\"And\" | Got: "+givenTokens[i].type());
                    }
                }
                case Number -> {
                    double pt1 = (double)givenTokens[i].getValue();
                    i++;
                    switch (givenTokens[i].type()){
                        case isMoreEqual -> {
                            i++;
                            switch (givenTokens[i].type()){
                                case Number -> {
                                    double pt2 = (double)givenTokens[i].getValue();
                                    out = new Condition(getLine(), Condition.ConditionType.MoreThanEq,pt1,pt2);
                                    if (nextIsType(Rparenth)){
                                        i++;
                                    } else {
                                        i++;
                                        throwError("Expected \")\" | Got: "+givenTokens[i].type());
                                    }
                                }
                                case Name -> {
                                    String pt2 = (String)givenTokens[i].getValue();
                                    out = new Condition(getLine(), Condition.ConditionType.MoreThanEq,pt1,pt2);
                                    if (nextIsType(Rparenth)){
                                        i++;
                                    } else {
                                        i++;
                                        throwError("Expected \")\" | Got: "+givenTokens[i].type());
                                    }
                                }
                            }
                        }
                        case isLessEqual -> {
                            i++;
                            switch (givenTokens[i].type()){
                                case Number -> {
                                    double pt2 = (double)givenTokens[i].getValue();
                                    out = new Condition(getLine(), Condition.ConditionType.LessThanEq,pt1,pt2);
                                    if (nextIsType(Rparenth)){
                                        i++;
                                    } else {
                                        i++;
                                        throwError("Expected \")\" | Got: "+givenTokens[i].type());
                                    }
                                }
                                case Name -> {
                                    String pt2 = (String)givenTokens[i].getValue();
                                    out = new Condition(getLine(), Condition.ConditionType.LessThanEq,pt1,pt2);
                                    if (nextIsType(Rparenth)){
                                        i++;
                                    } else {
                                        i++;
                                        throwError("Expected \")\" | Got: "+givenTokens[i].type());
                                    }
                                }
                            }
                        }
                        case NotEqual -> {
                            i++;
                            switch (givenTokens[i].type()){
                                case Number -> {
                                    double pt2 = (double)givenTokens[i].getValue();
                                    out = new Condition(getLine(), Condition.ConditionType.NotEquals,pt1,pt2);
                                    if (nextIsType(Rparenth)){
                                        i++;
                                    } else {
                                        i++;
                                        throwError("Expected \")\" | Got: "+givenTokens[i].type());
                                    }
                                }
                                case Name -> {
                                    String pt2 = (String)givenTokens[i].getValue();
                                    out = new Condition(getLine(), Condition.ConditionType.NotEquals,pt1,pt2);
                                    if (nextIsType(Rparenth)){
                                        i++;
                                    } else {
                                        i++;
                                        throwError("Expected \")\" | Got: "+givenTokens[i].type());
                                    }
                                }
                            }
                        }
                        case Equals -> {
                            i++;
                            switch (givenTokens[i].type()){
                                case Number -> {
                                    double pt2 = (double)givenTokens[i].getValue();
                                    out = new Condition(getLine(), Condition.ConditionType.Equals,pt1,pt2);
                                    if (nextIsType(Rparenth)){
                                        i++;
                                    } else {
                                        i++;
                                        throwError("Expected \")\" | Got: "+givenTokens[i].type());
                                    }
                                }
                                case Name -> {
                                    String pt2 = (String)givenTokens[i].getValue();
                                    out = new Condition(getLine(), Condition.ConditionType.Equals,pt1,pt2);
                                    if (nextIsType(Rparenth)){
                                        i++;
                                    } else {
                                        i++;
                                        throwError("Expected \")\" | Got: "+givenTokens[i].type());
                                    }
                                }
                            }
                        }
                        case isMore -> {
                            i++;
                            switch (givenTokens[i].type()){
                                case Number -> {
                                    double pt2 = (double)givenTokens[i].getValue();
                                    out = new Condition(getLine(), Condition.ConditionType.MoreThan,pt1,pt2);
                                    if (nextIsType(Rparenth)){
                                        i++;
                                    } else {
                                        i++;
                                        throwError("Expected \")\" | Got: "+givenTokens[i].type());
                                    }
                                }
                                case Name -> {
                                    String pt2 = (String)givenTokens[i].getValue();
                                    out = new Condition(getLine(), Condition.ConditionType.MoreThan,pt1,pt2);
                                    if (nextIsType(Rparenth)){
                                        i++;
                                    } else {
                                        i++;
                                        throwError("Expected \")\" | Got: "+givenTokens[i].type());
                                    }
                                }
                            }
                        }
                        case isLess -> {
                            i++;
                            switch (givenTokens[i].type()){
                                case Number -> {
                                    double pt2 = (double)givenTokens[i].getValue();
                                    out = new Condition(getLine(), Condition.ConditionType.LessThan,pt1,pt2);
                                    if (nextIsType(Rparenth)){
                                        i++;
                                    } else {
                                        i++;
                                        throwError("Expected \")\" | Got: "+givenTokens[i].type());
                                    }
                                }
                                case Name -> {
                                    String pt2 = (String)givenTokens[i].getValue();
                                    out = new Condition(getLine(), Condition.ConditionType.LessThan,pt1,pt2);
                                    if (nextIsType(Rparenth)){
                                        i++;
                                    } else {
                                        i++;
                                        throwError("Expected \")\" | Got: "+givenTokens[i].type());
                                    }
                                }
                            }
                        }
                        default -> throwError("Expected ==/!=/>=/<=/</> | Got: "+givenTokens[i].type());
                    }
                }
                case Name -> {
                    String pt1 = (String)givenTokens[i].getValue();
                    i++;
                    switch (givenTokens[i].type()){
                        case Rparenth -> out = new Condition(getLine(),pt1);
                        case isMoreEqual -> {
                            i++;
                            switch (givenTokens[i].type()){
                                case Number -> {
                                    double pt2 = (double)givenTokens[i].getValue();
                                    out = new Condition(getLine(), Condition.ConditionType.MoreThanEq,pt1,pt2);
                                    if (nextIsType(Rparenth)){
                                        i++;
                                    } else {
                                        i++;
                                        throwError("Expected \")\" | Got: "+givenTokens[i].type());
                                    }
                                }
                                case Name -> {
                                    String pt2 = (String)givenTokens[i].getValue();
                                    out = new Condition(getLine(), Condition.ConditionType.MoreThanEq,pt1,pt2);
                                    if (nextIsType(Rparenth)){
                                        i++;
                                    } else {
                                        i++;
                                        throwError("Expected \")\" | Got: "+givenTokens[i].type());
                                    }
                                }
                            }
                        }
                        case isLessEqual -> {
                            i++;
                            switch (givenTokens[i].type()){
                                case Number -> {
                                    double pt2 = (double)givenTokens[i].getValue();
                                    out = new Condition(getLine(), Condition.ConditionType.LessThanEq,pt1,pt2);
                                    if (nextIsType(Rparenth)){
                                        i++;
                                    } else {
                                        i++;
                                        throwError("Expected \")\" | Got: "+givenTokens[i].type());
                                    }
                                }
                                case Name -> {
                                    String pt2 = (String)givenTokens[i].getValue();
                                    out = new Condition(getLine(), Condition.ConditionType.LessThanEq,pt1,pt2);
                                    if (nextIsType(Rparenth)){
                                        i++;
                                    } else {
                                        i++;
                                        throwError("Expected \")\" | Got: "+givenTokens[i].type());
                                    }
                                }
                            }
                        }
                        case NotEqual -> {
                            i++;
                            switch (givenTokens[i].type()){
                                case Number -> {
                                    double pt2 = (double)givenTokens[i].getValue();
                                    out = new Condition(getLine(), Condition.ConditionType.NotEquals,pt1,pt2);
                                    if (nextIsType(Rparenth)){
                                        i++;
                                    } else {
                                        i++;
                                        throwError("Expected \")\" | Got: "+givenTokens[i].type());
                                    }
                                }
                                case Name -> {
                                    String pt2 = (String)givenTokens[i].getValue();
                                    out = new Condition(getLine(), Condition.ConditionType.NotEquals,pt1,pt2);
                                    if (nextIsType(Rparenth)){
                                        i++;
                                    } else {
                                        i++;
                                        throwError("Expected \")\" | Got: "+givenTokens[i].type());
                                    }
                                }
                                case Boolean -> {
                                    boolean pt2 = (boolean)givenTokens[i].getValue();
                                    out = new Condition(getLine(), Condition.ConditionType.NotEquals,pt1,pt2);
                                    if (nextIsType(Rparenth)){
                                        i++;
                                    } else {
                                        i++;
                                        throwError("Expected \")\" | Got: "+givenTokens[i].type());
                                    }
                                }
                                case Rparenth -> {
                                    i--;
                                    Condition pt2 = processCondition();
                                    out = new Condition(getLine(), Condition.ConditionType.NotEquals,pt1,pt2);
                                    if (nextIsType(Rparenth)){
                                        i++;
                                    } else {
                                        i++;
                                        throwError("Expected \")\" | Got: "+givenTokens[i].type());
                                    }
                                }
                                case String -> {
                                    String pt2 = (String)givenTokens[i].getValue();
                                    out = new Condition(getLine(), Condition.ConditionType.NotEquals,pt1,new DynString(pt2));
                                    if (nextIsType(Rparenth)){
                                        i++;
                                    } else {
                                        i++;
                                        throwError("Expected \")\" | Got: "+givenTokens[i].type());
                                    }
                                }
                            }
                        }
                        case Equals -> {
                            i++;
                            switch (givenTokens[i].type()){
                                case Number -> {
                                    double pt2 = (double)givenTokens[i].getValue();
                                    out = new Condition(getLine(), Condition.ConditionType.Equals,pt1,pt2);
                                    if (nextIsType(Rparenth)){
                                        i++;
                                    } else {
                                        i++;
                                        throwError("Expected \")\" | Got: "+givenTokens[i].type());
                                    }
                                }
                                case Name -> {
                                    String pt2 = (String)givenTokens[i].getValue();
                                    out = new Condition(getLine(), Condition.ConditionType.Equals,pt1,pt2);
                                    if (nextIsType(Rparenth)){
                                        i++;
                                    } else {
                                        i++;
                                        throwError("Expected \")\" | Got: "+givenTokens[i].type());
                                    }
                                }
                                case Boolean -> {
                                    boolean pt2 = (boolean)givenTokens[i].getValue();
                                    out = new Condition(getLine(), Condition.ConditionType.Equals,pt1,pt2);
                                    if (nextIsType(Rparenth)){
                                        i++;
                                    } else {
                                        i++;
                                        throwError("Expected \")\" | Got: "+givenTokens[i].type());
                                    }
                                }
                                case Rparenth -> {
                                    i--;
                                    Condition pt2 = processCondition();
                                    out = new Condition(getLine(), Condition.ConditionType.Equals,pt1,pt2);
                                    if (nextIsType(Rparenth)){
                                        i++;
                                    } else {
                                        i++;
                                        throwError("Expected \")\" | Got: "+givenTokens[i].type());
                                    }
                                }
                                case String -> {
                                    String pt2 = (String)givenTokens[i].getValue();
                                    out = new Condition(getLine(), Condition.ConditionType.Equals,pt1,new DynString(pt2));
                                    if (nextIsType(Rparenth)){
                                        i++;
                                    } else {
                                        i++;
                                        throwError("Expected \")\" | Got: "+givenTokens[i].type());
                                    }
                                }
                            }
                        }
                        case isMore -> {
                            i++;
                            switch (givenTokens[i].type()){
                                case Number -> {
                                    double pt2 = (double)givenTokens[i].getValue();
                                    out = new Condition(getLine(), Condition.ConditionType.MoreThan,pt1,pt2);
                                    if (nextIsType(Rparenth)){
                                        i++;
                                    } else {
                                        i++;
                                        throwError("Expected \")\" | Got: "+givenTokens[i].type());
                                    }
                                }
                                case Name -> {
                                    String pt2 = (String)givenTokens[i].getValue();
                                    out = new Condition(getLine(), Condition.ConditionType.MoreThan,pt1,pt2);
                                    if (nextIsType(Rparenth)){
                                        i++;
                                    } else {
                                        i++;
                                        throwError("Expected \")\" | Got: "+givenTokens[i].type());
                                    }
                                }
                            }
                        }
                        case isLess -> {
                            i++;
                            switch (givenTokens[i].type()){
                                case Number -> {
                                    double pt2 = (double)givenTokens[i].getValue();
                                    out = new Condition(getLine(), Condition.ConditionType.LessThan,pt1,pt2);
                                    if (nextIsType(Rparenth)){
                                        i++;
                                    } else {
                                        i++;
                                        throwError("Expected \")\" | Got: "+givenTokens[i].type());
                                    }
                                }
                                case Name -> {
                                    String pt2 = (String)givenTokens[i].getValue();
                                    out = new Condition(getLine(), Condition.ConditionType.LessThan,pt1,pt2);
                                    if (nextIsType(Rparenth)){
                                        i++;
                                    } else {
                                        i++;
                                        throwError("Expected \")\" | Got: "+givenTokens[i].type());
                                    }
                                }
                            }
                        }
                        case And -> {
                            i++;
                            switch (givenTokens[i].type()){
                                case Name -> {
                                    String pt2 = (String)givenTokens[i].getValue();
                                    out = new Condition(getLine(), Condition.ConditionType.And,pt1,pt2);
                                    if (nextIsType(Rparenth)){
                                        i++;
                                    } else {
                                        i++;
                                        throwError("Expected \")\" | Got: "+givenTokens[i].type());
                                    }
                                }
                                case Boolean -> {
                                    boolean pt2 = (boolean)givenTokens[i].getValue();
                                    out = new Condition(getLine(), Condition.ConditionType.And,pt1,pt2);
                                    if (nextIsType(Rparenth)){
                                        i++;
                                    } else {
                                        i++;
                                        throwError("Expected \")\" | Got: "+givenTokens[i].type());
                                    }
                                }
                                case Rparenth -> {
                                    i--;
                                    Condition pt2 = processCondition();
                                    out = new Condition(getLine(), Condition.ConditionType.And,pt1,pt2);
                                    if (nextIsType(Rparenth)){
                                        i++;
                                    } else {
                                        i++;
                                        throwError("Expected \")\" | Got: "+givenTokens[i].type());
                                    }
                                }
                            }
                        }
                        case Or -> {
                            i++;
                            switch (givenTokens[i].type()){
                                case Name -> {
                                    String pt2 = (String)givenTokens[i].getValue();
                                    out = new Condition(getLine(), Condition.ConditionType.Or,pt1,pt2);
                                    if (nextIsType(Rparenth)){
                                        i++;
                                    } else {
                                        i++;
                                        throwError("Expected \")\" | Got: "+givenTokens[i].type());
                                    }
                                }
                                case Boolean -> {
                                    boolean pt2 = (boolean)givenTokens[i].getValue();
                                    out = new Condition(getLine(), Condition.ConditionType.Or,pt1,pt2);
                                    if (nextIsType(Rparenth)){
                                        i++;
                                    } else {
                                        i++;
                                        throwError("Expected \")\" | Got: "+givenTokens[i].type());
                                    }
                                }
                                case Rparenth -> {
                                    i--;
                                    Condition pt2 = processCondition();
                                    out = new Condition(getLine(), Condition.ConditionType.Or,pt1,pt2);
                                    if (nextIsType(Rparenth)){
                                        i++;
                                    } else {
                                        i++;
                                        throwError("Expected \")\" | Got: "+givenTokens[i].type());
                                    }
                                }
                            }
                        }
                        default -> throwError("Expected ==/!=/>=/<=/>/</\"And\"/\"Or\" | Got: "+givenTokens[i].type());
                    }
                }
                case String -> {
                    String pt1 = (String)givenTokens[i].getValue();
                    i++;
                    switch (givenTokens[i].type()){
                        case Equals -> {
                            i++;
                            switch (givenTokens[i].type()){
                                case String -> {
                                    String pt2 = (String)givenTokens[i].getValue();
                                    out = new Condition(getLine(), Condition.ConditionType.Equals, new DynString(pt1), new DynString(pt2));
                                    if (nextIsType(Rparenth)){
                                        i++;
                                    } else {
                                        i++;
                                        throwError("Expected \")\" | Got: "+givenTokens[i].type());
                                    }
                                }
                                case Name -> {
                                    String pt2 = (String)givenTokens[i].getValue();
                                    out = new Condition(getLine(), Condition.ConditionType.Equals, new DynString(pt1), pt2);
                                    if (nextIsType(Rparenth)){
                                        i++;
                                    } else {
                                        i++;
                                        throwError("Expected \")\" | Got: "+givenTokens[i].type());
                                    }
                                }
                            }
                        }
                        case NotEqual -> {
                            i++;
                            switch (givenTokens[i].type()){
                                case String -> {
                                    String pt2 = (String)givenTokens[i].getValue();
                                    out = new Condition(getLine(), Condition.ConditionType.NotEquals, new DynString(pt1), new DynString(pt2));
                                    if (nextIsType(Rparenth)){
                                        i++;
                                    } else {
                                        i++;
                                        throwError("Expected \")\" | Got: "+givenTokens[i].type());
                                    }
                                }
                                case Name -> {
                                    String pt2 = (String)givenTokens[i].getValue();
                                    out = new Condition(getLine(), Condition.ConditionType.NotEquals, new DynString(pt1), pt2);
                                    if (nextIsType(Rparenth)){
                                        i++;
                                    } else {
                                        i++;
                                        throwError("Expected \")\" | Got: "+givenTokens[i].type());
                                    }
                                }
                            }
                        }
                        default -> throwError("Expected ==/!= | Got: "+givenTokens[i].type());
                    }
                }
                case Not -> {
                    i++;
                    switch (givenTokens[i].type()){
                        case Boolean -> {
                            boolean value = (boolean)givenTokens[i].getValue();
                            out = new Condition(getLine(), Condition.ConditionType.Not,value);
                            if (nextIsType(Rparenth)){
                                i++;
                            } else {
                                i++;
                                throwError("Expected \")\" | Got: "+givenTokens[i].type());
                            }
                        }
                        case Number -> {
                            double value = (double)givenTokens[i].getValue();
                            out = new Condition(getLine(), Condition.ConditionType.Not,value);
                            if (nextIsType(Rparenth)){
                                i++;
                            } else {
                                i++;
                                throwError("Expected \")\" | Got: "+givenTokens[i].type());
                            }
                        }
                        case Name -> {
                            String value = (String)givenTokens[i].getValue();
                            out = new Condition(getLine(), Condition.ConditionType.Not,value);
                            if (nextIsType(Rparenth)){
                                i++;
                            } else {
                                i++;
                                throwError("Expected \")\" | Got: "+givenTokens[i].type());
                            }
                        }
                        case Lparenth -> {
                            i--;
                            Condition value = processCondition();
                            out = new Condition(getLine(), Condition.ConditionType.Not,value);
                            if (nextIsType(Rparenth)){
                                i++;
                            } else {
                                i++;
                                throwError("Expected \")\" | Got: "+givenTokens[i].type());
                            }
                        }
                    }
                }
                default -> throwError("Expected \"(\"/boolean/number/name/string/\"Not\" | Got: "+givenTokens[i].type());
            }
        } else {
            i++;
            throwError("Expected '(' | Got: "+givenTokens[i].type());
        }
        if (out == null){
            Token current = givenTokens[i];
            int line = current.getLine();
            int column = current.getColumn();
            throw new CommandException(line,column,"Could not construct condition!");
        }
        return out;
    }

    private void processListJsonOp(){
        Token current = givenTokens[i];
        switch (current.type()){
            case Append -> {
                if (givenTokens[i+2].type() == To){
                    Token in = givenTokens[i+1];
                    if (givenTokens[i+3].type() == Name){
                        String out = (String)givenTokens[i+3].getValue();
                        addCommand(new Append(in.getLine(),in,out));
                        i+=4;
                    } else {
                        throwError("Must use variable as command output!");
                    }
                } else if (givenTokens[i+3].type() == To){
                    Token in1 = givenTokens[i+1];
                    Token in2 = givenTokens[i+2];
                    if (givenTokens[i+4].type() == Name){
                        String out = (String)givenTokens[i+4].getValue();
                        addCommand(new Append(in1.getLine(), in1,in2, out));
                        i+=5;
                    } else {
                        throwError("Must use variable as command output!");
                    }
                    String out;
                } else {
                    throwError("Improperly formatted command!");
                }
            }
            case Insert -> {
                if (givenTokens[i+3].type() == To){
                    Token in1 = givenTokens[i+1];
                    Token in2 = givenTokens[i+2];
                    if (givenTokens[i+4].type() == Name){
                        String out = (String)givenTokens[i+4].getValue();
                        addCommand(new Insert(in1.getLine(),in1,in2,out));
                        i+=5;
                    } else {
                        throwError("Must use variable as command output!");
                    }
                } else {
                    throwError("Improperly formatted command!");
                }
            }
            case Remove -> {
                if (givenTokens[i+3].type() == To){
                    Token in1 = givenTokens[i+1];
                    String in2 = "";
                    if (givenTokens[i+2].type() == Name){
                        in2 = (String)givenTokens[i+2].getValue();
                    } else {
                        throwError("Must use variable as command target!");
                    }
                    if (givenTokens[i+4].type() == Name){
                        String out = (String)givenTokens[i+4].getValue();
                        addCommand(new Remove(in1.getLine(), in1,in2, out));
                        i+=5;
                    } else {
                        throwError("Must use variable as command output!");
                    }
                } else if (nextIsType(Boolean,Name)){
                    Token in = givenTokens[i+1];
                    String out = (String)givenTokens[i+2].getValue();
                    addCommand(new Remove(in.getLine(), in, out));
                    i+=3;
                } else if (nextIsType(Number,Name)){
                    Token in = givenTokens[i+1];
                    String out = (String)givenTokens[i+2].getValue();
                    addCommand(new Remove(in.getLine(), in, out));
                    i+=3;
                } else if (nextIsType(String,Name)){
                    Token in = givenTokens[i+1];
                    String out = (String)givenTokens[i+2].getValue();
                    addCommand(new Remove(in.getLine(), in, out));
                    i+=3;
                } else if (nextIsType(Name,Name)){
                    Token in = givenTokens[i+1];
                    String out = (String)givenTokens[i+2].getValue();
                    addCommand(new Remove(in.getLine(), in, out));
                    i+=3;
                } else {
                    throwError("Improperly formatted command!");
                }
            }
            case Get -> {
                String in = "";
                if (givenTokens[i+1].type() == Name){
                    in = (String)givenTokens[i+1].getValue();
                } else {
                    throwError("Must use variable for command input!");
                }
                Token in1 = givenTokens[i+2];
                if (givenTokens[i+3].type() == To) {
                    if (givenTokens[i+4].type() == Name) {
                        String out = (String) givenTokens[i+4].getValue();
                        addCommand(new Get(in1.getLine(), in, in1, out));
                        i+=5;
                    } else {
                        throwError("Must ise variable for command output!");
                    }
                } else {
                    throwError("Improperly formatted command!");
                }
            }
            case Set -> {
                Token in1 = givenTokens[i+1];
                Token in2 = givenTokens[i+2];
                if (givenTokens[i+3].type() == To){
                    if (givenTokens[i+4].type() == Name){
                        String out = (String)givenTokens[i+4].getValue();
                        addCommand(new Set(in1.getLine(), in1,in2, out));
                        i+=5;
                    } else {
                        throwError("Must use variable as command output!");
                    }
                } else {
                    throwError("Improperly formatted command!");
                }
            }
            default -> throw new IllegalStateException("Unexpected value: " + current.type());
        }
    }
    private void processFuncLoopIf(){
        Token currentToken = givenTokens[i];
        switch (currentToken.type()){
            case DefPath -> {
                if (nextIsType(Name)){
                    i++;
                    if (nextIsType(Start)){
                        if (depthTracker.isEmpty()){
                            DynPath func = new DynPath(givenTokens[i].getLine());
                            funcIDmap.put((String)givenTokens[i].getValue(), func);
                            depthTracker.add(func);
                            i+=2;
                        } else {
                            throwError("Cannot defined functions inside of functions!");
                        }
                    } else {
                        i++;
                        throwError("Expected start | Got: "+givenTokens[i].type());
                    }
                } else {
                    i++;
                    throwError("Expected name | Got: "+givenTokens[i].type());
                }
            }
            case While -> {
                if (nextIsType(Boolean)){
                    i++;
                    Token bool = givenTokens[i];
                    depthTracker.add(new While(bool.getLine(), new Condition(bool.getLine(),(boolean)bool.getValue())));
                    i++;
                } else if (nextIsType(Lparenth)){
                    Token tk = givenTokens[i];
                    Condition con = processCondition();
                    depthTracker.add(new While(tk.getLine(),con));
                    i+=2;
                } else {
                    i++;
                    throwError("Expected boolean/condition | Got: "+givenTokens[i].type());
                }
            }
            case For -> {
                i++;
                if (nextIsType(To,Name,Start)){
                    depthTracker.add(new For(getLine(), givenTokens[i], (String)givenTokens[i+2].getValue()));
                    i+=4;
                } else {
                    if (nextIsType(To)){
                        i++;
                        if (nextIsType(Name)){
                            i++;
                            if (!nextIsType(Start)){
                                i++;
                                throwError("Expcted start | Got: "+givenTokens[i].type());
                            }
                        } else {
                            i++;
                            throwError("Expected name | Got: "+givenTokens[i].type());
                        }
                    } else {
                        i++;
                        throwError("Expected to | Got: "+givenTokens[i].type());
                    }
                }
            }
            case If -> {
                Condition con = processCondition();
                depthTracker.add(new If(getLine(),con));
                i+=2;
            }
        }
    }
    private void processMainPathFunc(){
        if (nextIsType(Name)){
            mainFuncName = (String)givenTokens[i+1].getValue();
            i+=2;
        } else {
            i++;
            throwError("Expected name | Got: "+givenTokens[i].type());
        }
    }

    private void addCommand(Command c){
        if (!depthTracker.isEmpty()) {
            depthTracker.get(depthTracker.size() - 1).addCommand(c);
        } else {
            throwError("Cannot have commands outside of functions!");
        }
    }
    private int getLine(){
        return givenTokens[i].getLine();
    }

    private boolean nextIsType(TokenTypes type1){
        if (i+1 >= givenTokens.length) throwError("Expected a finished command | Given an incomplete command.");
        return isTokenType(i+1,type1);
    }
    private boolean nextIsType(TokenTypes type1, TokenTypes type2){
        if (i+2 >= givenTokens.length) throwError("Expected a finished command | Given an incomplete command.");
        return isTokenType(i+1,type1)&&isTokenType(i+2,type2);
    }
    private boolean nextIsType(TokenTypes type1, TokenTypes type2, TokenTypes type3){
        if (i+3 >= givenTokens.length) throwError("Expected a finished command | Given an incomplete command.");
        return isTokenType(i+1,type1)&&isTokenType(i+2,type2)&&isTokenType(i+3,type3);
    }
    private boolean nextIsType(TokenTypes type1, TokenTypes type2, TokenTypes type3, TokenTypes type4){
        if (i+4 >= givenTokens.length) throwError("Expected a finished command | Given an incomplete command.");
        return isTokenType(i+1,type1)&&isTokenType(i+2,type2)&&isTokenType(i+3,type3)&&isTokenType(i+4,type4);
    }
    private boolean nextIsType(TokenTypes type1, TokenTypes type2, TokenTypes type3, TokenTypes type4, TokenTypes type5){
        if (i+5 >= givenTokens.length) throwError("Expected a finished command | Given an incomplete command.");
        return isTokenType(i+1,type1)&&isTokenType(i+2,type2)&&isTokenType(i+3,type3)&&isTokenType(i+4,type4)&&isTokenType(i+5,type5);
    }
    private boolean nextIsType(TokenTypes type1, TokenTypes type2, TokenTypes type3, TokenTypes type4, TokenTypes type5, TokenTypes type6){
        if (i+6 >= givenTokens.length) throwError("Expected a finished command | Given an incomplete command.");
        return isTokenType(i+1,type1)&&isTokenType(i+2,type2)&&isTokenType(i+3,type3)&&isTokenType(i+4,type4)&&isTokenType(i+5,type5)&&isTokenType(i+6,type6);
    }
    private boolean nextIsType(TokenTypes type1, TokenTypes type2, TokenTypes type3, TokenTypes type4, TokenTypes type5, TokenTypes type6, TokenTypes type7){
        if (i+7 >= givenTokens.length) throwError("Expected a finished command | Given an incomplete command.");
        return isTokenType(i+1,type1)&&isTokenType(i+2,type2)&&isTokenType(i+3,type3)&&isTokenType(i+4,type4)&&isTokenType(i+5,type5)&&isTokenType(i+6,type6)&&isTokenType(i+7,type7);
    }
    private boolean nextIsType(TokenTypes type1, TokenTypes type2, TokenTypes type3, TokenTypes type4, TokenTypes type5, TokenTypes type6, TokenTypes type7, TokenTypes type8){
        if (i+8 >= givenTokens.length) throwError("Expected a finished command | Given an incomplete command.");
        return isTokenType(i+1,type1)&&isTokenType(i+2,type2)&&isTokenType(i+3,type3)&&isTokenType(i+4,type4)&&isTokenType(i+5,type5)&&isTokenType(i+6,type6)&&isTokenType(i+7,type7)&&isTokenType(i+8,type8);
    }
    private boolean nextIsType(TokenTypes type1, TokenTypes type2, TokenTypes type3, TokenTypes type4, TokenTypes type5, TokenTypes type6, TokenTypes type7, TokenTypes type8, TokenTypes type9){
        if (i+9 >= givenTokens.length) throwError("Expected a finished command | Given an incomplete command.");
        return isTokenType(i+1,type1)&&isTokenType(i+2,type2)&&isTokenType(i+3,type3)&&isTokenType(i+4,type4)&&isTokenType(i+5,type5)&&isTokenType(i+6,type6)&&isTokenType(i+7,type7)&&isTokenType(i+8,type8)&&isTokenType(i+9,type9);
    }
    private boolean nextIsType(TokenTypes type1, TokenTypes type2, TokenTypes type3, TokenTypes type4, TokenTypes type5, TokenTypes type6, TokenTypes type7, TokenTypes type8, TokenTypes type9, TokenTypes type10){
        if (i+10 >= givenTokens.length) throwError("Expected a finished command | Given an incomplete command.");
        return isTokenType(i+1,type1)&&isTokenType(i+2,type2)&&isTokenType(i+3,type3)&&isTokenType(i+4,type4)&&isTokenType(i+5,type5)&&isTokenType(i+6,type6)&&isTokenType(i+7,type7)&&isTokenType(i+8,type8)&&isTokenType(i+9,type9)&&isTokenType(i+10,type10);
    }
    private boolean nextIsType(TokenTypes type1, TokenTypes type2, TokenTypes type3, TokenTypes type4, TokenTypes type5, TokenTypes type6, TokenTypes type7, TokenTypes type8, TokenTypes type9, TokenTypes type10, TokenTypes type11){
        if (i+11 >= givenTokens.length) throwError("Expected a finished command | Given an incomplete command.");
        return isTokenType(i+1,type1)&&isTokenType(i+2,type2)&&isTokenType(i+3,type3)&&isTokenType(i+4,type4)&&isTokenType(i+5,type5)&&isTokenType(i+6,type6)&&isTokenType(i+7,type7)&&isTokenType(i+8,type8)&&isTokenType(i+9,type9)&&isTokenType(i+10,type10)&&isTokenType(i+11,type11);
    }
    private boolean nextIsType(TokenTypes type1, TokenTypes type2, TokenTypes type3, TokenTypes type4, TokenTypes type5, TokenTypes type6, TokenTypes type7, TokenTypes type8, TokenTypes type9, TokenTypes type10, TokenTypes type11, TokenTypes type12){
        if (i+11 >= givenTokens.length) throwError("Expected a finished command | Given an incomplete command.");
        return isTokenType(i+1,type1)&&isTokenType(i+2,type2)&&isTokenType(i+3,type3)&&isTokenType(i+4,type4)&&isTokenType(i+5,type5)&&isTokenType(i+6,type6)&&isTokenType(i+7,type7)&&isTokenType(i+8,type8)&&isTokenType(i+9,type9)&&isTokenType(i+10,type10)&&isTokenType(i+11,type11)&&isTokenType(i+12,type12);
    }
    private boolean nextIsType(TokenTypes type1, TokenTypes type2, TokenTypes type3, TokenTypes type4, TokenTypes type5, TokenTypes type6, TokenTypes type7, TokenTypes type8, TokenTypes type9, TokenTypes type10, TokenTypes type11, TokenTypes type12, TokenTypes type13){
        if (i+11 >= givenTokens.length) throwError("Expected a finished command | Given an incomplete command.");
        return isTokenType(i+1,type1)&&isTokenType(i+2,type2)&&isTokenType(i+3,type3)&&isTokenType(i+4,type4)&&isTokenType(i+5,type5)&&isTokenType(i+6,type6)&&isTokenType(i+7,type7)&&isTokenType(i+8,type8)&&isTokenType(i+9,type9)&&isTokenType(i+10,type10)&&isTokenType(i+11,type11)&&isTokenType(i+12,type12)&&isTokenType(i+13,type13);
    }
    private boolean nextIsType(TokenTypes type1, TokenTypes type2, TokenTypes type3, TokenTypes type4, TokenTypes type5, TokenTypes type6, TokenTypes type7, TokenTypes type8, TokenTypes type9, TokenTypes type10, TokenTypes type11, TokenTypes type12, TokenTypes type13, TokenTypes type14){
        if (i+11 >= givenTokens.length) throwError("Expected a finished command | Given an incomplete command.");
        return isTokenType(i+1,type1)&&isTokenType(i+2,type2)&&isTokenType(i+3,type3)&&isTokenType(i+4,type4)&&isTokenType(i+5,type5)&&isTokenType(i+6,type6)&&isTokenType(i+7,type7)&&isTokenType(i+8,type8)&&isTokenType(i+9,type9)&&isTokenType(i+10,type10)&&isTokenType(i+11,type11)&&isTokenType(i+12,type12)&&isTokenType(i+13,type13)&&isTokenType(i+14,type14);
    }
    private boolean nextIsType(TokenTypes type1, TokenTypes type2, TokenTypes type3, TokenTypes type4, TokenTypes type5, TokenTypes type6, TokenTypes type7, TokenTypes type8, TokenTypes type9, TokenTypes type10, TokenTypes type11, TokenTypes type12, TokenTypes type13, TokenTypes type14, TokenTypes type15){
        if (i+11 >= givenTokens.length) throwError("Expected a finished command | Given an incomplete command.");
        return isTokenType(i+1,type1)&&isTokenType(i+2,type2)&&isTokenType(i+3,type3)&&isTokenType(i+4,type4)&&isTokenType(i+5,type5)&&isTokenType(i+6,type6)&&isTokenType(i+7,type7)&&isTokenType(i+8,type8)&&isTokenType(i+9,type9)&&isTokenType(i+10,type10)&&isTokenType(i+11,type11)&&isTokenType(i+12,type12)&&isTokenType(i+13,type13)&&isTokenType(i+14,type14)&&isTokenType(i+15,type15);
    }
    private boolean isTokenType(int i, TokenTypes type){
        return givenTokens[i].type()==type;
    }

    private Object[] getNextTouple(){ // literally stealing form python
        if (nextIsType(Lparenth)){
            ArrayList<Object> items = new ArrayList<>();
            while (!nextIsType(Rparenth)){
                if (nextIsType(Name)||nextIsType(Number)){
                    i++;
                    items.add(givenTokens[i].getValue());
                } else {
                    i++;
                    throwError("Expected name/number | Got: "+givenTokens[i].type());
                }
                if (nextIsType(Comma)){
                    i++;
                } else {
                    i++;
                    throwError("Expected comma | Got: "+givenTokens[i].type());
                }
            }
            return items.toArray(new Object[0]);
        } else {
            i++;
            throwError("Expected \"(\" | Got: "+givenTokens[i].type());
            return null;
        }
    }

    private void throwError(String reason){
        Token current = givenTokens[i];
        int line = current.getLine();
        int column = current.getColumn();
        throw new CommandException(line,column,reason);
    }

    public String toString(){
        StringBuilder debug = new StringBuilder("Command constructor dump:");
        for (String key : funcIDmap.keySet()){
            DynPath func = funcIDmap.get(key);
            debug.append("\n    ");
            debug.append(key);
            debug.append(" -\\/ \n");
            // unravel function
            debug.append(unravelCommand(func));
        }
        return debug.toString();
    }
    private String unravelCommand(Command cmd){
        return unravelCommand(cmd,"    ");
    }
    private String unravelCommand(Command cmd, String indent){
        indent = indent+"    ";
        StringBuilder out = new StringBuilder('\n');
        out.append(indent);
        out.append(cmd.toString());
        if (cmd instanceof DynPath command){
            for (Command c : command.getCommandList()){
                out.append("\n");
                out.append(unravelCommand(c,indent));
            }
        } else if (cmd instanceof For command){
            for (Command c : command.getCommandList()){
                out.append("\n");
                out.append(unravelCommand(c,indent));
            }
        } else if (cmd instanceof If command){
            for (Command c : command.getCommandList()){
                out.append("\n");
                out.append(unravelCommand(c,indent));
            }
        } else if (cmd instanceof While command){
            for (Command c : command.getCommandList()){
                out.append("\n");
                out.append(unravelCommand(c,indent));
            }
        }
        return out.toString();
    }
}