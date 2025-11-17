package org.firstinspires.ftc.teamcode.dynamite;//import android.annotation.SuppressLint;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import org.firstinspires.ftc.teamcode.dynamite.DynVars.*;

public class DynCmdBuffer {
    public List<String> usedVariableNames = new ArrayList<>();
    // field cords
    public List<DynVarFieldCords> fieldCords = new ArrayList<>();
    // field positions
    public List<DynVarFieldPos> fieldPoses = new ArrayList<>();
    // numbers
    public List<DynVarNumber> numbers = new ArrayList<>();
    // booleans
    public List<DynVarBoolean> booleans = new ArrayList<>();
    // strings
    public List<DynVarString> strings = new ArrayList<>();
    // lists
    public List<DynVarList> lists = new ArrayList<>();
    // json
    public List<DynVarJson> jsons = new ArrayList<>();

    public void init(){
        //simpleSubsystem Subsystem = new simpleSubsystem();
        //Consumer<String[]> func1 = args -> Subsystem.start(args);

    }
}