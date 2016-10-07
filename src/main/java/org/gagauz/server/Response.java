package org.gagauz.server;

import java.io.IOException;
import java.io.OutputStream;

import org.gagauz.server.api.ClientConnection;

public class Response {
	private boolean commited;
	private final OutputStream outputStream;

	public Response(ClientConnection connection) {
		outputStream = connection.getOutput();
	}

	public synchronized OutputStream getOutputStream() throws IOException {
		return outputStream;
	}

	public boolean isCommited() {
		return commited;
	}

	public synchronized void commit() throws IOException {
		if (commited) {
			throw new IllegalStateException();
		}
		commited = true;
		outputStream.close();
	}
}
