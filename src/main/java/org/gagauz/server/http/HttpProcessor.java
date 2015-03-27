package org.gagauz.server.http;

import org.gagauz.server.SocketProcessor;

import java.io.File;
import java.io.IOException;
import java.net.Socket;

public class HttpProcessor extends SocketProcessor {

    private HttpServer container;

    public HttpProcessor(HttpServer container) {
        this.container = container;
    }

    @Override
    public void process(Socket socket) throws IOException {
        System.out.println("Start processing...");
        HttpRequest request = new HttpRequest(socket);
        HttpResponse response = new HttpResponse(socket);

        System.out.println(request.getMethod());
        System.out.println(request.getRequestUri());
        System.out.println(new String(request.getBytes()));
        System.out.println(request.getParameters());

        File file = new File(container.getDocumentRoot() + request.getPath());
        if (file.exists()) {
            if (file.canRead()) {
                if (file.isFile()) {
                    if (file.getName().endsWith(".php")) {
                        new PhpProcessor(container).processFile(file, request, response);
                    } else {
                        new HttpFileProcessor(container).processFile(file, request, response);
                    }
                } else if (file.isDirectory()) {
                    response.addHeader("Content-Type", "text/html;charset=UTF-8");
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
                        response.print(f.getName());
                        response.print("\">");
                        response.print(f.getName());
                        response.println("</a><br/>");
                    }

                    response.print("</body></html>");
                }
            } else {
                response.setStatus(HttpResult.Forbidden_403);
            }

        } else {
            System.out.println("unknown request");
            response.setStatus(HttpResult.Not_Found_404);
        }

        if (!response.isCommited()) {
            response.commit();
            System.out.println("Commit response");
        }

        socket.close();
    }
}
