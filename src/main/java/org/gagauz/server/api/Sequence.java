package org.gagauz.server.api;

public interface Sequence {

	boolean canAdvance();

	void stop(String string);

	boolean isStop(int read);

	void handleExceptoin(Exception e);

	Sequence next();
}
