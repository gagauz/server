package ru.test;

import ru.test.server.Server;
import ru.test.server.ServerConfig;
import ru.test.server.http.HttpGetResultProcessor;

public class Test {
    public static void main(String[] args) {
        ServerConfig.addRequestProcessor(new HttpGetResultProcessor());
        ServerConfig.setServerThreadCount(10);
        Server server = new Server(999);
    }
}
