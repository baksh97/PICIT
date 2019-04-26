package com.example.kashish.picit;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.AWSStartupHandler;
import com.amazonaws.mobile.client.AWSStartupResult;

import static com.example.kashish.picit.functions.createUser;
import static com.example.kashish.picit.functions.isLoggedIn;
import static com.example.kashish.picit.functions.signup;

public class Signup extends AppCompatActivity {

    String TAG = "Signup";
    TextInputLayout email_tl, password_tl, username_til;
    Button signup_btn,signin_btn;
    void initViews(){
        email_tl = (TextInputLayout) findViewById(R.id.email_til_signup);
        password_tl = (TextInputLayout) findViewById(R.id.password_til_signup);
        signup_btn = (Button) findViewById(R.id.button_signup);
        signin_btn = (Button) findViewById(R.id.button_signup_login);
        username_til = (TextInputLayout) findViewById(R.id.username_til_signup);
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        if(isLoggedIn()){
            startActivity(new Intent(Signup.this, MainActivity.class));
        }

        AWSMobileClient.getInstance().initialize(this, new AWSStartupHandler() {
            @Override
            public void onComplete(AWSStartupResult awsStartupResult) {
                Log.d(TAG, "AWSMobileClient is instantiated and you are connected to AWS!");
            }
        }).execute();

        initViews();

        signin_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Signup.this, Signin.class));
            }
        });

        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = email_tl.getEditText().getText().toString();
                String password = password_tl.getEditText().getText().toString();
                String userName = username_til.getEditText().getText().toString();

                if(password.equals("") || email.equals("") || userName.equals("")){
                    Toast.makeText(Signup.this, "Please enter correct information!", Toast.LENGTH_SHORT).show();
                }
                else{
                    signup(getApplicationContext(),email,password);
//                    if(b){
//                        createUser(email,userName);
//                        startActivity(new Intent(Signup.this, MainActivity.class));
//                        finish();
//                    }
//                    else{
//                        Toast.makeText(Signup.this, "Could not signup!", Toast.LENGTH_SHORT).show();
//                    }

                }
            }
        });

    }
}
