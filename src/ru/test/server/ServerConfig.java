package ru.test.server;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class ServerConfig {
    private static int serverThreadCount = 10;
    private static List<RequestProcessor> requestProcessors = new ArrayList<RequestProcessor>();

    private ServerConfig() {
    }

    public static int getServerThreadCount() {
        return serverThreadCount;
    }

    public static void setServerThreadCount(int arg) {
        serverThreadCount = arg;
    }

    public static Collection<RequestProcessor> getRequestProcessors() {
        return Collections.unmodifiableList(requestProcessors);
    }

    public static void addRequestProcessor(RequestProcessor arg) {
        requestProcessors.add(arg);
    }

}
