package com.nd.pixieditor;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ShapeDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.nd.pixieditor.Adapters.ImgListAdapter;

import java.util.ArrayList;
import java.util.List;

public class ImagesActivity extends AppCompatActivity {
    static final int GALLERY_REQUEST_CODE = 1;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_images);

        recyclerView = (RecyclerView) findViewById(R.id.imgs_recycleView);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        List<Integer> dataForList = new ArrayList<>();
        dataForList.add(R.drawable.cushion);
        dataForList.add(R.drawable.kotlin);
        dataForList.add(R.drawable.cushion);
        dataForList.add(R.drawable.cushion);
        dataForList.add(R.drawable.cushion);
        dataForList.add(R.drawable.kotlin);
        dataForList.add(R.drawable.kotlin);
        dataForList.add(R.drawable.kotlin);
        dataForList.add(R.drawable.cushion);

        adapter = new ImgListAdapter(dataForList);
        recyclerView.setAdapter(adapter);

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
                //Toast.makeText(this,"i am starting to add an image",Toast.LENGTH_SHORT).show();
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

        switch (requestCode){
            case GALLERY_REQUEST_CODE:
                if(resultCode == RESULT_OK)
                    selectedImageUri = data.getData();
                Toast.makeText(this,selectedImageUri.toString(),Toast.LENGTH_LONG).show();

                break;
        }

    }
}

