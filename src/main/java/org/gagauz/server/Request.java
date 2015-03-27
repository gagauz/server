package org.gagauz.server;

import org.gagauz.server.utils.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class Request {
    private final Socket socket;
    private final InputStream inputStream;

    public Request(Socket socket) throws IOException {
        this.socket = socket;
        this.inputStream = socket.getInputStream();
    }

    public InputStream getInputStream() throws IOException {
        return inputStream;
    }

    public String readLine() throws IOException {
        return IOUtils.readLine(inputStream);
    }

    public String readWord() throws IOException {
        return IOUtils.readWord(inputStream);
    }

    public int getServerPort() {
        return socket.getLocalPort();
    }

    public String getServerAddress() {
        return socket.getLocalAddress().getHostAddress();
    }

    public String getServerHostname() {
        return socket.getLocalAddress().getHostName();
    }

    public int getRemotePort() {
        return socket.getPort();
    }

    public String getRemoteAddress() {
        return socket.getInetAddress().getHostAddress();
    }

}
