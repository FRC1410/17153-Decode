package org.firstinspires.ftc.teamcode.dynamite.DYNCore.commandConstructor;

import static org.firstinspires.ftc.teamcode.dynamite.DYNCore.tokenizer.TokenTypes.Boolean;
import static org.firstinspires.ftc.teamcode.dynamite.DYNCore.tokenizer.TokenTypes.Name;
import static org.firstinspires.ftc.teamcode.dynamite.DYNCore.tokenizer.TokenTypes.Number;
import static org.firstinspires.ftc.teamcode.dynamite.DYNCore.tokenizer.TokenTypes.String;
import static org.firstinspires.ftc.teamcode.dynamite.DYNCore.tokenizer.TokenTypes.To;

import org.firstinspires.ftc.teamcode.dynamite.DYNCore.commands.variables.Append;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.commands.variables.Get;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.commands.variables.Insert;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.commands.variables.Remove;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.commands.variables.Set;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.tokenizer.Token;

class ListJsonOp extends ConstructorUtils{
    public static void Do(){
        Token current = givenTokens[i];
        switch (current.type()){
            case Append -> {
                if (givenTokens[i+2].type() == To){
                    Token in = givenTokens[i+1];
                    if (givenTokens[i+3].type() == Name){
                        String out = (String)givenTokens[i+3].getValue();
                        addCommand(new Append(in.getLine(),in,out));
                        i+=4;
                    } else {
                        throwError("Must use variable as command output!");
                    }
                } else if (givenTokens[i+3].type() == To){
                    Token in1 = givenTokens[i+1];
                    Token in2 = givenTokens[i+2];
                    if (givenTokens[i+4].type() == Name){
                        String out = (String)givenTokens[i+4].getValue();
                        addCommand(new Append(in1.getLine(), in1,in2, out));
                        i+=5;
                    } else {
                        throwError("Must use variable as command output!");
                    }
                } else {
                    throwError("Improperly formatted command!");
                }
            }
            case Insert -> {
                if (givenTokens[i+3].type() == To){
                    Token in1 = givenTokens[i+1];
                    Token in2 = givenTokens[i+2];
                    if (givenTokens[i+4].type() == Name){
                        String out = (String)givenTokens[i+4].getValue();
                        addCommand(new Insert(in1.getLine(),in1,in2,out));
                        i+=5;
                    } else {
                        throwError("Must use variable as command output!");
                    }
                } else {
                    throwError("Improperly formatted command!");
                }
            }
            case Remove -> {
                if (givenTokens[i+3].type() == To){
                    Token in1 = givenTokens[i+1];
                    String in2 = "";
                    if (givenTokens[i+2].type() == Name){
                        in2 = (String)givenTokens[i+2].getValue();
                    } else {
                        throwError("Must use variable as command target!");
                    }
                    if (givenTokens[i+4].type() == Name){
                        String out = (String)givenTokens[i+4].getValue();
                        addCommand(new Remove(in1.getLine(), in1,in2, out));
                        i+=5;
                    } else {
                        throwError("Must use variable as command output!");
                    }
                } else if (nextIsType(Boolean,Name)){
                    Token in = givenTokens[i+1];
                    String out = (String)givenTokens[i+2].getValue();
                    addCommand(new Remove(in.getLine(), in, out));
                    i+=3;
                } else if (nextIsType(Number,Name)){
                    Token in = givenTokens[i+1];
                    String out = (String)givenTokens[i+2].getValue();
                    addCommand(new Remove(in.getLine(), in, out));
                    i+=3;
                } else if (nextIsType(String,Name)){
                    Token in = givenTokens[i+1];
                    String out = (String)givenTokens[i+2].getValue();
                    addCommand(new Remove(in.getLine(), in, out));
                    i+=3;
                } else if (nextIsType(Name,Name)){
                    Token in = givenTokens[i+1];
                    String out = (String)givenTokens[i+2].getValue();
                    addCommand(new Remove(in.getLine(), in, out));
                    i+=3;
                } else {
                    throwError("Improperly formatted command!");
                }
            }
            case Get -> {
                String in = "";
                if (givenTokens[i+1].type() == Name){
                    in = (String)givenTokens[i+1].getValue();
                } else {
                    throwError("Must use variable for command input!");
                }
                Token in1 = givenTokens[i+2];
                if (givenTokens[i+3].type() == To) {
                    if (givenTokens[i+4].type() == Name) {
                        String out = (String) givenTokens[i+4].getValue();
                        addCommand(new Get(in1.getLine(), in, in1, out));
                        i+=5;
                    } else {
                        throwError("Must ise variable for command output!");
                    }
                } else {
                    throwError("Improperly formatted command!");
                }
            }
            case Set -> {
                Token in1 = givenTokens[i+1];
                Token in2 = givenTokens[i+2];
                if (givenTokens[i+3].type() == To){
                    if (givenTokens[i+4].type() == Name){
                        String out = (String)givenTokens[i+4].getValue();
                        addCommand(new Set(in1.getLine(), in1,in2, out));
                        i+=5;
                    } else {
                        throwError("Must use variable as command output!");
                    }
                } else {
                    throwError("Improperly formatted command!");
                }
            }
            default -> throw new IllegalStateException("Unexpected value: " + current.type());
        }
    }
}
