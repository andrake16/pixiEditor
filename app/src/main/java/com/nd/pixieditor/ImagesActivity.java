package com.nd.pixieditor;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class ImagesActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_images);

        //Intent intent = new Intent(this,EditorActivity.class);
        Intent intent = new Intent(this,ImgEditorActivity.class);
        startActivity(intent);
    }
}
