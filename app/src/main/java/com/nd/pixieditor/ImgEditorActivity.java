package com.nd.pixieditor;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.nd.pixieditor.Classes.Box;
import com.nd.pixieditor.Classes.DrawableInThread;
import com.nd.pixieditor.Classes.ImgEditorView;
import com.nd.pixieditor.Classes.PShape;
import com.nd.pixieditor.Fragments.SaveChangesDialogFragment;
import com.nd.pixieditor.Utils.BitmapTransformer;

import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class ImgEditorActivity extends AppCompatActivity  implements View.OnTouchListener  {

    private static final String TAG = ImgEditorActivity.class.toString();
    public static final String SAVE_CHANGES_DIALOG = "SAVE_CHANGES_DIALOG";

    private DrawableInThread drawableInThread;
    Bitmap bitmapOfEditImageOrigSize;
    Bitmap bitmapOfEditImageToFitScreenSize;
    private Paint paint;
    Paint backgroundPaint;
    Box currentBox;
    List<PShape> boxen = new ArrayList<>();
    String imagePath;
    ImgEditorView imgEditorView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDrawableInThread();
        initPaint();
        imgEditorView = new ImgEditorView(this, drawableInThread);
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
                boxen.add(currentBox);
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

    private void drawing(Canvas canvas) {
        drawing(canvas,true,boxen);
    }

    private void drawing(Canvas canvas, boolean drawBaseImage, List<PShape> listOfShapes){

        if(drawBaseImage) {
            canvas.drawPaint(backgroundPaint);
            canvas.drawBitmap(bitmapOfEditImageToFitScreenSize, 0, 0, paint);
        }

        paint.setAlpha(getResources().getInteger(R.integer.highlightOpacity));

        for(int i = 0; i< listOfShapes.size(); i++)
            listOfShapes.get(i).draw(canvas,paint);
        if(currentBox != null)
            currentBox.draw(canvas,paint);

        paint.setAlpha(getResources().getInteger(R.integer.fullOpacity));

    }

    private void initPaint() {
        imagePath = getIntent().getStringExtra(ImagesActivity.EXTRA_IMG_PATH);
        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.YELLOW);
        paint.setAlpha(getResources().getInteger(R.integer.fullOpacity));

        bitmapOfEditImageOrigSize = BitmapFactory.decodeFile(imagePath);
        bitmapOfEditImageToFitScreenSize = fitImage();

        backgroundPaint = new Paint();
        int color = ContextCompat.getColor(this,R.color.editorBackGroundColor);
        backgroundPaint.setColor(color);

    }

    private Bitmap fitImage() {
        Point scrDimens = new Point();
        Point imgDimens = new Point();
        ((WindowManager) getSystemService(WINDOW_SERVICE)).getDefaultDisplay().getSize(scrDimens);
        imgDimens.set(bitmapOfEditImageOrigSize.getWidth(),bitmapOfEditImageOrigSize.getHeight());

        Log.i(TAG,"*Display dimensions* " + scrDimens.x + "x" + scrDimens.y);
        Bitmap fitBm;
        float scaleFactor = 1f;

        if ( imgDimens.x < scrDimens.x && imgDimens.y < scrDimens.y ) {
            scaleFactor = 1f;
        }
        if ( imgDimens.x < scrDimens.x && imgDimens.y > scrDimens.y ) {
            scaleFactor = ((float)scrDimens.y)/imgDimens.y;
        }
        if ( imgDimens.x > scrDimens.x && imgDimens.y < scrDimens.y ) {
            scaleFactor = ((float)scrDimens.x)/imgDimens.x;
        }
        if ( imgDimens.x > scrDimens.x && imgDimens.y > scrDimens.y ) {
            scaleFactor = Math.min( ((float)scrDimens.y)/imgDimens.y,
                    ((float)scrDimens.x)/imgDimens.x );
        }

        Matrix matrix = new Matrix();
        matrix.postScale(scaleFactor , scaleFactor);
        fitBm = Bitmap.createBitmap(bitmapOfEditImageOrigSize, 0, 0, imgDimens.x, imgDimens.y, matrix, false);

        return fitBm;
    }


    @Override
    public void onBackPressed() {
        Log.i(TAG,"back is pressed");
        saveDialog();
        //super.onBackPressed();

    }

    private void saveEditedImage() {
        File dirToSaveImage = ((PixiEditorApp)getApplicationContext()).getAppImgStorageDirectoryPath();

        File saveFile = new File(dirToSaveImage , FilenameUtils.getName(imagePath));
        //File saveFile = new File(dirToSaveImage , "blabla.jpg");


        try {
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(saveFile);
                Bitmap bitmap = drawAndGetBitmapForSave();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);

            } finally {
                if (fos != null) fos.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private Bitmap drawAndGetBitmapForSave() {
        Bitmap.Config conf = Bitmap.Config.ARGB_8888;
        Bitmap bitmap = Bitmap.createBitmap(
                bitmapOfEditImageOrigSize.getWidth(),
                bitmapOfEditImageOrigSize.getHeight(),
                conf);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawBitmap(bitmapOfEditImageOrigSize, 0, 0, paint);
        drawing(canvas,false, calculatePositionOfBoxenForOrigImgSize());
        return bitmap;
    }

    private List<PShape> calculatePositionOfBoxenForOrigImgSize() {
        List<PShape> reBoxen = new ArrayList<>();
        float scaleFactor =(float) bitmapOfEditImageOrigSize.getWidth()/
                (float) bitmapOfEditImageToFitScreenSize.getWidth();

        PointF startP,endP;
        Box reBox;

        for(PShape b : boxen) {
            startP = ((Box)b).getStartPoint();
            endP = ((Box)b).getEndPoint();
            reBox = new Box();
            reBox.setStartPoint( new PointF(
                    startP.x*scaleFactor,
                    startP.y*scaleFactor) );
            reBox.setEndPoint( new PointF(
                    endP.x*scaleFactor,
                    endP.y*scaleFactor) );

            reBoxen.add(reBox);
        }
    return reBoxen;
    }


    private void saveDialog() {
        if(boxen.size()>0) {
            FragmentManager manager = getSupportFragmentManager();
            SaveChangesDialogFragment dialogFragment = new SaveChangesDialogFragment();
            dialogFragment.show(manager,SAVE_CHANGES_DIALOG);
        }
        else onDialogDiscardChanges();

    }


    public void onDialogChangesShouldBeSaved() {
        saveEditedImage();
        int position = getIntent().getIntExtra(ImagesActivity.EXTRA_IMG_POSITION,0);
        ((PixiEditorApp)getApplicationContext()).getImgListAdapter().notifyItemChanged(position);

        super.onBackPressed();
    }

    public void onDialogDiscardChanges() {
        super.onBackPressed();
    }

}
