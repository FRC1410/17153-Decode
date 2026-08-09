package org.firstinspires.ftc.teamcode.dynamite;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

/**
 * @deprecated Desktop-only. This class is built on {@code java.nio.file} ({@link Path},
 * {@link Files}), which Android only provides from API 26. The REV Control Hub runs
 * Android 7.1.2 (API 25), so every method here throws {@code NoClassDefFoundError} on
 * the robot even though it compiles cleanly against {@code compileSdk 34}. Its only
 * caller is the desktop {@link Main} harness. Robot-side script loading needs a
 * {@code java.io}-based reader over the Control Hub's own storage instead.
 */
@Deprecated
public class FileReader {

    /**
     * ISO-8859-1 reproduces the original `(char) byteValue` cast exactly and is the
     * cheapest possible decode on Java 9+ (compact strings store LATIN1 natively).
     * Switch to StandardCharsets.UTF_8 if .dyn files may contain non-ASCII text --
     * the line splitter below is UTF-8 safe either way, since continuation bytes
     * are always >= 0x80 and can never collide with '\r' or '\n'.
     */
    private static final Charset CHARSET = StandardCharsets.ISO_8859_1;

    private final Path scriptDir;

    public FileReader() {
        this("./DYN_Scripts");
    }

    public FileReader(String startDir) {
        this.scriptDir = null;//Path.of(startDir);
    }

    public String readFile(String name) {
        // Files.readString would be one allocation fewer, but it is absent from
        // Android's android.jar at any API level. readBytes already wraps IOException.
        return new String(readBytes(name), CHARSET);
    }

    public String[] readLines(String name) {
        byte[] b = readBytes(name);
        int len = b.length;
        if (len == 0) {
            return new String[] { "" }; // matches String.split on an empty input
        }

        // Pass 1: count fields so the result array is allocated exactly once.
        int count = 1;
        for (int i = 0; i < len; i++) {
            byte c = b[i];
            if (c == '\n') {
                count++;
            } else if (c == '\r') {
                count++;
                if (i + 1 < len && b[i + 1] == '\n') i++;
            }
        }

        // Pass 2: materialise the lines.
        String[] lines = new String[count];
        int idx = 0;
        int start = 0;
        for (int i = 0; i < len; i++) {
            byte c = b[i];
            if (c == '\n' || c == '\r') {
                lines[idx++] = new String(b, start, i - start, CHARSET);
                if (c == '\r' && i + 1 < len && b[i + 1] == '\n') i++;
                start = i + 1;
            }
        }
        lines[idx] = new String(b, start, len - start, CHARSET);

        // String.split(regex) with the default limit drops trailing empty fields.
        int end = count;
        while (end > 0 && lines[end - 1].isEmpty()) end--;
        return end == count ? lines : Arrays.copyOf(lines, end);
    }

    private byte[] readBytes(String name) {
        return null;/*
        try {
            return null;//Files.readAllBytes(resolve(name));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }*/
    }

    private Path resolve(String name) {
        String base = name.endsWith(".dyn") ? name.substring(0, name.length() - 4) : name;
        return null;//scriptDir.resolve(base + ".dyn");
    }
}