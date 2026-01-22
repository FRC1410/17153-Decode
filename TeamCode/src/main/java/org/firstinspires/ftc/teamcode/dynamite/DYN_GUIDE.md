# DYN Scripting Language Guide
## A Comprehensive Guide for FTC Autonomous Programming

---

## Table of Contents
1. [Overview](#overview)
2. [Quick Start](#quick-start)
3. [Creating Your OpMode](#creating-your-opmode)
4. [DYN Script Syntax](#dyn-script-syntax)
5. [Variables](#variables)
6. [Math Operations](#math-operations)
7. [Movement Commands](#movement-commands)
8. [Control Flow](#control-flow)
9. [Path Functions](#path-functions)
10. [Custom Commands](#custom-commands)
11. [Example Scripts](#example-scripts)
12. [Troubleshooting](#troubleshooting)

---

## Overview

DYN (DYNamite) is a domain-specific scripting language designed for FTC autonomous routines. It allows you to write readable, maintainable autonomous programs that integrate seamlessly with PedroPathing for motion control.

**Key Benefits:**
- Human-readable syntax
- Hot-swappable scripts (no recompile needed for script changes)
- Built-in PedroPathing integration
- Variable system with math operations
- Control flow (loops, conditionals)
- Custom command registration for subsystems

---

## Quick Start

### Step 1: Create a script file
Place your `.dyn` script in `TeamCode/src/main/assets/`:

```dyn
// MyAuto.dyn - Simple autonomous example

def_path main start
    Num startX 0
    Num startY 0
    Num startH 0
    FieldPos startPos (startX startY startH)
    
    PathStartPosition startPos
    
    Num targetX 24
    Num targetY 24
    Num targetH 90
    FieldPos target (targetX targetY targetH)
    
    goTo(target)
    turnTo(180, 30)
    AddData "Done!"
end

autoPath main
```

### Step 2: Create your OpMode
```java
@Autonomous(name = "My DYN Auto")
public class MyDynAuto extends DynAutoOpMode {
    @Override
    protected String getScriptName() {
        return "MyAuto.dyn";
    }
    
    @Override
    protected Pose getStartPose() {
        return new Pose(0, 0, 0);
    }
}
```

#### Optional: One-time Initialization Hook
Subclasses can override `onInit()` to run custom setup after hardware, PedroPathing, and the DYN system are ready, and before the script is loaded and `waitForStart()`:

```java
@Autonomous(name = "My DYN Auto")
public class MyDynAuto extends DynAutoOpMode {
    @Override
    protected String getScriptName() { return "MyAuto.dyn"; }

    @Override
    protected Pose getStartPose() { return new Pose(0, 0, 0); }

    @Override
    protected void onInit() {
        // Example: set initial servo positions, zero sensors, etc.
        // hoodServo.setPosition(INIT_POS);
        // imu.resetYaw();
        telemetry.addData("Init", "Robot primed for autonomous");
    }
}
```

### Step 3: Run it!
Select "My DYN Auto" from the Driver Station and run.

---

## DYN Script Syntax

### Comments
```dyn
// Single-line comment

'''
Multi-line comment
Can span multiple lines
The triple quotes must be at the start of the line
'''
```

### Basic Structure
- One command per line
- Variable names are plain identifiers (no $ prefix)
- Strings use double quotes: `"hello"`
- Numbers can be integers or decimals: `42`, `3.14`
- All trigonometric functions use radians

---

## Variables

### Variable Types

#### Num (Number)
```dyn
Num name value
Num speed 0.5
Num distance 24.5
Num count 10
```

#### Bool (Boolean)
```dyn
Bool name true/false
Bool isReady true
Bool isDone false
```

#### String
```dyn
String name "text"
String message "Hello World!"
```

#### FieldCoord (Field Coordinate)
```dyn
FieldCoord name (x, y)
FieldCoord pickupSpot (24, 36)
```

#### FieldPos (Field Position with heading)
```dyn
FieldPos name (x, y, heading)
FieldPos startPos (0, 0, 0)
FieldPos scoringPos (48, 24, 90)
```

#### List
```dyn
List name [item1, item2, item3]
List positions [pos1, pos2, pos3]
```
Methods: `.get(index)`, `.insert(index, data)`, `.append(data)`, `.remove(index)`

#### Json
```dyn
Json name {"id": value, "id2": value2}
```
Methods: `.id(idName)` to get value by ID

---

## Math Operations

All math operations follow the pattern: `OPERATION val1 val2 to result`

For single-input operations, you can also use: `OPERATION val to result` or just `OPERATION val` (modifies val in place)

### Basic Arithmetic

```dyn
// Addition: result = val1 + val2
ADD val1 val2 to result

// Subtraction: result = val1 - val2
SUB val1 val2 to result

// Multiplication: result = val1 * val2
MUX val1 val2 to result

// Division: result = val1 / val2
DIV val1 val2 to result

// Power: result = val1 ^ val2
POW val1 val2 to result
```

### Single-Input Operations

```dyn
// Square root
SQR val to result
SQR val              // modifies val in place
```

### Trigonometry (all in radians)

```dyn
// Sine
SIN val to result
SIN val

// Cosine
COS val to result
COS val

// Tangent
TAN val to result
TAN val

// Inverse sine (arcsine)
invSin val to result
invSin val

// Inverse cosine (arccosine)
invCos val to result
invCos val

// Inverse tangent (arctangent)
invTan val to result
invTan val
```

### Angle Conversion

```dyn
// Degrees to radians
toRad degrees to radians
toRad degrees

// Radians to degrees
toDeg radians to degrees
toDeg radians
```

### Example: Calculate Distance

```dyn
Num x1 0
Num y1 0
Num x2 3
Num y2 4
Num dx 0
Num dy 0
Num dx2 0
Num dy2 0
Num sum 0
Num distance 0

SUB x2 x1 to dx      // dx = 3 - 0 = 3
SUB y2 y1 to dy      // dy = 4 - 0 = 4
MUX dx dx to dx2     // dx2 = 3 * 3 = 9
MUX dy dy to dy2     // dy2 = 4 * 4 = 16
ADD dx2 dy2 to sum   // sum = 9 + 16 = 25
SQR sum to distance  // distance = sqrt(25) = 5
```

---

## Movement Commands

### PathStartPosition
Sets the robot's starting position. Must be the first command in an auto function.

```dyn
PathStartPosition positionVar
```

### goTo
Move to a field position in a straight line.

```dyn
goTo(fieldPosition)
goTo(targetPos)
```

### turnTo
Turn to a specific heading.

```dyn
turnTo(headingInDegrees, degreesPerSecond)
turnTo(90, 30)  // Turn to 90 degrees at 30 deg/sec
```

### followBezier
Follow a bezier curve path.

```dyn
followBezier(midPoint1, midPoint2, ..., endPosition)
followBezier(controlPoint, endPos)
followBezier(cp1, cp2, endPos)
```

---

## Control Flow

### While Loop

```dyn
while booleanVar Wstart
    // loop body
Wend
```

Example:
```dyn
Num counter 0
Bool keepGoing true
while keepGoing Wstart
    ADD counter 1 to counter
    if (counter > 10) start
        Bool keepGoing false
    end
Wend
```

### For Loop

```dyn
for count as iteratorVar start
    // loop body
end
```

Example:
```dyn
Num total 0
for 5 as i start
    ADD total i to total
    AddData total
end
```

### If Statement

```dyn
if (condition) start
    // code to execute if true
end
```

Example:
```dyn
Bool isReady true
if isReady start
    AddData "Let's go!"
    goTo(targetPos)
end
```

### Logical Operators

```dyn
and    // logical AND
or     // logical OR
not    // logical NOT
```

---

## Path Functions

### Defining a Path Function

```dyn
def_path functionName start
    // function body
end
```

### Running a Path Function

```dyn
RUN functionName
```

### Setting the Auto Entry Point

```dyn
autoPath functionName
```

### Example Structure

```dyn
def_path pickupSample start
    goTo(samplePos)
    cmd grabSample
end

def_path scoreSample start
    goTo(basketPos)
    cmd dropSample
end

def_path main start
    PathStartPosition startPos
    RUN pickupSample
    RUN scoreSample
    AddData "Done!"
end

autoPath main
```

---

## Custom Commands

### Syntax

```dyn
// Simple command (no input/output)
cmd commandName

// Command with input variable
cmd commandName from inputVar

// Command with output variable
cmd commandName to outputVar

// Command with both input and output
cmd commandName from inputVar to outputVar
```

### Registering in Java

In your OpMode:

```java
@Override
protected String[] getCustomFunctionIds() {
    return new String[] {
        "grabSample",
        "dropSample",
        "raiseArm"
    };
}

@Override
protected void registerCustomCommands() {
    super.registerCustomCommands();
    
    dynAuto.registerCustomCommand("grabSample", 
        new CustomCommand.CustomCommandHandler() {
            @Override
            public DynVar execute(String name, DynVar input) {
                clawServo.setPosition(CLAW_CLOSED);
                sleep(300);
                return null;
            }
        });
    
    dynAuto.registerCustomCommand("dropSample",
        new CustomCommand.CustomCommandHandler() {
            @Override
            public DynVar execute(String name, DynVar input) {
                clawServo.setPosition(CLAW_OPEN);
                sleep(300);
                return null;
            }
        });
}
```

### Using in Script

```dyn
cmd grabSample
cmd dropSample

// With input
Num armPosition 1500
cmd setArmPosition from armPosition

// With output
cmd getArmPosition to currentPosition
AddData currentPosition
```

---

## Telemetry

### AddData
Output a variable or string to telemetry.

```dyn
AddData variableName
AddData "String message"
AddData counter
```

---

## Example Scripts

### Basic Auto

```dyn
'''
BasicAuto.dyn
Simple 3-point autonomous
'''

def_path main start
    // Setup positions
    Num startX 0
    Num startY 0
    Num startH 0
    FieldPos startPos (startX startY startH)
    
    Num subX 24
    Num subY 0
    Num subH 0
    FieldPos submersible (subX subY subH)
    
    Num scoreX 24
    Num scoreY 12
    Num scoreH 45
    FieldPos scoringPos (scoreX scoreY scoreH)
    
    Num parkX 48
    Num parkY -24
    Num parkH 90
    FieldPos parkPos (parkX parkY parkH)
    
    // Start auto
    PathStartPosition startPos
    
    // Go to submersible
    goTo(submersible)
    AddData "At submersible"
    
    // Score preload
    goTo(scoringPos)
    cmd grabSample
    
    // Park
    goTo(parkPos)
    AddData "Parked!"
end

autoPath main
```

### Specimen Auto with Loop

```dyn
'''
SpecimenAuto.dyn
Score 3 specimens from wall
'''

def_path scoreSpecimen start
    goTo(chamberPos)
    cmd scoreSpecimen
end

def_path main start
    // Positions
    Num wallX 0
    Num wallY -36
    Num wallH 90
    FieldPos wallPos (wallX wallY wallH)
    
    Num chamberX 24
    Num chamberY 0
    Num chamberH 0
    FieldPos chamberPos (chamberX chamberY chamberH)
    
    PathStartPosition wallPos
    
    // Score 3 specimens
    for 3 as i start
        // Pick from wall
        goTo(wallPos)
        cmd grabSpecimen
        
        // Score on chamber
        RUN scoreSpecimen
        
        // Offset for next specimen
        ADD chamberX 3 to chamberX
        FieldPos chamberPos (chamberX chamberY chamberH)
    end
    
    // Park
    Num parkX 48
    Num parkY -48
    Num parkH 0
    FieldPos parkPos (parkX parkY parkH)
    goTo(parkPos)
    AddData "Done!"
end

autoPath main
```

### Sample Auto with Math

```dyn
'''
MathDemo.dyn
Calculate positions dynamically
'''

def_path main start
    Num baseX 24
    Num baseY 24
    Num radius 12
    
    Num startX 0
    Num startY 0
    Num startH 0
    FieldPos startPos (startX startY startH)
    PathStartPosition startPos
    
    // Visit 4 points in a circle
    for 4 as i start
        // Calculate angle: i * 90 degrees
        Num angleDeg 0
        MUX i 90 to angleDeg
        
        // Convert to radians
        Num angleRad 0
        toRad angleDeg to angleRad
        
        // Calculate x offset: radius * cos(angle)
        Num cosVal 0
        COS angleRad to cosVal
        Num offsetX 0
        MUX cosVal radius to offsetX
        
        // Calculate y offset: radius * sin(angle)
        Num sinVal 0
        SIN angleRad to sinVal
        Num offsetY 0
        MUX sinVal radius to offsetY
        
        // Calculate target position
        Num targetX 0
        Num targetY 0
        ADD baseX offsetX to targetX
        ADD baseY offsetY to targetY
        
        FieldPos target (targetX targetY angleDeg)
        goTo(target)
        AddData "Visited point"
    end
    
    AddData "Circle complete!"
end

autoPath main
```

---

## Troubleshooting

### Common Errors

**"Unknown command: xyz"**
- Ensure command is registered in `getCustomFunctionIds()`
- Check spelling matches exactly

**"Variable not found: xyz"**
- Declare variables before use with `Num xyz value`
- Check variable name spelling

**"Unterminated multi-line comment"**
- Ensure `'''` is at the start of the line for both opening and closing

**"Script file not found"**
- Place `.dyn` files in `TeamCode/src/main/assets/`
- Check filename in `getScriptName()` matches exactly

**Robot doesn't move**
- Verify PedroPathing is tuned for your robot
- Check `getStartPose()` matches physical robot position
- Ensure Follower is being updated

### Debugging Tips

1. **Use AddData statements**
   ```dyn
   AddData "Starting movement"
   goTo(target)
   AddData "Movement complete"
   ```

2. **Check telemetry** - DYN outputs to telemetry during execution

3. **Test incrementally** - Add one command at a time

4. **Keep paths simple** - Break complex movements into steps

### Best Practices

1. **Comment your scripts** - Use `//` for documentation
2. **Use meaningful variable names** - Makes scripts readable
3. **Test incrementally** - Add one command at a time
4. **Define reusable paths** - Use `def_path` for repeated actions
5. **Register all custom commands** - Both in `getCustomFunctionIds()` and `registerCustomCommands()`

---

## File Locations

| File Type | Location |
|-----------|----------|
| `.dyn` scripts | `TeamCode/src/main/assets/` |
| OpMode classes | `TeamCode/src/main/java/org/firstinspires/ftc/teamcode/` |
| DYN library | `TeamCode/src/main/java/org/firstinspires/ftc/teamcode/dynamite/` |

---

## Syntax Quick Reference

```dyn
// Variables
Num name value
Bool name true/false
String name "text"
FieldCoord name (x, y)
FieldPos name (x, y, heading)
List name [...]
Json name {...}

// Math (result = val1 OP val2)
ADD val1 val2 to result
SUB val1 val2 to result
MUX val1 val2 to result
DIV val1 val2 to result
POW val1 val2 to result
SQR val to result
SIN/COS/TAN val to result
invSin/invCos/invTan val to result
toRad/toDeg val to result

// Movement
PathStartPosition pos
goTo(position)
turnTo(heading, degPerSec)
followBezier(points..., endPos)

// Control flow
if (condition) start ... end
while bool Wstart ... Wend
for count as var start ... end

// Functions
def_path name start ... end
RUN functionName
autoPath entryPoint

// Custom commands
cmd name
cmd name from inVar
cmd name to outVar
cmd name from inVar to outVar

// Output
AddData variable
AddData "string"

// Comments
// single line
''' multi
line '''
```

---

Happy coding! 🚀
