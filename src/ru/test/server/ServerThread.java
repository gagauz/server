package ru.test.server;

import java.io.IOException;
import java.net.Socket;

public class ServerThread implements Runnable {

    private final Socket socket;
    private final Server server;

    public ServerThread(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;
    }

    @Override
    public void run() {
        System.out.println("Start new thread " + Thread.currentThread().getId());
        try {
            Request request = new Request(socket.getInputStream());
            Response response = new Response(socket.getOutputStream());
            server.process(request, response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
