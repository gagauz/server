package org.gagauz.server.utils;

import java.io.IOException;
import java.io.InputStream;

public class IOUtils {

	public static String readLine(InputStream is) throws IOException {

		if (is.available() > 0) {
			char c;
			int i;
			StringBuilder sb = new StringBuilder();
			while ((i = is.read()) != -1) {
				c = (char) i;
				if (c == '\n') {
					break;
				}
				if (c != '\r') {
					sb.append(c);
				}
			}
			return sb.toString();
		}
		return null;
	}

	public static String readLine(InputStream is, Runnable closeHandler) throws IOException {
		char c;
		int i;
		int read = 0;
		StringBuilder sb = new StringBuilder();
		while (true) {
			i = is.read();
			if (i == -1) {
				closeHandler.run();
				break;
			}
			c = (char) i;
			read++;
			if (c == '\n' || c == '\r') {
				if (read > 1) {
					break;
				} else {
					continue;
				}
			}
			sb.append(c);
		}
		return sb.toString();

	}

	public static String readWord(InputStream is) throws IOException {
		char c;
		int i;
		StringBuilder sb = new StringBuilder();
		while ((i = is.read()) != -1) {
			c = (char) i;
			if (c == ' ' || c == '\n' || c == '\t') {
				break;
			}
			if (c != '\r') {
				sb.append(c);
			}
		}
		return sb.toString();
	}

	public static String readWord(InputStream is, Runnable closeHandler) throws IOException {
		char c;
		int i;
		int read = 0;
		StringBuilder sb = new StringBuilder();
		while (true) {
			i = is.read();
			if (i == -1) {
				closeHandler.run();
				break;
			}
			c = (char) i;
			read++;
			if (c == ' ' || c == '\t' || c == '\n' || c == '\r') {
				if (read > 0) {
					break;
				} else {
					continue;
				}
			}
			sb.append(c);
		}
		return sb.toString();
	}

	public static class ReaderImpl implements Reader {

		protected boolean shouldRead = true;
		protected StringBuilder word = new StringBuilder();
		protected StringBuilder line = new StringBuilder();

		@Override
		public boolean shouldRead() {
			return shouldRead;
		}

		@Override
		public void handleClose() {
			shouldRead = false;
		}

		@Override
		public void receive(char c) {
			if (' ' == c || '\t' == c || '\n' == c || '\r' == c) {
				if (word.length() > 0) {
					handleWord(word.toString());
				}
				if (line.length() > 0) {
					handleLine(line.toString());
				}
			} else if ('\n' == c || '\r' == c) {
				if (line.length() > 0) {
					handleLine(line.toString());
				}
			} else {
				line.append(c);
			}
		}

		void handleWord(String string) {

		}

		void handleLine(String string) {

		}

	}

	public static class WordReader implements Reader {

		boolean shouldread = true;
		StringBuilder word = new StringBuilder();

		@Override
		public boolean shouldRead() {
			return shouldread;
		}

		@Override
		public void handleClose() {
			shouldread = false;
		}

		@Override
		public void receive(char c) {
			if (' ' == c || '\t' == c || '\n' == c || '\r' == c) {
				shouldread = false;
			} else {
				word.append(c);
			}
		}
	}

	public static class LineReader implements Reader {

		boolean shouldread = true;
		StringBuilder line = new StringBuilder();

		@Override
		public boolean shouldRead() {
			return shouldread;
		}

		@Override
		public void handleClose() {
			shouldread = false;
		}

		@Override
		public void receive(char c) {
			if ('\n' == c || '\r' == c) {
				shouldread = false;
			} else {
				line.append(c);
			}
		}
	}

	public static interface Reader {

		boolean shouldRead();

		void handleClose();

		void receive(char c);
	}

	public static void read(InputStream is, Reader reader) throws IOException {
		char c;
		int i;
		while (reader.shouldRead()) {
			i = is.read();
			if (i == -1) {
				reader.handleClose();
				break;
			}
			c = (char) i;
			reader.receive(c);
		}
	}

}
