package ru.test.server;

import java.io.IOException;
import java.io.InputStream;

public interface Request {
    InputStream getInputStream() throws IOException;
}
