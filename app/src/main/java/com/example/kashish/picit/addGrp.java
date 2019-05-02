package com.example.kashish.picit;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;

import static com.example.kashish.picit.MainActivity.Uid;
import static com.example.kashish.picit.functions.getsUserIdFromEmailId;
import static com.example.kashish.picit.functions.createGroup;

public class addGrp extends AppCompatActivity {


    TextInputLayout grpName_til,grpMembers_til;
    Button addGrp_btn;

    void initViews(){
        grpMembers_til = (TextInputLayout) findViewById(R.id.til_addGrp_grpMembers);
        grpName_til = (TextInputLayout) findViewById(R.id.textInputLayout_addGrp_grpName);
        addGrp_btn = (Button) findViewById(R.id.button_addGrp);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_grp);

        initViews();

        addGrp_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean error = false;
                Group g = new Group();
                String text = grpName_til.getEditText().getText().toString();
                String allmember = grpMembers_til.getEditText().getText().toString();
                String [] members = allmember.split(",");

                Vector<Integer> memberIds = new Vector<>();
                memberIds.add(Uid);
                for(String m: members){
                    int mId = getsUserIdFromEmailId(m);
                    if(mId!=-1) {
                        memberIds.add(mId);
                    }
                    else{
                        error = true;
                        Toast.makeText(addGrp.this, "Member id of "+m+" not found!", Toast.LENGTH_SHORT).show();
                        break;
                    }
                }

                int groupID = createGroup(new Vector<Integer>(memberIds),Uid, text);
                if(groupID==-1){
                    Toast.makeText(addGrp.this, "Could not create group!", Toast.LENGTH_SHORT).show();
                }
                else if(!error){
                    g.members = new ArrayList<>(Arrays.asList(members));
                    g.name = text;
                    g.id = groupID;

                    MainActivity.addGrp(g);
                    startActivity(new Intent(addGrp.this, MainActivity.class));
                    finish();
                }
            }
        });

    }
}
