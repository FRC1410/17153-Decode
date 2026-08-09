package org.firstinspires.ftc.teamcode.dynamite.Tokenizer;

import org.SquidSquad.commandSequencer.CommandException;

public class TokenizerException extends CommandException {
    public TokenizerException(int line, int ch, String message, String deets) {
        super(line,ch,message+"\n"+deets);
    }
}
