package com.example.kashish.picit;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Random;
import java.util.Vector;

public class functions {
    static Bitmap b;

    static FirebaseAuth mAuth = FirebaseAuth.getInstance();

    static FirebaseStorage storage = FirebaseStorage.getInstance();
    static StorageReference storageRef = storage.getReference();


    static boolean isLoggedIn(){
        if(mAuth.getCurrentUser()!=null){
            return true;
        }
        else{
            return false;
        }
    }

    static Vector<String> getGroupsOfUser(int Uid){
        return new Vector<>();
    }

    static int createAlbumServer(String albumName, int Uid){
        return 1;
    }

    static Drawable bitmap2Drawable(Bitmap b, Context context){
        Drawable d = new BitmapDrawable(context.getResources(), b);
        return d;
    }

    static void getUpdates(Context context, String chatName, int chatID){
        Vector<String> images = getPicturesInGroup(chatID);

        if(images==null){
            Toast.makeText(context, "Could not load images! Please try again later.", Toast.LENGTH_SHORT).show();
        }else{
            String chatFolder = chatName+String.valueOf(chatID);

            for(String s: images){
                String[] parts = s.split(",");

                String picid = parts[0];
                String picName = picid+".jpg";

                downloadImaeFromFirebaseStorage(context,chatFolder,picName);
            }
        }
    }

    static void downloadImaeFromFirebaseStorage(final Context context, String chatFolder , final String imageName){
        StorageReference islandRef = storageRef.child(imageName);

        File chatDirectory=new File(context.getFilesDir(),chatFolder);

        if(!chatDirectory.exists()) {
            chatDirectory.mkdir();
        }
        File mypath = new File(chatDirectory,imageName);


        islandRef.getFile(mypath).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                // Local temp file has been created
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                Toast.makeText(context, "Could not download image "+imageName, Toast.LENGTH_SHORT).show();
            }
        });

    }

    static void saveImageOnFirebaseStorage(final Context context, Bitmap b, int id){
        String imageName = String.valueOf(id)+".jpg";
        StorageReference imageRef = storageRef.child(imageName);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        b.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        UploadTask uploadTask = imageRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                Toast.makeText(context, "Could not save Image on cloud!", Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
                Toast.makeText(context, "Image saved", Toast.LENGTH_SHORT).show();
            }
        });
    }

    static void signup(final Context context, String email , String password){
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
//                    Toast.makeText(context, "Registration sucessful", Toast.LENGTH_SHORT).show();
                    context.startActivity(new Intent(context, MainActivity.class));
                }
                else{
                    Toast.makeText(context, task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                }
            }
        });
    }


    static void createUser(String email, String userName){

    }

    static int uploadPicture(int Uid){
        Random r = new Random();
        return r.nextInt();
//        return ;
    }

    static void sharePictureToGroup(int picid, int uid, int groupid){

    }

    static void signin(final Context context, String email , String password){
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
//                    Toast.makeText(context, "Signin", Toast.LENGTH_SHORT).show();
                    context.startActivity(new Intent(context, MainActivity.class));
                }
                else{
                    Toast.makeText(context, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    static int severCreateGroup(int Uid, Vector<Integer> memberIds, String grpName){
        Random r = new Random();
        return r.nextInt();
//        return 0;
    }

    static int getsUserIdFromEmailId(String m){
        Random r = new Random();
        return r.nextInt();
//        return 0;
    }

    static void addUserToGroup(int Uid, int chatID, boolean isActive){

    }

    static Vector<String> getPicturesInGroup(int gid){
        return null;
    }

}
