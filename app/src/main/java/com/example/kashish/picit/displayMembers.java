package com.example.kashish.picit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.Vector;

public class displayMembers extends AppCompatActivity {

    ListView members_lv;

    void initViews(){
        members_lv = (ListView) findViewById(R.id.lv_members);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_members);

        initViews();

        Intent intent = getIntent();
        int chatId = intent.getIntExtra("chatID",-1);

        Vector<String> memberEmails = functions.getUsersInGroup(chatId);

        ArrayAdapter adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,memberEmails);
        members_lv.setAdapter(adapter);
    }
}
