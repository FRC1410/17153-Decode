package org.firstinspires.ftc.teamcode.dynamite.DYNCore.commandConstructor;

import static org.firstinspires.ftc.teamcode.dynamite.DYNCore.tokenizer.TokenTypes.Comma;
import static org.firstinspires.ftc.teamcode.dynamite.DYNCore.tokenizer.TokenTypes.Lparenth;
import static org.firstinspires.ftc.teamcode.dynamite.DYNCore.tokenizer.TokenTypes.Name;
import static org.firstinspires.ftc.teamcode.dynamite.DYNCore.tokenizer.TokenTypes.Number;
import static org.firstinspires.ftc.teamcode.dynamite.DYNCore.tokenizer.TokenTypes.Rparenth;
import static org.firstinspires.ftc.teamcode.dynamite.DYNCore.tokenizer.TokenTypes.To;

import org.firstinspires.ftc.teamcode.dynamite.DYNCore.commands.movement.GoTo;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.commands.movement.TurnTo;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.commands.movement.bezierStuff.DoBezier;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.commands.movement.splineStuff.DoSpline;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.commands.movement.splineStuff.DoSplineLinear;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.commands.movement.splineStuff.DoSplineSpline;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.tokenizer.Token;

import java.util.ArrayList;

class MoveOp extends ConstructorUtils{
    public static void Do(){
        Token current = givenTokens[i];
        switch (current.type()){
            case TurnTo -> {
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
                        throwError("Expected a number! got: " + givenTokens[i].type());
                    }
                } else {
                    throwError("Expected name/number! got: " + givenTokens[i].type());
                }
            }
            case GoTo -> {
                if (nextIsType(Name)){
                    i++;
                    Token name = givenTokens[i];
                    addCommand(new GoTo(name.getLine(),(String)name.getValue()));
                    i++;
                }
                else if (nextIsType(Lparenth,Name,Comma,Name,Rparenth)){
                    i+=2;
                    Token name0 = givenTokens[i];
                    Token name1 = givenTokens[i+2];
                    addCommand(new GoTo(name0.getLine(),(String)name0.getValue(),(String)name1.getValue()));
                    i+=4;
                }
                else if (nextIsType(Lparenth,Name,Comma,Number,Rparenth)){
                    i+=2;
                    Token name = givenTokens[i];
                    Token number = givenTokens[i+2];
                    addCommand(new GoTo(name.getLine(),(String)name.getValue(),(double)number.getValue()));
                    i+=4;
                }
                else if (nextIsType(Lparenth,Number,Comma,Name,Rparenth)){
                    i+=2;
                    Token number = givenTokens[i];
                    Token name = givenTokens[i+2];
                    addCommand(new GoTo(number.getLine(),(double)number.getValue(),(String)name.getValue()));
                    i+=4;
                }
                else if (nextIsType(Lparenth,Number,Comma,Number,Rparenth)){
                    i+=2;
                    Token number0 = givenTokens[i];
                    Token number1 = givenTokens[i+2];
                    addCommand(new GoTo(number0.getLine(),(double)number0.getValue(),(double)number1.getValue()));
                    i+=4;
                }
                else if (nextIsType(Lparenth,Name,Comma,Name,Comma,Name,Rparenth)){
                    i+=2;
                    Token name0 = givenTokens[i];
                    Token name1 = givenTokens[i+2];
                    Token name2 = givenTokens[i+4];
                    addCommand(new GoTo(name0.getLine(),(String)name0.getValue(),(String)name1.getValue(),(String)name2.getValue()));
                }
                else if (nextIsType(Lparenth,Name,Comma,Name,Comma,Number,Rparenth)){
                    i+=2;
                    Token name0 = givenTokens[i];
                    Token name1 = givenTokens[i+2];
                    Token number = givenTokens[i+4];
                    addCommand(new GoTo(name0.getLine(),(String)name0.getValue(),(String)name1.getValue(),(double)number.getValue()));
                }
                else if (nextIsType(Lparenth,Name,Comma,Number,Comma,Name,Rparenth)){
                    i+=2;
                    Token name0 = givenTokens[i];
                    Token number = givenTokens[i+2];
                    Token name1 = givenTokens[i+4];
                    addCommand(new GoTo(name0.getLine(),(String)name0.getValue(),(double)number.getValue(),(String)name1.getValue()));
                }
                else if (nextIsType(Lparenth,Number,Comma,Name,Comma,Name,Rparenth)){
                    i+=2;
                    Token number = givenTokens[i];
                    Token name0 = givenTokens[i+2];
                    Token name1 = givenTokens[i+4];
                    addCommand(new GoTo(number.getLine(),(double)number.getValue(),(String)name0.getValue(),(String)name1.getValue()));
                }
                else if (nextIsType(Lparenth,Name,Comma,Number,Comma,Number,Rparenth)){
                    i+=2;
                    Token name = givenTokens[i];
                    Token number0 = givenTokens[i+2];
                    Token number1 = givenTokens[i+4];
                    addCommand(new GoTo(name.getLine(),(String)name.getValue(),(double)number0.getValue(),(double)number1.getValue()));
                }
                else if (nextIsType(Lparenth,Number,Comma,Number,Comma,Name,Rparenth)){
                    i+=2;
                    Token number0 = givenTokens[i];
                    Token number1 = givenTokens[i+2];
                    Token name = givenTokens[i+4];
                    addCommand(new GoTo(number0.getLine(),(double)number0.getValue(),(double)number1.getValue(),(String)name.getValue()));
                }
                else if (nextIsType(Lparenth,Number,Comma,Name,Comma,Number,Rparenth)){
                    i+=2;
                    Token number0 = givenTokens[i];
                    Token name = givenTokens[i+2];
                    Token number1 = givenTokens[i+4];
                    addCommand(new GoTo(number0.getLine(),(double)number0.getValue(),(String)name.getValue(),(double)number1.getValue()));
                }
                else if (nextIsType(Lparenth,Number,Comma,Number,Comma,Number,Rparenth)){
                    i+=2;
                    Token number0 = givenTokens[i];
                    Token number1 = givenTokens[i+2];
                    Token number2 = givenTokens[i+4];
                    addCommand(new GoTo(number0.getLine(),(double)number0.getValue(),(double)number1.getValue(),(double)number2.getValue()));
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
            }
            case doBez -> {
                // pos1 to pos2
                if (nextIsType(Name, To, Name)) {
                    i++;
                    Token name0 = givenTokens[i];
                    Token name1 = givenTokens[i+2];
                    addCommand(new DoBezier(name0.getLine(),(String)name0.getValue(),(String)name1.getValue()));
                    i+=3;
                }
                // ((x,y), (x,y,z)... (x,y,z))
                else if (nextIsType(Lparenth,Lparenth)){
                    i++;
                    ArrayList<Object[]> touples = new ArrayList<>();
                    while (true){
                        Token currentTk = givenTokens[i];
                        switch (currentTk.type()){
                            case Rparenth -> {
                                i++;
                                addCommand(new DoBezier(getLine(),touples.toArray(new Object[0][])));
                                return;
                            }
                            case Comma -> touples.add(getNextTouple());
                            default -> throwError("Expected \"(\" | Got: "+currentTk.type());
                        }
                    }
                }
                // (x,y) to (x,y)
                else if (nextIsType(Lparenth,Name,Comma,Name,Rparenth,To,Lparenth,Name,Comma,Name,Rparenth)){
                    Object[] pt1 = getNextTouple();
                    Object[] pt2 = getNextTouple();
                    addCommand(new DoBezier(givenTokens[i].getLine(),new Object[][]{pt1,pt2}));
                }
                else if (nextIsType(Lparenth,Name,Comma,Name,Rparenth,To,Lparenth,Name,Comma,Number,Rparenth)){
                    Object[] pt1 = getNextTouple();
                    Object[] pt2 = getNextTouple();
                    addCommand(new DoBezier(givenTokens[i].getLine(),new Object[][]{pt1,pt2}));
                }
                else if (nextIsType(Lparenth,Name,Comma,Name,Rparenth,To,Lparenth,Number,Comma,Name,Rparenth)){
                    Object[] pt1 = getNextTouple();
                    Object[] pt2 = getNextTouple();
                    addCommand(new DoBezier(givenTokens[i].getLine(),new Object[][]{pt1,pt2}));
                }
                else if (nextIsType(Lparenth,Name,Comma,Number,Rparenth,To,Lparenth,Name,Comma,Name,Rparenth)){
                    Object[] pt1 = getNextTouple();
                    Object[] pt2 = getNextTouple();
                    addCommand(new DoBezier(givenTokens[i].getLine(),new Object[][]{pt1,pt2}));
                }
                else if (nextIsType(Lparenth,Number,Comma,Name,Rparenth,To,Lparenth,Name,Comma,Name,Rparenth)){
                    Object[] pt1 = getNextTouple();
                    Object[] pt2 = getNextTouple();
                    addCommand(new DoBezier(givenTokens[i].getLine(),new Object[][]{pt1,pt2}));
                }
                else if (nextIsType(Lparenth,Name,Comma,Name,Rparenth,To,Lparenth,Number,Comma,Number,Rparenth)){
                    Object[] pt1 = getNextTouple();
                    Object[] pt2 = getNextTouple();
                    addCommand(new DoBezier(givenTokens[i].getLine(),new Object[][]{pt1,pt2}));
                }
                else if (nextIsType(Lparenth,Name,Comma,Number,Rparenth,To,Lparenth,Number,Comma,Name,Rparenth)){
                    Object[] pt1 = getNextTouple();
                    Object[] pt2 = getNextTouple();
                    addCommand(new DoBezier(givenTokens[i].getLine(),new Object[][]{pt1,pt2}));
                }
                else if (nextIsType(Lparenth,Number,Comma,Number,Rparenth,To,Lparenth,Name,Comma,Name,Rparenth)){
                    Object[] pt1 = getNextTouple();
                    Object[] pt2 = getNextTouple();
                    addCommand(new DoBezier(givenTokens[i].getLine(),new Object[][]{pt1,pt2}));
                }
                else if (nextIsType(Lparenth,Number,Comma,Name,Rparenth,To,Lparenth,Name,Comma,Number,Rparenth)){
                    Object[] pt1 = getNextTouple();
                    Object[] pt2 = getNextTouple();
                    addCommand(new DoBezier(givenTokens[i].getLine(),new Object[][]{pt1,pt2}));
                }
                else if (nextIsType(Lparenth,Name,Comma,Number,Rparenth,To,Lparenth,Number,Comma,Number,Rparenth)){
                    Object[] pt1 = getNextTouple();
                    Object[] pt2 = getNextTouple();
                    addCommand(new DoBezier(givenTokens[i].getLine(),new Object[][]{pt1,pt2}));
                }
                else if (nextIsType(Lparenth,Number,Comma,Number,Rparenth,To,Lparenth,Number,Comma,Name,Rparenth)){
                    Object[] pt1 = getNextTouple();
                    Object[] pt2 = getNextTouple();
                    addCommand(new DoBezier(givenTokens[i].getLine(),new Object[][]{pt1,pt2}));
                }
                else if (nextIsType(Lparenth,Number,Comma,Number,Rparenth,To,Lparenth,Name,Comma,Number,Rparenth)){
                    Object[] pt1 = getNextTouple();
                    Object[] pt2 = getNextTouple();
                    addCommand(new DoBezier(givenTokens[i].getLine(),new Object[][]{pt1,pt2}));
                }
                else if (nextIsType(Lparenth,Number,Comma,Name,Rparenth,To,Lparenth,Number,Comma,Number,Rparenth)){
                    Object[] pt1 = getNextTouple();
                    Object[] pt2 = getNextTouple();
                    addCommand(new DoBezier(givenTokens[i].getLine(),new Object[][]{pt1,pt2}));
                }
                else if (nextIsType(Lparenth,Number,Comma,Number,Rparenth,To,Lparenth,Number,Comma,Number,Rparenth)){
                    Object[] pt1 = getNextTouple();
                    Object[] pt2 = getNextTouple();
                    addCommand(new DoBezier(givenTokens[i].getLine(),new Object[][]{pt1,pt2}));
                }
                // (x,y) to (x,y,h)
                else if (nextIsType(Lparenth,Name,Comma,Name,Rparenth,To,Lparenth,Name,Comma,Name,Comma,Name,Rparenth)){
                    Object[] pt1 = getNextTouple();
                    Object[] pt2 = getNextTouple();
                    addCommand(new DoBezier(givenTokens[i].getLine(),new Object[][]{pt1,pt2}));
                }
                else if (nextIsType(Lparenth,Name,Comma,Name,Rparenth,To,Lparenth,Name,Comma,Name,Comma,Number,Rparenth)){
                    Object[] pt1 = getNextTouple();
                    Object[] pt2 = getNextTouple();
                    addCommand(new DoBezier(givenTokens[i].getLine(),new Object[][]{pt1,pt2}));
                }
                else if (nextIsType(Lparenth,Name,Comma,Name,Rparenth,To,Lparenth,Name,Comma,Number,Comma,Name,Rparenth)){
                    Object[] pt1 = getNextTouple();
                    Object[] pt2 = getNextTouple();
                    addCommand(new DoBezier(givenTokens[i].getLine(),new Object[][]{pt1,pt2}));
                }
                else if (nextIsType(Lparenth,Name,Comma,Name,Rparenth,To,Lparenth,Number,Comma,Name,Comma,Name,Rparenth)){
                    Object[] pt1 = getNextTouple();
                    Object[] pt2 = getNextTouple();
                    addCommand(new DoBezier(givenTokens[i].getLine(),new Object[][]{pt1,pt2}));
                }
                else if (nextIsType(Lparenth,Name,Comma,Number,Rparenth,To,Lparenth,Name,Comma,Name,Comma,Name,Rparenth)){
                    Object[] pt1 = getNextTouple();
                    Object[] pt2 = getNextTouple();
                    addCommand(new DoBezier(givenTokens[i].getLine(),new Object[][]{pt1,pt2}));
                }
                else if (nextIsType(Lparenth,Number,Comma,Name,Rparenth,To,Lparenth,Name,Comma,Name,Comma,Name,Rparenth)){
                    Object[] pt1 = getNextTouple();
                    Object[] pt2 = getNextTouple();
                    addCommand(new DoBezier(givenTokens[i].getLine(),new Object[][]{pt1,pt2}));
                }
                else if (nextIsType(Lparenth,Name,Comma,Name,Rparenth,To,Lparenth,Name,Comma,Number,Comma,Number,Rparenth)){
                    Object[] pt1 = getNextTouple();
                    Object[] pt2 = getNextTouple();
                    addCommand(new DoBezier(givenTokens[i].getLine(),new Object[][]{pt1,pt2}));
                }
                else if (nextIsType(Lparenth,Name,Comma,Name,Rparenth,To,Lparenth,Number,Comma,Number,Comma,Name,Rparenth)){
                    Object[] pt1 = getNextTouple();
                    Object[] pt2 = getNextTouple();
                    addCommand(new DoBezier(givenTokens[i].getLine(),new Object[][]{pt1,pt2}));
                }
                else if (nextIsType(Lparenth,Name,Comma,Number,Rparenth,To,Lparenth,Number,Comma,Name,Comma,Name,Rparenth)){
                    Object[] pt1 = getNextTouple();
                    Object[] pt2 = getNextTouple();
                    addCommand(new DoBezier(givenTokens[i].getLine(),new Object[][]{pt1,pt2}));
                }
                else if (nextIsType(Lparenth,Number,Comma,Number,Rparenth,To,Lparenth,Name,Comma,Name,Comma,Name,Rparenth)){
                    Object[] pt1 = getNextTouple();
                    Object[] pt2 = getNextTouple();
                    addCommand(new DoBezier(givenTokens[i].getLine(),new Object[][]{pt1,pt2}));
                }
                else if (nextIsType(Lparenth,Number,Comma,Number,Rparenth,To,Lparenth,Name,Comma,Name,Comma,Name,Rparenth)){
                    Object[] pt1 = getNextTouple();
                    Object[] pt2 = getNextTouple();
                    addCommand(new DoBezier(givenTokens[i].getLine(),new Object[][]{pt1,pt2}));
                }
                else if (nextIsType(Lparenth,Name,Comma,Name,Rparenth,To,Lparenth,Number,Comma,Number,Comma,Number,Rparenth)){
                    Object[] pt1 = getNextTouple();
                    Object[] pt2 = getNextTouple();
                    addCommand(new DoBezier(givenTokens[i].getLine(),new Object[][]{pt1,pt2}));
                }
                else if (nextIsType(Lparenth,Name,Comma,Number,Rparenth,To,Lparenth,Number,Comma,Number,Comma,Name,Rparenth)){
                    Object[] pt1 = getNextTouple();
                    Object[] pt2 = getNextTouple();
                    addCommand(new DoBezier(givenTokens[i].getLine(),new Object[][]{pt1,pt2}));
                }
                else if (nextIsType(Lparenth,Number,Comma,Number,Rparenth,To,Lparenth,Number,Comma,Name,Comma,Name,Rparenth)){
                    Object[] pt1 = getNextTouple();
                    Object[] pt2 = getNextTouple();
                    addCommand(new DoBezier(givenTokens[i].getLine(),new Object[][]{pt1,pt2}));
                }
                else if (nextIsType(Lparenth,Number,Comma,Number,Rparenth,To,Lparenth,Name,Comma,Name,Comma,Number,Rparenth)){
                    Object[] pt1 = getNextTouple();
                    Object[] pt2 = getNextTouple();
                    addCommand(new DoBezier(givenTokens[i].getLine(),new Object[][]{pt1,pt2}));
                }
                else if (nextIsType(Lparenth,Name,Comma,Number,Rparenth,To,Lparenth,Number,Comma,Number,Comma,Number,Rparenth)){
                    Object[] pt1 = getNextTouple();
                    Object[] pt2 = getNextTouple();
                    addCommand(new DoBezier(givenTokens[i].getLine(),new Object[][]{pt1,pt2}));
                }
                else if (nextIsType(Lparenth,Number,Comma,Number,Rparenth,To,Lparenth,Number,Comma,Number,Comma,Name,Rparenth)){
                    Object[] pt1 = getNextTouple();
                    Object[] pt2 = getNextTouple();
                    addCommand(new DoBezier(givenTokens[i].getLine(),new Object[][]{pt1,pt2}));
                }
                else if (nextIsType(Lparenth,Number,Comma,Number,Rparenth,To,Lparenth,Number,Comma,Name,Comma,Number,Rparenth)){
                    Object[] pt1 = getNextTouple();
                    Object[] pt2 = getNextTouple();
                    addCommand(new DoBezier(givenTokens[i].getLine(),new Object[][]{pt1,pt2}));
                }
                else if (nextIsType(Lparenth,Number,Comma,Number,Rparenth,To,Lparenth,Number,Comma,Name,Comma,Number,Rparenth)){
                    Object[] pt1 = getNextTouple();
                    Object[] pt2 = getNextTouple();
                    addCommand(new DoBezier(givenTokens[i].getLine(),new Object[][]{pt1,pt2}));
                }
                else if (nextIsType(Lparenth,Number,Comma,Number,Rparenth,To,Lparenth,Name,Comma,Number,Comma,Number,Rparenth)){
                    Object[] pt1 = getNextTouple();
                    Object[] pt2 = getNextTouple();
                    addCommand(new DoBezier(givenTokens[i].getLine(),new Object[][]{pt1,pt2}));
                }
                else if (nextIsType(Lparenth,Number,Comma,Name,Rparenth,To,Lparenth,Number,Comma,Number,Comma,Number,Rparenth)){
                    Object[] pt1 = getNextTouple();
                    Object[] pt2 = getNextTouple();
                    addCommand(new DoBezier(givenTokens[i].getLine(),new Object[][]{pt1,pt2}));
                }
                else if (nextIsType(Lparenth,Number,Comma,Number,Rparenth,To,Lparenth,Number,Comma,Number,Comma,Number,Rparenth)){
                    Object[] pt1 = getNextTouple();
                    Object[] pt2 = getNextTouple();
                    addCommand(new DoBezier(givenTokens[i].getLine(),new Object[][]{pt1,pt2}));
                }
                // (x,y,h) to (x,y)
                else if (nextIsType(Lparenth,Name,Comma,Name,Comma,Name,Rparenth,To,Lparenth,Name,Comma,Name,Rparenth)){
                    Object[] pt1 = getNextTouple();
                    Object[] pt2 = getNextTouple();
                    addCommand(new DoBezier(givenTokens[i].getLine(),new Object[][]{pt1,pt2}));
                }
                else if (nextIsType(Lparenth,Name,Comma,Name,Comma,Name,Rparenth,To,Lparenth,Name,Comma,Number,Rparenth)){
                    Object[] pt1 = getNextTouple();
                    Object[] pt2 = getNextTouple();
                    addCommand(new DoBezier(givenTokens[i].getLine(),new Object[][]{pt1,pt2}));
                }
                else if (nextIsType(Lparenth,Name,Comma,Name,Comma,Name,Rparenth,To,Lparenth,Number,Comma,Name,Rparenth)){
                    Object[] pt1 = getNextTouple();
                    Object[] pt2 = getNextTouple();
                    addCommand(new DoBezier(givenTokens[i].getLine(),new Object[][]{pt1,pt2}));
                }
                else if (nextIsType(Lparenth,Name,Comma,Name,Comma,Number,Rparenth,To,Lparenth,Name,Comma,Name,Rparenth)){
                    Object[] pt1 = getNextTouple();
                    Object[] pt2 = getNextTouple();
                    addCommand(new DoBezier(givenTokens[i].getLine(),new Object[][]{pt1,pt2}));
                }
                else if (nextIsType(Lparenth,Name,Comma,Number,Comma,Name,Rparenth,To,Lparenth,Name,Comma,Name,Rparenth)){
                    Object[] pt1 = getNextTouple();
                    Object[] pt2 = getNextTouple();
                    addCommand(new DoBezier(givenTokens[i].getLine(),new Object[][]{pt1,pt2}));
                }
                else if (nextIsType(Lparenth,Number,Comma,Name,Comma,Name,Rparenth,To,Lparenth,Name,Comma,Name,Rparenth)){
                    Object[] pt1 = getNextTouple();
                    Object[] pt2 = getNextTouple();
                    addCommand(new DoBezier(givenTokens[i].getLine(),new Object[][]{pt1,pt2}));
                }
                else if (nextIsType(Lparenth,Name,Comma,Name,Comma,Name,Rparenth,To,Lparenth,Number,Comma,Number,Rparenth)){
                    Object[] pt1 = getNextTouple();
                    Object[] pt2 = getNextTouple();
                    addCommand(new DoBezier(givenTokens[i].getLine(),new Object[][]{pt1,pt2}));
                }
                else if (nextIsType(Lparenth,Name,Comma,Name,Comma,Number,Rparenth,To,Lparenth,Number,Comma,Name,Rparenth)){
                    Object[] pt1 = getNextTouple();
                    Object[] pt2 = getNextTouple();
                    addCommand(new DoBezier(givenTokens[i].getLine(),new Object[][]{pt1,pt2}));
                }
                else if (nextIsType(Lparenth,Name,Comma,Number,Comma,Number,Rparenth,To,Lparenth,Name,Comma,Name,Rparenth)){
                    Object[] pt1 = getNextTouple();
                    Object[] pt2 = getNextTouple();
                    addCommand(new DoBezier(givenTokens[i].getLine(),new Object[][]{pt1,pt2}));
                }
                else if (nextIsType(Lparenth,Number,Comma,Number,Comma,Name,Rparenth,To,Lparenth,Name,Comma,Name,Rparenth)){
                    Object[] pt1 = getNextTouple();
                    Object[] pt2 = getNextTouple();
                    addCommand(new DoBezier(givenTokens[i].getLine(),new Object[][]{pt1,pt2}));
                }
                else if (nextIsType(Lparenth,Number,Comma,Name,Comma,Name,Rparenth,To,Lparenth,Name,Comma,Number,Rparenth)){
                    Object[] pt1 = getNextTouple();
                    Object[] pt2 = getNextTouple();
                    addCommand(new DoBezier(givenTokens[i].getLine(),new Object[][]{pt1,pt2}));
                }
                else if (nextIsType(Lparenth,Name,Comma,Name,Comma,Number,Rparenth,To,Lparenth,Number,Comma,Number,Rparenth)){
                    Object[] pt1 = getNextTouple();
                    Object[] pt2 = getNextTouple();
                    addCommand(new DoBezier(givenTokens[i].getLine(),new Object[][]{pt1,pt2}));
                }
                else if (nextIsType(Lparenth,Name,Comma,Number,Comma,Number,Rparenth,To,Lparenth,Number,Comma,Name,Rparenth)){
                    Object[] pt1 = getNextTouple();
                    Object[] pt2 = getNextTouple();
                    addCommand(new DoBezier(givenTokens[i].getLine(),new Object[][]{pt1,pt2}));
                }
                else if (nextIsType(Lparenth,Number,Comma,Number,Comma,Number,Rparenth,To,Lparenth,Name,Comma,Name,Rparenth)){
                    Object[] pt1 = getNextTouple();
                    Object[] pt2 = getNextTouple();
                    addCommand(new DoBezier(givenTokens[i].getLine(),new Object[][]{pt1,pt2}));
                }
                else if (nextIsType(Lparenth,Number,Comma,Number,Comma,Name,Rparenth,To,Lparenth,Name,Comma,Number,Rparenth)){
                    Object[] pt1 = getNextTouple();
                    Object[] pt2 = getNextTouple();
                    addCommand(new DoBezier(givenTokens[i].getLine(),new Object[][]{pt1,pt2}));
                }
                else if (nextIsType(Lparenth,Number,Comma,Name,Comma,Name,Rparenth,To,Lparenth,Number,Comma,Number,Rparenth)){
                    Object[] pt1 = getNextTouple();
                    Object[] pt2 = getNextTouple();
                    addCommand(new DoBezier(givenTokens[i].getLine(),new Object[][]{pt1,pt2}));
                }
                else if (nextIsType(Lparenth,Name,Comma,Number,Comma,Number,Rparenth,To,Lparenth,Number,Comma,Number,Rparenth)){
                    Object[] pt1 = getNextTouple();
                    Object[] pt2 = getNextTouple();
                    addCommand(new DoBezier(givenTokens[i].getLine(),new Object[][]{pt1,pt2}));
                }
                else if (nextIsType(Lparenth,Number,Comma,Number,Comma,Number,Rparenth,To,Lparenth,Number,Comma,Name,Rparenth)){
                    Object[] pt1 = getNextTouple();
                    Object[] pt2 = getNextTouple();
                    addCommand(new DoBezier(givenTokens[i].getLine(),new Object[][]{pt1,pt2}));
                }
                else if (nextIsType(Lparenth,Number,Comma,Number,Comma,Number,Rparenth,To,Lparenth,Name,Comma,Number,Rparenth)){
                    Object[] pt1 = getNextTouple();
                    Object[] pt2 = getNextTouple();
                    addCommand(new DoBezier(givenTokens[i].getLine(),new Object[][]{pt1,pt2}));
                }
                else if (nextIsType(Lparenth,Number,Comma,Number,Comma,Name,Rparenth,To,Lparenth,Number,Comma,Number,Rparenth)){
                    Object[] pt1 = getNextTouple();
                    Object[] pt2 = getNextTouple();
                    addCommand(new DoBezier(givenTokens[i].getLine(),new Object[][]{pt1,pt2}));
                }
                else if (nextIsType(Lparenth,Number,Comma,Name,Comma,Number,Rparenth,To,Lparenth,Number,Comma,Number,Rparenth)){
                    Object[] pt1 = getNextTouple();
                    Object[] pt2 = getNextTouple();
                    addCommand(new DoBezier(givenTokens[i].getLine(),new Object[][]{pt1,pt2}));
                }
                else if (nextIsType(Lparenth,Number,Comma,Number,Comma,Number,Rparenth,To,Lparenth,Number,Comma,Number,Rparenth)){
                    Object[] pt1 = getNextTouple();
                    Object[] pt2 = getNextTouple();
                    addCommand(new DoBezier(givenTokens[i].getLine(),new Object[][]{pt1,pt2}));
                }
                // (x,y,h) to (x,y,h)
                else if (nextIsType(Lparenth,Name,Comma,Name,Comma,Name,Rparenth,To,Lparenth,Name,Comma,Name,Comma,Name,Rparenth)){
                    Object[] pt1 = getNextTouple();
                    Object[] pt2 = getNextTouple();
                    addCommand(new DoBezier(givenTokens[i].getLine(),new Object[][]{pt1,pt2}));
                }
                else if (nextIsType(Lparenth,Name,Comma,Name,Comma,Name,Rparenth,To,Lparenth,Name,Comma,Name,Comma,Number,Rparenth)){
                    Object[] pt1 = getNextTouple();
                    Object[] pt2 = getNextTouple();
                    addCommand(new DoBezier(givenTokens[i].getLine(),new Object[][]{pt1,pt2}));
                }
                else if (nextIsType(Lparenth,Name,Comma,Name,Comma,Name,Rparenth,To,Lparenth,Name,Comma,Number,Comma,Name,Rparenth)){
                    Object[] pt1 = getNextTouple();
                    Object[] pt2 = getNextTouple();
                    addCommand(new DoBezier(givenTokens[i].getLine(),new Object[][]{pt1,pt2}));
                }
                else if (nextIsType(Lparenth,Name,Comma,Name,Comma,Name,Rparenth,To,Lparenth,Number,Comma,Name,Comma,Name,Rparenth)){
                    Object[] pt1 = getNextTouple();
                    Object[] pt2 = getNextTouple();
                    addCommand(new DoBezier(givenTokens[i].getLine(),new Object[][]{pt1,pt2}));
                }
                else if (nextIsType(Lparenth,Name,Comma,Name,Comma,Number,Rparenth,To,Lparenth,Name,Comma,Name,Comma,Name,Rparenth)){
                    Object[] pt1 = getNextTouple();
                    Object[] pt2 = getNextTouple();
                    addCommand(new DoBezier(givenTokens[i].getLine(),new Object[][]{pt1,pt2}));
                }
                else if (nextIsType(Lparenth,Name,Comma,Number,Comma,Name,Rparenth,To,Lparenth,Name,Comma,Name,Comma,Name,Rparenth)){
                    Object[] pt1 = getNextTouple();
                    Object[] pt2 = getNextTouple();
                    addCommand(new DoBezier(givenTokens[i].getLine(),new Object[][]{pt1,pt2}));
                }
                else if (nextIsType(Lparenth,Number,Comma,Name,Comma,Name,Rparenth,To,Lparenth,Name,Comma,Name,Comma,Name,Rparenth)){
                    Object[] pt1 = getNextTouple();
                    Object[] pt2 = getNextTouple();
                    addCommand(new DoBezier(givenTokens[i].getLine(),new Object[][]{pt1,pt2}));
                }
                else if (nextIsType(Lparenth,Name,Comma,Name,Comma,Name,Rparenth,To,Lparenth,Name,Comma,Number,Comma,Number,Rparenth)){
                    Object[] pt1 = getNextTouple();
                    Object[] pt2 = getNextTouple();
                    addCommand(new DoBezier(givenTokens[i].getLine(),new Object[][]{pt1,pt2}));
                }
                else if (nextIsType(Lparenth,Name,Comma,Name,Comma,Name,Rparenth,To,Lparenth,Number,Comma,Number,Comma,Name,Rparenth)){
                    Object[] pt1 = getNextTouple();
                    Object[] pt2 = getNextTouple();
                    addCommand(new DoBezier(givenTokens[i].getLine(),new Object[][]{pt1,pt2}));
                }
                else if (nextIsType(Lparenth,Name,Comma,Name,Comma,Number,Rparenth,To,Lparenth,Number,Comma,Name,Comma,Name,Rparenth)){
                    Object[] pt1 = getNextTouple();
                    Object[] pt2 = getNextTouple();
                    addCommand(new DoBezier(givenTokens[i].getLine(),new Object[][]{pt1,pt2}));
                }
                else if (nextIsType(Lparenth,Name,Comma,Number,Comma,Number,Rparenth,To,Lparenth,Name,Comma,Name,Comma,Name,Rparenth)){
                    Object[] pt1 = getNextTouple();
                    Object[] pt2 = getNextTouple();
                    addCommand(new DoBezier(givenTokens[i].getLine(),new Object[][]{pt1,pt2}));
                }
                else if (nextIsType(Lparenth,Number,Comma,Number,Comma,Name,Rparenth,To,Lparenth,Name,Comma,Name,Comma,Name,Rparenth)){
                    Object[] pt1 = getNextTouple();
                    Object[] pt2 = getNextTouple();
                    addCommand(new DoBezier(givenTokens[i].getLine(),new Object[][]{pt1,pt2}));
                }
                else if (nextIsType(Lparenth,Number,Comma,Name,Comma,Name,Rparenth,To,Lparenth,Name,Comma,Name,Comma,Number,Rparenth)){
                    Object[] pt1 = getNextTouple();
                    Object[] pt2 = getNextTouple();
                    addCommand(new DoBezier(givenTokens[i].getLine(),new Object[][]{pt1,pt2}));
                }
                else if (nextIsType(Lparenth,Name,Comma,Name,Comma,Name,Rparenth,To,Lparenth,Number,Comma,Number,Comma,Number,Rparenth)){
                    Object[] pt1 = getNextTouple();
                    Object[] pt2 = getNextTouple();
                    addCommand(new DoBezier(givenTokens[i].getLine(),new Object[][]{pt1,pt2}));
                }
                else if (nextIsType(Lparenth,Name,Comma,Name,Comma,Number,Rparenth,To,Lparenth,Number,Comma,Number,Comma,Name,Rparenth)){
                    Object[] pt1 = getNextTouple();
                    Object[] pt2 = getNextTouple();
                    addCommand(new DoBezier(givenTokens[i].getLine(),new Object[][]{pt1,pt2}));
                }
                else if (nextIsType(Lparenth,Name,Comma,Number,Comma,Number,Rparenth,To,Lparenth,Number,Comma,Name,Comma,Name,Rparenth)){
                    Object[] pt1 = getNextTouple();
                    Object[] pt2 = getNextTouple();
                    addCommand(new DoBezier(givenTokens[i].getLine(),new Object[][]{pt1,pt2}));
                }
                else if (nextIsType(Lparenth,Number,Comma,Number,Comma,Number,Rparenth,To,Lparenth,Name,Comma,Name,Comma,Name,Rparenth)){
                    Object[] pt1 = getNextTouple();
                    Object[] pt2 = getNextTouple();
                    addCommand(new DoBezier(givenTokens[i].getLine(),new Object[][]{pt1,pt2}));
                }
                else if (nextIsType(Lparenth,Number,Comma,Number,Comma,Name,Rparenth,To,Lparenth,Name,Comma,Name,Comma,Number,Rparenth)){
                    Object[] pt1 = getNextTouple();
                    Object[] pt2 = getNextTouple();
                    addCommand(new DoBezier(givenTokens[i].getLine(),new Object[][]{pt1,pt2}));
                }
                else if (nextIsType(Lparenth,Number,Comma,Name,Comma,Name,Rparenth,To,Lparenth,Name,Comma,Number,Comma,Number,Rparenth)){
                    Object[] pt1 = getNextTouple();
                    Object[] pt2 = getNextTouple();
                    addCommand(new DoBezier(givenTokens[i].getLine(),new Object[][]{pt1,pt2}));
                }
                else if (nextIsType(Lparenth,Name,Comma,Name,Comma,Number,Rparenth,To,Lparenth,Number,Comma,Number,Comma,Number,Rparenth)){
                    Object[] pt1 = getNextTouple();
                    Object[] pt2 = getNextTouple();
                    addCommand(new DoBezier(givenTokens[i].getLine(),new Object[][]{pt1,pt2}));
                }
                else if (nextIsType(Lparenth,Name,Comma,Number,Comma,Number,Rparenth,To,Lparenth,Number,Comma,Number,Comma,Name,Rparenth)){
                    Object[] pt1 = getNextTouple();
                    Object[] pt2 = getNextTouple();
                    addCommand(new DoBezier(givenTokens[i].getLine(),new Object[][]{pt1,pt2}));
                }
                else if (nextIsType(Lparenth,Number,Comma,Number,Comma,Number,Rparenth,To,Lparenth,Number,Comma,Name,Comma,Name,Rparenth)){
                    Object[] pt1 = getNextTouple();
                    Object[] pt2 = getNextTouple();
                    addCommand(new DoBezier(givenTokens[i].getLine(),new Object[][]{pt1,pt2}));
                }
                else if (nextIsType(Lparenth,Number,Comma,Number,Comma,Number,Rparenth,To,Lparenth,Name,Comma,Name,Comma,Number,Rparenth)){
                    Object[] pt1 = getNextTouple();
                    Object[] pt2 = getNextTouple();
                    addCommand(new DoBezier(givenTokens[i].getLine(),new Object[][]{pt1,pt2}));
                }
                else if (nextIsType(Lparenth,Number,Comma,Number,Comma,Name,Rparenth,To,Lparenth,Name,Comma,Number,Comma,Number,Rparenth)){
                    Object[] pt1 = getNextTouple();
                    Object[] pt2 = getNextTouple();
                    addCommand(new DoBezier(givenTokens[i].getLine(),new Object[][]{pt1,pt2}));
                }
                else if (nextIsType(Lparenth,Number,Comma,Name,Comma,Name,Rparenth,To,Lparenth,Number,Comma,Number,Comma,Number,Rparenth)){
                    Object[] pt1 = getNextTouple();
                    Object[] pt2 = getNextTouple();
                    addCommand(new DoBezier(givenTokens[i].getLine(),new Object[][]{pt1,pt2}));
                }
                else if (nextIsType(Lparenth,Name,Comma,Number,Comma,Number,Rparenth,To,Lparenth,Number,Comma,Number,Comma,Number,Rparenth)){
                    Object[] pt1 = getNextTouple();
                    Object[] pt2 = getNextTouple();
                    addCommand(new DoBezier(givenTokens[i].getLine(),new Object[][]{pt1,pt2}));
                }
                else if (nextIsType(Lparenth,Number,Comma,Number,Comma,Number,Rparenth,To,Lparenth,Number,Comma,Number,Comma,Name,Rparenth)){
                    Object[] pt1 = getNextTouple();
                    Object[] pt2 = getNextTouple();
                    addCommand(new DoBezier(givenTokens[i].getLine(),new Object[][]{pt1,pt2}));
                }
                else if (nextIsType(Lparenth,Number,Comma,Number,Comma,Number,Rparenth,To,Lparenth,Number,Comma,Name,Comma,Number,Rparenth)){
                    Object[] pt1 = getNextTouple();
                    Object[] pt2 = getNextTouple();
                    addCommand(new DoBezier(givenTokens[i].getLine(),new Object[][]{pt1,pt2}));
                }
                else if (nextIsType(Lparenth,Number,Comma,Number,Comma,Number,Rparenth,To,Lparenth,Name,Comma,Number,Comma,Number,Rparenth)){
                    Object[] pt1 = getNextTouple();
                    Object[] pt2 = getNextTouple();
                    addCommand(new DoBezier(givenTokens[i].getLine(),new Object[][]{pt1,pt2}));
                }
                else if (nextIsType(Lparenth,Number,Comma,Number,Comma,Name,Rparenth,To,Lparenth,Number,Comma,Number,Comma,Number,Rparenth)){
                    Object[] pt1 = getNextTouple();
                    Object[] pt2 = getNextTouple();
                    addCommand(new DoBezier(givenTokens[i].getLine(),new Object[][]{pt1,pt2}));
                }
                else if (nextIsType(Lparenth,Number,Comma,Name,Comma,Number,Rparenth,To,Lparenth,Number,Comma,Number,Comma,Number,Rparenth)){
                    Object[] pt1 = getNextTouple();
                    Object[] pt2 = getNextTouple();
                    addCommand(new DoBezier(givenTokens[i].getLine(),new Object[][]{pt1,pt2}));
                }
                else if (nextIsType(Lparenth,Number,Comma,Number,Comma,Number,Rparenth,To,Lparenth,Number,Comma,Number,Comma,Number,Rparenth)){
                    Object[] pt1 = getNextTouple();
                    Object[] pt2 = getNextTouple();
                    addCommand(new DoBezier(givenTokens[i].getLine(),new Object[][]{pt1,pt2}));
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
}
