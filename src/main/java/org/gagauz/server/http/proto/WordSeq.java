package org.gagauz.server.http.proto;

public abstract class WordSeq extends LineSeq {
	@Override
	public boolean isStop(int read) {
		return super.isStop(read) || read == ' ' || read == '\t';
	}
}
