package com.example.kashish.picit;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

public class gallery extends AppCompatActivity {

    RecyclerView rv_gallery;

    private ArrayList<Bitmap> images = new ArrayList<>(10);

    void initViews(){
        rv_gallery = (RecyclerView) findViewById(R.id.rv_gallery);
    }

    void getImages(){
        for(int i=0;i<10;i++){
            images.add(null);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        initViews();
        getImages();


        rv_gallery.setLayoutManager(new LinearLayoutManager(this));
        gallery_rv_adapter adapter = new gallery_rv_adapter(images,this);
        rv_gallery.setAdapter(adapter);

    }
}
