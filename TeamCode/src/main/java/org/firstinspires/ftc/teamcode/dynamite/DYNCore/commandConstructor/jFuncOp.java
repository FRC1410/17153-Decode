package org.firstinspires.ftc.teamcode.dynamite.DYNCore.commandConstructor;

import static org.firstinspires.ftc.teamcode.dynamite.DYNCore.tokenizer.TokenTypes.Name;
import static org.firstinspires.ftc.teamcode.dynamite.DYNCore.tokenizer.TokenTypes.String;
import static org.firstinspires.ftc.teamcode.dynamite.DYNCore.tokenizer.TokenTypes.To;

import org.firstinspires.ftc.teamcode.dynamite.DYNCore.commands.function.jFunc;

class jFuncOp extends ConstructorUtils{
    public static void Do(){
        if (nextIsType(String)||nextIsType(Name)) {
            i++;
            String funcID = (String)givenTokens[i].getValue();
            if (nextIsType(Name)) {
                i++;
                String name = (String) givenTokens[i].getValue();
                if (nextIsType(To, Name)) {
                    i += 2;
                    String out = (String) givenTokens[i].getValue();
                    addCommand(new jFunc(getLine(), funcID, name, out));
                    i++;
                } else if (nextIsType(To)) {
                    i += 2;
                    throwError("Expected name | Got: " + givenTokens[i].type());
                } else {
                    addCommand(new jFunc(getLine(), funcID, name, true));
                    i++;
                }
            } else if (nextIsType(To, Name)) {
                i += 2;
                String out = (String)givenTokens[i].getValue();
                addCommand(new jFunc(getLine(), funcID, out, false));
                i++;
            } else {
                // no ins or outs
                addCommand(new jFunc(getLine(),funcID));
                i++;
            }
        } else {
            i++;
            throwError("Expected string | Got: "+givenTokens[i].type());
        }
    }
}
