package com.example.kashish.picit;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;

public class Chat extends AppCompatActivity {
    private static final String TAG = "Chat";
    RecyclerView rv_chat;
    ArrayList<Bitmap> images;
    ArrayList<File> folders;

    static String currentChatName;

    void displayImages(File file){
        images = new ArrayList<>();
        folders = new ArrayList<>();
//        File file = this.getFilesDir();
        if(file.isDirectory()){
            for(File fs: file.listFiles()){
                Log.d(TAG, fs.getAbsolutePath());
                if(!fs.isDirectory()) {
                    String imagePath = fs.getAbsolutePath();
                    Bitmap myBitmap = BitmapFactory.decodeFile(imagePath);
                    images.add(0,myBitmap);
                }
                else{
                    folders.add(fs);
                }
//                fs.delete();
//                holder.imageview.setImageBitmap(myBitmap);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        File file = new File(this.getFilesDir(),currentChatName);

        displayImages(file);

        rv_chat = (RecyclerView) findViewById(R.id.rv_chat);

        rv_chat.setLayoutManager(new LinearLayoutManager(this));
        gallery_rv_adapter adapter = new gallery_rv_adapter(images,this);
        rv_chat.setAdapter(adapter);
    }
}
