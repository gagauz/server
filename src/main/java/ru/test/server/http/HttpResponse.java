package ru.test.server.http;

import java.io.OutputStream;

import ru.test.server.Response;

public class HttpResponse extends Response {

    public HttpResponse(OutputStream outputStream) {
        super(outputStream);
    }
}
