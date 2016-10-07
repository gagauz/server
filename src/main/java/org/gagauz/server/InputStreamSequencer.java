package org.gagauz.server;

import java.io.InputStream;

import org.gagauz.server.api.Sequence;

public class InputStreamSequencer implements Runnable {
	private InputStream inputStream;
	private Sequence sequence;

	public InputStreamSequencer(InputStream inputStream, Sequence sequence) {
		this.inputStream = inputStream;
	}

	@Override
	public void run() {
		StringBuilder builder = new StringBuilder();
		while (sequence.canAdvance()) {
			int read;
			try {
				read = inputStream.read();
				if (read < 0) {
					sequence.stop(builder.toString());
					break;
				}
				if (sequence.isStop(read)) {
					sequence = sequence.next();
				} else {
					builder.append((char) read);
				}
			} catch (Exception e) {
				sequence.handleExceptoin(e);
			}
		}
	}
}
