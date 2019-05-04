package com.example.kashish.picit;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.Vector;

import static com.example.kashish.picit.functions.removeUserFromGroup;

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
        final int chatId = intent.getIntExtra("chatID",-1);

        final Vector<String> memberEmails = functions.getUsersInGroup(chatId);

        ArrayAdapter adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,memberEmails);
        members_lv.setAdapter(adapter);

        members_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder;
                builder = new AlertDialog.Builder(displayMembers.this);
//                builder.setMessage("Do you want to remove this user?");

                //Setting message manually and performing action on button click
                builder.setMessage("Do you want to remove this user?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                removeUserFromGroup(Integer.parseInt(memberEmails.get(position).split(",")[0]), chatId);
                                onBackPressed();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //  Action for 'NO' Button
                                dialog.cancel();
//                                Toast.makeText(getApplicationContext(),"you choose no action for alertbox",
//                                        Toast.LENGTH_SHORT).show();
                            }
                        });
                //Creating dialog box
                AlertDialog alert = builder.create();
                //Setting the title manually
                alert.setTitle("Remove User");
                alert.show();
            }
        });
    }
}
