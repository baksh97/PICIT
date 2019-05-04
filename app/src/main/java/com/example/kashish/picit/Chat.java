package com.example.kashish.picit;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Vector;

import static com.example.kashish.picit.MainActivity.Uid;
import static com.example.kashish.picit.MainActivity.refreshingChat;
//import static com.example.kashish.picit.functions.adapter;
import static com.example.kashish.picit.MainActivity.userFolder;
import static com.example.kashish.picit.functions.getImagesInFolder;
import static com.example.kashish.picit.functions.getPicturesInGroup;
import static com.example.kashish.picit.functions.getUpdates;

public class Chat extends AppCompatActivity {
    private static final String TAG = "Chat";
    RecyclerView rv_chat;//,rv_chatAlbums;
    ArrayList<Bitmap> images;
    ArrayList<String> names;
    ArrayList<File> imageFiles;

    Intent intent;

    String currentChatName;
    int chatID;

    void displayImages(File file){
        images = new ArrayList<>();
        imageFiles = new ArrayList<>();
        names = new ArrayList<>();
        getImagesInFolder(file, imageFiles, images, names);
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
                Intent intent2 = new Intent(Chat.this, addMembers.class);
                intent2.putExtra("chatID",chatID);
                startActivity(intent2);
                break;
            case R.id.refresh_chat:
                if(!refreshingChat) new Refresh().execute();
                else Toast.makeText(this, "Already refreshing a chat! Please wait.", Toast.LENGTH_SHORT).show();
                break;

            case R.id.member_chat:
                Intent intent = new Intent(Chat.this, displayMembers.class);
                intent.putExtra("chatName",currentChatName);
                intent.putExtra("chatID", chatID);
                startActivity(intent);
                break;

            case R.id.albums_chat:
                Intent intent1 = new Intent(Chat.this, galleryAlbums.class);
                File f = new File(userFolder,currentChatName+String.valueOf(chatID));
                intent1.putExtra("file",f);
                startActivity(intent1);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);


        intent = getIntent();
        chatID = intent.getIntExtra("chatID",0);
        currentChatName = intent.getStringExtra("chatName");

        File file = new File(userFolder,currentChatName+chatID);
        displayImages(file);

        rv_chat = (RecyclerView) findViewById(R.id.rv_chat);

        rv_chat.setLayoutManager(new LinearLayoutManager(this));
        galleryImages_rv_adapter adapter = new galleryImages_rv_adapter(imageFiles,names,images,this);
        rv_chat.setAdapter(adapter);
    }

    class Refresh extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void ... params) {
            getUpdates(Chat.this, currentChatName,chatID);
            return null;
        }
    }

}
