package com.nd.pixieditor;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class ImgEditorThread extends Thread {

    private boolean running = false;
    private SurfaceHolder surfaceHolder;
    private DrawableInThread drawableInThread;

    public ImgEditorThread(SurfaceHolder surfaceHolder,DrawableInThread drawableInThread) {
        this.surfaceHolder = surfaceHolder;
        this.drawableInThread = drawableInThread;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    @Override
    public void run() {
        Canvas canvas;
        while (running) {
            canvas = null;
            try {
                canvas = surfaceHolder.lockCanvas(null);
                if (canvas == null)
                    continue;
                drawableInThread.draw(canvas);
            } finally {
                if (canvas != null) {
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
        }
    }
}
