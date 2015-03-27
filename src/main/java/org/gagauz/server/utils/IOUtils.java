package org.gagauz.server.utils;

import java.io.IOException;
import java.io.InputStream;

public class IOUtils {

    public static String readLine(InputStream is) throws IOException {

        if (is.available() > 0) {
            char c;
            int i;
            StringBuilder sb = new StringBuilder();
            while ((i = is.read()) != -1) {
                c = (char) i;
                if (c == '\n') {
                    break;
                }
                if (c != '\r') {
                    sb.append(c);
                }
            }
            return sb.toString();
        }
        return null;
    }

    public static String readWord(InputStream is) throws IOException {
        char c;
        int i;
        StringBuilder sb = new StringBuilder();
        while ((i = is.read()) != -1) {
            c = (char) i;
            if (c == ' ' || c == '\n' || c == '\t') {
                break;
            }
            if (c != '\r') {
                sb.append(c);
            }
        }
        return sb.toString();
    }

}
