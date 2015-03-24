package ru.test.server;

import java.io.IOException;
import java.io.InputStream;

import ru.test.server.utils.IOUtils;

public class Request {
    private final InputStream inputStream;

    public Request(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public InputStream getInputStream() throws IOException {
        return inputStream;
    }

    public String readLine() throws IOException {
        return IOUtils.readLine(inputStream);
    }
}
