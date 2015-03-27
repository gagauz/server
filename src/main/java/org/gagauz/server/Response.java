package org.gagauz.server;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class Response {
    private boolean commited;
    private final OutputStream outputStream;

    public Response(Socket socket) throws IOException {
        this.outputStream = socket.getOutputStream();
    }

    public synchronized OutputStream getOutputStream() throws IOException {
        return outputStream;
    }

    public boolean isCommited() {
        return commited;
    }

    public synchronized void commit() throws IOException {
        if (commited) {
            throw new IllegalStateException();
        }
        commited = true;
        outputStream.close();
    }
}
