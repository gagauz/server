package org.gagauz.server.http;

import org.gagauz.server.Request;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class HttpRequest extends Request {

    private Charset charset = Charset.defaultCharset();
    private String method;
    private String requestUri;
    private String protocolVersion;
    private final Map<String, String> headers = new HashMap<String, String>();
    private Map<String, Object> parameters;

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
        return i > -1 ? requestUri.substring(0, i) : requestUri;
    }

    public String getProtocolVersion() {
        return protocolVersion;
    }

    public String getQuery() {
        int i = requestUri.indexOf('?');
        return i > -1 ? requestUri.substring(i + 1) : null;
    }

    public String getRequestUri() {
        return requestUri;
    }

    public Map<String, Object> getParameters() throws IOException {
        if (null == parameters) {
            Map<String, Object> params = parseUrlEncodedParameters(getQuery(), urlEncodedDecoder);
            String ct = getHeaders().get("Content-Type");
            if (ct != null) {
                if (ct.startsWith("application/x-www-form-urlencoded")) {
                    params.putAll(parseUrlEncodedParameters(new String(getBytes(), getCharset()), urlEncodedDecoder));
                } else if (ct.startsWith("multipart/form-data")) {
                    String boundary = "";
                    String l;
                    while ((l = readLine()) != null) {
                        if (l.equals("--" + boundary)) {
                            String dispos = readLine();
                            int p = dispos.indexOf("name=\"");

                        }
                    }
                } else if (ct.startsWith("text/plain")) {
                    params.putAll(parseUrlEncodedParameters(new String(getBytes(), getCharset()), textPlainDecoder));
                }
            }
            parameters = params;
        }
        return parameters;
    }

    protected Map<String, Object> parseMultipartParameters(String data) {
        return null;
    }

    protected Map<String, Object> parseUrlEncodedParameters(String data, Decoder decoder) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        if (null != data) {
            data = decoder.decode(data, getCharset().name());
            StringBuilder p = new StringBuilder();
            String lastKey = null;
            for (int i = 0; i < data.length(); i++) {
                char c = data.charAt(i);
                if (c == '=') {
                    lastKey = p.toString();
                    p = new StringBuilder();
                } else if (c == '&') {
                    parameters.put(lastKey, p.toString());
                    lastKey = null;
                    p = new StringBuilder();
                } else {
                    p.append(c);
                }
            }
            if (null != lastKey) {
                parameters.put(lastKey, p.toString());
            } else if (p.length() > 0) {
                parameters.put(p.toString(), "");
            }
        }
        return parameters;
    }

    public Charset getCharset() {
        return charset;
    }

    private static final Decoder urlEncodedDecoder = new Decoder() {

        @Override
        public String decode(String string, String charset) {
            try {
                return URLDecoder.decode(string, charset);
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        }

    };

    private static final Decoder textPlainDecoder = new Decoder() {

        @Override
        public String decode(String string, String charset) {
            return string.replace('+', ' ');
        }

    };

    private interface Decoder {
        String decode(String string, String charset);
    }
}
