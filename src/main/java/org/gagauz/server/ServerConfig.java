package org.gagauz.server;


public class ServerConfig {
    private static int serverThreadCount = 10;

    private ServerConfig() {
    }

    public static int getServerMaxThreadCount() {
        return serverThreadCount;
    }

    public static void setServerThreadCount(int arg) {
        serverThreadCount = arg;
    }

}
