package com.nd.pixieditor;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.nd.pixieditor.Adapters.ImgListAdapter;

import java.util.ArrayList;
import java.util.List;

public class ImagesActivity extends AppCompatActivity {

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
}

