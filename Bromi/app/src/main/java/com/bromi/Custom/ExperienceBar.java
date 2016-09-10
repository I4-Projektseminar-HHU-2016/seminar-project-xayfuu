package com.bromi.Custom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class ExperienceBar extends View {

    private float start_x = 10;
    private float start_y = 10;
    private float x_length = 360;
    private float y_length = 30;
    private float xp_progress_length = 0;

    private RectF xpRect, baseRect;
    private Paint blackBaseRect, blackXpRect;

    public ExperienceBar(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    public void init() {

        baseRect = new RectF(start_x, start_y, x_length, y_length);
        xpRect = new RectF();

        blackXpRect = new Paint();
        blackXpRect.setColor(Color.BLACK);
        blackXpRect.setStyle(Paint.Style.FILL);

        blackBaseRect = new Paint();
        blackBaseRect.setColor(Color.BLACK);
        blackBaseRect.setStrokeWidth(4);
        blackBaseRect.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onDraw(Canvas c) {
        super.onDraw(c);

        xpRect.set(start_x, start_y, xp_progress_length, y_length);

        c.drawRect(baseRect, blackBaseRect);
        c.drawRect(xpRect, blackXpRect);
    }

    @Override
    protected void onMeasure(int i, int i2) {
        setMeasuredDimension(375, 45);
    }

    public float getXpBarLength() {
        return x_length;
    }

    public void setXpBarLength(float x) {
        xp_progress_length = x;
    }
}
