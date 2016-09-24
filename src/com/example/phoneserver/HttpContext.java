package com.example.phoneserver;

import java.net.Socket;
import java.util.HashMap;

/**
 * Created by Administrator on 2016/9/20.
 */

public class HttpContext {
    private Socket underlySocket;
    private final HashMap<String, String> requestHeaders;

    public HttpContext() {
        requestHeaders = new HashMap<String, String>();
    }

    public void setUnderlySocket(Socket socket) {
        this.underlySocket = socket;
    }

    public Socket getUnderltSocket() {
        return this.underlySocket;
    }

    public void addResuestHeader(String headerName, String headerValue) {
        requestHeaders.put(headerName, headerValue);
    }

    public String getRequestHeaderValue(String headerName) {
        return requestHeaders.get(headerName);
    }
}
