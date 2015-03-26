package ru.test.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server<X extends Request, Y extends Response> implements Processor<X, Y> {
    private ExecutorService executorService;
    private ServerSocket server;

    public void start(int port) {
        try {
            server = new ServerSocket(port);
            executorService = Executors.newFixedThreadPool(ServerConfig.getServerThreadCount(), new ServerSocketThreadFactory());
            Socket socket;
            while ((socket = server.accept()) != null) {
                executorService.execute(new ServerThread(socket, this));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stop() throws IOException {
        executorService.shutdown();
        server.close();
    }

    @Override
    public void process(X request, Y response) throws IOException {
    }

}
