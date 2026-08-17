package org.firstinspires.ftc.teamcode.dynamite.DYNCore.commandConstructor;

import static org.firstinspires.ftc.teamcode.dynamite.DYNCore.tokenizer.TokenTypes.Boolean;
import static org.firstinspires.ftc.teamcode.dynamite.DYNCore.tokenizer.TokenTypes.Name;
import static org.firstinspires.ftc.teamcode.dynamite.DYNCore.tokenizer.TokenTypes.Number;
import static org.firstinspires.ftc.teamcode.dynamite.DYNCore.tokenizer.TokenTypes.String;

import org.firstinspires.ftc.teamcode.dynamite.DYNCore.commands.util.HardQuit;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.commands.util.Quit;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.commands.util.Sleep;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.variables.primitives.DynString;

class ExtraOps extends ConstructorUtils{
    public static void Do(){
        switch (givenTokens[i].type()) {
            case Sleep -> {
                if (nextIsType(Name)) {
                    i++;
                    String name = (String) givenTokens[i].getValue();
                    addCommand(new Sleep(getLine(), name));
                    i++;
                } else if (nextIsType(Number)) {
                    i++;
                    double time = (double) givenTokens[i].getValue();
                    addCommand(new Sleep(getLine(), time));
                    i++;
                } else {
                    i++;
                    throwError("Expected name/number | Got: " + givenTokens[i].type());
                }
            }
            case Quit -> {
                if (nextIsType(Name)){
                    addCommand(new Quit(getLine(),(String)givenTokens[i+1].getValue()));
                    i+=2;
                } else if (nextIsType(Number)){
                    addCommand(new Quit(getLine(),(double)givenTokens[i+1].getValue()));
                    i+=2;
                } else if (nextIsType(String)){
                    addCommand(new Quit(getLine(),new DynString((String)givenTokens[i+1].getValue())));
                    i+=2;
                } else if (nextIsType(Boolean)){
                    addCommand(new Quit(getLine(),java.lang.String.valueOf((boolean)givenTokens[i+1].getValue())));
                    i+=2;
                }
            }
            case HardQuit -> {
                if (nextIsType(Name)){
                    addCommand(new HardQuit(getLine(),(String)givenTokens[i+1].getValue()));
                    i+=2;
                } else if (nextIsType(Number)){
                    addCommand(new HardQuit(getLine(),(double)givenTokens[i+1].getValue()));
                    i+=2;
                } else if (nextIsType(String)){
                    addCommand(new HardQuit(getLine(),new DynString((String)givenTokens[i+1].getValue())));
                    i+=2;
                } else if (nextIsType(Boolean)){
                    addCommand(new HardQuit(getLine(),java.lang.String.valueOf((boolean)givenTokens[i+1].getValue())));
                    i+=2;
                }
            }
        }
    }
}
