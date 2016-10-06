package org.gagauz.server.api.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

import org.gagauz.server.api.Connection;

public class IOConnection implements Connection {

	private Socket socket;

	public IOConnection(Socket socket) {
		this.socket = socket;
	}

	@Override
	public InputStream getInput() {
		try {
			return socket.getInputStream();
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}

	@Override
	public OutputStream getOutput() {
		try {
			return socket.getOutputStream();
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}

	@Override
	public void close() {
		try {
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public int getLocalPort() {
		return socket.getLocalPort();
	}

	@Override
	public InetAddress getLocalAddress() {
		return socket.getLocalAddress();
	}

	@Override
	public int getPort() {
		return socket.getPort();
	}

	@Override
	public InetAddress getInetAddress() {
		return socket.getInetAddress();
	}

}
