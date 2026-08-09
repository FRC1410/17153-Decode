package org.firstinspires.ftc.teamcode.dynamite.Tokenizer;

public class Token {
    private final TokenTypes type;
    private Object value;
    private final int line;
    private final int col;

    public TokenTypes type(){
        return type;
    }
    public int getLine(){
        return line;
    }
    public int getColumn() {
        return col;
    }

    public Object getValue(){
        return value;
    }

    public Token(TokenTypes type,int line,int col){
        this.type = type;
        this.line = line;
        this.col = col;
    }
    public Token(TokenTypes type,int line,int col,Object value){
        this.type = type;
        this.line = line;
        this.col = col;
        this.value = value;
    }
    public String toString(){
        // "[Line,char X,Y; toeknType; value]"
        String tokenType = "";
        switch (type){
            // single chars
            case Lparenth -> tokenType = "Left Parenthesis";
            case Rparenth -> tokenType = "Right Parenthesis";
            case Comma -> tokenType = "Comma";

            case Lbracket -> tokenType = "Left Bracket";
            case Rbracket -> tokenType = "Right Bracket";

            case LCbracket -> tokenType = "Left Curly Bracket";
            case RCbracket ->  tokenType = "Right Curly Bracket";

            case Colon -> tokenType = "Color";

            // math ops
            case Add -> tokenType = "Add";
            case Sub -> tokenType = "Subtract";
            case Mux -> tokenType = "Multiply";
            case Div -> tokenType = "Divide";

            case Pow -> tokenType = "Exponential";
            case Sqrt -> tokenType = "Square Root";
            case Sin -> tokenType = "Sine";
            case iSin -> tokenType = "Inverse Sine";

            case Cos -> tokenType = "Cosine";
            case iCos -> tokenType = "Inverse Cosine";
            case Tan -> tokenType = "Tangent";
            case iTan -> tokenType = "Inverse Tangent";

            case toRad -> tokenType = "To Radian";
            case toDeg -> tokenType = "To Degree";

            case Increment -> tokenType = "Increment Number";
            case Decrement ->  tokenType = "Decrement Number";

            // variable types
            case NumberDef -> tokenType = "Number definition";
            case BoolDef -> tokenType = "Boolean definition";
            case StringDef -> tokenType = "String definition";

            case List -> tokenType = "List";
            case Json -> tokenType = "Json";
            case FieldCord -> tokenType = "Field Coordinate";

            case FieldPos -> tokenType = "Field Position";

            // list/json Ops
            case Get -> tokenType = "Get";
            case Insert -> tokenType = "Insert";
            case Append -> tokenType = "Append";

            case Remove -> tokenType = "Remove";
            case Set -> tokenType = "Set";

            // logical ops
            case Equals -> tokenType = "Equals";
            case NotEqual -> tokenType = "Not Equal";
            case isMore -> tokenType = "Is More";

            case isLess -> tokenType = "Is Less";
            case And -> tokenType = "And";
            case Or -> tokenType = "Or";
            case Not -> tokenType = "Not";

            // movement ops
            case TurnTo -> tokenType = "Turn-to";
            case GoTo -> tokenType = "Go-to";

            case doBez -> tokenType = "Move bezier";
            case followSpline -> tokenType = "Move spline";

            case followSplineLinear -> tokenType = "Move spline linear";

            case followSplineSpline -> tokenType = "Move spline spline";

            // funcs/loop/if
            case DefPath -> tokenType = "Define Path";
            case Run -> tokenType = "Run"; // run target path function
            case While -> tokenType = "While Loop";
            case For -> tokenType = "For Loop";
            case If -> tokenType = "If Statement";

            // telem
            case AddData -> tokenType = "Add Telemetry Data";
            case Update -> tokenType = "Update Telemetry";
            case Clear -> tokenType = "Clear Telemetry";

            // literals
            case Boolean -> tokenType = "Boolean"; // variable value, etc.
            case Name -> tokenType = "Name"; // name of variable, or path func
            case Number -> tokenType = "Number";
            case String -> tokenType = "String";

            // random commands
            case RngFloat -> tokenType = "Random Float";
            case RngDouble -> tokenType = "Random Double";

            case RngInteger -> tokenType = "Random Integer";
            case RngBoolean -> tokenType = "Random Boolean";

            // extra
            case Start -> tokenType = "Start"; // start path func, or loop
            case End -> tokenType = "End"; // end of path func, or loop
            case PathStartPos -> tokenType = "Path Start Position";

            case Cmd -> tokenType = "Execute Command"; // execute java defined command, can take N values, and can return 1 value.
            case To -> tokenType = "To"; // this is like a output variable pointer thingy, it tells where to specifically send the operation output to. ex: Add Var1 Var2 *TO* Var3
            case MainPathFunc -> tokenType = "Main Path Function";

            default -> tokenType = "null (BAD)";
        }
        return "[Line,Char "+line+","+col+"; "+tokenType+"; "+value+"]";
    }
}
