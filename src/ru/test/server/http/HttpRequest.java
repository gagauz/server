package ru.test.server.http;

import ru.test.server.Request;

import java.util.Collection;
import java.util.Map;

public interface HttpRequest extends Request {
    Map<String, String> getHeaders();

    Collection<Cookie> getCookies();

    String getMethod();

    String getPath();
}
