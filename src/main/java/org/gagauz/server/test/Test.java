package org.gagauz.server.test;

import java.nio.charset.Charset;

import org.gagauz.server.ServerConfig;
import org.gagauz.server.http.HttpServer;

public class Test {
	public static void main(String[] args) {
		HttpServer s = new HttpServer();
		ServerConfig.setServerThreadCount(10);
		// s.setDocumentRoot("c:\\Users\\java\\apache-tomcat-6.0.41\\webapps");
		s.setDocumentRoot("d:/DOWNLOADS/");
		s.setCharset(Charset.forName("utf-8"));
		s.setSessionIdCookieName("MYSSID");
		s.start(8099);
	}
}
