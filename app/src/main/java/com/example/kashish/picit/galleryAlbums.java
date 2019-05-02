package com.example.kashish.picit;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

import static com.example.kashish.picit.MainActivity.Uid;
import static com.example.kashish.picit.MainActivity.userFolder;

public class galleryAlbums extends AppCompatActivity {

    private static final String TAG = "galleryAlbums";
    RecyclerView rv_gallery_album;
    ArrayList<File> albums;

    void initViews(){
        rv_gallery_album = (RecyclerView) findViewById(R.id.rv_gallery_albums);
    }

    void getAlbums(File file){
        albums = new ArrayList<>();
        functions.getAlbumsInFolder(file , albums);
        for(File f:albums)Log.d(TAG,"album: "+f.getName());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_albums);

        initViews();

        Intent intent = getIntent();
        File file = (File)intent.getExtras().get("file");
        getAlbums(file);

        rv_gallery_album.setLayoutManager(new LinearLayoutManager(this));
        galleryAlbums_rv_adapter adapter = new galleryAlbums_rv_adapter(albums,this);
        rv_gallery_album.setAdapter(adapter);
    }
}
