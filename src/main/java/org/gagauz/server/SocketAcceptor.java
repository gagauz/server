package org.gagauz.server;

import java.io.IOException;
import java.net.Socket;

public class SocketAcceptor implements Runnable {
    protected final Socket socket;
    protected final SocketProcessor processor;

    public SocketAcceptor(Socket socket, SocketProcessor processor) {
        this.socket = socket;
        this.processor = processor;
    }

    @Override
    public void run() {
        System.out.println("Accept socket in thread " + Thread.currentThread().getId());
        try {
            long time = System.currentTimeMillis();
            processor.process(socket);
            System.out.println("Processing time " + (System.currentTimeMillis() - time) + " ms");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
