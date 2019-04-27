package com.example.kashish.picit;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Signin extends AppCompatActivity {

    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    TextInputLayout email_tl, password_tl;
    Button signin_btn;
    void initViews(){
        email_tl = (TextInputLayout) findViewById(R.id.email_til_signin);
        password_tl = (TextInputLayout) findViewById(R.id.password_til_signin);
        signin_btn = (Button) findViewById(R.id.button_signin);
    }

    void signin(final Context context, String email , String password){
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
//                    Toast.makeText(context, "Signin", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
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
        setContentView(R.layout.activity_signin);

        initViews();

        if(mAuth.getCurrentUser()!=null)
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
