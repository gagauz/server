package org.gagauz.server.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpCookie;
import java.net.URLDecoder;
import java.nio.charset.Charset;

import org.gagauz.server.api.ClientConnection;
import org.gagauz.server.api.ConnectionListener;
import org.gagauz.server.utils.IOUtils;
import org.gagauz.server.utils.KeyValueEvent;
import org.gagauz.server.utils.KeyValueParser;
import org.gagauz.utils.multimap.ListMultimap;
import org.gagauz.utils.multimap.Multimaps;

public class HttpRequestParser {

	static class Reader {

		InputStream is;
		Runnable closeHandler;

		String readWord() throws IOException {
			return IOUtils.readWord(is, closeHandler);
		}

		String readLine() throws IOException {
			return IOUtils.readLine(is, closeHandler);
		}
	}

	public static HttpRequest parseInput(final ClientConnection connection, final ConnectionListener listener) {

		Reader reader = new Reader();
		reader.is = connection.getInput();

		reader.closeHandler = listener::stopListen;

		HttpRequest request = new HttpRequest();

		try {
			request.method = reader.readWord();
			request.uri = reader.readWord();
			request.protocol = reader.readLine();
			request.path = request.getUri();
			if (request.uri.length() > 1) {
				String[] paths = request.uri.split("\\?", 2);
				if (paths.length > 1) {
					request.path = paths[0];
					request.query = paths[1];
				}
			}

			final ListMultimap<String, String> headers = Multimaps.newArrayListMultimap();

			while (true) {
				String line = reader.readLine();
				if (null == line || "".equals(line)) {
					break;
				}
				int p = line.indexOf(':');
				if (p > -1) {
					headers.put(line.substring(0, p).trim(), line.substring(p + 1).trim());
				}
			}
			request.headers = headers;

			final ListMultimap<String, HttpCookie> cookies = Multimaps.newArrayListMultimap();

			KeyValueEvent handler = (key, value) -> {
				key = key.trim();
				HttpCookie cookie = new HttpCookie(key, value);
				cookies.put(key, cookie);
			};
			headers.get(Constants.cookies).forEach(cookie -> KeyValueParser.parse(cookie, '=', ';', handler));
			headers.get(Constants.cookies2).forEach(cookie -> KeyValueParser.parse(cookie, '=', ';', handler));
			request.cookies = cookies;

			String contentType = headers.getFirst(Constants.content_type);
			if (null != contentType) {
				KeyValueParser.parse(contentType, '=', ';', (key, value) -> {
					if (Constants.charset.equals(key) && null != value) {
						request.charset = Charset.forName(value.trim());
					}
				});
			}

			ListMultimap<String, String> parameters = Multimaps.newArrayListMultimap();

			KeyValueParser.parse(request.getQuery(), '=', '&', (key, value) -> {
				if (Constants.charset.equals(key)) {
					parameters.put(key, urlEncodedDecoder.decode(value, request.getCharset().name()));
				}
			});

			if (null != contentType && request.getMethod().equalsIgnoreCase(Constants.POST)) {
				contentType = contentType.toLowerCase();
				if (contentType.startsWith(Constants.application_x_www_form_urlencoded)) {
					KeyValueParser.parse(connection.getInput(), '=', '&',
							(key, value) -> parameters.put(key, urlEncodedDecoder.decode(value, request.getCharset().name())));

				} else if (contentType.startsWith(Constants.multipart_form_data)) {

				}
			}
			request.parameters = parameters;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return request;
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
