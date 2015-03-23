package ru.test.server.http;

import ru.test.server.RequestProcessor;
import ru.test.server.utils.IOUtils;

import java.io.IOException;

public class HttpResultProcessor implements RequestProcessor<HttpRequest, HttpResponse> {

    @Override
    public boolean process(HttpRequest request, HttpResponse response) throws IOException {
        String line;
        while (request.getInputStream().available() > 0) {
            line = IOUtils.readLine(request.getInputStream());
            System.out.print(line);
        }
        response.getOutputStream().write("DONE".getBytes());
        response.getOutputStream().flush();
        response.getOutputStream().close();
        return true;
    }

}
