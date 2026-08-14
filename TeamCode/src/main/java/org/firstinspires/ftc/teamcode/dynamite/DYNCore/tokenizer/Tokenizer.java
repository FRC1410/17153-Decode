package org.firstinspires.ftc.teamcode.dynamite.DYNCore.tokenizer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Tokenizer {

    private final Map<Integer, Integer> lineMap = new HashMap<>();
    private final Map<Integer, Map<Integer, Integer>> charMap = new HashMap<>();

    private String[] removeComments(String[] in) {
        in = in.clone();
        ArrayList<String> out = new ArrayList<>();
        lineMap.clear();
        charMap.clear();
        boolean inMultiComment = false;
        int originalIndex = 0;

        for (String str : in) {
            StringBuilder lineBuffer = new StringBuilder();
            Map<Integer, Integer> charIndexMap = new HashMap<>();
            int i = 0;

            while (i < str.length()) {
                if (i + 2 < str.length()
                        && str.charAt(i) == '\''
                        && str.charAt(i + 1) == '\''
                        && str.charAt(i + 2) == '\'') {
                    inMultiComment = !inMultiComment;
                    i += 3;
                    continue;
                }
                if (!inMultiComment
                        && i + 1 < str.length()
                        && str.charAt(i) == '/'
                        && str.charAt(i + 1) == '/') {
                    break;
                }
                if (!inMultiComment) {
                    charIndexMap.put(lineBuffer.length(), i);
                    lineBuffer.append(str.charAt(i));
                }
                i++;
            }

            String raw = lineBuffer.toString();
            String cleaned = raw.trim().replaceAll("\\s+", " ");
            if (!cleaned.isEmpty()) {
                int cleanLineIndex = out.size();
                lineMap.put(cleanLineIndex, originalIndex);

                Map<Integer, Integer> remappedCharMap = new HashMap<>();
                int rawOffset = raw.indexOf(cleaned.charAt(0));
                int cleanIdx = 0;
                int rawIdx = rawOffset;
                while (cleanIdx < cleaned.length() && rawIdx < raw.length()) {
                    char cleanChr = cleaned.charAt(cleanIdx);
                    char rawChr = raw.charAt(rawIdx);
                    if (cleanChr == ' ' && rawChr != ' ') {
                        rawIdx++;
                        continue;
                    }
                    if (rawChr != cleanChr) {
                        rawIdx++;
                        continue;
                    }
                    remappedCharMap.put(cleanIdx, charIndexMap.getOrDefault(rawIdx, rawIdx));
                    cleanIdx++;
                    rawIdx++;
                }

                charMap.put(cleanLineIndex, remappedCharMap);
                out.add(cleaned);
            }

            originalIndex++;
        }

        return out.toArray(new String[0]);
    }

    public Token[] processScript(String script) {
        String[] lines = script.split("\r\n|\r|\n");
        lines = removeComments(lines);
        return tokenize(lines);
    }

    public Token[] processScript(String[] lines) {
        lines = removeComments(lines);
        return tokenize(lines);
    }

    public Token[] tokenize(String[] cleanLines) {
        ArrayList<Token> tokens = new ArrayList<>();
        int cleanLineIndex = 0;

        for (String line : cleanLines) {
            int lineIndex = lineMap.get(cleanLineIndex);
            int cleanCharIndex = 0;
            boolean inString = false;
            StringBuilder stringBuffer = new StringBuilder();
            int stringStartChar = -1;

            // FIX: handle string literals at line level before splitting on spaces,
            // since string content may contain spaces
            ArrayList<String> lineChunks = new ArrayList<>();
            ArrayList<Integer> chunkOffsets = new ArrayList<>();

            int i = 0;
            StringBuilder chunkBuffer = new StringBuilder();
            int chunkStart = 0;

            while (i < line.length()) {
                char c = line.charAt(i);
                if (c == '"') {
                    if (!inString) {
                        // flush any pending chunk before the string
                        if (chunkBuffer.length() > 0) {
                            lineChunks.add(chunkBuffer.toString());
                            chunkOffsets.add(chunkStart);
                            chunkBuffer = new StringBuilder();
                        }
                        inString = true;
                        stringStartChar = i;
                        stringBuffer = new StringBuilder();
                    } else {
                        // end of string — emit as a special quoted chunk
                        inString = false;
                        lineChunks.add("\"" + stringBuffer + "\"");
                        chunkOffsets.add(stringStartChar);
                        chunkStart = i + 1;
                    }
                    i++;
                    continue;
                }
                if (inString) {
                    stringBuffer.append(c);
                    i++;
                    continue;
                }
                if (c == ' ') {
                    if (chunkBuffer.length() > 0) {
                        lineChunks.add(chunkBuffer.toString());
                        chunkOffsets.add(chunkStart);
                        chunkBuffer = new StringBuilder();
                    }
                    chunkStart = i + 1;
                    i++;
                    continue;
                }
                chunkBuffer.append(c);
                i++;
            }
            if (chunkBuffer.length() > 0) {
                lineChunks.add(chunkBuffer.toString());
                chunkOffsets.add(chunkStart);
            }
            if (inString) {
                throw new TokenizerException(lineIndex,cleanCharIndex,"Unterminated string literal", "line " + lineIndex);
            }

            for (int ci = 0; ci < lineChunks.size(); ci++) {
                String chunk = lineChunks.get(ci);
                int charIndex = charMap.get(cleanLineIndex).getOrDefault(chunkOffsets.get(ci), -1);

                // FIX: handle quoted string chunks directly
                if (chunk.startsWith("\"") && chunk.endsWith("\"") && chunk.length() >= 2) {
                    String content = chunk.substring(1, chunk.length() - 1);
                    tokens.add(new Token(TokenTypes.String, lineIndex, charIndex, content));
                } else {
                    Token[] processedChunk = processChunk(chunk, lineIndex, charIndex);
                    for (Token token : processedChunk) {
                        tokens.add(token);
                    }
                }
            }

            cleanLineIndex++;
        }

        return tokens.toArray(new Token[0]);
    }

    private Token[] processChunk(String chunk, int lineIndex, int charIndex) {
        int chunkSize = chunk.length();
        ArrayList<Token> out = new ArrayList<>();
        String internalChunk = "";
        int counter = 0;

        for (char piece : chunk.toCharArray()) {
            counter++;

            if (piece == '(') {
                flushInternalChunk(internalChunk, out, lineIndex, charIndex);
                internalChunk = "";
                out.add(new Token(TokenTypes.Lparenth, lineIndex, charIndex));
            } else if (piece == '[') {
                flushInternalChunk(internalChunk, out, lineIndex, charIndex);
                internalChunk = "";
                out.add(new Token(TokenTypes.Lbracket, lineIndex, charIndex));
            } else if (piece == '{') {
                flushInternalChunk(internalChunk, out, lineIndex, charIndex);
                internalChunk = "";
                out.add(new Token(TokenTypes.LCbracket, lineIndex, charIndex));
            } else if (piece == ')') {
                flushInternalChunk(internalChunk, out, lineIndex, charIndex);
                internalChunk = "";
                out.add(new Token(TokenTypes.Rparenth, lineIndex, charIndex));
            } else if (piece == ']') {
                flushInternalChunk(internalChunk, out, lineIndex, charIndex);
                internalChunk = "";
                out.add(new Token(TokenTypes.Rbracket, lineIndex, charIndex));
            } else if (piece == '}') {
                flushInternalChunk(internalChunk, out, lineIndex, charIndex);
                internalChunk = "";
                out.add(new Token(TokenTypes.RCbracket, lineIndex, charIndex));
            } else if (piece == ',') {
                flushInternalChunk(internalChunk, out, lineIndex, charIndex);
                internalChunk = "";
                out.add(new Token(TokenTypes.Comma, lineIndex, charIndex));
            } else if (piece == ':'){
                flushInternalChunk(internalChunk, out, lineIndex, charIndex);
                internalChunk = "";
                out.add(new Token(TokenTypes.Colon, lineIndex, charIndex));
            } else {
                internalChunk = internalChunk + piece;

                // FIX: comparison operators — checked before any alpha keywords
                // multi-char operators first to avoid prefix conflicts
                if (internalChunk.equals("==")) {
                    out.add(new Token(TokenTypes.Equals, lineIndex, charIndex));
                    internalChunk = "";
                } else if (internalChunk.equals("!=")) {
                    out.add(new Token(TokenTypes.NotEqual, lineIndex, charIndex));
                    internalChunk = "";
                } else if (internalChunk.equals(">=")) {
                    out.add(new Token(TokenTypes.isMoreEqual, lineIndex, charIndex)); // >= isMore
                    internalChunk = "";
                } else if (internalChunk.equals("<=")) {
                    out.add(new Token(TokenTypes.isLessEqual, lineIndex, charIndex)); // <= isLess
                    internalChunk = "";
                } else if (internalChunk.equals(">")) {
                    // peek: if next char is '=' we need to keep accumulating — handled above on next iteration
                    // if this is the last char or next isn't '=', emit now
                    if (counter == chunkSize || chunk.charAt(counter) != '=') {
                        out.add(new Token(TokenTypes.isMore, lineIndex, charIndex));
                        internalChunk = "";
                    }
                } else if (internalChunk.equals("<")) {
                    if (counter == chunkSize || chunk.charAt(counter) != '=') {
                        out.add(new Token(TokenTypes.isLess, lineIndex, charIndex));
                        internalChunk = "";
                    }
                }
                // list/json operations
                else if (internalChunk.equals("GET") && isWordBoundary(chunk, counter, chunkSize)) {
                    out.add(new Token(TokenTypes.Get, lineIndex, charIndex));
                    internalChunk = "";
                } else if (internalChunk.equals("INSERT") && isWordBoundary(chunk, counter, chunkSize)) {
                    out.add(new Token(TokenTypes.Insert, lineIndex, charIndex));
                    internalChunk = "";
                } else if (internalChunk.equals("APPEND") && isWordBoundary(chunk, counter, chunkSize)) {
                    out.add(new Token(TokenTypes.Append, lineIndex, charIndex));
                    internalChunk = "";
                } else if (internalChunk.equals("REMOVE") && isWordBoundary(chunk, counter, chunkSize)) {
                    out.add(new Token(TokenTypes.Remove, lineIndex, charIndex));
                    internalChunk = "";
                } else if (internalChunk.equals("SET") && isWordBoundary(chunk, counter, chunkSize)) {
                    out.add(new Token(TokenTypes.Set, lineIndex, charIndex));
                    internalChunk = "";
                }
                // variable declarations
                else if (internalChunk.equals("FieldCoord") && isWordBoundary(chunk, counter, chunkSize)) {
                    out.add(new Token(TokenTypes.FieldCord, lineIndex, charIndex));
                    internalChunk = "";
                } else if (internalChunk.equals("FieldPos") && isWordBoundary(chunk, counter, chunkSize)) {
                    out.add(new Token(TokenTypes.FieldPos, lineIndex, charIndex));
                    internalChunk = "";
                } else if (internalChunk.equals("Num") && isWordBoundary(chunk, counter, chunkSize)) {
                    out.add(new Token(TokenTypes.NumberDef, lineIndex, charIndex));
                    internalChunk = "";
                } else if (internalChunk.equals("Bool") && isWordBoundary(chunk, counter, chunkSize)) {
                    out.add(new Token(TokenTypes.BoolDef, lineIndex, charIndex));
                    internalChunk = "";
                } else if (internalChunk.equals("String") && isWordBoundary(chunk, counter, chunkSize)) {
                    out.add(new Token(TokenTypes.StringDef, lineIndex, charIndex));
                    internalChunk = "";
                } else if (internalChunk.equals("List") && isWordBoundary(chunk, counter, chunkSize)) {
                    out.add(new Token(TokenTypes.List, lineIndex, charIndex));
                    internalChunk = "";
                } else if (internalChunk.equals("Json") && isWordBoundary(chunk, counter, chunkSize)) {
                    out.add(new Token(TokenTypes.Json, lineIndex, charIndex));
                    internalChunk = "";
                }
                // math commands
                else if (internalChunk.equals("ADD") && isWordBoundary(chunk, counter, chunkSize)) {
                    out.add(new Token(TokenTypes.Add, lineIndex, charIndex));
                    internalChunk = "";
                } else if (internalChunk.equals("SUB") && isWordBoundary(chunk, counter, chunkSize)) {
                    out.add(new Token(TokenTypes.Sub, lineIndex, charIndex));
                    internalChunk = "";
                } else if (internalChunk.equals("MUX") && isWordBoundary(chunk, counter, chunkSize)) {
                    out.add(new Token(TokenTypes.Mux, lineIndex, charIndex));
                    internalChunk = "";
                } else if (internalChunk.equals("DIV") && isWordBoundary(chunk, counter, chunkSize)) {
                    out.add(new Token(TokenTypes.Div, lineIndex, charIndex));
                    internalChunk = "";
                } else if (internalChunk.equals("POW") && isWordBoundary(chunk, counter, chunkSize)) {
                    out.add(new Token(TokenTypes.Pow, lineIndex, charIndex));
                    internalChunk = "";
                } else if (internalChunk.equals("SQR") && isWordBoundary(chunk, counter, chunkSize)) {
                    out.add(new Token(TokenTypes.Sqrt, lineIndex, charIndex));
                    internalChunk = "";
                } else if (internalChunk.equals("SIN") && isWordBoundary(chunk, counter, chunkSize)) {
                    out.add(new Token(TokenTypes.Sin, lineIndex, charIndex));
                    internalChunk = "";
                } else if (internalChunk.equals("invSIN") && isWordBoundary(chunk, counter, chunkSize)) {
                    out.add(new Token(TokenTypes.iSin, lineIndex, charIndex));
                    internalChunk = "";
                } else if (internalChunk.equals("COS") && isWordBoundary(chunk, counter, chunkSize)) {
                    out.add(new Token(TokenTypes.Cos, lineIndex, charIndex));
                    internalChunk = "";
                } else if (internalChunk.equals("invCOS") && isWordBoundary(chunk, counter, chunkSize)) {
                    out.add(new Token(TokenTypes.iCos, lineIndex, charIndex));
                    internalChunk = "";
                } else if (internalChunk.equals("TAN") && isWordBoundary(chunk, counter, chunkSize)) {
                    out.add(new Token(TokenTypes.Tan, lineIndex, charIndex));
                    internalChunk = "";
                } else if (internalChunk.equals("invTAN") && isWordBoundary(chunk, counter, chunkSize)) {
                    out.add(new Token(TokenTypes.iTan, lineIndex, charIndex));
                    internalChunk = "";
                } else if (internalChunk.equals("toDeg") && isWordBoundary(chunk, counter, chunkSize)) {
                    out.add(new Token(TokenTypes.toDeg, lineIndex, charIndex));
                    internalChunk = "";
                } else if (internalChunk.equals("toRad") && isWordBoundary(chunk, counter, chunkSize)) {
                    out.add(new Token(TokenTypes.toRad, lineIndex, charIndex));
                    internalChunk = "";
                } else if (internalChunk.equals("Inc") && isWordBoundary(chunk, counter, chunkSize)) {
                    out.add(new Token(TokenTypes.Increment, lineIndex, charIndex));
                    internalChunk = "";
                }
                // FIX: Dec was missing
                else if (internalChunk.equals("Dec") && isWordBoundary(chunk, counter, chunkSize)) {
                    out.add(new Token(TokenTypes.Decrement, lineIndex, charIndex));
                    internalChunk = "";
                }
                // pointer
                else if (internalChunk.equals("to") && isWordBoundary(chunk, counter, chunkSize)) {
                    out.add(new Token(TokenTypes.To, lineIndex, charIndex));
                    internalChunk = "";
                }
                // function/loop/condition declarations
                else if (internalChunk.equals("def_path") && isWordBoundary(chunk, counter, chunkSize)) {
                    out.add(new Token(TokenTypes.DefPath, lineIndex, charIndex));
                    internalChunk = "";
                } else if (internalChunk.equals("for") && isWordBoundary(chunk, counter, chunkSize)) {
                    out.add(new Token(TokenTypes.For, lineIndex, charIndex));
                    internalChunk = "";
                } else if (internalChunk.equals("while") && isWordBoundary(chunk, counter, chunkSize)) {
                    out.add(new Token(TokenTypes.While, lineIndex, charIndex));
                    internalChunk = "";
                } else if (internalChunk.equals("if") && isWordBoundary(chunk, counter, chunkSize)) {
                    out.add(new Token(TokenTypes.If, lineIndex, charIndex));
                    internalChunk = "";
                } else if (internalChunk.equals("end") && isWordBoundary(chunk, counter, chunkSize)) {
                    out.add(new Token(TokenTypes.End, lineIndex, charIndex));
                    internalChunk = "";
                } else if (internalChunk.equals("autoPath") && isWordBoundary(chunk, counter, chunkSize)) {
                    out.add(new Token(TokenTypes.MainPathFunc, lineIndex, charIndex));
                    internalChunk = "";
                } else if (internalChunk.equals("start") && isWordBoundary(chunk, counter, chunkSize)) {
                    out.add(new Token(TokenTypes.Start, lineIndex, charIndex));
                    internalChunk = "";
                }
                // logical operators
                else if (internalChunk.equals("or") && isWordBoundary(chunk, counter, chunkSize)) {
                    out.add(new Token(TokenTypes.Or, lineIndex, charIndex));
                    internalChunk = "";
                } else if (internalChunk.equals("And") && isWordBoundary(chunk, counter, chunkSize)) {
                    out.add(new Token(TokenTypes.And, lineIndex, charIndex));
                    internalChunk = "";
                } else if (internalChunk.equals("Not") && isWordBoundary(chunk, counter, chunkSize)) {
                    out.add(new Token(TokenTypes.Not, lineIndex, charIndex));
                    internalChunk = "";
                }
                // movement commands
                else if (internalChunk.equals("turnTo") && isWordBoundary(chunk, counter, chunkSize)) {
                    out.add(new Token(TokenTypes.TurnTo, lineIndex, charIndex));
                    internalChunk = "";
                } else if (internalChunk.equals("goTo") && isWordBoundary(chunk, counter, chunkSize)) {
                    out.add(new Token(TokenTypes.GoTo, lineIndex, charIndex));
                    internalChunk = "";
                } else if (internalChunk.equals("followBezier") && isWordBoundary(chunk, counter, chunkSize)) {
                    out.add(new Token(TokenTypes.doBez, lineIndex, charIndex));
                    internalChunk = "";
                } else if (internalChunk.equals("followSpline") && isWordBoundary(chunk, counter, chunkSize)) {
                    out.add(new Token(TokenTypes.followSpline, lineIndex, charIndex));
                    internalChunk = "";
                } else if (internalChunk.equals("followSplineLinear") && isWordBoundary(chunk, counter, chunkSize)) {
                    out.add(new Token(TokenTypes.followSplineLinear, lineIndex, charIndex));
                    internalChunk = "";
                } else if (internalChunk.equals("followSplineSpline") && isWordBoundary(chunk, counter, chunkSize)) {
                    out.add(new Token(TokenTypes.followSplineSpline, lineIndex, charIndex));
                    internalChunk = "";
                }
                // starting declaration
                else if (internalChunk.equals("PathStartPosition") && isWordBoundary(chunk, counter, chunkSize)) {
                    out.add(new Token(TokenTypes.PathStartPos, lineIndex, charIndex));
                    internalChunk = "";
                }
                // jFunc / RUN
                else if (internalChunk.equals("jFunc") && isWordBoundary(chunk, counter, chunkSize)) {
                    out.add(new Token(TokenTypes.Cmd, lineIndex, charIndex));
                    internalChunk = "";
                } else if (internalChunk.equals("RUN") && isWordBoundary(chunk, counter, chunkSize)) {
                    out.add(new Token(TokenTypes.Run, lineIndex, charIndex));
                    internalChunk = "";
                }
                // telemetry
                else if (internalChunk.equals("AddData") && isWordBoundary(chunk, counter, chunkSize)) {
                    out.add(new Token(TokenTypes.AddData, lineIndex, charIndex));
                    internalChunk = "";
                } else if (internalChunk.equals("Update") && isWordBoundary(chunk, counter, chunkSize)) {
                    out.add(new Token(TokenTypes.Update, lineIndex, charIndex));
                    internalChunk = "";
                } else if (internalChunk.equals("Clear") && isWordBoundary(chunk, counter, chunkSize)) {
                    out.add(new Token(TokenTypes.Clear, lineIndex, charIndex));
                    internalChunk = "";
                }
                // FIX: RndFlt -> RngFlt, Rngint -> RngInt to match doc
                else if (internalChunk.equals("RngFlt") && isWordBoundary(chunk, counter, chunkSize)) {
                    out.add(new Token(TokenTypes.RngFloat, lineIndex, charIndex));
                    internalChunk = "";
                } else if (internalChunk.equals("RngDbl") && isWordBoundary(chunk, counter, chunkSize)) {
                    out.add(new Token(TokenTypes.RngDouble, lineIndex, charIndex));
                    internalChunk = "";
                } else if (internalChunk.equals("RngInt") && isWordBoundary(chunk, counter, chunkSize)) {
                    out.add(new Token(TokenTypes.RngInteger, lineIndex, charIndex));
                    internalChunk = "";
                } else if (internalChunk.equals("RngBool") && isWordBoundary(chunk, counter, chunkSize)) {
                    out.add(new Token(TokenTypes.RngBoolean, lineIndex, charIndex));
                    internalChunk = "";
                }
                // extra commands
                else if (internalChunk.equals("Sleep") && isWordBoundary(chunk, counter, chunkSize)){
                    out.add(new Token(TokenTypes.Sleep, lineIndex, charIndex));
                    internalChunk = "";
                } else {
                    if (counter == chunkSize) {
                        flushInternalChunk(internalChunk, out, lineIndex, charIndex);
                    }
                }
            }
        }
        return out.toArray(new Token[0]);
    }

    private boolean isWordBoundary(String chunk, int counter, int chunkSize) {
        return counter == chunkSize
                || (!Character.isLetterOrDigit(chunk.charAt(counter))
                && chunk.charAt(counter) != '_');
    }

    private void flushInternalChunk(String internalChunk, ArrayList<Token> out, int lineIndex, int charIndex) {
        if (internalChunk.isEmpty()) return;
        if (internalChunk.equals("true")) {
            out.add(new Token(TokenTypes.Boolean, lineIndex, charIndex, true));
        } else if (internalChunk.equals("false")) {
            out.add(new Token(TokenTypes.Boolean, lineIndex, charIndex, false));
        } else if (Character.isDigit(internalChunk.charAt(0)) || (internalChunk.charAt(0) == '-' && internalChunk.length() > 1)) {
            if (!internalChunk.contains(".")) internalChunk += ".0";
            try {
                out.add(new Token(TokenTypes.Number, lineIndex, charIndex, Double.parseDouble(internalChunk)));
            } catch (NumberFormatException e) {
                throw new TokenizerException(lineIndex,charIndex,e.getMessage(), "invalid number format at line " + lineIndex + " column: " + charIndex);
            }
        } else {
            String nameVal = screenName(internalChunk,lineIndex,charIndex);
            out.add(new Token(TokenTypes.Name, lineIndex, charIndex, internalChunk));
        }
    }

    private static final String nameWhiteList = "abcedfghijklmnopqrstuvwxyzABCDEFHIJKLMNOPQRSTUVWXYZ1234567890-_|<>";
    private static final char[] nameWhiteListCharArr = nameWhiteList.toCharArray();
    private String screenName(String internalChunk, int line,int ch) {
        // check all chuck chars
        for (char c : internalChunk.toCharArray()){
            boolean trip = true;
            for (char v : nameWhiteListCharArr){
                if (c == v){
                    trip = false;
                }
            }
            if (trip){
                throw new TokenizerException(line,ch,"Illigal character in name!","Cannot use: '"+c+"' !");
            }
        }
        return internalChunk;
    }
}