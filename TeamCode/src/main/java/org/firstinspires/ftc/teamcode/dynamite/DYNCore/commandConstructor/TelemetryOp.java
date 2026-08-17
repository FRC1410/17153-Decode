package org.firstinspires.ftc.teamcode.dynamite.DYNCore.commandConstructor;

import static org.firstinspires.ftc.teamcode.dynamite.DYNCore.tokenizer.TokenTypes.Boolean;
import static org.firstinspires.ftc.teamcode.dynamite.DYNCore.tokenizer.TokenTypes.Name;
import static org.firstinspires.ftc.teamcode.dynamite.DYNCore.tokenizer.TokenTypes.Number;
import static org.firstinspires.ftc.teamcode.dynamite.DYNCore.tokenizer.TokenTypes.String;

import org.firstinspires.ftc.teamcode.dynamite.DYNCore.commands.telemetry.AddData;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.commands.telemetry.Clear;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.commands.telemetry.Update;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.tokenizer.Token;

class TelemetryOp extends ConstructorUtils{
    public static void Do(){
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
}
