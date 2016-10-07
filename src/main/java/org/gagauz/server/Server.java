package org.gagauz.server;

import java.io.IOException;

import org.gagauz.server.api.ClientConnection;
import org.gagauz.server.api.ConnectionListener;
import org.gagauz.server.api.ServerConnection;
import org.gagauz.server.api.ServerConnectionFactory;
import org.gagauz.server.api.ThreadPool;

public abstract class Server {
	private ServerConnection serverSocket;

	protected abstract ServerConnectionFactory getServerConnectionFactory();

	protected abstract ConnectionListener getConnectionListener();

	protected abstract ThreadPool getThreadPool();

	public final synchronized void start(int port) {
		serverSocket = getServerConnectionFactory().createServerConnection(port);
		try {
			while (acceptClient(serverSocket.accept())) {
				// empty cycle
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private final boolean acceptClient(final ClientConnection connection) {
		getThreadPool().append(getConnectionListener().startListen(connection));
		return true;
	}

	public final void stop() {
		synchronized (this) {
			try {
				serverSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
