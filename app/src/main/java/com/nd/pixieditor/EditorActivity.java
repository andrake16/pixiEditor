package com.nd.pixieditor;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.EventLog;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;

import com.nd.pixieditor.Utils.BitmapTransformer;

import java.util.ArrayList;
import java.util.List;

public class EditorActivity extends AppCompatActivity  implements View.OnTouchListener {

    private Thread drawingThread;
    private SurfaceView editorSurfaceView;
    private SurfaceHolder editorSurfaceHolder;
    Canvas canvas;
    Bitmap bitmap;
    private Paint paint;
    volatile boolean running;
    volatile boolean firstPass=true;
    volatile boolean newShapeToDraw = false;
    TextView textView;
    Paint backgroundPaint;
    int shapesDrown=0;
    Box box;
    List<Box> Boxen = new ArrayList<>();

    int touchX;
    int touchY;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        editorSurfaceView = (SurfaceView) findViewById(R.id.editorSurfaceView);
        editorSurfaceHolder = editorSurfaceView.getHolder();
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        editorSurfaceView.setOnTouchListener(this);
        textView = (TextView) findViewById(R.id.text);

        initPaint();
        startDrawingThread();
        //paint.setAlpha(getResources().getInteger(R.integer.highlightOpacity));
    }

    @Override
    protected void onResume() {
        super.onResume();
        running = true;
        startDrawingThread();

    }

    @Override
    protected void onPause() {
        super.onPause();
        editorSurfaceHolder.unlockCanvasAndPost(canvas);
        running = false;
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

                d = new ShapeDrawable(new RectShape());

                d.setIntrinsicHeight(100);
                d.setIntrinsicWidth(100);

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


    public void startDrawingThread() {

        running = true;
        drawingThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (running) {
                    if(editorSurfaceHolder.getSurface().isValid()) {
                        drawing();
                        try {
                            Thread.sleep(1);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }

            }
        });

        drawingThread.start();
    }

    private void drawing(){

        canvas = editorSurfaceHolder.lockCanvas();

        canvas.drawPaint(backgroundPaint);
        canvas.drawBitmap(bitmap, 0, 0, paint);

        paint.setAlpha(getResources().getInteger(R.integer.highlightOpacity));
        for(Box box:Boxen)
            canvas.drawRect(box.getStartPoint().x,
                            box.getStartPoint().y,
                            box.getEndPoint().x,
                            box.getEndPoint().y,paint);

        paint.setAlpha(getResources().getInteger(R.integer.fullOpacity));
        editorSurfaceHolder.unlockCanvasAndPost(canvas);


    }

    private void onTouchTest(View v, MotionEvent event) {
        String action = " ";


        switch(event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                action = String.format("ACTION DOWN idx = %d  count = %d ", event.getActionIndex(),event.getPointerCount());
                Log.i("***MOOVING***: ", action );
                break;
            case MotionEvent.ACTION_UP:
                action = String.format("ACTION UP idx = %d  count = %d ", event.getActionIndex(),event.getPointerCount());
                Log.i("***MOOVING***: ", action );
                break;
            case MotionEvent.ACTION_MOVE:
                action = String.format("ACTION MOVE idx = %d  count = %d  X = %.1f  Y = %.1f", event.getActionIndex(),event.getPointerCount(),event.getX(),event.getY());
                Log.i("***MOOVING***:", action);
                break;
            case MotionEvent.ACTION_CANCEL:
                action = "ACTION_CANCEL";
                Log.i("***MOOVING***:", "CANCEL" );
                break;

            case MotionEvent.ACTION_POINTER_DOWN:
                action = String.format("ACTION_POINTER_DOWN  idx = %d  count = %d ", event.getActionIndex(),event.getPointerCount());
                Log.i("***MOOVING***: ", action );
                break;
            case MotionEvent.ACTION_POINTER_UP:
                action = String.format("ACTION_POINTER_UP idx = %d  count = %d ", event.getActionIndex(),event.getPointerCount());
                Log.i("***MOOVING***: ", action );
                break;

        }

        //textView.setText(String.format("%s: X=%.1f, Y=%.1f", action, event.getX(), event.getY()));


    }

    private void initPaint() {
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.YELLOW);
        paint.setAlpha(getResources().getInteger(R.integer.fullOpacity));

        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.kotlin);
        //int canvasMaxSide = Math.min(canvas.getHeight(), canvas.getWidth());
        int canvasMaxSide = 555;
        bitmap = BitmapTransformer.getScaledDownBitmap(bitmap, canvasMaxSide, true);

        backgroundPaint = new Paint();
        int color = ContextCompat.getColor(this,R.color.editorBackGroundColor);
        backgroundPaint.setColor(color);

    }

}
