package org.gagauz.server;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.gagauz.server.api.Connection;
import org.gagauz.server.utils.IOUtils;

public class Request {
	private final Connection socket;
	private final InputStream inputStream;
	private byte[] body;

	public Request(Connection connection) {
		socket = connection;
		inputStream = connection.getInput();
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

	public byte[] getBytes() throws IOException {
		if (null == body) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			if (inputStream.available() > 0) {
				byte[] buff = new byte[1024];
				int r;
				while ((r = inputStream.read(buff)) != -1) {
					baos.write(buff, 0, r);
					if (r < 1024) {
						break;
					}
				}
			}
			body = baos.toByteArray();
		}
		return body;
	}
}
