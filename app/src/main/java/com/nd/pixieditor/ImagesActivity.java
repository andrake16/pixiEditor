package com.nd.pixieditor;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.nd.pixieditor.Adapters.ImgListAdapter;
import com.nd.pixieditor.Utils.CustomFileUtils;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ImagesActivity extends AppCompatActivity {
    static final int GALLERY_REQUEST_CODE = 1;
    static final String TAG = ImagesActivity.class.toString();
    public static final String EXTRA_IMG_PATH = "EXTRA_IMG_PATH";
    public static final String EXTRA_IMG_POSITION = "EXTRA_IMG_POSITION";

    private RecyclerView recyclerView;
    private ImgListAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    File appImgStorageDirectoryPath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_images);

        recyclerView = (RecyclerView) findViewById(R.id.imgs_recycleView);
        recyclerView.setHasFixedSize(false);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        appImgStorageDirectoryPath = ((PixiEditorApp)getApplicationContext())
                .getAppImgStorageDirectoryPath();

        adapter = new ImgListAdapter(this);
        loadAllAppStoreImagesToAdapter();
        recyclerView.setAdapter(adapter);
        ((PixiEditorApp)getApplicationContext()).setImgListAdapter(adapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.img_list_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()) {
            case R.id.imgListMenu_addImg:
                pickImageFromGallery();
                break;
            case R.id.imgListMenu_reloadImages:
                loadAllAppStoreImagesToAdapter();
                adapter.notifyDataSetChanged();
                break;
        }


        return super.onOptionsItemSelected(item);

    }

    private void pickImageFromGallery() {

        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/jpeg").setType("image/png").setType("image/bmp");
        startActivityForResult(photoPickerIntent,GALLERY_REQUEST_CODE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Uri selectedImageUri = null;
        File destFilePath;

        switch (requestCode){
            case GALLERY_REQUEST_CODE:
                if(resultCode == RESULT_OK) {
                    selectedImageUri = data.getData();

                    try {
                        destFilePath = copyImageToLocalStorage(selectedImageUri);
                         adapter.addImage(destFilePath);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;

        }

    }

    @Nullable
    private File copyImageToLocalStorage(Uri imageUri) throws IOException {

        String realImagePath = CustomFileUtils.getRealPathFromURI(this,imageUri);
        Log.i(TAG, "Will be copied file: " + realImagePath);
        Log.i(TAG, "folder to save image is: " + appImgStorageDirectoryPath);

        String state = Environment.getExternalStorageState(); //mounted
        Log.i(TAG, "ExternalStorageState: " + state);
        if(!state.equals(Environment.MEDIA_MOUNTED))
            Log.i(TAG, "SD Card is not Available");

        File file = new File(realImagePath);

        FileUtils.copyFileToDirectory(file,appImgStorageDirectoryPath);
        String outputFileFullPath = appImgStorageDirectoryPath + "/" + FilenameUtils.getName(file.toString());
        return new File(outputFileFullPath);

    }

    private void loadAllAppStoreImagesToAdapter() {
        adapter.clearList();
        File[] listFiles = appImgStorageDirectoryPath.listFiles();
        for(File filePath: listFiles) {
            adapter.addImage(filePath);
            Log.i(TAG, getString(R.string.will_be_load_files) + filePath.toString());
        }




    }

}

