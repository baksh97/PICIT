package com.example.kashish.picit;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import static com.example.kashish.picit.functions.isLoggedIn;
import static com.example.kashish.picit.functions.signin;

public class Signin extends AppCompatActivity {

    TextInputLayout email_tl, password_tl;
    Button signin_btn;
    void initViews(){
        email_tl = (TextInputLayout) findViewById(R.id.email_til_signin);
        password_tl = (TextInputLayout) findViewById(R.id.password_til_signin);
        signin_btn = (Button) findViewById(R.id.button_signin);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        initViews();

        if(isLoggedIn())
        startActivity(new Intent(Signin.this, MainActivity.class));

        signin_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = email_tl.getEditText().getText().toString();
                String password = password_tl.getEditText().getText().toString();

                if(password.equals("") || email.equals("")){
                    Toast.makeText(Signin.this, "Please enter correct information!", Toast.LENGTH_SHORT).show();
                }
                else{
                    signin(getApplicationContext(),email,password);
//                    if(b){
//                        startActivity(new Intent(Signin.this, MainActivity.class));
//                        finish();
//                    }
//                    else{
//                        Toast.makeText(Signin.this, "Could not signin!", Toast.LENGTH_SHORT).show();
//                    }

                }
            }
        });

    }
}
