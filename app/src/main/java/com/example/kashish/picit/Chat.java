package com.example.kashish.picit;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.io.File;
import java.util.ArrayList;
import java.util.Vector;

import static com.example.kashish.picit.functions.getPicturesInGroup;
import static com.example.kashish.picit.functions.getUpdates;

public class Chat extends AppCompatActivity {
    private static final String TAG = "Chat";
    RecyclerView rv_chat;//,rv_chatAlbums;
    ArrayList<Bitmap> images;
//    ArrayList<>
    ArrayList<String> names;
    ArrayList<File> folders;

    Intent intent;

    String currentChatName;
    int chatID;

    void displayImages(File file){
        images = new ArrayList<>();
        folders = new ArrayList<>();
        names = new ArrayList<>();
//        File file = this.getFilesDir();
        if(file.isDirectory()){
            for(File fs: file.listFiles()){
                Log.d(TAG, fs.getAbsolutePath());
                if(!fs.isDirectory()) {
                    String imagePath = fs.getAbsolutePath();
                    Bitmap myBitmap = BitmapFactory.decodeFile(imagePath);
                    images.add(0,myBitmap);
                    names.add(fs.getName());
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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.chat_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){
            case R.id.add_member:
                startActivity(new Intent(Chat.this, addMembers.class));

            case R.id.refresh_chat:
                getUpdates(Chat.this, currentChatName,chatID);
                try {
                    wait(1);
                    startActivity(intent);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


//                Vector<String> images = getPicturesInGroup(chatID);
//
//                for(String s: images){
//                    String[] parts = s.split(",");
//                    String picid = parts[0];
//
//
//                    //TODO
//                }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        File file = new File(this.getFilesDir(),currentChatName);

        displayImages(file);

        rv_chat = (RecyclerView) findViewById(R.id.rv_chat);

        intent = getIntent();

        chatID = intent.getIntExtra("chatID",0);
        currentChatName = intent.getStringExtra("chatName");

        rv_chat.setLayoutManager(new LinearLayoutManager(this));
        galleryImages_rv_adapter adapter = new galleryImages_rv_adapter(names,images,this);
        rv_chat.setAdapter(adapter);
    }
}
