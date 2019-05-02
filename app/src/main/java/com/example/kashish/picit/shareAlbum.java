package com.example.kashish.picit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

import static com.example.kashish.picit.functions.shareAlbumWithGroup;

public class shareAlbum extends AppCompatActivity {

    static ArrayList<Integer> selectedGroupsIds = new ArrayList<>();

    RecyclerView rv_shareAlbum;
    int albumId;
    private String TAG = "shareAlbum";

    void initViews(){
        rv_shareAlbum = (RecyclerView) findViewById(R.id.rv_shareAlbum);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_album);

        initViews();

        selectedGroupsIds = new ArrayList<>();

        Intent intent = getIntent();
        albumId = intent.getIntExtra("albumID",-1);

        rv_shareAlbum.setLayoutManager(new LinearLayoutManager(this));
        ArrayList<String> chats = new ArrayList<>(MainActivity.active_chats);
        chats.addAll(MainActivity.inactive_chats);
        ArrayList<Integer> chatIds = new ArrayList<>(MainActivity.active_chat_ids);
        chatIds.addAll(MainActivity.inactive_chat_ids);
        shareAlbum_rv_adapter adapter = new shareAlbum_rv_adapter(chats,chatIds,this);
        rv_shareAlbum.setAdapter(adapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.share_album_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){
            case R.id.share_album_actual:
                if(selectedGroupsIds.size()==0){
                    Toast.makeText(this, "Please select atleast 1 group to share album!", Toast.LENGTH_SHORT).show();
                }
                else {
                    for(int i: selectedGroupsIds) {
                        Log.d(TAG, "Sharing album: "+albumId+" with group: "+i);
                        boolean b = shareAlbumWithGroup(albumId, i);
                        if(!b){
                            Toast.makeText(this, "Could not share with group: "+i, Toast.LENGTH_SHORT).show();
                        }
                    }
                    onBackPressed();
                }
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
