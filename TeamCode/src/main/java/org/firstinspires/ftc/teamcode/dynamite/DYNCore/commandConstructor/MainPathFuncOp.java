package org.firstinspires.ftc.teamcode.dynamite.DYNCore.commandConstructor;

import static org.firstinspires.ftc.teamcode.dynamite.DYNCore.tokenizer.TokenTypes.Name;

class MainPathFuncOp extends ConstructorUtils{
    public static void Do(){
        if (nextIsType(Name)){
            mainFuncName = (String)givenTokens[i+1].getValue();
            i+=2;
        } else {
            i++;
            throwError("Expected name | Got: "+givenTokens[i].type());
        }
    }
}
