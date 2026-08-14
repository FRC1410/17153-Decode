package org.firstinspires.ftc.teamcode.dynamite.FTCInterface;

import org.firstinspires.ftc.teamcode.dynamite.DYNCore.commandSequencer.variables.Variable;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.tokenizer.Token;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.tokenizer.Tokenizer;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.commandSequencer.CommandConstructor;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.commandSequencer.CommandException;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.commandSequencer.CommandRunner;
import org.firstinspires.ftc.teamcode.dynamite.DYNCore.commandSequencer.Commands.Command;

// Takes the already constructed FTCInterface
// manages the execution of DYN commands
public class DYNInterpreter {
    private final FTCInterface ftcInterface;
    public DYNInterpreter(FTCInterface ftcInterface){
        this.ftcInterface = ftcInterface;
    }
    private boolean loadFromUSB = false;
    public void loadFromUSB(){
        loadFromUSB = true;
    }
    public void setScriptPath(String path){
        scriptPath = path;
    }
    DYNFileReader fr;
    String scriptPath;
    String scriptContents;
    Tokenizer tk;
    CommandConstructor cc;
    Token[] tokens;
    CommandRunner cr;
    public void init(){
        fr = new DYNFileReader(loadFromUSB,ftcInterface.getHardwareMap());
        Command.linkInterface(ftcInterface);
        loadScript();
        tokenize();
        constructCommands();
    }
    private void loadScript(){
        scriptContents = fr.readFile(scriptPath);
        CommandException.linkFile(scriptContents);
    }
    private void tokenize(){
        tk = new Tokenizer();
        tokens = tk.processScript(scriptContents);
    }
    private void constructCommands(){
        cc = new CommandConstructor(scriptContents);
        cc.processTokens(tokens);
        cr = new CommandRunner(cc);
        cr.linkUpCommand();
    }
    public String runScript(){
        return cr.run(); // this function returns the program exit code as a string
    }

    public void registerVar(Variable var){
        cr.registerVar(var);
    }

    public void halt() {
        Command.halt();
    }
}
