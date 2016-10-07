package org.gagauz.server.http;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpCookie;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.gagauz.server.Response;
import org.gagauz.server.api.ClientConnection;

public class HttpResponse extends Response {

	private final int bufferSize = 10000;
	private HttpResult status = HttpResult.OK_200;
	private Charset charset = Charset.defaultCharset();
	private final Map<String, String> headers;
	private List<HttpCookie> cookies;
	protected final ByteArrayOutputStream body;

	public HttpResponse(ClientConnection connection) {
		super(connection);
		headers = new HashMap<>();
		body = new ByteArrayOutputStream();
	}

	public void setHeader(String name, Object value) {
		headers.put(name, String.valueOf(value));
	}

	private void write(String string) throws IOException {
		if (null != string) {
			getOutputStream().write(string.getBytes(charset));
		}
	}

	private void write(char chr) throws IOException {
		getOutputStream().write(chr);
	}

	@Override
	public void commit() throws IOException {
		getOutputStream().write(("HTTP/1.1 " + status.getStatus() + " " + status.getMessage() + "\r\n").getBytes(charset));
		if (body.size() > 0) {
			setHeader("Content-Length", body.size());
		}
		if (!headers.containsKey("Content-Type")) {
			headers.put("Content-Type", "unknown; charset=" + charset.name());
		}
		for (Entry<String, String> e : headers.entrySet()) {
			write(e.getKey());
			write(':');
			write(e.getValue());
			write('\r');
			write('\n');
		}
		if (null != cookies) {
			for (HttpCookie cookie : cookies) {
				if (cookie.getVersion() > 1) {
					write("Set-Cookie2:");
				} else {
					write("Set-Cookie:");
				}
				write(cookie.getName());
				write('=');
				write(cookie.getValue());
				if (cookie.getPath() != null) {
					write("; Path=");
					write(cookie.getPath());
				}
				if (cookie.getDomain() != null) {
					write("; Domain=");
					write(cookie.getDomain());
				}
				if (cookie.getComment() != null) {
					write("; Comment=");
					write(cookie.getComment());
				}
				if (cookie.getCommentURL() != null) {
					write("; CommentURL=");
					write(cookie.getCommentURL());
				}
				if (cookie.getPortlist() != null) {
					write("; Port=");
					write('"');
					write(cookie.getPortlist());
					write('"');
				}
				if (cookie.getMaxAge() > -1) {
					write("; Max-Age=");
					write(String.valueOf(cookie.getMaxAge()));
				}
				if (cookie.getDiscard()) {
					write("; Discard");
				}
				if (cookie.getSecure()) {
					write("; Secure");
				}
				write("; Version=");
				write(String.valueOf(cookie.getVersion()));
				write('\r');
				write('\n');
			}
		}
		write('\r');
		write('\n');
		body.writeTo(getOutputStream());
		getOutputStream().flush();
		super.commit();
	}

	public HttpResult getStatus() {
		return status;
	}

	public void setStatus(HttpResult status) {
		this.status = status;
	}

	public Charset getCharset() {
		return charset;
	}

	public void setCharset(Charset charset) {
		this.charset = charset;
	}

	public void print(InputStream is) throws IOException {
		int r = 0;
		byte[] buffer = new byte[bufferSize];
		while ((r = is.read(buffer)) > -1) {
			body.write(buffer, 0, r);
		}
		is.close();
	}

	public void print(String string) throws IOException {
		if (string != null) {
			body.write(string.toString().getBytes(charset));
		}
	}

	public void println(String string) throws IOException {
		body.write(string.getBytes(charset));
		body.write('\r');
		body.write('\n');
	}

	public void setCookie(HttpCookie httpCookie) {
		if (null == cookies) {
			cookies = new ArrayList<>();
		}
		cookies.add(httpCookie);
	}
}
