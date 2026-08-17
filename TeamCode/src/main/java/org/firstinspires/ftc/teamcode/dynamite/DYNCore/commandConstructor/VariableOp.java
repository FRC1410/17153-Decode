package org.firstinspires.ftc.teamcode.dynamite.DYNCore.commandConstructor;

import static org.firstinspires.ftc.teamcode.dynamite.DYNCore.tokenizer.TokenTypes.Boolean;
import static org.firstinspires.ftc.teamcode.dynamite.DYNCore.tokenizer.TokenTypes.Comma;
import static org.firstinspires.ftc.teamcode.dynamite.DYNCore.tokenizer.TokenTypes.LCbracket;
import static org.firstinspires.ftc.teamcode.dynamite.DYNCore.tokenizer.TokenTypes.Lbracket;
import static org.firstinspires.ftc.teamcode.dynamite.DYNCore.tokenizer.TokenTypes.Lparenth;
import static org.firstinspires.ftc.teamcode.dynamite.DYNCore.tokenizer.TokenTypes.Name;
import static org.firstinspires.ftc.teamcode.dynamite.DYNCore.tokenizer.TokenTypes.Number;
import static org.firstinspires.ftc.teamcode.dynamite.DYNCore.tokenizer.TokenTypes.RCbracket;
import static org.firstinspires.ftc.teamcode.dynamite.DYNCore.tokenizer.TokenTypes.Rbracket;
import static org.firstinspires.ftc.teamcode.dynamite.DYNCore.tokenizer.TokenTypes.Rparenth;
import static org.firstinspires.ftc.teamcode.dynamite.DYNCore.tokenizer.TokenTypes.String;

import org.firstinspires.ftc.teamcode.dynamite.DYNCore.commands.controlFlow.Condition;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.commands.variables.AddVar;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.variables.VariableTypes;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.variables.primitives.DynBoolean;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.variables.primitives.DynNumber;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.variables.primitives.DynString;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.tokenizer.Token;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

class VariableOp extends ConstructorUtils{
    public static void Do(){
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
                if (nextIsType(Name,String)) {
                    i++;
                    Token name = givenTokens[i];
                    Token value = givenTokens[i+1];
                    addCommand(new AddVar(getLine(), (String) name.getValue(), VariableTypes.String, new DynString((String)value.getValue())));
                    i+=2;
                } else if (nextIsType(Name,Name)){
                    i++;
                    Token name = givenTokens[i];
                    Token value = givenTokens[i+1];
                    addCommand(new AddVar(getLine(), (String)name.getValue(), VariableTypes.String, value.getValue())); // normal String classes are considered to be variables
                    i+=2;
                } else if (nextIsType(Name)){
                    i++;
                    Token name = givenTokens[i];
                    addCommand(new AddVar(getLine(), (String)name.getValue(), VariableTypes.String, new DynString("")));
                    i++;
                } else {
                    if (nextIsType(Name)){
                        i++;
                        throwError("Expected string/name | Got: "+givenTokens[i].type());
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
}
