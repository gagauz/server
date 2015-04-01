package org.gagauz.server.http;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpCookie;
import java.net.Socket;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import org.gagauz.server.Request;
import org.gagauz.server.utils.KeyValueParser;
import org.gagauz.server.utils.KeyValueParser.ParseEventHandler;

import com.gagauz.common.utils.C;

public class HttpRequest extends Request {

    private final Charset charset = Charset.defaultCharset();
    private String method;
    private String path;
    private String query;
    private String requestUri;
    private String protocolVersion;
    private final Map<String, String> headers = new HashMap<String, String>();
    private Map<String, Object> parameters;
    private Map<String, HttpCookie> cookies;
    private HttpSession session;
    private final HttpServer httpServer;

    public HttpRequest(Socket socket, HttpServer httpServer) throws IOException {
        super(socket);
        this.httpServer = httpServer;
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

    public String getMethod() {
        return method;
    }

    public String getPath() {
        if (null == path) {
            int i = requestUri.indexOf('?');
            path = URLDecoder.decode(i > -1 ? requestUri.substring(0, i) : requestUri);
        }
        return path;
    }

    public String getProtocolVersion() {
        return protocolVersion;
    }

    public String getQuery() {
        if (null == query) {
            int i = requestUri.indexOf('?');
            query = i > -1 ? URLDecoder.decode(requestUri.substring(i + 1)) : null;
        }
        return query;
    }

    public String getRequestUri() {
        return requestUri;
    }

    public Map<String, Object> getParameters() throws IOException {
        if (null == parameters) {
            Map<String, Object> params = parseUrlEncodedParameters(getQuery(), textPlainDecoder);
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

    public Map<String, HttpCookie> getCookies() {
        if (null == cookies) {
            final Map<String, HttpCookie> cookies0 = C.newHashMap();

            ParseEventHandler handler = new ParseEventHandler() {
                @Override
                public void onKeyValue(String key, String value) {
                    key = key.trim();
                    HttpCookie cookie = new HttpCookie(key, value);
                    cookies0.put(key, cookie);
                }
            };
            String cookie = getHeaders().get("Cookie");
            if (null != cookie) {
                KeyValueParser.parse(cookie, '=', ';', handler);
            }
            cookie = getHeaders().get("Cookie2");
            if (null != cookie) {
                KeyValueParser.parse(cookie, '=', ';', handler);
            }
            cookies = cookies0;
        }
        return cookies;
    }

    protected Map<String, Object> parseMultipartParameters(String data) {
        return null;
    }

    protected Map<String, Object> parseUrlEncodedParameters(String data, Decoder decoder) {
        final Map<String, Object> parameters = new HashMap<String, Object>();
        if (null != data) {
            data = decoder.decode(data, getCharset().name());
            ParseEventHandler handler = new ParseEventHandler() {
                @Override
                public void onKeyValue(String key, String value) {
                    parameters.put(key, value);
                }
            };
            KeyValueParser.parse(data, '=', '&', handler);
        }
        return parameters;
    }

    public Charset getCharset() {
        return charset;
    }

    public HttpSession getSession() {
        return getSession(false);
    }

    public HttpSession getSession(boolean create) {
        if (null == session) {
            session = httpServer.getSession(create);
        }
        return session;
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
            return string;
        }
    };

    private interface Decoder {
        String decode(String string, String charset);
    }
}
