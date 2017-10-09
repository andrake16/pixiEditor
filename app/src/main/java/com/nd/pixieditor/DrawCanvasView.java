package com.nd.pixieditor;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.ShapeDrawable;
import android.view.View;

public class DrawCanvasView extends View {

    private ShapeDrawable drawable;

    public DrawCanvasView(Context context) {
        super(context);
        setFocusable(true);
        drawable = new ShapeDrawable();
    }

    public void setDrawable(ShapeDrawable shapeDrawable) {
        drawable = shapeDrawable;
        drawable.setBounds(10,10,110,110);
        this.getHeight();
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawable.draw(canvas);
        //super.onDraw(canvas);
    }
}
