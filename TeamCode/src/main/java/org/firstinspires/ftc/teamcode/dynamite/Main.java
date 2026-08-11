package org.firstinspires.ftc.teamcode.dynamite;

import org.firstinspires.ftc.teamcode.dynamite.Tokenizer.Token;
import org.firstinspires.ftc.teamcode.dynamite.Tokenizer.Tokenizer;
import org.firstinspires.ftc.teamcode.dynamite.commandSequencer.CommandConstructor;
import org.firstinspires.ftc.teamcode.dynamite.commandSequencer.CommandException;
import org.firstinspires.ftc.teamcode.dynamite.commandSequencer.CommandRunner;

import java.util.Scanner;


//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    private static Tokenizer tk;
    public static void main(String[] args) {
        // load the script
        FileReader fr = new FileReader();
        String ScriptContents = fr.readFile("Main.dyn");
        // link up the exceptions
        CommandException.linkFile(ScriptContents);
        // Tokenize
        tk = new Tokenizer();
        Token[] tokens = tk.processScript(ScriptContents);
        // Construct the commands
        CommandConstructor cc = new CommandConstructor(ScriptContents);
        cc.processTokens(tokens);
        // Run the commands
        CommandRunner cr = new CommandRunner(cc);
        cr.linkUpCommand();
        cr.run();
    }
}