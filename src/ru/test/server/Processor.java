package ru.test.server;

import java.io.IOException;

public interface Processor<X extends Request, Y extends Response> {
    void process(X request, Y response) throws IOException;
}
