package com.nd.pixieditor.Classes;

import android.graphics.PointF;

public class Box {
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
}
