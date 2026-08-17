package org.firstinspires.ftc.teamcode.dynamite.DYNCore.commandConstructor;

import static org.firstinspires.ftc.teamcode.dynamite.DYNCore.tokenizer.TokenTypes.Boolean;
import static org.firstinspires.ftc.teamcode.dynamite.DYNCore.tokenizer.TokenTypes.Comma;
import static org.firstinspires.ftc.teamcode.dynamite.DYNCore.tokenizer.TokenTypes.Lparenth;
import static org.firstinspires.ftc.teamcode.dynamite.DYNCore.tokenizer.TokenTypes.Name;
import static org.firstinspires.ftc.teamcode.dynamite.DYNCore.tokenizer.TokenTypes.Number;
import static org.firstinspires.ftc.teamcode.dynamite.DYNCore.tokenizer.TokenTypes.Rparenth;
import static org.firstinspires.ftc.teamcode.dynamite.DYNCore.tokenizer.TokenTypes.String;

import org.firstinspires.ftc.teamcode.dynamite.DYNCore.CommandException;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.commands.Command;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.commands.controlFlow.Condition;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.commands.function.DynPath;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.variables.primitives.DynBoolean;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.variables.primitives.DynNumber;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.variables.primitives.DynString;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.tokenizer.Token;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.tokenizer.TokenTypes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

class ConstructorUtils {
    protected static String mainFuncName = "Main";
    protected static final ArrayList<Command> depthTracker = new ArrayList<>();
    protected static Token[] givenTokens;
    protected static final ArrayList<Command> finalCommands = new ArrayList<>();
    protected static final Map<String, DynPath> funcIDmap = new HashMap<>();
    static int i = 0;

    protected static void addCommand(Command c){
        if (!depthTracker.isEmpty()) {
            depthTracker.get(depthTracker.size() - 1).addCommand(c);
        } else {
            throwError("Cannot have commands outside of functions!");
        }
    }

    protected static int getLine(){
        return givenTokens[i].getLine();
    }

