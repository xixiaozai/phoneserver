package com.example.phoneserver;


import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;

public class MainActivity extends Activity {

    private SimpleHttpServer sh;
    private ImageView image;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        WebConfiguration wc = new WebConfiguration();
        wc.setPort(8088);
        wc.setMaxParallels(50);
        sh = new SimpleHttpServer(wc);
        sh.registerResourceHandler(new ResourceInAssetsHandler(this));
        sh.registerResourceHandler(new UpLoadImageHandler() {
            @Override
            protected void onImageLoaded(String path) {
                showImage(path);
            }
        });
        sh.startAsync();

    }

    private void showImage(final String path) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                image = (ImageView) findViewById(R.id.image);
                Bitmap bitmap = BitmapFactory.decodeFile(path);
                image.setImageBitmap(bitmap);
                Toast.makeText(MainActivity.this, "显示图片", Toast.LENGTH_SHORT).show();
            }
        });

    }

    protected void onDestroy() {
        // TODO Auto-generated method stub
        try {
            sh.stopAsync();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            Log.e("phone", e.toString());
        }
        super.onDestroy();

    }
}
