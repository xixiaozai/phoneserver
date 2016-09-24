package com.example.phoneserver;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SimpleHttpServer {
    private boolean isEnable;
    private WebConfiguration webConfig;
    private ServerSocket socket;
    private ExecutorService threadPool;
    private Set<IResourceUriHandler> resourceHandlers;

    public SimpleHttpServer(WebConfiguration webConfig) {
        this.webConfig = webConfig;
        /*
		 * 创建一个线程池，这个方法不会立即被销毁
		 */
        threadPool = Executors.newCachedThreadPool();
        resourceHandlers=new HashSet<IResourceUriHandler>();
    }

    // ����server
    public void startAsync() {
        isEnable = true;
        new Thread(new Runnable() {

            @Override
            public void run() {
                doProcAsync();
            }

        }).start();
    }

    private void doProcAsync() {

        try {
            InetSocketAddress inetSocketAddress = new InetSocketAddress(
                    webConfig.getPort());
            socket = new ServerSocket();
            socket.bind(inetSocketAddress);
            while (isEnable) {
                final Socket remotePeer = socket.accept();
                threadPool.submit(new Runnable() {

                    @Override
                    public void run() {

                        Log.d("phone", "a  remote peer  accepted..."
                                + remotePeer.getRemoteSocketAddress()
                                .toString());
                        onAcceptRemotePeer(remotePeer);
                    }

                });
            }
        } catch (IOException e) {


            Log.e("phone", e.toString());
        }
    }


    public void stopAsync() throws IOException {
        if (!isEnable) {
            return;
        }
        isEnable = false;
        socket.close();
        socket = null;

    }
 public void registerResourceHandler(IResourceUriHandler handler){
     resourceHandlers.add(handler);
 }
    private void onAcceptRemotePeer(Socket remotePeer) {
        // TODO Auto-generated method stub
        try {
            HttpContext httpContext = new HttpContext();
            httpContext.setUnderlySocket(remotePeer);
            InputStream ins = remotePeer.getInputStream();
            String headerLiner = null;
            String resourceUri = headerLiner = StreamToolKit.readLine(ins).split(" ")[1];
            Log.d("phone", resourceUri);
            while ((headerLiner = StreamToolKit.readLine(ins)) != null) {
                if (headerLiner.equals("\r\n")) {
                    break;
                }
                String[] pair = headerLiner.split(":");
                if (pair.length > 1) {
                    httpContext.addResuestHeader(pair[0], pair[1]);

                }
                Log.d("phone", "headerLine=" + headerLiner);
            }
            for(IResourceUriHandler handler:resourceHandlers){
               if(!handler.accept(resourceUri)) {
                   continue;
               }
                handler.handle(resourceUri,httpContext);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            Log.e("phone", e.toString());
        }

    }
}
