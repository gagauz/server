package org.gagauz.server.http;

import java.net.HttpCookie;
import java.nio.charset.Charset;

import org.gagauz.server.Request;
import org.gagauz.utils.multimap.Multimap;

public class HttpRequest implements Request {

	protected Charset charset = Charset.defaultCharset();
	protected String protocol;
	protected String method;
	protected String path;
	protected String query;
	protected String uri;
	protected Multimap<String, String> headers;
	protected Multimap<String, String> parameters;
	protected Multimap<String, HttpCookie> cookies;
	protected HttpSession session;

	public Charset getCharset() {
		return charset;
	}

	public String getProtocol() {
		return protocol;
	}

	public String getMethod() {
		return method;
	}

	public String getPath() {
		return path;
	}

	public String getQuery() {
		return query;
	}

	public String getUri() {
		return uri;
	}

	public Multimap<String, String> getHeaders() {
		return headers;
	}

	public Multimap<String, String> getParameters() {
		return parameters;
	}

	public Multimap<String, HttpCookie> getCookies() {
		return cookies;
	}

	public HttpSession getSession() {
		return session;
	}

}
