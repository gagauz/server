package org.gagauz.server;

import java.io.IOException;

import org.gagauz.server.api.Connection;
import org.gagauz.server.api.Processor;
import org.gagauz.server.api.ServerConnection;
import org.gagauz.server.api.ServerConnectionFactory;
import org.gagauz.server.api.ThreadPool;

public abstract class Server {
	private ServerConnection serverSocket;

	protected abstract ServerConnectionFactory getServerConnectionFactory();

	protected abstract Processor getProcessor();

	protected abstract ThreadPool getThreadPool();

	public final synchronized void start(int port) {
		System.out.println("Starting server on " + port + " port");
		serverSocket = getServerConnectionFactory().createServerConnection(port);
		System.out.println("Server started, waiting for connections...");
		try {
			while (acceptConnection(serverSocket.accept())) {
				// empty cycle
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private final boolean acceptConnection(final Connection connection) {
		getThreadPool().append(() -> getProcessor().process(connection));
		return true;
	}

	public final synchronized void stop() {
		try {
			serverSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
