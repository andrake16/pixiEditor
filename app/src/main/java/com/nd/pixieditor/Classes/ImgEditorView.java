package com.nd.pixieditor.Classes;

import android.content.Context;
import android.graphics.Canvas;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class ImgEditorView extends SurfaceView implements SurfaceHolder.Callback {

    private ImgEditorThread ImgEditorThread;
    private DrawableInThread drawableInThread;

    public ImgEditorView(Context context, DrawableInThread drawableInThread) {
        super(context);
        this.drawableInThread = drawableInThread;
        getHolder().addCallback(this);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        ImgEditorThread = new ImgEditorThread(getHolder(),drawableInThread);
        ImgEditorThread.setRunning(true);
        ImgEditorThread.start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        ImgEditorThread.setRunning(false);
        while (retry) {
            try {
                ImgEditorThread.join();
                retry = false;
            } catch (InterruptedException e) {
            }
        }
    }


    public ImgEditorThread getImgEditorThread() {
        return ImgEditorThread;
    }
}