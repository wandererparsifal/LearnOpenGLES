package com.example.test6;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private GLSurfaceView mGlv;

    private MyRender myRender;

    Bitmap bitmap;

    private SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mGlv = findViewById(R.id.glv);
        mGlv.setEGLContextClientVersion(2);
        bitmap = Bitmap.createBitmap(800, 600, Bitmap.Config.ARGB_8888);

        String vertex = getFromRaw(R.raw.vertex);
        String fragment = getFromRaw(R.raw.fragment);

        myRender = new MyRender(vertex, fragment, getBitmap(Color.RED));
        myRender.setOnFrameListener(new OnFrameListener() {
            @Override
            public void onFrame() {
                myRender.update(getBitmap(Color.RED));
                mGlv.requestRender();
            }
        });
        mGlv.setRenderer(myRender);
        mGlv.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

    public Bitmap getBitmap(int color) {
        Paint p = new Paint();
        p.setColor(color);
        p.setTextSize(32);
        Canvas canvas = new Canvas(bitmap);
        p.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        canvas.drawPaint(p);
        p.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));
        String time = mDateFormat.format(Calendar.getInstance().getTime());
        canvas.rotate(270, 200, 400);
        canvas.drawText(time, 200, 400, p);
        canvas.rotate(-270, 200, 400);
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
