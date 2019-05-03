package com.example.kashish.picit;

import android.content.Context;
import android.content.Intent;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.AWSStartupHandler;
import com.amazonaws.mobile.client.AWSStartupResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import static com.example.kashish.picit.functions.createUser;
import static com.example.kashish.picit.functions.signout;


public class Signup extends AppCompatActivity {

    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    ProgressBar pb_signup;
    String TAG = "Signup";
    TextInputLayout email_tl, password_tl, username_til;
    Button signup_btn,signin_btn;
    void initViews(){
        pb_signup = (ProgressBar) findViewById(R.id.progressBar_signup);
        pb_signup.setVisibility(View.INVISIBLE);
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
                    pb_signup.setVisibility(View.INVISIBLE);
                    Intent i = new Intent(context, MainActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(i);

                    finish();
                }
                else{
                    Toast.makeText(context, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    pb_signup.setVisibility(View.INVISIBLE);

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
        else {

//            if (ContextCompat.checkSelfPermission(Signup.this,
//                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                    != PackageManager.PERMISSION_GRANTED) {
//
//                // Permission is not granted
//                // Should we show an explanation?
//                if (ActivityCompat.shouldShowRequestPermissionRationale(thisActivity,
//                        Manifest.permission.READ_CONTACTS)) {
//                    // Show an explanation to the user *asynchronously* -- don't block
//                    // this thread waiting for the user's response! After the user
//                    // sees the explanation, try again to request the permission.
//                } else {
//                    // No explanation needed; request the permission
//                    ActivityCompat.requestPermissions(thisActivity,
//                            new String[]{Manifest.permission.READ_CONTACTS},
//                            MY_PERMISSIONS_REQUEST_READ_CONTACTS);
//
//                    // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
//                    // app-defined int constant. The callback method gets the
//                    // result of the request.
//                }
//            } else {
//                // Permission has already been granted
//            }

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
                    pb_signup.setVisibility(View.VISIBLE);

                    String email = email_tl.getEditText().getText().toString();
                    String password = password_tl.getEditText().getText().toString();
                    String userName = username_til.getEditText().getText().toString();

                    String[] splitted = userName.split("[\t\n _.,]+");
                    if(splitted.length>=2){
                        pb_signup.setVisibility(View.INVISIBLE);
                        Toast.makeText(Signup.this, R.string.error, Toast.LENGTH_SHORT).show();
                    }
                    else if (password.equals("") || email.equals("") || userName.equals("")) {
                        pb_signup.setVisibility(View.INVISIBLE);
                        Toast.makeText(Signup.this, "Please enter correct information!", Toast.LENGTH_SHORT).show();
                    } else {

                        int uid = createUser(email, userName);
                        if (uid == -1) {
                            pb_signup.setVisibility(View.INVISIBLE);
                            Toast.makeText(Signup.this, "Could not create user!", Toast.LENGTH_SHORT).show();
                        } else {
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
}
