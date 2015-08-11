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
        for (i = 0; i < mTotal; i++) {
            mCanvas.drawPoint(50, 50, mPaint);
        }

        for (i = 0; i < mTotal; i++) {
            mCanvas.drawCircle(150 + i, 150, 25, mPaint);
        }

        mCanvas.drawText(String.valueOf(mTotal), 250, 20, mPaint);
        mCanvas.drawText("ImageView.isHardwareAccelerated: " + mImageView.isHardwareAccelerated(), 250, 50, mPaint);
        mCanvas.drawText("Canvas.isHardwareAccelerated: " + mCanvas.isHardwareAccelerated(), 250, 80, mPaint);

        //the real issue is because I'm trying to render a width variable line segment
        drawSegment(mCanvas, mPaint, 50, 250, 150, 250, .1f, mTotal);

        mImageView.invalidate();
    }

    public double distance(double x1, double y1, double x2, double y2) {
        double dx = x1 - x2;
        double dy = y1 - y2;
        return Math.sqrt(dx * dx + dy * dy);
    }

    public void drawSegment(Canvas canvas, Paint paint, float startX, float startY, float endX, float endY, float startWidth, float endWidth) {
        int drawSteps = (int) (distance(startX, startY, endX, endY) +
                .5f);
        float widthDelta = endWidth - startWidth;

        float lastX = Float.NaN;
        float lastY = Float.NaN;

        float diffX = endX - startX;
        float diffY = endY - startY;

        for (int i = 0; i < drawSteps; i++) {
            float t = ((float) i) / drawSteps;
            paint.setStrokeWidth(startWidth + t * widthDelta);

            float x = startX + t * diffX;
            float y = startY + t * diffY;

            if (!Float.isNaN(lastX)) {
                canvas.drawLine(lastX, lastY, x, y, paint);
            }
            //canvas.drawPoint(x, y, paint);

            lastX = x;
            lastY = y;
        }

        paint.setStrokeWidth(endWidth);
        if (drawSteps > 0) {
            canvas.drawLine(lastX, lastY, endX, endY, paint);
        } else {
            canvas.drawLine(startX, startY, endX, endY, paint);
        }
    }

    private void initDrawingConfig() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.BLUE);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setSubpixelText(true);

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
