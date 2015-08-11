package com.test.antialiasissue;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.SeekBar;

public class MainActivity extends AppCompatActivity {
    private final String KEY_TOTAL = "total";

    private ImageView mImageView;
    private SeekBar mSeekBar;

    private Bitmap mBitmap;
    private Canvas mCanvas;
    private Paint mPaint;

    private int mTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTotal = savedInstanceState != null ? savedInstanceState.getInt(KEY_TOTAL) : 1;
        initDrawingConfig();

        mSeekBar = (SeekBar) findViewById(R.id.seekbar);
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mTotal = Math.max(1, progress);
                draw();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mImageView = (ImageView) findViewById(R.id.image);
        mImageView.setImageBitmap(mBitmap);

        draw();
    }

    private void draw() {
        mCanvas.drawColor(Color.WHITE);
        mPaint.setStrokeWidth(50);

        int i;
        for(i = 0; i < mTotal; i++) {
            mCanvas.drawPoint(50, 50, mPaint);
        }

        for(i = 0; i < mTotal; i++) {
            mCanvas.drawCircle(150, 150, 25, mPaint);
        }

        mCanvas.drawText(String.valueOf(mTotal), 300, 10, mPaint);
        mImageView.invalidate();
    }

    private void initDrawingConfig() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.BLUE);
        mPaint.setStrokeCap(Paint.Cap.ROUND);

        mBitmap = Bitmap.createBitmap(500, 500, Bitmap.Config.RGB_565);
        mCanvas = new Canvas(mBitmap);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBitmap.recycle();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(KEY_TOTAL, mTotal);
    }
}
