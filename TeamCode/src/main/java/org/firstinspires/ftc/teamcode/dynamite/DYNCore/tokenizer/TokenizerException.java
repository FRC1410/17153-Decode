package org.firstinspires.ftc.teamcode.dynamite.DYNCore.tokenizer;

import org.firstinspires.ftc.teamcode.dynamite.DYNCore.commandSequencer.CommandException;

public class TokenizerException extends CommandException {
    public TokenizerException(int line, int ch, String message, String deets) {
        super(line,ch,message+"\n"+deets);
    }
}
