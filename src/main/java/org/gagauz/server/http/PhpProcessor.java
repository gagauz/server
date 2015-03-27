package org.gagauz.server.http;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class PhpProcessor extends HttpFileProcessor {
    private static final Map<String, String> env = new HashMap<String, String>();
    static {
        env.put("DOCUMENT_ROOT", "z:/home/localhost/www");
        env.put("REMOTE_ADDR", "");
        env.put("REMOTE_PORT", "");
        env.put("SCRIPT_FILENAME", "");
        env.put("SERVER_ADDR", "");
        env.put("SERVER_ADMIN", "admin@localhost");
        env.put("SERVER_NAME", "");
        env.put("SERVER_PORT", "");
        env.put("SERVER_SIGNATURE", "<ADDRESS>Server/1 at localhost Port 80</ADDRESS>");
        env.put("SERVER_SOFTWARE", "Server/1.3.27 (Win32)");
        env.put("GATEWAY_INTERFACE", "CGI/1.1");
        env.put("SERVER_PROTOCOL", "HTTP/1.1");
        env.put("QUERY_STRING", "");
        env.put("REQUEST_URI", "");
        env.put("SCRIPT_NAME", "");
    }

    public PhpProcessor(HttpServer container) {
        super(container);
    }

    private String getEmptyIfNull(HttpRequest request, String name) {
        String header = request.getHeaders().get(name);
        return null == header ? "" : header;
    }

    @Override
    protected void processFile(File file, HttpRequest request, HttpResponse response) throws IOException {
        response.addHeader("Content-Type", "text/html");

        ProcessBuilder pb = new ProcessBuilder("php", "-f", file.getAbsolutePath());
        pb.environment().putAll(env);
        pb.environment().put("REMOTE_ADDR", request.getRemoteAddress());
        pb.environment().put("REMOTE_PORT", "" + request.getRemotePort());
        pb.environment().put("SERVER_NAME", request.getServerHostname());
        pb.environment().put("SERVER_ADDR", request.getServerAddress());
        pb.environment().put("SERVER_PORT", "" + request.getServerPort());
        pb.environment().put("REQUEST_METHOD", request.getMethod());
        pb.environment().put("SCRIPT_FILENAME", file.getName());
        pb.environment().put("SCRIPT_NAME", request.getPath());
        pb.environment().put("QUERY_STRING", request.getQuery() == null ? "" : request.getQuery());
        pb.environment().put("REQUEST_URI", request.getRequestUri());

        for (Entry<String, String> e : request.getHeaders().entrySet()) {
            System.out.println("HTTP_" + e.getKey().toUpperCase().replace('-', '_'));
            pb.environment().put("HTTP_" + e.getKey().toUpperCase().replace('-', '_'), e.getValue());
        }

        //        Process p = Runtime.getRuntime().exec("php -f " + );
        Process p = pb.start();
        response.print(p.getInputStream());
        //        try {
        //            p.waitFor();
        //        } catch (InterruptedException e) {
        //            e.printStackTrace();
        //        }
        //        if (p.exitValue() != 0) {
        //            response.setStatus(HttpResult.Internal_Server_Error_500);
        //        }
    }
}
