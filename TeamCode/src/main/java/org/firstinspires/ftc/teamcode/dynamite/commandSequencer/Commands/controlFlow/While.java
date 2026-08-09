package org.firstinspires.ftc.teamcode.dynamite.commandSequencer.Commands.controlFlow;

import org.SquidSquad.commandSequencer.Commands.Command;
import org.SquidSquad.commandSequencer.Commands.CommandType;

import java.util.ArrayList;

public class While extends Command {
    private final Condition condition;
    private final ArrayList<Command> innerCommands = new ArrayList<>();
    public While(int line, Condition condition){
        super(line, CommandType.While, new String[0],"");
        this.condition = condition;
    }
    public void addCommand(Command command){
        innerCommands.add(command);
    }
    @Override
    public void run(){
        super.run();
        while (condition.getResult()){
            if (!running) return; // stop execution
            for (Command cmd : innerCommands){
                if (!running) return; // stop execution
                cmd.run();
            }
        }
    }
    public Command[] getCommandList(){
        return innerCommands.toArray(new Command[0]);
    }
}
