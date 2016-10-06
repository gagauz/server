package org.gagauz.server.http;

import java.io.File;

import org.gagauz.server.api.Connection;
import org.gagauz.server.api.Processor;

public class HttpSocketProcessor implements Processor {

	private final HttpServer container;
	private final File root;

	public HttpSocketProcessor(HttpServer container) {
		this.container = container;
		root = new File(container.getDocumentRoot());
	}

	@Override
	public void process(Connection connection) {
		try {
			System.out.println("Start processing...");
			HttpRequest request = new HttpRequest(connection, container);
			HttpResponse response = new HttpResponse(connection);

			HttpRequestResponseHolder.set(request, response);

			System.out.print(request.getProtocolVersion() + " " + request.getMethod() + " ");
			System.out.println(request.getRequestUri());
			System.out.println(request.getParameters());
			System.out.println(request.getHeadersMap());
			System.out.println(request.getCookies());

			File file = new File(root, request.getPath());
			if (file.exists()) {
				if (file.isFile()) {
					new HttpFileProcessor(container).processFile(file, request, response);
				} else if (file.isDirectory()) {
					response.setHeader("Content-Type", "text/html;charset=UTF-8");
					response.print("<html><body><pre>");
					if (!file.getPath().equals(container.getDocumentRoot())) {
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

			HttpSession ss = request.getSession(true);
			System.out.println(ss.getId());

			if (!response.isCommited()) {
				response.commit();
				System.out.println("Commit response");
			}
			System.out.println("Response " + response.getStatus());

			connection.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
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
