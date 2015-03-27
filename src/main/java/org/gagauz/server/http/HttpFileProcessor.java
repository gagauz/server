package org.gagauz.server.http;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLConnection;

public class HttpFileProcessor {
    protected HttpServer container;

    public HttpFileProcessor(HttpServer container) {
        this.container = container;
    }

    protected void processFile(File file, HttpRequest request, HttpResponse response) throws IOException {
        String mimeType = URLConnection.guessContentTypeFromName(file.getName());
        response.addHeader("Content-Type", mimeType);
        FileInputStream fis = new FileInputStream(file);
        response.print(fis);
    }
}
