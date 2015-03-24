package ru.test.server;


public class ServerConfig {
    private static int serverThreadCount = 10;
    private static Processor<Request, Response> requestProcessor;

    private ServerConfig() {
    }

    public static int getServerThreadCount() {
        return serverThreadCount;
    }

    public static void setServerThreadCount(int arg) {
        serverThreadCount = arg;
    }

    public static Processor<Request, Response> getRequestProcessors() {
        return requestProcessor;
    }

    public static void setRequestProcessor(Processor<Request, Response> arg) {
        requestProcessor = arg;
    }

}
