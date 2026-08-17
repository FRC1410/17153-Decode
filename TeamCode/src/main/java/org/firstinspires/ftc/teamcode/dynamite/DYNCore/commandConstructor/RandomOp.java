package org.firstinspires.ftc.teamcode.dynamite.DYNCore.commandConstructor;

import static org.firstinspires.ftc.teamcode.dynamite.DYNCore.tokenizer.TokenTypes.Name;
import static org.firstinspires.ftc.teamcode.dynamite.DYNCore.tokenizer.TokenTypes.Number;
import static org.firstinspires.ftc.teamcode.dynamite.DYNCore.tokenizer.TokenTypes.To;

import org.firstinspires.ftc.teamcode.dynamite.DYNCore.commands.random.RngBoolean;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.commands.random.RngDouble;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.commands.random.RngFloat;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.commands.random.RngInteger;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.tokenizer.Token;

class RandomOp extends ConstructorUtils{
    public static void Do(){
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
}
