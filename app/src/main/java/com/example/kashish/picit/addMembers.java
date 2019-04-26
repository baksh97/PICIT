package com.example.kashish.picit;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import static com.example.kashish.picit.MainActivity.Uid;
import static com.example.kashish.picit.functions.addUserToGroup;

public class addMembers extends AppCompatActivity {

    TextInputLayout memberEmail_til;
    Button addMember_btn;

    void initViews(){
        memberEmail_til = (TextInputLayout) findViewById(R.id.addMembers_til);
        addMember_btn = (Button) findViewById(R.id.button_addMember);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_members);

        initViews();

        final Intent intent = getIntent();

        addMember_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String members = memberEmail_til.getEditText().getText().toString();
                int chatID = intent.getIntExtra("chatID",0);
                addUserToGroup(Uid, chatID, true);
                onBackPressed();
            }
        });

    }
}
