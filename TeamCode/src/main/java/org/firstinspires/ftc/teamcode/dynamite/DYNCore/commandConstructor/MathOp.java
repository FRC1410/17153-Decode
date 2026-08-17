// Call me Linus Torvalds the way I be hatin' on these indentations.
package org.firstinspires.ftc.teamcode.dynamite.DYNCore.commandConstructor;

import static org.firstinspires.ftc.teamcode.dynamite.DYNCore.tokenizer.TokenTypes.Name;
import static org.firstinspires.ftc.teamcode.dynamite.DYNCore.tokenizer.TokenTypes.Number;
import static org.firstinspires.ftc.teamcode.dynamite.DYNCore.tokenizer.TokenTypes.To;

import org.firstinspires.ftc.teamcode.dynamite.DYNCore.commands.math.arithmetic.Add;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.commands.math.arithmetic.Decrement;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.commands.math.arithmetic.Div;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.commands.math.arithmetic.Increment;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.commands.math.arithmetic.Mux;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.commands.math.arithmetic.Pow;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.commands.math.arithmetic.Sqrt;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.commands.math.arithmetic.Sub;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.commands.math.trig.Cos;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.commands.math.trig.Sin;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.commands.math.trig.Tan;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.commands.math.trig.ToDeg;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.commands.math.trig.ToRad;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.commands.math.trig.iCos;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.commands.math.trig.iSin;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.commands.math.trig.iTan;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.tokenizer.Token;

class MathOp extends ConstructorUtils{
    public static void Do(){
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
                                i++; // step to problematic token
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
                if (nextIsType(Name,To,Name)) {
                    i++;
                    String inVar = (String)givenTokens[i].getValue();
                    String outVar = (String)givenTokens[i+2].getValue();
                    addCommand(new ToRad(getLine(), inVar,outVar));
                    i+=3;
                } else if (nextIsType(Number,To,Name)){
                    i++;
                    double inVar = (double)givenTokens[i].getValue();
                    String outVar = (String)givenTokens[i+2].getValue();
                    addCommand(new ToRad(getLine(), inVar,outVar));
                    i+=3;
                } else if (nextIsType(Name)){
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
                if (nextIsType(Name,To,Name)) {
                    i++;
                    String inVar = (String)givenTokens[i].getValue();
                    String outVar = (String)givenTokens[i+2].getValue();
                    addCommand(new ToDeg(getLine(), inVar,outVar));
                    i+=3;
                } else if (nextIsType(Number,To,Name)){
                    i++;
                    double inVar = (double)givenTokens[i].getValue();
                    String outVar = (String)givenTokens[i+2].getValue();
                    addCommand(new ToDeg(getLine(), inVar,outVar));
                    i+=3;
                } else if (nextIsType(Name)){
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
}
