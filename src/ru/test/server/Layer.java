package ru.test.server;

import java.io.IOException;

public interface Layer<XRequest extends Request, XResponse extends Response> {
    boolean accept(Request request) throws IOException;

    XRequest down(Request request) throws IOException;

    XResponse up(Response request) throws IOException;
}
