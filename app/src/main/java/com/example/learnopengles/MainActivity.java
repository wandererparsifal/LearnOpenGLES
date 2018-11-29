package com.example.learnopengles;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView imageView = findViewById(R.id.iv);

        Paint p = new Paint();
        p.setColor(Color.RED);
        p.setTextSize(32);
        Bitmap bitmap = Bitmap.createBitmap(800, 600, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        String time = mDateFormat.format(Calendar.getInstance().getTime());
        canvas.rotate(90, 100, 100);
        canvas.drawText(time, 100, 100, p);
        canvas.rotate(-90, 100, 100);

        imageView.setImageBitmap(bitmap);
    }
}
