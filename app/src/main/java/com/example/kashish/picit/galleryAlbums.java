package com.example.kashish.picit;

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

public class galleryAlbums extends AppCompatActivity {

    private static final String TAG = "galleryAlbums";
    RecyclerView rv_gallery_album;
    ArrayList<File> albums;

    void initViews(){
        rv_gallery_album = (RecyclerView) findViewById(R.id.rv_gallery_albums);

    }

    void getAlbums(File file){
        albums = new ArrayList<>();
//        folders = new ArrayList<>();
//        imagesFiles = new ArrayList<>();
//        File file = this.getFilesDir();
        if(file.isDirectory()){
            for(File fs: file.listFiles()){
                Log.d(TAG, fs.getAbsolutePath());
//                if(!fs.isDirectory() && fs.getAbsolutePath().endsWith(".jpg")) {
////                    imagesFiles.add(0,fs);
//                    String imagePath = fs.getAbsolutePath();
//                    Bitmap myBitmap = BitmapFactory.decodeFile(imagePath);
//                    images.add(0,myBitmap);
//                }
                if(fs.isDirectory() && fs.getAbsolutePath().contains("album_")){
                    albums.add(fs);
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
        setContentView(R.layout.activity_gallery_albums);

        initViews();
        getAlbums(this.getFilesDir());

//        Toast.makeText(this, "albums: "+albums.size(), Toast.LENGTH_SHORT).show();

        rv_gallery_album.setLayoutManager(new LinearLayoutManager(this));
        galleryAlbums_rv_adapter adapter = new galleryAlbums_rv_adapter(albums,this);
        rv_gallery_album.setAdapter(adapter);
    }
}
