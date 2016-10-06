package org.gagauz.server.api;

public interface ServerConnectionFactory {

	ServerConnection createServerConnection(int port);

}
