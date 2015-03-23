package ru.test.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    public Server(int port) {
        try {
            ServerSocket server = new ServerSocket(port);
            ExecutorService es = Executors.newFixedThreadPool(ServerConfig.getServerThreadCount(), new ServerSocketThreadFactory());
            Socket socket;
            while ((socket = server.accept()) != null) {
                es.execute(new ServerThread(socket, new HttpResultProcessor()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
