package ru.test.server;

import java.io.IOException;
import java.io.OutputStream;

public class Response {
    private final OutputStream outputStream;

    public Response(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public OutputStream getOutputStream() throws IOException {
        return outputStream;
    }
}
