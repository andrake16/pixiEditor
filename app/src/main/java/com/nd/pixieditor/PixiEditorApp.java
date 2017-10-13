package com.nd.pixieditor;

import android.app.Application;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;

import com.nd.pixieditor.Adapters.ImgListAdapter;
import com.nd.pixieditor.Classes.PShape;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PixiEditorApp extends Application {

    private File appImgStorageDirectoryPath;
    private RecyclerView.Adapter imgListAdapter;
    private List<PShape> boxenTmp = new ArrayList<>();

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

    public RecyclerView.Adapter getImgListAdapter() {
        return imgListAdapter;
    }

    public void setImgListAdapter(RecyclerView.Adapter imgListAdapter) {
        this.imgListAdapter = imgListAdapter;
    }

    public List<PShape> getBoxenTmp() {
        return boxenTmp;
    }

    public void setBoxenTmp(List<PShape> boxenTmp) {
        this.boxenTmp = boxenTmp;
    }
}
