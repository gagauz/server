package org.gagauz.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class Server {
    private ExecutorService executorService;
    private ServerSocket server;

    private String host = "localhost";
    private int port;

    public synchronized void start(int port) {
        try {
            server = new ServerSocket(port);
            executorService = Executors.newFixedThreadPool(ServerConfig.getServerThreadCount());
            Socket socket;
            while ((socket = server.accept()) != null) {
                executorService.execute(getAcceptor(socket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stop() throws IOException {
        executorService.shutdown();
        server.close();
    }

    protected SocketAcceptor getAcceptor(Socket socket) {
        return new SocketAcceptor(socket, new SocketProcessor());
    }

}
