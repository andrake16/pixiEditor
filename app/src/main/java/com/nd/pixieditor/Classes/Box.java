package com.nd.pixieditor.Classes;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;

public class Box implements PShape {
    private PointF startPoint;
    private PointF endPoint;

    public PointF getStartPoint() {
        return startPoint;
    }

    public PointF getEndPoint() {
        return endPoint;
    }

    public void setStartPoint(PointF startPoint) {
        this.startPoint = startPoint;
    }

    public void setEndPoint(PointF endPoint) {
        this.endPoint = endPoint;
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        canvas.drawRect(this.getStartPoint().x,
                        this.getStartPoint().y,
                        this.getEndPoint().x,
                        this.getEndPoint().y, paint);

    }
}
