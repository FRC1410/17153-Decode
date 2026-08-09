package org.firstinspires.ftc.teamcode.dynamite.FTCInterface;

import org.firstinspires.ftc.teamcode.dynamite.DynInterpreter;

public interface FTCInterface {
    void init();
    void loop();
    void stop();

    void runGeneralMovement(GeneralMovement move);

    DynInterpreter getInterpreter();

    // uses the in-built 'assets' folder in the base android SDK (or whatever file loading mechanisim the end-user ends up using)
    // '/' is the base of said assets folder.
    String loadFile(String path);
}
