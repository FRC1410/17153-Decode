package org.firstinspires.ftc.teamcode.dynamite.commandSequencer.Commands.controlFlow;

import org.firstinspires.ftc.teamcode.dynamite.commandSequencer.Commands.Command;
import org.firstinspires.ftc.teamcode.dynamite.commandSequencer.Commands.CommandType;

import java.util.ArrayList;

public class If extends Command {
    private final Condition condition;
    private final ArrayList<Command> innerCommands = new ArrayList<>();
    public If(int line, Condition condition){
        super(line, CommandType.If,new String[]{condition.toString()});
        this.condition = condition;
    }

    public void addCommand(Command command){
        innerCommands.add(command);
    }

    @Override
    public void run(){
        super.run();
        if (condition.getResult()){
            for (Command cmd : innerCommands){
                cmd.run();
            }
        }
    }
    public Command[] getCommandList(){
        return innerCommands.toArray(new Command[0]);
    }
}