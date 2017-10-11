package com.nd.pixieditor;

import android.app.Application;
import android.content.Intent;
import android.os.Environment;

import java.io.File;

public class PixiEditorApp extends Application {

    private File appImgStorageDirectoryPath;

    @Override
    public void onCreate() {
        super.onCreate();

        setAppImgStorageDirectoryPath(getExternalFilesDir(Environment.DIRECTORY_PICTURES));

        Intent intent = new Intent(this,ImagesActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public File getAppImgStorageDirectoryPath() {
        return appImgStorageDirectoryPath;
    }

    private void setAppImgStorageDirectoryPath(File appImgStorageDirectoryPath) {
        this.appImgStorageDirectoryPath = appImgStorageDirectoryPath;
    }


}
