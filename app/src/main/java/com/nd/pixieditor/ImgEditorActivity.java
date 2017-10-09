package com.nd.pixieditor;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.drawable.ShapeDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import com.nd.pixieditor.Classes.Box;
import com.nd.pixieditor.Classes.DrawableInThread;
import com.nd.pixieditor.Classes.ImgEditorView;
import com.nd.pixieditor.Utils.BitmapTransformer;

import java.util.ArrayList;
import java.util.List;

public class ImgEditorActivity extends AppCompatActivity  implements View.OnTouchListener  {

    private DrawableInThread drawableInThread;
    Bitmap bitmap;
    private Paint paint;
    Paint backgroundPaint;
    Box box;
    List<Box> Boxen = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDrawableInThread();
        initPaint();
        ImgEditorView imgEditorView = new ImgEditorView(this, drawableInThread);
        setContentView(imgEditorView);
        imgEditorView.setOnTouchListener(this);


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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.editor_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        ShapeDrawable d = null;

        switch(item.getItemId()) {
            case R.id.editorMenu_rectangle:
                break;
        }

        return super.onOptionsItemSelected(item);

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        PointF touchPointF = new PointF(event.getX(), event.getY());

        switch(event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                box = new Box();
                box.setStartPoint(touchPointF);
                break;
            case MotionEvent.ACTION_UP:
                box.setEndPoint(touchPointF);
                Boxen.add(box);
                break;
            case MotionEvent.ACTION_MOVE:
                box.setEndPoint(touchPointF);
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

        //for(Box box:Boxen)
        Box box;
        for(int i=0;i<Boxen.size();i++) {
            box = Boxen.get(i);
            canvas.drawRect(box.getStartPoint().x,
                    box.getStartPoint().y,
                    box.getEndPoint().x,
                    box.getEndPoint().y, paint);
        }

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
