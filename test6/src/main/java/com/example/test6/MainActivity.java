package com.example.test6;

import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    private GLSurfaceView mGlv;

    private MyRender myRender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mGlv = findViewById(R.id.glv);
        mGlv.setEGLContextClientVersion(2);

        String vertex = getFromRaw(R.raw.vertex);
        String fragment = getFromRaw(R.raw.fragment);

        myRender = new MyRender(vertex, fragment);
        myRender.setOnFrameListener(new OnFrameListener() {
            @Override
            public void onFrame() {
                mGlv.requestRender();
            }
        });
        mGlv.setRenderer(myRender);
        mGlv.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

    public String getFromRaw(int id) {
        String result = "";
        try {
            InputStream in = getResources().openRawResource(id);
            int lenght = in.available();
            byte[] buffer = new byte[lenght];
            in.read(buffer);
            result = new String(buffer, "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
