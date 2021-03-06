package com.example.kashish.picit;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
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
    ProgressBar pb_add_grp;

    void initViews(){
        pb_add_grp = (ProgressBar) findViewById(R.id.progressBar_add_grp);
        pb_add_grp.setVisibility(View.INVISIBLE);
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
                pb_add_grp.setVisibility(View.VISIBLE);
                boolean error = false;
                Group g = new Group();
                String text = grpName_til.getEditText().getText().toString();
                String[] splitted = text.split("[\t\n _.,]+");
                if(splitted.length==1){
                    String allmember = grpMembers_til.getEditText().getText().toString();
                    String[] members = allmember.split("[^A-Za-z0-9.@_]+");

                    if(text=="" || allmember==""){
                        pb_add_grp.setVisibility(View.INVISIBLE);
                        Toast.makeText(addGrp.this, "Please fill in all the fields!", Toast.LENGTH_SHORT).show();
                    }
                    else {

                        Vector<Integer> memberIds = new Vector<>();
                        memberIds.add(Uid);
                        for (String m : members) {
                            int mId = getsUserIdFromEmailId(m);
                            if (mId != -1) {
                                memberIds.add(mId);
                            } else {
                                error = true;
                                //                        pb_add_grp.setVisibility(View.INVISIBLE);

                                Toast.makeText(addGrp.this, "Member id of " + m + " not found!", Toast.LENGTH_SHORT).show();
                                //                        break;
                            }
                        }

                        int groupID = createGroup(new Vector<Integer>(memberIds), Uid, text);
                        if (groupID == -1) {
                            pb_add_grp.setVisibility(View.INVISIBLE);
                            Toast.makeText(addGrp.this, "Could not create group!", Toast.LENGTH_SHORT).show();
                        } else if (!error) {
                            g.members = new ArrayList<>(Arrays.asList(members));
                            g.name = text;
                            g.id = groupID;

                            MainActivity.addGrp(g);
                            pb_add_grp.setVisibility(View.INVISIBLE);
                            startActivity(new Intent(addGrp.this, MainActivity.class));
                            finish();
                        }
                    }
                }
                else if(splitted.length>=2){
                    pb_add_grp.setVisibility(View.INVISIBLE);
                    Toast.makeText(addGrp.this, R.string.error, Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
}
