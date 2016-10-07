package org.gagauz.server.http.proto;

import org.gagauz.server.api.Sequence;

public class KeyValueSequence implements Sequence {

	Sequence key;
	Sequence value;
	Sequence current;

	@Override
	public boolean canAdvance() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void stop(String string) {
		current.stop(string);
	}

	@Override
	public boolean isStop(int read) {
		return current.isStop(read);
	}

	@Override
	public void handleExceptoin(Exception e) {

	}

	@Override
	public Sequence next() {
		// TODO Auto-generated method stub
		return null;
	}

}
