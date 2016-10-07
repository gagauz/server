package org.gagauz.server.api.io;

import java.io.IOException;
import java.net.ServerSocket;

import org.gagauz.server.api.ClientConnection;
import org.gagauz.server.api.ServerConnection;

public class IOServerConnection implements ServerConnection {

	ServerSocket socket;

	public IOServerConnection(int port) {
		try {
			socket = new ServerSocket(port);
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}

	@Override
	public ClientConnection accept() throws IOException {
		return new IOConnection(socket.accept());
	}

	@Override
	public void close() throws IOException {
		socket.close();
	}

}
