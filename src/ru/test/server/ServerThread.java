package ru.test.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ServerThread implements Runnable {

    private final Socket socket;
    private final RequestProcessor<Request, Response> processor;

    public ServerThread(Socket socket, RequestProcessor<Request, Response> processor) {
        this.socket = socket;
        this.processor = processor;
    }

    @Override
    public void run() {
        System.out.println("Start new thread " + Thread.currentThread().getId());
        try {

            Request request = new Request() {
                @Override
                public InputStream getInputStream() throws IOException {
                    return socket.getInputStream();
                }
            };

            Response response = new Response() {
                @Override
                public OutputStream getOutputStream() throws IOException {
                    return socket.getOutputStream();
                }

            };

            processor.process(request, response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
