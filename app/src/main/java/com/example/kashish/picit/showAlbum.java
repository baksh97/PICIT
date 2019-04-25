package com.example.kashish.picit;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;

public class showAlbum extends AppCompatActivity {

    private static final String TAG = "showAlbum";
    ArrayList<Bitmap> images;
    ArrayList<String> names;
    String albumName;

    RecyclerView rv_show_album;

    void getImages(File file){
        images = new ArrayList<>();
        names = new ArrayList<>();
//        folders = new ArrayList<>();
//        imagesFiles = new ArrayList<>();
//        File file = this.getFilesDir();
        if(file.isDirectory()){

            Log.d(TAG,"file "+file.getName()+" is a directory.");

            for(File fs: file.listFiles()){
                Log.d(TAG, fs.getAbsolutePath());
                if(!fs.isDirectory() && fs.getAbsolutePath().endsWith(".jpg")) {
                    Log.d(TAG,"fs: "+fs.getName());
//                    imagesFiles.add(0,fs);
                    String imagePath = fs.getAbsolutePath();
                    Bitmap myBitmap = BitmapFactory.decodeFile(imagePath);
                    images.add(0,myBitmap);
                    names.add(fs.getName());
                }
//                else{
//                    folders.add(fs);
//                }
//                fs.delete();
//                holder.imageview.setImageBitmap(myBitmap);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_album);

        Intent intent = getIntent();
        File file = (File)intent.getExtras().get("file");
        getImages(file);

        rv_show_album = (RecyclerView) findViewById(R.id.rv_show_album);

        rv_show_album.setLayoutManager(new LinearLayoutManager(this));
        galleryImages_rv_adapter adapter = new galleryImages_rv_adapter(names, images,this);
        rv_show_album.setAdapter(adapter);

    }
}
