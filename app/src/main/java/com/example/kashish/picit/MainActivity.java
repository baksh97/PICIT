package com.example.kashish.picit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import static com.example.kashish.picit.takePic.REQUEST_CAMERA;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    //    private ListView mListView;
    private RecyclerView rv_main_active,rv_main_inactive;
    private FloatingActionButton camera_fab, gallery_fab,add_grp_fab;
    Intent cameraIntent;

//    static String [] active ={"Hiren", "Pratik", "Dhruv", "Narendra", "Piyush", "Priyank"};
//    static String [] inactive ={"Kirit", "Miral", "Bhushan", "Jiten", "Ajay", "Kamlesh"};
//
//    static String [] activeIds ={"Hiren", "Pratik", "Dhruv", "Narendra", "Piyush", "Priyank"};
//    static String [] inactiveIds ={"Kirit", "Miral", "Bhushan", "Jiten", "Ajay", "Kamlesh"};
//    private
    static ArrayList<String> active_chats,inactive_chats;
    static ArrayList<String> active_chat_ids,inactive_chat_ids;

    static main_rv_adapter_active active_adapter;
    static main_rv_adapter_inactive inactive_adapter;

    static void addGrp(Group g, Context context){
        active_chats.add(g.name);
        active_chat_ids.add(g.id);

        active_adapter.notifyDataSetChanged();

        try {
            String fileName = "chats_picit";
            File file = new File(context.getFilesDir(),fileName);
            FileOutputStream outputStream;
            if(file.exists()){
                outputStream = new FileOutputStream(file,true);
            }
            else{
                outputStream = new FileOutputStream(file);
            }

//            Toast.makeText(context,"chat stored in file: "+file.getAbsolutePath(),Toast.LENGTH_SHORT).show();
            outputStream.write((g.name+"\t"+g.id+"\tactive\n").getBytes());
            outputStream.close();

            Log.d(TAG,"reading file");
            InputStream inputStream = context.openFileInput("chats_picit");
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            String line;

            while ((line = br.readLine()) != null) {

//                Toast.makeText(context, "Read line: " + line, Toast.LENGTH_SHORT).show();
                Log.d(TAG, "Line: " + line);
            }

            inputStream.close();


        } catch (java.io.IOException e) {
            e.printStackTrace();
        }


    }

    static void active2inactive(int position,Context context){

        String s = "";
        try {
            InputStream inputStream = null;
            inputStream = context.openFileInput("chats_picit");
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            String line;

            String changedLine = "";

            while ((line = br.readLine()) != null) {

//                Toast.makeText(context, "Read line: " + line, Toast.LENGTH_SHORT).show();
//                Log.d(TAG, "Line: " + line);
                String[] parts = line.split("\t");

                if(parts.length!=0) {

//                    Toast.makeText(context,"active chats: "+active_chats.size(),Toast.LENGTH_SHORT).show();
//                    Log.d(TAG,"active chats: "+active_chats.size());

                    if (parts[0].equals(active_chats.get(position)) && parts[1].equals(active_chat_ids.get(position))) {
                        changedLine = parts[0] + "\t" + parts[1] + "\t" + "inactive\n";
                    } else {
                        s += line + "\n";
                    }
                }
            }

            s += changedLine;
            inputStream.close();

            String fileName = "chats_picit";
            File file = new File(context.getFilesDir(),fileName);
            FileOutputStream outputStream = new FileOutputStream(file);

//            Toast.makeText(context,"chat stored in file: "+file.getAbsolutePath(),Toast.LENGTH_SHORT).show();
            outputStream.write(s.getBytes());
            outputStream.close();


        } catch (java.io.IOException e) {
            e.printStackTrace();
        }

        String chatName = active_chats.remove(position);
        String chatId = active_chat_ids.remove(position);

        inactive_chats.add(0,chatName);
        inactive_chat_ids.add(0,chatId);

        active_adapter.notifyDataSetChanged();
        inactive_adapter.notifyDataSetChanged();
//        context.startActivity(new Intent(context,MainActivity.class));


    }

    static void inactive2active(int position,Context context){

        String s = "";
        try {
            InputStream inputStream = null;
            inputStream = context.openFileInput("chats_picit");
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            String line;

            String changedLine = "";

            while ((line = br.readLine()) != null) {

//                Toast.makeText(context, "Read line: " + line, Toast.LENGTH_SHORT).show();
//                Log.d(TAG, "Line: " + line);
                String[] parts = line.split("\t");

                if(parts.length!=0) {
                    if (parts[0].equals(inactive_chats.get(position)) && parts[1].equals(inactive_chat_ids.get(position))) {
                        changedLine = parts[0] + "\t" + parts[1] + "\t" + "active\n";
                    } else {
                        s += line + "\n";
                    }
                }
            }

            s += changedLine;
            inputStream.close();

            String fileName = "chats_picit";
            File file = new File(context.getFilesDir(),fileName);
            FileOutputStream outputStream = new FileOutputStream(file);

//            Toast.makeText(context,"chat stored in file: "+file.getAbsolutePath(),Toast.LENGTH_SHORT).show();
            outputStream.write(s.getBytes());
            outputStream.close();


        } catch (java.io.IOException e) {
            e.printStackTrace();
        }

//        context.startActivity(new Intent(context,MainActivity.class));
        String chatName = inactive_chats.remove(position);
        String chatId = inactive_chat_ids.remove(position);

        active_chats.add(0,chatName);
        active_chat_ids.add(0,chatId);

        active_adapter.notifyDataSetChanged();
        inactive_adapter.notifyDataSetChanged();
    }

    public void deleteRecursive(File fileOrDirectory) {

        if (fileOrDirectory.isDirectory()) {
            for (File child : fileOrDirectory.listFiles()) {
                deleteRecursive(child);
            }
        }

        fileOrDirectory.delete();
    }

    void initChats(){
        active_chat_ids = new ArrayList<>();
        active_chats = new ArrayList<>();
        inactive_chat_ids = new ArrayList<>();
        inactive_chats = new ArrayList<>();
//        active_chats = new ArrayList<>(Arrays.asList(active));
//        inactive_chats = new ArrayList<>(Arrays.asList(inactive));
//
//        active_chat_ids = new ArrayList<>(Arrays.asList(activeIds));
//        inactive_chat_ids = new ArrayList<>(Arrays.asList(inactiveIds));

        try {
            for(File f: this.getFilesDir().listFiles()){
                Log.d(TAG,"file: "+f.getAbsolutePath());
//                if(f.isDirectory()){
//                    deleteRecursive(f);
//                }
//                if(f.getAbsolutePath().endsWith("/chats_picit")){
//                    Log.d(TAG,"found file: "+f.getAbsolutePath());
//                    f.delete();
//                }
            }

//            File file = new File(this.getFilesDir(),"chats_picit");
            InputStream inputStream = this.openFileInput("chats_picit");
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            String line;

            while ((line = br.readLine()) != null) {

//                Toast.makeText(this, "Read line: "+line,Toast.LENGTH_SHORT).show();
                Log.d(TAG,"Line: "+line);
//                text.append(line);
//                text.append('\n');
                String[] parts = line.split("\t");
                for(String s: parts){
                    Log.d(TAG,"parts: "+s);
                }
                if(parts.length!=0) {

                    Toast.makeText(this,"parts[2]: "+parts[2],Toast.LENGTH_SHORT).show();
                    Log.d(TAG,"parts[2]: "+parts[2]);

                    if (parts[2].contains("inactive")) {
                        inactive_chats.add(0, parts[0]);
                        inactive_chat_ids.add(0, parts[1]);
                    } else{
                        active_chats.add(0, parts[0]);
                        active_chat_ids.add(0, parts[1]);
                    }
                }
            }
            br.close();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }

    void saveImageToChat(Bitmap bitmapImage, String chatFolder, String imageName){
        File chatDirectory=new File(this.getFilesDir(),chatFolder);

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

    void getUpdates(String chatName, String chatID){
        ArrayList<Bitmap> images = new ArrayList<>();
        ArrayList<String> imageNames = new ArrayList<>();

        for(int i=0;i<imageNames.size();i++){
            Bitmap b = images.get(i);
            saveImageToChat(b,chatName+chatID,imageNames.get(i));
            saveToInternalStorage(b,imageNames.get(i));
        }
    }

    void initViews(){
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
//            captured_iv.setImageBitmap(photo);
            SimpleDateFormat formatter = new SimpleDateFormat("ddMMyyyyHHmmss");
            Date date = new Date();
            String imageName = "Image"+ formatter.format(date) +".jpg";
            String path = saveToInternalStorage(photo,imageName);
            for(int i=0;i<active_chats.size();i++){
                saveImageToChat(photo,active_chats.get(i)+active_chat_ids.get(i),imageName);
            }
//            Toast.makeText(takePic.this, "image stored at: "+path, Toast.LENGTH_SHORT).show();
            startActivityForResult(cameraIntent, REQUEST_CAMERA);
        }
    }

    private String saveToInternalStorage(Bitmap bitmapImage, String imageName){
//        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
//        SimpleDateFormat formatter = new SimpleDateFormat("ddMMyyyyHHmmss");
//        Date date = new Date();
        File mypath = new File(this.getFilesDir(), imageName);
        // Create imageDir
//        File mypath=new File(directory,"profile.jpg");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
//            try {
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
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
                startActivity(new Intent(MainActivity.this, galleryAlbums.class));
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        chats = new ArrayList<>();

        if(active_chats==null){
            initChats();
        }

        initViews();

        rv_main_active.setLayoutManager(new LinearLayoutManager(this));
        rv_main_inactive.setLayoutManager(new LinearLayoutManager(this));

        active_adapter = new main_rv_adapter_active(active_chats,active_chat_ids,null,this);
        inactive_adapter = new main_rv_adapter_inactive(inactive_chats,inactive_chat_ids,null,this);

        rv_main_active.setAdapter(active_adapter);
        rv_main_inactive.setAdapter(inactive_adapter);

//        camera_fab.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                return false;
//            }
//        });

        camera_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(MainActivity.this, takePic.class));
                cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, REQUEST_CAMERA);
            }
        });

        gallery_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, galleryImages.class);
                i.putExtra("file",MainActivity.this.getFilesDir());
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
