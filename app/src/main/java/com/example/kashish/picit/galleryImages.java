package com.example.kashish.picit;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.util.ArrayList;

public class galleryImages extends AppCompatActivity {

    private static final int FILE_SELECT_CODE = 0;
    private static final String TAG = "galleryImages" ;

    RecyclerView rv_gallery_images;
    FloatingActionButton fab_createAlbum;

    private ArrayList<Bitmap> images = new ArrayList<>();
    private ArrayList<String> names = new ArrayList<>();
    private ArrayList<File> imagesFiles = new ArrayList<>();

    void initViews(){
        rv_gallery_images = (RecyclerView) findViewById(R.id.rv_gallery_images);
        fab_createAlbum = (FloatingActionButton) findViewById(R.id.floatingActionButton_createAlbum);
    }

    void displayImages(File file){
        images = new ArrayList<>();
        names = new ArrayList<>();
        imagesFiles = new ArrayList<>();
        functions.getImagesInFolder(file, imagesFiles, images, names);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_images);

        initViews();
        Intent intent = getIntent();
        File file = (File)intent.getExtras().get("file");
        displayImages(file);

        fab_createAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(galleryImages.this, create_album.class));
            }
        });

        rv_gallery_images.setLayoutManager(new LinearLayoutManager(this));
        galleryImages_rv_adapter adapter = new galleryImages_rv_adapter(imagesFiles,names,images,this);
        rv_gallery_images.setAdapter(adapter);
    }
}
