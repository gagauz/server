package ru.test.server.http;

import ru.test.server.Response;

public interface HttpResponse extends Response {
    void addHeader(String name, String value);

    void addCookie(Cookie cookie);
}
