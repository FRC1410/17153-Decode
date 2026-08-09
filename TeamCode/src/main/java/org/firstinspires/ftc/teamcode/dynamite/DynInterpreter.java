package org.firstinspires.ftc.teamcode.dynamite;

import org.SquidSquad.FTCInterface.FTCInterface;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class DynInterpreter {
    public DynInterpreter(FTCInterface ftcInterface){}

    public void loadScript(String path){}
    public String runScript(){
        return null; // this function returns the program exit code as a string
    }

    public void linkJFunc(String ID, Function<Object,Object> func){}
    public void linkJFunc(String ID, Consumer<Object> func){}
    public void linkJFunc(String ID, Supplier<Object> func){}
}
