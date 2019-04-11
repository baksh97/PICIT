package com.example.kashish.picit;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Arrays;

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
                Group g = new Group();
                String text = grpName_til.getEditText().getText().toString();
                String allmember = grpMembers_til.getEditText().getText().toString();
                String [] members = allmember.split(",");

                g.members = new ArrayList<>(Arrays.asList(members));
                g.name = text;
                g.id = text;
                MainActivity.addGrp(g);
                startActivity(new Intent(addGrp.this, MainActivity.class));
            }
        });

    }
}
