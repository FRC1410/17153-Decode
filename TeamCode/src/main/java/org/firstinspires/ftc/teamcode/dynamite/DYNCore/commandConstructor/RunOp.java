package org.firstinspires.ftc.teamcode.dynamite.DYNCore.commandConstructor;

import static org.firstinspires.ftc.teamcode.dynamite.DYNCore.tokenizer.TokenTypes.Name;

import org.firstinspires.ftc.teamcode.dynamite.DYNCore.commands.function.RunPath;

class RunOp extends ConstructorUtils{
    public static void Do(){
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
}
