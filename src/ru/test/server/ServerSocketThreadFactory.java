package ru.test.server;

import java.util.concurrent.ThreadFactory;

public class ServerSocketThreadFactory implements ThreadFactory {
    @Override
    public Thread newThread(Runnable r) {
        return new Thread(r, "server-thread");
    }
}
