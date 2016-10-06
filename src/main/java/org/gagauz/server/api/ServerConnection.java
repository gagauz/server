package org.gagauz.server.api;

import java.io.IOException;

public interface ServerConnection {

	Connection accept() throws IOException;

	void close() throws IOException;

}
