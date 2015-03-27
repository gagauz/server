package org.gagauz.server.http;

import org.gagauz.server.Server;
import org.gagauz.server.SocketAcceptor;

import java.net.Socket;
import java.nio.charset.Charset;

public class HttpServer extends Server {

    private String documentRoot;
    private Charset charset = Charset.defaultCharset();

    public String getDocumentRoot() {
        return documentRoot;
    }

    public void setDocumentRoot(String documentRoot) {
        this.documentRoot = documentRoot;
    }

    @Override
    protected SocketAcceptor getAcceptor(Socket socket) {
        return new SocketAcceptor(socket, new HttpProcessor(this));
    }

    public Charset getCharset() {
        return charset;
    }

    public void setCharset(Charset charset) {
        this.charset = charset;
    }

}
