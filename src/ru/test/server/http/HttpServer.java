package ru.test.server.http;

import java.io.IOException;

import ru.test.server.Server;

public class HttpServer extends Server<HttpRequest, HttpResponse> {
    @Override
    public void process(HttpRequest request, HttpResponse response) throws IOException {
        // TODO Auto-generated method stub
        super.process(request, response);
    }
}
