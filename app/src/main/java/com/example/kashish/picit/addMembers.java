package com.example.kashish.picit;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import static com.example.kashish.picit.MainActivity.Uid;
import static com.example.kashish.picit.functions.addUserToGroup;
import static com.example.kashish.picit.functions.getsUserIdFromEmailId;

public class addMembers extends AppCompatActivity {

    private static final String TAG = "addMembers";
    TextInputLayout memberEmail_til;
    Button addMember_btn;
    ProgressBar pb_add_member;

    void initViews(){
        pb_add_member = (ProgressBar) findViewById(R.id.progressBar_add_member);
        pb_add_member.setVisibility(View.INVISIBLE);
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
                pb_add_member.setVisibility(View.VISIBLE);
                String members = memberEmail_til.getEditText().getText().toString();
                int memberID = getsUserIdFromEmailId(members);
                if(memberID==-1){
                    Toast.makeText(addMembers.this, "Invalid email ID!", Toast.LENGTH_SHORT).show();
                }
                else {
                    int chatID = intent.getIntExtra("chatID", 0);
                    Log.d(TAG, "chatID: " + chatID);
                    boolean b = addUserToGroup(memberID, chatID, true);
                    if (!b) {
                        pb_add_member.setVisibility(View.INVISIBLE);

                        Toast.makeText(addMembers.this, "Could not add user!", Toast.LENGTH_SHORT).show();
                    } else {
                        pb_add_member.setVisibility(View.INVISIBLE);

                        Toast.makeText(addMembers.this, "User added successfully!", Toast.LENGTH_SHORT).show();
                    }
                    onBackPressed();
                }
            }
        });

    }
}
