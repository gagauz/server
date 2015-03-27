package org.gagauz.server.http;

import org.gagauz.server.Server;
import org.gagauz.server.SocketAcceptor;

import java.net.Socket;

public class HttpServer extends Server {

    private String documentRoot;

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

}
