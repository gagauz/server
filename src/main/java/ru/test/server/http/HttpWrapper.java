package ru.test.server.http;

import java.io.IOException;

import ru.test.server.Processor;
import ru.test.server.ResponseProcessor;

public class HttpWrapper implements Processor<Request, Response> {

    @Override
    public ResponseProcessor<Response> in(Request request) throws IOException {
        return null;
    }

}
