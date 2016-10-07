package org.gagauz.server.http.proto;

import org.gagauz.server.api.Sequence;

public abstract class LineSeq implements Sequence {

	private boolean stop = false;

	@Override
	public boolean canAdvance() {
		return !stop;
	}

	@Override
	public abstract void stop(String string);

	@Override
	public boolean isStop(int read) {
		return read == '\n' || read == '\r';
	}

	@Override
	public void handleExceptoin(Exception e) {
		stop = true;
	}

	@Override
	public abstract Sequence next();

}