    protected static Condition processCondition(){
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
    protected static Object[] processJsonChunk(){
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

    protected static boolean nextIsType(TokenTypes type1){
        if (i+1 >= givenTokens.length) throwError("Expected a finished command | Given an incomplete command.");
        return isTokenType(i+1,type1);
    }
    protected static boolean nextIsType(TokenTypes type1, TokenTypes type2){
        if (i+2 >= givenTokens.length) throwError("Expected a finished command | Given an incomplete command.");
        return isTokenType(i+1,type1)&&isTokenType(i+2,type2);
    }
    protected static boolean nextIsType(TokenTypes type1, TokenTypes type2, TokenTypes type3){
        if (i+3 >= givenTokens.length) throwError("Expected a finished command | Given an incomplete command.");
        return isTokenType(i+1,type1)&&isTokenType(i+2,type2)&&isTokenType(i+3,type3);
    }
    protected static boolean nextIsType(TokenTypes type1, TokenTypes type2, TokenTypes type3, TokenTypes type4){
        if (i+4 >= givenTokens.length) throwError("Expected a finished command | Given an incomplete command.");
        return isTokenType(i+1,type1)&&isTokenType(i+2,type2)&&isTokenType(i+3,type3)&&isTokenType(i+4,type4);
    }
    protected static boolean nextIsType(TokenTypes type1, TokenTypes type2, TokenTypes type3, TokenTypes type4, TokenTypes type5){
        if (i+5 >= givenTokens.length) throwError("Expected a finished command | Given an incomplete command.");
        return isTokenType(i+1,type1)&&isTokenType(i+2,type2)&&isTokenType(i+3,type3)&&isTokenType(i+4,type4)&&isTokenType(i+5,type5);
    }
    protected static boolean nextIsType(TokenTypes type1, TokenTypes type2, TokenTypes type3, TokenTypes type4, TokenTypes type5, TokenTypes type6){
        if (i+6 >= givenTokens.length) throwError("Expected a finished command | Given an incomplete command.");
        return isTokenType(i+1,type1)&&isTokenType(i+2,type2)&&isTokenType(i+3,type3)&&isTokenType(i+4,type4)&&isTokenType(i+5,type5)&&isTokenType(i+6,type6);
    }
    protected static boolean nextIsType(TokenTypes type1, TokenTypes type2, TokenTypes type3, TokenTypes type4, TokenTypes type5, TokenTypes type6, TokenTypes type7){
        if (i+7 >= givenTokens.length) throwError("Expected a finished command | Given an incomplete command.");
        return isTokenType(i+1,type1)&&isTokenType(i+2,type2)&&isTokenType(i+3,type3)&&isTokenType(i+4,type4)&&isTokenType(i+5,type5)&&isTokenType(i+6,type6)&&isTokenType(i+7,type7);
    }
    protected static boolean nextIsType(TokenTypes type1, TokenTypes type2, TokenTypes type3, TokenTypes type4, TokenTypes type5, TokenTypes type6, TokenTypes type7, TokenTypes type8){
        if (i+8 >= givenTokens.length) throwError("Expected a finished command | Given an incomplete command.");
        return isTokenType(i+1,type1)&&isTokenType(i+2,type2)&&isTokenType(i+3,type3)&&isTokenType(i+4,type4)&&isTokenType(i+5,type5)&&isTokenType(i+6,type6)&&isTokenType(i+7,type7)&&isTokenType(i+8,type8);
    }
    protected static boolean nextIsType(TokenTypes type1, TokenTypes type2, TokenTypes type3, TokenTypes type4, TokenTypes type5, TokenTypes type6, TokenTypes type7, TokenTypes type8, TokenTypes type9){
        if (i+9 >= givenTokens.length) throwError("Expected a finished command | Given an incomplete command.");
        return isTokenType(i+1,type1)&&isTokenType(i+2,type2)&&isTokenType(i+3,type3)&&isTokenType(i+4,type4)&&isTokenType(i+5,type5)&&isTokenType(i+6,type6)&&isTokenType(i+7,type7)&&isTokenType(i+8,type8)&&isTokenType(i+9,type9);
    }
    protected static boolean nextIsType(TokenTypes type1, TokenTypes type2, TokenTypes type3, TokenTypes type4, TokenTypes type5, TokenTypes type6, TokenTypes type7, TokenTypes type8, TokenTypes type9, TokenTypes type10){
        if (i+10 >= givenTokens.length) throwError("Expected a finished command | Given an incomplete command.");
        return isTokenType(i+1,type1)&&isTokenType(i+2,type2)&&isTokenType(i+3,type3)&&isTokenType(i+4,type4)&&isTokenType(i+5,type5)&&isTokenType(i+6,type6)&&isTokenType(i+7,type7)&&isTokenType(i+8,type8)&&isTokenType(i+9,type9)&&isTokenType(i+10,type10);
    }
    protected static boolean nextIsType(TokenTypes type1, TokenTypes type2, TokenTypes type3, TokenTypes type4, TokenTypes type5, TokenTypes type6, TokenTypes type7, TokenTypes type8, TokenTypes type9, TokenTypes type10, TokenTypes type11){
        if (i+11 >= givenTokens.length) throwError("Expected a finished command | Given an incomplete command.");
        return isTokenType(i+1,type1)&&isTokenType(i+2,type2)&&isTokenType(i+3,type3)&&isTokenType(i+4,type4)&&isTokenType(i+5,type5)&&isTokenType(i+6,type6)&&isTokenType(i+7,type7)&&isTokenType(i+8,type8)&&isTokenType(i+9,type9)&&isTokenType(i+10,type10)&&isTokenType(i+11,type11);
    }
    protected static boolean nextIsType(TokenTypes type1, TokenTypes type2, TokenTypes type3, TokenTypes type4, TokenTypes type5, TokenTypes type6, TokenTypes type7, TokenTypes type8, TokenTypes type9, TokenTypes type10, TokenTypes type11, TokenTypes type12){
        if (i+11 >= givenTokens.length) throwError("Expected a finished command | Given an incomplete command.");
        return isTokenType(i+1,type1)&&isTokenType(i+2,type2)&&isTokenType(i+3,type3)&&isTokenType(i+4,type4)&&isTokenType(i+5,type5)&&isTokenType(i+6,type6)&&isTokenType(i+7,type7)&&isTokenType(i+8,type8)&&isTokenType(i+9,type9)&&isTokenType(i+10,type10)&&isTokenType(i+11,type11)&&isTokenType(i+12,type12);
    }
    protected static boolean nextIsType(TokenTypes type1, TokenTypes type2, TokenTypes type3, TokenTypes type4, TokenTypes type5, TokenTypes type6, TokenTypes type7, TokenTypes type8, TokenTypes type9, TokenTypes type10, TokenTypes type11, TokenTypes type12, TokenTypes type13){
        if (i+11 >= givenTokens.length) throwError("Expected a finished command | Given an incomplete command.");
        return isTokenType(i+1,type1)&&isTokenType(i+2,type2)&&isTokenType(i+3,type3)&&isTokenType(i+4,type4)&&isTokenType(i+5,type5)&&isTokenType(i+6,type6)&&isTokenType(i+7,type7)&&isTokenType(i+8,type8)&&isTokenType(i+9,type9)&&isTokenType(i+10,type10)&&isTokenType(i+11,type11)&&isTokenType(i+12,type12)&&isTokenType(i+13,type13);
    }
    protected static boolean nextIsType(TokenTypes type1, TokenTypes type2, TokenTypes type3, TokenTypes type4, TokenTypes type5, TokenTypes type6, TokenTypes type7, TokenTypes type8, TokenTypes type9, TokenTypes type10, TokenTypes type11, TokenTypes type12, TokenTypes type13, TokenTypes type14){
        if (i+11 >= givenTokens.length) throwError("Expected a finished command | Given an incomplete command.");
        return isTokenType(i+1,type1)&&isTokenType(i+2,type2)&&isTokenType(i+3,type3)&&isTokenType(i+4,type4)&&isTokenType(i+5,type5)&&isTokenType(i+6,type6)&&isTokenType(i+7,type7)&&isTokenType(i+8,type8)&&isTokenType(i+9,type9)&&isTokenType(i+10,type10)&&isTokenType(i+11,type11)&&isTokenType(i+12,type12)&&isTokenType(i+13,type13)&&isTokenType(i+14,type14);
    }
    protected static boolean nextIsType(TokenTypes type1, TokenTypes type2, TokenTypes type3, TokenTypes type4, TokenTypes type5, TokenTypes type6, TokenTypes type7, TokenTypes type8, TokenTypes type9, TokenTypes type10, TokenTypes type11, TokenTypes type12, TokenTypes type13, TokenTypes type14, TokenTypes type15){
        if (i+11 >= givenTokens.length) throwError("Expected a finished command | Given an incomplete command.");
        return isTokenType(i+1,type1)&&isTokenType(i+2,type2)&&isTokenType(i+3,type3)&&isTokenType(i+4,type4)&&isTokenType(i+5,type5)&&isTokenType(i+6,type6)&&isTokenType(i+7,type7)&&isTokenType(i+8,type8)&&isTokenType(i+9,type9)&&isTokenType(i+10,type10)&&isTokenType(i+11,type11)&&isTokenType(i+12,type12)&&isTokenType(i+13,type13)&&isTokenType(i+14,type14)&&isTokenType(i+15,type15);
    }
    protected static boolean isTokenType(int i, TokenTypes type){
        return givenTokens[i].type()==type;
    }

    protected static Object[] getNextTouple(){ // literally stealing form python
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

    protected static void throwError(String reason){
        Token current = givenTokens[i];
        int line = current.getLine();
        int column = current.getColumn();
        throw new CommandException(line,column,reason);
    }
}
