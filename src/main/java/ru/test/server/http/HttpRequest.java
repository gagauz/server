package ru.test.server.http;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import ru.test.server.Request;

public class HttpRequest extends Request {

    private String method;
    private String path;
    private final Map<String, String> headers = new HashMap<>();

    public HttpRequest(InputStream inputStream) {
        super(inputStream);
    }

    private void init() throws IOException {
        getInputStream().reset();
        String line = readLine();
        int pos = line.indexOf(' ');
        method = line.substring(0, pos);
        int pos2 = line.indexOf(' ', pos);
        path = line.substring(pos, pos2);

    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public Collection<Cookie> getCookies() {
        return null;
    }

    public String getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }
}
