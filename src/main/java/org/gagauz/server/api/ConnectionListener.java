package org.gagauz.server.api;

import java.io.IOException;

public class ConnectionListener {
	private volatile boolean listen;

	public final Runnable startListen(ClientConnection connection) {
		listen = true;
		ConnectionReader reader = getReader(connection);
		return () -> {
			System.out.println("Start listening");
			while (listen) {
				reader.read();
			}
		};
	}

	public ConnectionReader getReader(final ClientConnection connection) {
		final byte[] bytes = new byte[1024];
		return () -> {
			try {
				int read = connection.getInput().read(bytes);
				if (read > 0) {
					System.out.println(new String(bytes, 0, read));
				} else if (read < 0) {
					stopListen();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		};
	}

	public void stopListen() {
		System.out.println("Stop listening");
		listen = false;
	}
}
