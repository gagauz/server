package org.gagauz.server.api;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;

public interface ClientConnection {
	InputStream getInput();

	OutputStream getOutput();

	void close();

	int getLocalPort();

	InetAddress getLocalAddress();

	int getPort();

	InetAddress getInetAddress();

	boolean isClosed();
}
