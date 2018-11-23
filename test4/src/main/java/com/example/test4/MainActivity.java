package com.example.test4;

import android.graphics.BitmapFactory;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private GLSurfaceView mGlv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mGlv = findViewById(R.id.glv);
        mGlv.setEGLContextClientVersion(2);
        mGlv.setRenderer(new MyRender(BitmapFactory.decodeResource(getResources(), R.drawable.v2)));
        mGlv.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }
}
