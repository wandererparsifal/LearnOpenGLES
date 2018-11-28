package com.example.test5;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private GLSurfaceView mGlv;

    private SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mGlv = findViewById(R.id.glv);
        mGlv.setEGLContextClientVersion(2);

        String vertex = getFromRaw(R.raw.vertex);
        String fragment = getFromRaw(R.raw.fragment);

        mGlv.setRenderer(new MyRender(vertex, fragment,
                BitmapFactory.decodeResource(getResources(), R.drawable.s),
                getBitmap(Color.RED)));
        mGlv.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

    public Bitmap getBitmap(int color) {
        Paint p = new Paint();
        p.setColor(color);
        p.setTextSize(50);
        Bitmap bitmap = Bitmap.createBitmap(800, 600, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        String time = mDateFormat.format(Calendar.getInstance().getTime());
        canvas.drawText(time, 100, 100, p);
        return bitmap;
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
