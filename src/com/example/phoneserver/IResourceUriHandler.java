package com.example.phoneserver;

import java.io.IOException;

/**
 * Created by Administrator on 2016/9/20.
 */

public interface  IResourceUriHandler {
    public boolean accept(String uri);

    public void handle(String uri, HttpContext httpContext) throws IOException;
}
