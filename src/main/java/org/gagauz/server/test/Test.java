package org.gagauz.server.test;

import java.nio.charset.Charset;

import org.gagauz.server.ServerConfig;
import org.gagauz.server.http.HttpServer;

public class Test {
    public static void main(String[] args) {
        HttpServer s = new HttpServer();
        ServerConfig.setServerThreadCount(10);
        s.setDocumentRoot("c:\\Users\\java\\apache-tomcat-6.0.41\\webapps");
        s.setCharset(Charset.forName("utf-8"));
        s.start(8099);
        // Socket st;
        // try {
        // st = new Socket("yandex.ru", 80);
        // System.out.println("connect...");
        // while (true) {
        // if (st.isConnected()) {
        // System.out.println("connected");
        //
        // st.getOutputStream()
        // .write(("GET / HTTP/1.1\r\n"
        // +
        // "Accept:text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8\r\n"
        // +
        // "Connection:keep-alive\r\n"
        // +
        // "Host:google.com\r\n"
        // +
        // "User-Agent:Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2272.89 Safari/537.36\r\n"
        // +
        // "\r\n").getBytes());
        // st.getOutputStream().flush();
        // while (st.getInputStream().available() > 0) {
        // System.out.write(st.getInputStream().read());
        // }
        // }
        // }
        // } catch (Exception e) {
        // e.printStackTrace();
        // }

    }
}
