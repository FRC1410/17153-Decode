package org.firstinspires.ftc.teamcode.dynamite.DYNCore.commandConstructor;

import static org.firstinspires.ftc.teamcode.dynamite.DYNCore.tokenizer.TokenTypes.Boolean;
import static org.firstinspires.ftc.teamcode.dynamite.DYNCore.tokenizer.TokenTypes.Lparenth;
import static org.firstinspires.ftc.teamcode.dynamite.DYNCore.tokenizer.TokenTypes.Name;
import static org.firstinspires.ftc.teamcode.dynamite.DYNCore.tokenizer.TokenTypes.PathStartPos;
import static org.firstinspires.ftc.teamcode.dynamite.DYNCore.tokenizer.TokenTypes.Start;
import static org.firstinspires.ftc.teamcode.dynamite.DYNCore.tokenizer.TokenTypes.To;

import org.firstinspires.ftc.teamcode.dynamite.DYNCore.commands.controlFlow.Condition;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.commands.controlFlow.For;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.commands.controlFlow.If;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.commands.controlFlow.While;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.commands.function.DynPath;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.tokenizer.Token;

class FuncLoopIfOp extends ConstructorUtils{
    public static void Do(){
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
                    if (nextIsType(Start)){
                        i+=2;
                    } else {
                        i++;
                        throwError("Expected start | Got: "+givenTokens[i].type());
                    }
                } else if (nextIsType(Lparenth)){
                    Token tk = givenTokens[i];
                    Condition con = processCondition();
                    depthTracker.add(new While(tk.getLine(),con));
                    if (nextIsType(Start)){
                        i+=2;
                    } else {
                        i++;
                        throwError("Expected start | Got: "+givenTokens[i].type());
                    }
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
                                throwError("Expected start | Got: "+givenTokens[i].type());
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
                if (nextIsType(Start)) {
                    depthTracker.add(new If(getLine(), con));
                    i += 2;
                } else {
                    i++;
                    throwError("Expected start | Got: "+givenTokens[i].type());
                }
            }
        }
    }
}
