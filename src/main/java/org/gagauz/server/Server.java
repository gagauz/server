package org.gagauz.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class Server {
	private ExecutorService executorService;
	private ServerSocket server;

	private String host = "localhost";
	private int port;

	public synchronized void start(int port) {
		try {
			System.out.println("Starting server on " + port + " port");
			server = new ServerSocket(port);
			System.out.println("Allocating " + ServerConfig.getServerMaxThreadCount() + " connection threads.");
			executorService = Executors.newFixedThreadPool(ServerConfig.getServerMaxThreadCount());
			Socket socket;
			System.out.println("Server started, waiting for connections...");
			while ((socket = server.accept()) != null) {
				executorService.execute(getAcceptor(socket));
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void stop() throws IOException {
		executorService.shutdown();
		server.close();
	}

	protected SocketAcceptor getAcceptor(Socket socket) {
		return new SocketAcceptor(socket, new SocketProcessor());
	}

}
