package com.example.kashish.picit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Vector;

import static com.example.kashish.picit.functions.getEmailOfUser;
import static com.example.kashish.picit.functions.getGroupsOfUser;
import static com.example.kashish.picit.functions.getPicturesInGroup;
import static com.example.kashish.picit.functions.getsUserIdFromEmailId;
import static com.example.kashish.picit.functions.saveImageOnFirebaseStorage;
//import static com.example.kashish.picit.functions.saveImageOnS3;
import static com.example.kashish.picit.functions.setGroupActive;
import static com.example.kashish.picit.functions.setGroupInactive;
import static com.example.kashish.picit.functions.sharePictureToGroup;
import static com.example.kashish.picit.functions.signout;
import static com.example.kashish.picit.functions.uploadPicture;

public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_CAMERA = 2;

    private static final String TAG = "MainActivity";
    //    private ListView mListView;
    private RecyclerView rv_main_active,rv_main_inactive;
    private FloatingActionButton camera_fab, gallery_fab,add_grp_fab;
    Intent cameraIntent;

    private ProgressBar pb_main;

    String currentImagePath;

    public static int Uid = 0;
    public static File userFolder;

    public static boolean refreshingChat = false;

    static Context context;

    static ArrayList<String> active_chats,inactive_chats;
    static ArrayList<Integer> active_chat_ids,inactive_chat_ids;

    static main_rv_adapter_active active_adapter;
    static main_rv_adapter_inactive inactive_adapter;

    static void addGrp(Group g){
            inactive_chats.add(g.name);
            inactive_chat_ids.add(g.id);
            inactive_adapter.notifyDataSetChanged();
    }

    static void active2inactive(int position,Context context){
        int chatId = active_chat_ids.get(position);
        boolean b = setGroupInactive(Uid, chatId);
        if(b){
            String chatName = active_chats.remove(position);
            active_chat_ids.remove(position);

            inactive_chats.add(0,chatName);
            inactive_chat_ids.add(0,chatId);

            active_adapter.notifyDataSetChanged();
            inactive_adapter.notifyDataSetChanged();
        }
        else{
            Toast.makeText(context, "Could not set Inactive! Please try again later.", Toast.LENGTH_SHORT).show();
        }
        Log.d(TAG, "setInactive: "+b);
    }

    static void inactive2active(int position,Context context){
        boolean b = setGroupActive(Uid, inactive_chat_ids.get(position));
        Log.d(TAG, "setActive: "+b);

        if(b){
            String chatName = inactive_chats.remove(position);
            int chatId = inactive_chat_ids.remove(position);

            active_chats.add(0,chatName);
            active_chat_ids.add(0,chatId);


            active_adapter.notifyDataSetChanged();
            inactive_adapter.notifyDataSetChanged();
        }
        else{
            Toast.makeText(context, "Could not set Active! Please try again later.", Toast.LENGTH_SHORT).show();
        }
    }

    void initChats(){
        active_chats.clear();
        inactive_chats.clear();
        active_chat_ids.clear();
        inactive_chat_ids.clear();
        Vector<String> v = getGroupsOfUser(Uid);

        if(v!=null) {
            for (String s : v) {
                String[] parts = s.split(",");
                int active = Integer.parseInt(parts[2]);
                if (active == 1) {
                    active_chats.add(parts[1]);
                    active_chat_ids.add(Integer.parseInt(parts[0]));
                    active_adapter.notifyDataSetChanged();
                } else {
                    inactive_chats.add(parts[1]);
                    inactive_chat_ids.add(Integer.parseInt(parts[0]));
                    inactive_adapter.notifyDataSetChanged();
                }
            }
        }
        else{
            Toast.makeText(context, "Could not load groups. Please reload!", Toast.LENGTH_SHORT).show();
        }
    }

    void saveImageToChat(Bitmap bitmapImage, String chatFolder, String imageName){
        File chatDirectory=new File(userFolder,chatFolder);

        if(!chatDirectory.exists()) {
            chatDirectory.mkdir();
        }
        File mypath = new File(chatDirectory,imageName);

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void initViews(){
        pb_main = (ProgressBar) findViewById(R.id.progressBar_main);
        pb_main.setVisibility(View.INVISIBLE);
        rv_main_active = (RecyclerView) findViewById(R.id.rv_main_active);
        rv_main_inactive = (RecyclerView) findViewById(R.id.rv_main_inactive);
        camera_fab = (FloatingActionButton) findViewById(R.id.floatingActionButton_camera);
        gallery_fab = (FloatingActionButton) findViewById(R.id.floatingActionButton_gallery);
        add_grp_fab = (FloatingActionButton) findViewById(R.id.floatingActionButton_addGrp);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == REQUEST_CAMERA && resultCode == Activity.RESULT_OK)
        {
            Bitmap photo = (Bitmap) data.getExtras().get("data");

//            Bitmap photo = BitmapFactory.decodeFile(currentImagePath);
//            startActivity(new Intent(MainActivity.this,fullImage.class));
            Log.d(TAG,"bytes: "+photo.getByteCount());



            int id = uploadPicture(Uid);
            if(id==-1){
                Toast.makeText(context, "Could not upload picture!", Toast.LENGTH_SHORT).show();
            }
            else{

                try {
                    String imageName = String.valueOf(id)+".jpg";
                    File mypath = new File(userFolder, imageName);
                    FileOutputStream fos = null;
                    fos = new FileOutputStream(mypath);
                    String path = saveToInternalStorage(photo,imageName);
                    saveImageOnFirebaseStorage(context,photo, id);              //storing on S3 to get
                    for(int i=0;i<active_chats.size();i++){
                        saveImageToChat(photo,active_chats.get(i)+active_chat_ids.get(i),imageName);
                        sharePictureToGroup(id,Uid,active_chat_ids.get(i));
                    }
                    startActivityForResult(cameraIntent, REQUEST_CAMERA);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

//                ByteArrayOutputStream out = new ByteArrayOutputStream();
//                photo.compress(Bitmap.CompressFormat.JPEG, 10, out);
//                photo = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));

//                if(Integer.valueOf(android.os.Build.VERSION.SDK)==28){
//                    Matrix matrix = new Matrix();
//
//                    matrix.postRotate(90);
//
//                    Bitmap scaledBitmap = Bitmap.createScaledBitmap(photo, photo.getWidth(), photo.getHeight(), true);
//
//                    photo = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
//
//                }

//                    photo.compress(Bitmap.CompressFormat.JPEG, 50, fos);
//                    fos.close();
//                    photo = BitmapFactory.decodeFile(mypath.getAbsolutePath());


            }

        }
//        else{
//            Toast.makeText(context, "Image not saved."+resultCode, Toast.LENGTH_SHORT).show();
//        }
    }

    public static String saveToInternalStorage(Bitmap bitmapImage, String imageName){
        File mypath = new File(userFolder, imageName);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mypath.getAbsolutePath();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){
            case R.id.show_albums:
                Intent i = new Intent(MainActivity.this, galleryAlbums.class);
                i.putExtra("file", userFolder);
                startActivity(i);
                break;
            case R.id.refresh_main:
                initChats();
                break;
            case R.id.signout_main:
                signout();
                startActivity(new Intent(MainActivity.this, Signup.class));
                break;

            case R.id.profile_main:
                startActivity(new Intent(MainActivity.this, profile.class));
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    void updateUserFolder(){
        File f = new File(this.getFilesDir(),String.valueOf(Uid));
        if(!f.exists())f.mkdir();
        userFolder = f;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        Uid = getsUserIdFromEmailId(getEmailOfUser());

        if(Uid == -1){
            Toast.makeText(context, "Server not online.. Try again later!", Toast.LENGTH_SHORT).show();
        }
        else{
            updateUserFolder();
            setContentView(R.layout.activity_main);
            initViews();

            active_chats = new ArrayList<>();
            inactive_chats = new ArrayList<>();

            active_chat_ids = new ArrayList<>();
            inactive_chat_ids = new ArrayList<>();

            active_adapter = new main_rv_adapter_active(active_chats, active_chat_ids, null, this);
            inactive_adapter = new main_rv_adapter_inactive(inactive_chats, inactive_chat_ids, null, this);

            initChats();

            rv_main_active.setLayoutManager(new LinearLayoutManager(this));
            rv_main_inactive.setLayoutManager(new LinearLayoutManager(this));

            rv_main_active.setAdapter(active_adapter);
            rv_main_inactive.setAdapter(inactive_adapter);

            camera_fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    try {
                        cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//                        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
//                        File f = File.createTempFile("temp",".jpg",storageDir);
//                        currentImagePath = f.getAbsolutePath();
//                        f = File.createTempFile("temp",".jpg",userFolder);
//                        Uri fileUri = Uri.fromFile(f);
//                        if(f==null){
//                            Toast.makeText(MainActivity.this, "file is null", Toast.LENGTH_SHORT).show();
//                        }
//                        else if(cameraIntent.resolveActivity(getPackageManager())!=null){
//                            Uri fileUri = FileProvider.getUriForFile(MainActivity.this, "com.example.kashish.picit.fileprovider",f);
//                            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                        startActivityForResult(cameraIntent, REQUEST_CAMERA);
//                        }


//                    Uri outuri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".com.example.kashish.picit.provider", f);
//                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outuri);
//                    startActivityForResult(intent, 2);


//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }

                }
            });

            gallery_fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(MainActivity.this, galleryImages.class);
                    i.putExtra("file", userFolder);
                    startActivity(i);
                }
            });

            add_grp_fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(MainActivity.this, addGrp.class));
                }
            });
        }
    }

    public void onBackPressed(){
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }

}

