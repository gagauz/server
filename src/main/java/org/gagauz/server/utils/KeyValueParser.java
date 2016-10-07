package org.gagauz.server.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

public class KeyValueParser {

	private static final String EMPTY = "";
	private boolean quote;
	private String lastKey;
	private KeyValueEvent handler;
	private StringBuilder p = new StringBuilder();
	private char keyValueSeparator;
	private char pairSeparator;

	private KeyValueParser(char keyValueSeparator2, char pairSeparator2, KeyValueEvent handler2) {
		keyValueSeparator = keyValueSeparator2;
		pairSeparator = pairSeparator2;
		handler = handler2;
	}

	public static void parse(String string, char keyValueSeparator, char pairSeparator, KeyValueEvent handler) {
		if (null == string) {
			return;
		}
		KeyValueParser parser = new KeyValueParser(keyValueSeparator, pairSeparator, handler);
		for (int i = 0; i < string.length(); i++) {
			parser.onChar(string.charAt(i));
		}
		parser.finish();
	}

	public static void parse(InputStream stream, char keyValueSeparator, char pairSeparator, KeyValueEvent handler) {
		KeyValueParser parser = new KeyValueParser(keyValueSeparator, pairSeparator, handler);
		int i;
		try {
			while ((i = stream.read()) > 0) {
				parser.onChar((char) i);
			}
			parser.finish();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static void parse(Reader reader, char keyValueSeparator, char pairSeparator, KeyValueEvent handler) {
		KeyValueParser parser = new KeyValueParser(keyValueSeparator, pairSeparator, handler);
		int i;
		try {
			while ((i = reader.read()) > 0) {
				parser.onChar((char) i);
			}
			parser.finish();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

	}

	private void onChar(char c) {
		if (c == '"') {
			quote = !quote;
		}
		if (c == keyValueSeparator && !quote) {
			lastKey = p.toString();
			p = new StringBuilder();
		} else if (c == pairSeparator && !quote) {
			handler.onKeyValue(lastKey, p.toString());
			lastKey = null;
			p = new StringBuilder();
		} else {
			p.append(c);
		}
	}

	private void finish() {
		if (null != lastKey) { // ends with a value
			handler.onKeyValue(lastKey, p.toString());
		} else if (p.length() > 0) { // ends with a key w/o value
			handler.onKeyValue(p.toString(), EMPTY);
		}
	}

}
