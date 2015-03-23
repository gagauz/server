package ru.test.server.utils;

import java.io.IOException;
import java.io.InputStream;

public class IOUtils {
    private static final char NEW_LINE = '\n';

    public static String readLine(InputStream is) throws IOException {
        char c;
        StringBuilder sb = new StringBuilder();
        while (is.available() > 0 && (c = (char) is.read()) != NEW_LINE) {
            sb.append(c);
        }
        return sb.toString();
    }
}
