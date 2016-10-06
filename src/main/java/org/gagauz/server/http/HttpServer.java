package org.gagauz.server.http;

import java.net.HttpCookie;
import java.nio.charset.Charset;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.gagauz.server.Server;
import org.gagauz.server.ServerConfig;
import org.gagauz.server.api.Processor;
import org.gagauz.server.api.ServerConnectionFactory;
import org.gagauz.server.api.ThreadPool;
import org.gagauz.server.api.io.IOServerConnection;

public class HttpServer extends Server {

	private String documentRoot;
	private String sessionIdCookieName;
	private HttpSessionManager httpSessionManager = new HttpSessionManager();
	private Executor executor;

	private Charset charset = Charset.defaultCharset();

	public String getDocumentRoot() {
		return documentRoot;
	}

	public void setDocumentRoot(String documentRoot) {
		this.documentRoot = documentRoot;
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

	@Override
	protected ServerConnectionFactory getServerConnectionFactory() {
		return IOServerConnection::new;
	}

	@Override
	protected Processor getProcessor() {
		return (connection) -> {

		};
	}

	@Override
	protected ThreadPool getThreadPool() {
		if (null == executor) {
			executor = Executors.newFixedThreadPool(ServerConfig.getServerMaxThreadCount());
		}
		return executor::execute;
	}
}
