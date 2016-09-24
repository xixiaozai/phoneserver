package com.example.phoneserver;

import android.annotation.SuppressLint;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;

/**
 * Created by Administrator on 2016/9/20.
 * ÏÂÔØÍ¼Æ¬²¢±£´æ
 */

public abstract class UpLoadImageHandler implements IResourceUriHandler {
    private String acceptPrefix = "/upload_image";

    @Override
    public boolean accept(String uri) {
        return uri.startsWith(acceptPrefix);
    }

    @SuppressLint("SdCardPath")
	@Override
    public void handle(String uri, HttpContext httpContext) throws IOException {
//        OutputStream ous;
//        try {
//            ous = httpContext.getUnderltSocket().getOutputStream();
//            PrintWriter writer = new PrintWriter(ous);
//            writer.println("HTTP/1.1 200 OK");
//            writer.println();
//            writer.println("from uploadimage ");
//            writer.flush();
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
        String tempPath = "/mnt/sdcard/text_upload.jpg";
        Long totalLength = Long.parseLong(httpContext.getRequestHeaderValue("Content-length"));
        FileOutputStream fos = new FileOutputStream(tempPath);
        InputStream nis = httpContext.getUnderltSocket().getInputStream();
        byte[] buffer = new byte[10240];
        int nReed = 0;
        Long nLeftLength = totalLength;
        while ((nReed = nis.read(buffer)) > 0 && nLeftLength > 0) {
            fos.write(buffer, 0, nReed);
            nLeftLength -= nReed;
        }
        fos.close();
        OutputStream nos = httpContext.getUnderltSocket().getOutputStream();
        PrintStream pr = new PrintStream(nos);
        pr.println("HTP/1.1  200 OK");
        pr.println();
        onImageLoaded(tempPath);
    }

    protected void onImageLoaded(String path) {

    }
}
