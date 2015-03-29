package org.gagauz.server.http;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class PhpProcessor extends HttpFileProcessor {
	private static final Map<String, String> env = new HashMap<String, String>();
	static {
		env.put("SERVER_ADMIN", "admin@localhost");
		env.put("SERVER_SIGNATURE",
				"<ADDRESS>Server/1 at localhost Port 80</ADDRESS>");
		env.put("SERVER_SOFTWARE", "Server/1.3.27 (Win32)");
		env.put("GATEWAY_INTERFACE", "CGI/1.1");
		env.put("SERVER_PROTOCOL", "HTTP/1.1");
	}

	public PhpProcessor(HttpServer container) {
		super(container);
		env.put("DOCUMENT_ROOT", container.getDocumentRoot());
	}

	private String getEmptyIfNull(HttpRequest request, String name) {
		String header = request.getHeaders().get(name);
		return null == header ? "" : header;
	}

	@Override
	protected void processFile(File file, HttpRequest request,
			HttpResponse response) throws IOException {
		response.setHeader("Content-Type",
				"text/html; charset=" + response.getCharset());
		response.setHeader("Date", new Date().toGMTString());

		ProcessBuilder pb = new ProcessBuilder("php", "-c",
				"/etc/php5/apache2", "-f", file.getAbsolutePath());
		pb.environment().putAll(env);
		pb.environment().put("REMOTE_ADDR", request.getRemoteAddress());
		pb.environment().put("REMOTE_PORT", "" + request.getRemotePort());
		pb.environment().put("SERVER_NAME", request.getServerHostname());
		pb.environment().put("SERVER_ADDR", request.getServerAddress());
		pb.environment().put("SERVER_PORT", "" + request.getServerPort());
		pb.environment().put("REQUEST_METHOD", request.getMethod());
		pb.environment().put("SCRIPT_FILENAME", file.getName());
		pb.environment().put("SCRIPT_NAME", request.getPath());
		pb.environment().put("QUERY_STRING",
				request.getQuery() == null ? "" : request.getQuery());
		pb.environment().put("REQUEST_URI", request.getRequestUri());

		for (Entry<String, String> e : request.getHeaders().entrySet()) {
			System.out.println("HTTP_"
					+ e.getKey().toUpperCase().replace('-', '_') + " "
					+ e.getValue());
			pb.environment().put(
					"HTTP_" + e.getKey().toUpperCase().replace('-', '_'),
					e.getValue());
		}

		// Process p = Runtime.getRuntime().exec("php -f " + );

		long start = System.currentTimeMillis();
		Process p = pb.start();
		System.out.println("PHP exe time "
				+ (System.currentTimeMillis() - start) + " ms");
		response.print(new BufferedInputStream(p.getInputStream()));
		System.out.println("PHP output time "
				+ (System.currentTimeMillis() - start) + " ms");
		// try {
		// p.waitFor();
		// } catch (InterruptedException e) {
		// e.printStackTrace();
		// }
		// if (p.exitValue() != 0) {
		// response.setStatus(HttpResult.Internal_Server_Error_500);
		// }
	}
}
