package ru.test.server;

import java.io.IOException;

public interface RequestProcessor<XRequest extends Request, XResponse extends Response> {
    boolean process(XRequest request, XResponse response) throws IOException;

}
