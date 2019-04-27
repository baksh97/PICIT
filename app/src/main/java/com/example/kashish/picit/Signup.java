package com.example.kashish.picit;

import android.content.Context;
import android.content.Intent;
import android.os.StrictMode;
import android.support.annotation.NonNull;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import static com.example.kashish.picit.functions.createUser;


public class Signup extends AppCompatActivity {

    FirebaseAuth mAuth = FirebaseAuth.getInstance();

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

    void signup(final Context context, String email , String password){
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
//                    Toast.makeText(context, "Registration sucessful", Toast.LENGTH_SHORT).show();
                    context.startActivity(new Intent(context, MainActivity.class));
                    finish();
                }
                else{
                    Toast.makeText(context, task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                }
            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);



        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        if(mAuth.getCurrentUser()!=null){
//            signout();
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

                    int uid = createUser(email,userName);
                    if(uid==-1){
                        Toast.makeText(Signup.this, "Could not create user!", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        MainActivity.Uid = uid;
                        signup(getApplicationContext(), email, password);
                    }
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
