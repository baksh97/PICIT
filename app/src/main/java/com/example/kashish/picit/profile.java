package com.example.kashish.picit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class profile extends AppCompatActivity {

    TextView email_tv;

    FirebaseAuth mAuth  = FirebaseAuth.getInstance();

    void initViews(){
        email_tv = (TextView) findViewById(R.id.email_tv_profile);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        initViews();

        email_tv.setText(mAuth.getCurrentUser().getEmail());
    }
}
