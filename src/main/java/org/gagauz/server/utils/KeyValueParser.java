package org.gagauz.server.utils;


public class KeyValueParser {

	public static class ParseEventHandler {
		public void onKeyValue(String key, String value) {
		}
	}

	public static <T> void parse(String data, char keyValueSeparator,
			char pairSeparator, ParseEventHandler handler) {
		StringBuilder p = new StringBuilder();
		String lastKey = null;
		for (int i = 0; i < data.length(); i++) {
			char c = data.charAt(i);
			if (c == keyValueSeparator) {
				lastKey = p.toString();
				p = new StringBuilder();
			} else if (c == pairSeparator) {
				handler.onKeyValue(lastKey, p.toString());
				lastKey = null;
				p = new StringBuilder();
			} else {
				p.append(c);
			}
		}
		if (null != lastKey) {
			handler.onKeyValue(lastKey, p.toString());
		} else if (p.length() > 0) {
			handler.onKeyValue(p.toString(), "");
		}
	}
}
