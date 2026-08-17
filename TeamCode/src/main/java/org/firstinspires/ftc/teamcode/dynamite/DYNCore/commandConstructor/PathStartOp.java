package org.firstinspires.ftc.teamcode.dynamite.DYNCore.commandConstructor;

import static org.firstinspires.ftc.teamcode.dynamite.DYNCore.tokenizer.TokenTypes.Name;

import org.firstinspires.ftc.teamcode.dynamite.DYNCore.commands.movement.SetStartPose;

class PathStartOp extends ConstructorUtils{
    public static void Do(){
        if (nextIsType(Name)){
            i++;
            String name = (String)givenTokens[i].getValue();
            addCommand(new SetStartPose(getLine(),name));
            i++;
        } else {
            i++;
            throwError("Expected name | Got: "+givenTokens[i].type());
        }
    }
}
