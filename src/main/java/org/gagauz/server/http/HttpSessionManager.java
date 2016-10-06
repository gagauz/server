package org.gagauz.server.http;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class HttpSessionManager {
	private static Random random = new Random();
	private static final Map<String, HttpSession> sessionMap = new HashMap<>();

	public HttpSession find(String name) {
		return sessionMap.get(name.toLowerCase());
	}

	public HttpSession create() {
		String key;
		do {
			key = Long.toHexString(random.nextLong()).toLowerCase();
		} while (sessionMap.containsKey(key));
		HttpSession session = new HttpSession(key);
		sessionMap.put(key, session);
		return session;
	}

}
