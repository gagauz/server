package org.gagauz.server.http;

import org.gagauz.server.Request;

import java.io.IOException;
import java.net.Socket;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class HttpRequest extends Request {

    private String method;
    private String requestUri;
    private String protocolVersion;
    private final Map<String, String> headers = new HashMap<String, String>();
    private final Map<String, Object> parameters = new HashMap<String, Object>();

    public HttpRequest(Socket socket) throws IOException {
        super(socket);
        init();
    }

    private void init() throws IOException {
        method = readWord();
        requestUri = readWord();
        protocolVersion = readWord();
        String l = "";
        while (!(l = readLine()).equals("")) {
            int p = l.indexOf(':');
            if (p > -1) {
                headers.put(l.substring(0, p).trim(), l.substring(p + 1).trim());
            }
        }
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
        int i = requestUri.indexOf('?');
        return i > -1 ? requestUri.substring(i) : requestUri;
    }

    public String getProtocolVersion() {
        return protocolVersion;
    }

    public String getQuery() {
        int i = requestUri.indexOf('?');
        return i > -1 ? requestUri.substring(i) : null;
    }

    public String getRequestUri() {
        return requestUri;
    }
}
