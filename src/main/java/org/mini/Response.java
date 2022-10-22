package org.mini;

public class Response {
    public Object value;
    public MESSAGE_TYPE type;

    public Response(Object value, MESSAGE_TYPE type) {
        this.value = value;
        this.type = type;
    }
}
