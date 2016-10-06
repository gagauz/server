package org.gagauz.server.http;

import java.net.HttpCookie;
import java.net.Socket;
import java.nio.charset.Charset;

import org.gagauz.server.Server;
import org.gagauz.server.SocketAcceptor;

public class HttpServer extends Server {

	private String documentRoot;
	private String sessionIdCookieName;
	private HttpSessionManager httpSessionManager = new HttpSessionManager();

	private Charset charset = Charset.defaultCharset();

	public String getDocumentRoot() {
		return documentRoot;
	}

	public void setDocumentRoot(String documentRoot) {
		this.documentRoot = documentRoot;
	}

	@Override
	protected SocketAcceptor getAcceptor(Socket socket) {
		return new SocketAcceptor(socket, new HttpSocketProcessor(this));
	}

	public Charset getCharset() {
		return charset;
	}

	public void setCharset(Charset charset) {
		this.charset = charset;
	}

	public String getSessionIdCookieName() {
		return sessionIdCookieName;
	}

	public void setSessionIdCookieName(String sessionIdCookieName) {
		this.sessionIdCookieName = sessionIdCookieName;
	}

	public HttpSessionManager getSessionManager() {
		return httpSessionManager;
	}

	public void setSessionManager(HttpSessionManager httpSessionManager) {
		this.httpSessionManager = httpSessionManager;
	}

	public HttpSession getSession(boolean create) {
		HttpSession session = null;
		HttpRequest request = HttpRequestResponseHolder.getRequest();
		HttpResponse response = HttpRequestResponseHolder.getResponse();
		if (null != request) {
			HttpCookie cookie = request.getCookies().getFirst(sessionIdCookieName);
			if (null != cookie) {
				session = httpSessionManager.find(cookie.getValue());
			}
			if (null == session && create) {
				session = httpSessionManager.create();
				cookie = new HttpCookie(sessionIdCookieName, session.getId());
				response.setCookie(cookie);
			}
		}
		return session;
	}
}
