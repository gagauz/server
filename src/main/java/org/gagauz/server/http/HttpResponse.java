package org.gagauz.server.http;

import org.gagauz.server.Response;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class HttpResponse extends Response {

    private int bufferSize = 1024;
    private HttpResult status = HttpResult.OK_200;
    private Charset charset = Charset.defaultCharset();
    private final Map<String, String> headers;
    protected final ByteArrayOutputStream body;

    public HttpResponse(Socket socket) throws IOException {
        super(socket);
        headers = new HashMap<String, String>();
        body = new ByteArrayOutputStream();
    }

    public void addHeader(String name, Object value) {
        headers.put(name, String.valueOf(value));
    }

    @Override
    public void commit() throws IOException {
        getOutputStream().write(("HTTP/1.1 " + status.getStatus() + " " + status.getMessage() + "\r\n").getBytes(charset));
        if (body.size() > 0) {
            addHeader("Content-Length", body.size());
        }
        for (Entry<String, String> e : headers.entrySet()) {
            getOutputStream().write(e.getKey().getBytes(charset));
            getOutputStream().write(':');
            getOutputStream().write(e.getValue().getBytes(charset));
            getOutputStream().write('\r');
            getOutputStream().write('\n');
        }
        getOutputStream().write('\r');
        getOutputStream().write('\n');
        body.writeTo(getOutputStream());
        getOutputStream().flush();
        super.commit();
    }

    public HttpResult getStatus() {
        return status;
    }

    public void setStatus(HttpResult status) {
        this.status = status;
    }

    public Charset getCharset() {
        return charset;
    }

    public void setCharset(Charset charset) {
        this.charset = charset;
    }

    public void print(InputStream is) throws IOException {
        int r = 0;
        byte[] b = new byte[bufferSize];
        while ((r = is.read(b)) > -1) {
            body.write(b, 0, r);
        }
        is.close();
    }

    public void print(String string) throws IOException {
        if (string != null) {
            body.write(string.toString().getBytes(charset));
        }
    }

    public void println(String string) throws IOException {
        body.write(string.getBytes(charset));
        body.write('\r');
        body.write('\n');
    }
}
