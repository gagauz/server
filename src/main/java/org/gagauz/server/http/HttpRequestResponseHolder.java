package org.gagauz.server.http;

public class HttpRequestResponseHolder {
    private static final ThreadLocal<HttpRequest> requestHolder = new ThreadLocal<>();
    private static final ThreadLocal<HttpResponse> responseHolder = new ThreadLocal<>();

    public static void set(HttpRequest request, HttpResponse response) {
        requestHolder.set(request);
        responseHolder.set(response);
    }

    public static HttpRequest getRequest() {
        return requestHolder.get();
    }

    public static HttpResponse getResponse() {
        return responseHolder.get();
    }
}
