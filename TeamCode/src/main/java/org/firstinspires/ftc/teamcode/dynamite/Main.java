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
    private static FileReader fr;
    private static String ScriptContents;
    private static Tokenizer tk;
    public static void main(String[] args) {
        // scanner (just cus)
        Scanner scanner = new Scanner(System.in);
        // we now testing the file reader

        // startup the scanner
        //print("Path to scrips (leave empty for default): ");
        String response = "";//scanner.nextLine();
        if (response != null) {
            if (!response.isEmpty()) {
                fr = new FileReader(response);
            } else {
                fr = new FileReader();
            }
        } else {
            fr = new FileReader();
        }

        // select the file, and start parsing
        //print("script name (leave empty for default): ");
        response = "";//scanner.nextLine();
        if (response != null){
            if (!response.isEmpty()){
                ScriptContents = fr.readFile(response);
            } else {
                ScriptContents = fr.readFile("Test_claude");
            }
        } else {
            ScriptContents = fr.readFile("Test_claude");
        }
        CommandException.linkFile(ScriptContents);
        //println("Read Script: ");
        //println(ScriptContents);
        // test tokenizer
        tk = new Tokenizer();
        Token[] tokens = tk.processScript(ScriptContents);
        for (Token tk : tokens){
            println(tk.toString());
        }
        println("");
        CommandConstructor cc = new CommandConstructor(ScriptContents);
        cc.processTokens(tokens);
        println(cc.toString());
        CommandRunner cr = new CommandRunner(cc);
        cr.linkUpCommand();
        cr.run();
    }
    private static void println(Object toBePrinted){
        //System.out.println(toBePrinted);
    }
    private static void print(Object toBePrinted){
        System.out.print(toBePrinted);
    }
}