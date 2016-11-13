package com.bromi.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * This class represents a custom View that is usable within the XML
 */
public class ExperienceBar extends View {

    /**
     * Start coordinate X of the rectangle (relative to the View dimension size set in onMeasure() method)
     */
    private float start_x = 10;

    /**
     * Start coordinate Y of the rectangle (relative to the View dimension size set in onMeasure() method)
     */
    private float start_y = 10;

    /**
     * Length of the rectangle (relative to the View dimension size set in onMeasure() method)
     */
    private float x_length = 360;

    /**
     * Height of the rectangle (relative to the View dimension size set in onMeasure() method)
     */
    private float y_length = 30;

    /**
     * Additional length for the second rectangle (xpRect) that is drawn over the baseRect.
     * Represents exp progress ratio
     */
    private float xp_progress_length = 0;

    /**
     * xpRect: Pitch black rectangle used to imply the exp progress
     * baseRect: White rectangle with black borders that is used as the base rect
     */
    private RectF xpRect, baseRect;
    private Paint blackBaseRect, blackXpRect;

    /**
     * Constructor
     * @param context needed for superclass
     * @param attrs needed for superclass
     */
    public ExperienceBar(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    /**
     * Instantiate values
     */
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

    /**
     * Draw rectangles
     * @param c
     */
    @Override
    protected void onDraw(Canvas c) {
        super.onDraw(c);

        xpRect.set(start_x, start_y, xp_progress_length, y_length);

        c.drawRect(baseRect, blackBaseRect);
        c.drawRect(xpRect, blackXpRect);
    }

    /**
     * Set custom View size
     * @param i
     * @param i2
     */
    @Override
    protected void onMeasure(int i, int i2) {
        setMeasuredDimension(375, 45);
    }

    /**
     * Get baseRect's x_length
     * @return
     */
    public float getXpBarLength() {
        return x_length;
    }

    /**
     * Set xpRect's length
     * @param x
     */
    public void setXpBarLength(float x) {
        xp_progress_length = x;
    }
}
