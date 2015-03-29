package org.gagauz.server.http;

import java.io.File;
import java.io.IOException;
import java.net.HttpCookie;
import java.net.Socket;

import org.gagauz.server.SocketProcessor;

public class HttpProcessor extends SocketProcessor {

	private final HttpServer container;
	private final File root;

	public HttpProcessor(HttpServer container) {
		this.container = container;
		root = new File(container.getDocumentRoot());
	}

	@Override
	public void process(Socket socket) throws IOException {
		System.out.println("Start processing...");
		HttpRequest request = new HttpRequest(socket);
		HttpResponse response = new HttpResponse(socket);

		System.out.println(request.getMethod());
		System.out.println(request.getRequestUri());
		System.out.println(request.getParameters());
		System.out.println(request.getCookies());

		File file = new File(root, request.getPath());
		if (file.exists()) {
			if (file.canRead()) {
				if (file.isFile()) {
					if (file.getName().endsWith(".php")) {
						new PhpProcessor(container).processFile(file, request,
								response);
					} else {
						new HttpFileProcessor(container).processFile(file,
								request, response);
					}
				} else if (file.isDirectory()) {
					response.setHeader("Content-Type",
							"text/html;charset=UTF-8");
					response.print("<html><body>");
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

					response.print("</body></html>");
				}
			} else {
				response.setStatus(HttpResult.Forbidden_403);
			}

			response.setCookie(new HttpCookie("cookie1", "b"));
			HttpCookie c = new HttpCookie("cookie2", "c");
			c.setComment("comment1");
			response.setCookie(c);

			c = new HttpCookie("cookie3", "a");
			c.setComment("comment3");
			c.setMaxAge(1000);
			response.setCookie(c);

			c = new HttpCookie("cookie4", "w");
			c.setMaxAge(60);
			c.setComment("sdgsdfgdfs");
			c.setCommentURL("sdgsdfgdfs");
			// response.setCookie(c);

		} else {
			System.out.println("unknown request");
			response.setStatus(HttpResult.Not_Found_404);
		}

		if (!response.isCommited()) {
			response.commit();
			System.out.println("Commit response");
		}
		System.out.println("Response " + response.getStatus());

		socket.close();
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
