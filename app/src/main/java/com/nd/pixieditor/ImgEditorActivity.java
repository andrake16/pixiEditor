package com.nd.pixieditor;

import android.app.ActionBar;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.drawable.ShapeDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.nd.pixieditor.Classes.Box;
import com.nd.pixieditor.Classes.DrawableInThread;
import com.nd.pixieditor.Classes.ImgEditorView;
import com.nd.pixieditor.Classes.PShape;
import com.nd.pixieditor.Utils.BitmapTransformer;

import java.util.ArrayList;
import java.util.List;

public class ImgEditorActivity extends AppCompatActivity  implements View.OnTouchListener  {

    private DrawableInThread drawableInThread;
    Bitmap bitmap;
    private Paint paint;
    Paint backgroundPaint;
    Box currentBox;
    List<PShape> Boxen = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDrawableInThread();
        initPaint();
        ImgEditorView imgEditorView = new ImgEditorView(this, drawableInThread);
        fullScreenModeEnable(true);
        setContentView(imgEditorView);
        imgEditorView.setOnTouchListener(this);


    }

    private void fullScreenModeEnable(boolean enable) {
        View mDecorView;

        if (enable) {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                mDecorView = getWindow().getDecorView();
                mDecorView.setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                                | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                                | View.SYSTEM_UI_FLAG_IMMERSIVE);
            }
        } else {
            mDecorView = getWindow().getDecorView();
            mDecorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        PointF touchPointF = new PointF(event.getX(), event.getY());

        switch(event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                currentBox = new Box();
                currentBox.setStartPoint(touchPointF);
                currentBox.setEndPoint(touchPointF);
                break;
            case MotionEvent.ACTION_UP:
                currentBox.setEndPoint(touchPointF);
                Boxen.add(currentBox);
                currentBox = null;
                break;
            case MotionEvent.ACTION_MOVE:
                currentBox.setEndPoint(touchPointF);
                break;
        }

       return true;

    }


    private void initDrawableInThread() {
        drawableInThread = new DrawableInThread() {
            @Override
            public void draw(Canvas canvas) {
                drawing(canvas);
            }
        };
    }

    private void drawing(Canvas canvas){

        canvas.drawPaint(backgroundPaint);
        canvas.drawBitmap(bitmap, 0, 0, paint);

        paint.setAlpha(getResources().getInteger(R.integer.highlightOpacity));

        for(int i=0;i<Boxen.size();i++)
            Boxen.get(i).draw(canvas,paint);
        if(currentBox != null)
            currentBox.draw(canvas,paint);

        paint.setAlpha(getResources().getInteger(R.integer.fullOpacity));

    }

    private void initPaint() {
        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.YELLOW);
        paint.setAlpha(getResources().getInteger(R.integer.fullOpacity));

        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.kotlin);
        int canvasMaxSide = 555;
        bitmap = BitmapTransformer.getScaledDownBitmap(bitmap, canvasMaxSide, true);

        backgroundPaint = new Paint();
        int color = ContextCompat.getColor(this,R.color.editorBackGroundColor);
        backgroundPaint.setColor(color);

    }

}
