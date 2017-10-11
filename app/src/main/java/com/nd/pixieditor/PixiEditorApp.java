package com.nd.pixieditor;

import android.app.Application;
import android.content.Intent;

import java.io.File;

public class PixiEditorApp extends Application {

    File appImgStorageDirectoryPath;

    @Override
    public void onCreate() {
        super.onCreate();
        Intent intent = new Intent(this,ImagesActivity.class);
        startActivity(intent);
    }

    public File getAppImgStorageDirectoryPath() {
        return appImgStorageDirectoryPath;
    }

    public void setAppImgStorageDirectoryPath(File appImgStorageDirectoryPath) {
        this.appImgStorageDirectoryPath = appImgStorageDirectoryPath;
    }


}
