package ru.test.server.http;

import ru.test.server.Layer;
import ru.test.server.Request;
import ru.test.server.Response;
import ru.test.server.utils.IOUtils;

import java.io.IOException;

public class HttpLayer implements Layer<HttpRequest, HttpResponse> {

    @Override
    public boolean accept(Request request) throws IOException {
        String line;
        while (request.getInputStream().available() > 0) {
            line = IOUtils.readLine(request.getInputStream());
            System.out.print(line);
        }
        return false;
    }

    @Override
    public HttpRequest down(Request request) {
        return null;
    }

    @Override
    public HttpResponse up(Response request) {
        // TODO Auto-generated method stub
        return null;
    }

}
