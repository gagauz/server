package org.gagauz.server.http;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpCookie;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.gagauz.server.Request;
import org.gagauz.server.api.Connection;
import org.gagauz.server.utils.KeyValueParser;
import org.gagauz.server.utils.KeyValueParser.ParseEventHandler;
import org.gagauz.utils.multimap.ListMultimap;
import org.gagauz.utils.multimap.Multimaps;

public class HttpRequest extends Request {
	private final Charset charset = Charset.defaultCharset();
	private String method;
	private String path;
	private String query;
	private String requestUri;
	private String protocolVersion;
	private ListMultimap<String, String> headers = Multimaps.newArrayListMultimap();
	private ListMultimap<String, String> parameters = Multimaps.newArrayListMultimap();
	private ListMultimap<String, HttpCookie> cookies;
	private HttpSession session;
	private final HttpServer httpServer;

	public HttpRequest(Connection connection, HttpServer httpServer) {
		super(connection);
		this.httpServer = httpServer;
		readRequestFromStream();
	}

	private static void putToMap(Map<String, List<String>> map, String key, String value) {
		key = key.toLowerCase();
		List<String> list = map.get(key);
		if (null == list) {
			list = new ArrayList<>(2);
			map.put(key, list);
		}
		list.add(value);
	}

	private void readRequestFromStream() {
		try {
			method = readWord();

			requestUri = readWord();
			protocolVersion = readWord();

			while (true) {
				String line = readLine();
				if (null == line || "".equals(line)) {
					break;
				}
				int p = line.indexOf(':');
				if (p > -1) {
					headers.put(line.substring(0, p).trim(), line.substring(p + 1).trim());
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public ListMultimap<String, String> getHeadersMap() {
		return headers;
	}

	public String getMethod() {
		return method;
	}

	public String getPath() {
		if (null == path) {
			int i = requestUri.indexOf('?');
			path = URLDecoder.decode(i > -1 ? requestUri.substring(0, i) : requestUri);
		}
		return path;
	}

	public String getProtocolVersion() {
		return protocolVersion;
	}

	public String getQuery() {
		if (null == query) {
			int i = requestUri.indexOf('?');
			query = i > -1 ? URLDecoder.decode(requestUri.substring(i + 1)) : null;
		}
		return query;
	}

	public String getRequestUri() {
		return requestUri;
	}

	public ListMultimap<String, String> getParameters() throws IOException {
		if (null == parameters) {
			ListMultimap<String, String> params = parseUrlEncodedParameters(getQuery(), textPlainDecoder);
			String ct = headers.getFirst(Constants.content_type);
			if (ct != null) {
				if (ct.startsWith("application/x-www-form-urlencoded")) {
					params.putAll(parseUrlEncodedParameters(new String(getBytes(), getCharset()), urlEncodedDecoder));
				} else if (ct.startsWith("multipart/form-data")) {
					String boundary = "";
					String l;
					while ((l = readLine()) != null) {
						if (l.equals("--" + boundary)) {
							String dispos = readLine();
							int p = dispos.indexOf("name=\"");
						}
					}
				} else if (ct.startsWith("text/plain")) {
					params.putAll(parseUrlEncodedParameters(new String(getBytes(), getCharset()), textPlainDecoder));
				}
			}
			parameters = params;
		}
		return parameters;
	}

	public ListMultimap<String, HttpCookie> getCookies() {
		if (null == cookies) {
			final ListMultimap<String, HttpCookie> cookies0 = Multimaps.newArrayListMultimap();

			ParseEventHandler handler = new ParseEventHandler() {
				@Override
				public void onKeyValue(String key, String value) {
					key = key.trim();
					HttpCookie cookie = new HttpCookie(key, value);
					cookies0.put(key, cookie);
				}
			};
			getHeadersMap().get("Cookie").forEach(cookie -> KeyValueParser.parse(cookie, '=', ';', handler));
			getHeadersMap().get("Cookie2").forEach(cookie -> KeyValueParser.parse(cookie, '=', ';', handler));
			cookies = cookies0;
		}
		return cookies;
	}

	protected Map<String, Object> parseMultipartParameters(String data) {
		return null;
	}

	protected ListMultimap<String, String> parseUrlEncodedParameters(String data, Decoder decoder) {
		final ListMultimap<String, String> parameters = Multimaps.newArrayListMultimap();
		if (null != data) {
			data = decoder.decode(data, getCharset().name());
			ParseEventHandler handler = new ParseEventHandler() {
				@Override
				public void onKeyValue(String key, String value) {
					parameters.put(key, value);
				}
			};
			KeyValueParser.parse(data, '=', '&', handler);
		}
		return parameters;
	}

	public Charset getCharset() {
		return charset;
	}

	public HttpSession getSession() {
		return getSession(false);
	}

	public HttpSession getSession(boolean create) {
		if (null == session) {
			session = httpServer.getSession(create);
		}
		return session;
	}

	private static final Decoder urlEncodedDecoder = new Decoder() {
		@Override
		public String decode(String string, String charset) {
			try {
				return URLDecoder.decode(string, charset);
			} catch (UnsupportedEncodingException e) {
				throw new RuntimeException(e);
			}
		}
	};

	private static final Decoder textPlainDecoder = new Decoder() {
		@Override
		public String decode(String string, String charset) {
			return string;
		}
	};

	private interface Decoder {
		String decode(String string, String charset);
	}

}
