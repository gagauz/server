package org.gagauz.server.api;

public interface ThreadPool {
	void append(Runnable thread);
}
