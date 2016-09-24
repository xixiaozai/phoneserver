package com.example.phoneserver;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;

/**
 * Created by Administrator on 2016/9/20.
 */

public class ResourceInAssetsHandler implements IResourceUriHandler {
    private String acceptPrefis = "/static";
    private Context context;

    public ResourceInAssetsHandler(Context context) {
        this.context = context;
    }

    @Override
    public boolean accept(String uri) {
        return uri.startsWith(acceptPrefis);
    }

    @Override
    public void handle(String uri, HttpContext httpContext) throws IOException {
       /* OutputStream ous;
        try {
            ous = httpContext.getUnderltSocket().getOutputStream();
            PrintWriter writer = new PrintWriter(ous);
            writer.println("HTTP/1.1 200 OK");
            writer.println();
            writer.println("from resource in asset handler");
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        int startIndex = acceptPrefis.length();
        String assetsPath = uri.substring(startIndex);
        InputStream fis = context.getAssets().open(assetsPath);
        byte[] raw = StreamToolKit.readRawFromStream(fis);
        fis.close();
        OutputStream ous = httpContext.getUnderltSocket().getOutputStream();
        PrintStream printer = new PrintStream(ous);
        printer.println("HTTP/1.1 200 ok");
        printer.println("Content-length" + raw.length);
        if (assetsPath.endsWith(".html")) {
            printer.println("Context-type:text/html");
        } else if (assetsPath.endsWith(".js")) {
            printer.println("Context-type:text/js");
        } else if (assetsPath.endsWith(".css")) {
            printer.println("Context-type:text/css");
        }
        printer.println();
        printer.write(raw);
    }
}
