package org.gagauz.server.api;

import java.io.IOException;

public interface ServerConnection {

	ClientConnection accept() throws IOException;

	void close() throws IOException;

}
