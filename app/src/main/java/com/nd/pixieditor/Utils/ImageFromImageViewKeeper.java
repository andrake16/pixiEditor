package com.nd.pixieditor.Utils;


import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.format.Time;
import android.widget.ImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class ImageFromImageViewKeeper {

    ImageView iv; // ImageView, содержащий изображение, которое нужно сохранить
    String folderToSave = Environment.getExternalStorageDirectory().toString(); // папка куда сохранять, в данном случае - корень SD-карты

    private String SavePicture(ImageView iv, String folderToSave)
    {
        OutputStream fOut = null;
        Time time = new Time();
        time.setToNow();

        try {
            File file = new File(folderToSave, Integer.toString(time.year) + Integer.toString(time.month) + Integer.toString(time.monthDay) + Integer.toString(time.hour) + Integer.toString(time.minute) + Integer.toString(time.second) +".jpg"); // создать уникальное имя для файла основываясь на дате сохранения
            fOut = new FileOutputStream(file);

            //Bitmap bitmap = (BitmapDrawable) iv.getDrawable().getBitmap();
            //bitmap.compress(Bitmap.CompressFormat.JPEG, 85, fOut); // сохранять картинку в jpeg-формате с 85% сжатия.
            fOut.flush();
            fOut.close();
            //MediaStore.Images.Media.insertImage(getContentResolver(), file.getAbsolutePath(), file.getName(),  file.getName()); // регистрация в фотоальбоме
        }
        catch (Exception e) // здесь необходим блок отслеживания реальных ошибок и исключений, общий Exception приведен в качестве примера
        {
            return e.getMessage();
        }
        return "";
    }

}
