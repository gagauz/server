package org.gagauz.server.http;

import java.util.Date;

public class HttpSession {
    private final String id;
    private final Date created;

    public HttpSession(String key) {
        this.id = key;
        this.created = new Date();
    }

    public String getId() {
        return id;
    }

    public Date getCreated() {
        return created;
    }

}
