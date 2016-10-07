package org.gagauz.server.http;

import java.io.File;
import java.io.IOException;

import org.gagauz.server.api.ClientConnection;
import org.gagauz.server.api.ConnectionListener;
import org.gagauz.server.api.ConnectionReader;

public class HttpSocketListener extends ConnectionListener {

	private final HttpServer server;

	public HttpSocketListener(HttpServer server) {
		this.server = server;
	}

	@Override
	public ConnectionReader getReader(ClientConnection connection) {
		final File root = new File(server.getDocumentRoot());
		return () -> {

			if (!connection.isClosed()) {

				try {
					System.out.println("Start processing...");
					HttpRequest request = HttpRequestParser.parseInput(connection, this);
					HttpResponse response = new HttpResponse(connection);

					System.out.print(request.getProtocol() + " " + request.getMethod() + " ");
					System.out.println(request.getUri());
					System.out.println(request.getParameters());
					System.out.println(request.getHeaders());
					System.out.println(request.getCookies());

					File file = new File(root, request.getPath());
					if (file.exists()) {
						if (file.isFile()) {
							new HttpFileProcessor(server).processFile(file, request, response);
						} else if (file.isDirectory()) {
							response.setHeader("Content-Type", "text/html;charset=UTF-8");
							response.print(
									"<html><head><style>html,body,pre,a{font:normal normal 1em/1em serif;line-height:1em;}</style></head><body><pre>");
							if (!file.getPath().equals(server.getDocumentRoot())) {
								response.print("<a href=\"");
								response.print(file.getParentFile().getName());
								response.print("\">");
								response.print("..");
								response.println("</a><br/>");
							}
							for (File f : file.listFiles()) {
								response.print(" |-<a href=\"");
								response.print(getUrl(f, root));
								response.print("\">");
								response.print(f.getName());
								response.println("</a><br/>");
							}

							response.print("</pre></body></html>");
						} else {
							response.setStatus(HttpResult.Not_Found_404);
						}
					} else {
						response.setStatus(HttpResult.Not_Found_404);
					}

					if (!response.isCommited()) {
						response.commit();
						System.out.println("Commit response");
					}
					System.out.println("Response " + response.getStatus());
				} catch (IOException e) {
					throw new IllegalStateException(e);
				}
				connection.close();
				stopListen();
			}
		};
	}

	String getUrl(File file, File root) {
		StringBuilder sb = new StringBuilder(file.getName());
		file = file.getParentFile();
		while (file != null && !file.equals(root)) {
			sb.insert(0, file.getName() + '/');
			file = file.getParentFile();
		}
		return '/' + sb.toString();
	}
}
