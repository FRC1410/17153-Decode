package org.firstinspires.ftc.teamcode.dynamite.DYNCore.commandConstructor;

import org.firstinspires.ftc.teamcode.dynamite.DYNCore.commands.Command;

class EndOp extends ConstructorUtils{
    public static void Do(){
        i++;
        // ArrayList.getLast()/removeLast() are Java 21 SequencedCollection methods and
        // are absent from android.jar; indexed access is the portable equivalent.
        if (depthTracker.size() == 1) {
            finalCommands.add(depthTracker.remove(depthTracker.size() - 1));
        } else if (!depthTracker.isEmpty()){
            Command currentDepth = depthTracker.get(depthTracker.size() - 1);
            depthTracker.remove(depthTracker.size() - 1);
            depthTracker.get(depthTracker.size() - 1).addCommand(currentDepth);
        }
    }
}
