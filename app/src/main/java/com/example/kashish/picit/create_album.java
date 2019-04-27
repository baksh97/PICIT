package com.example.kashish.picit;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Vector;

import static com.example.kashish.picit.MainActivity.Uid;
import static com.example.kashish.picit.functions.addPicturesToAlbum;
import static com.example.kashish.picit.functions.createAlbumServer;

public class create_album extends AppCompatActivity {

    private static final String TAG = "create_album";
    RecyclerView rv_album;
    EditText albumName_et;

    ArrayList<Bitmap> images;
    ArrayList<String> imageNames;

    public static ArrayList<Bitmap> selectedImages = new ArrayList<>();
    public static ArrayList<String> selectedNames = new ArrayList<>();



    boolean createAlbum(ArrayList<String> names,ArrayList<Bitmap> selectedImages, String albumName){

        int albumID = createAlbumServer(albumName, Uid);

        if(albumID==-1){
            Toast.makeText(this, "Could not create Album on server!", Toast.LENGTH_SHORT).show();
            return false;
        }
        else {

            albumName += "\t"+String.valueOf(albumID);
            File chatDirectory=new File(this.getFilesDir(),albumName);

            Vector<Integer> picIds = new Vector<>();

            for (String name : names) {
                Log.d(TAG, "name: " + name);
                picIds.add(Integer.parseInt(name.substring(0,name.length()-4)));
            }

            boolean b = addPicturesToAlbum(picIds,albumID);
            if(b){
                Log.d(TAG, "Creating album with " + names.size() + " images");

                if (!chatDirectory.exists()) {
                    chatDirectory.mkdir();
                    for (int i = 0; i < names.size(); i++) {
//                    Log.d(TAG,"name: ")
                        String name = names.get(i);
                        Bitmap bitmapImage = selectedImages.get(i);
                        File mypath = new File(chatDirectory, name);
                        FileOutputStream fos = null;
                        try {
                            fos = new FileOutputStream(mypath);
                            // Use the compress method on the BitMap object to write image to the OutputStream
                            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
                            fos.close();
                            Toast.makeText(this, "Album created!", Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            Toast.makeText(this, "Could not make album!", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                            return false;
                        }
                    }

                    return true;
                } else {
                    Toast.makeText(this, "Album with same name already exists!", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
            else{
                Toast.makeText(this, "Could not add pictures to album on server!", Toast.LENGTH_SHORT).show();
                return false;
            }


        }
    }


    void getImages(){
        images = new ArrayList<>();
        imageNames = new ArrayList<>();
//        folders = new ArrayList<>();
//        imagesFiles = new ArrayList<>();
        File file = this.getFilesDir();
        if(file.isDirectory()){
            for(File fs: file.listFiles()){
                Log.d(TAG, fs.getAbsolutePath());
                if(!fs.isDirectory() && fs.getAbsolutePath().endsWith(".jpg")) {
//                    imagesFiles.add(0,fs);
                    imageNames.add(fs.getName());
                    Log.d(TAG, "name: "+fs.getName());
                    String imagePath = fs.getAbsolutePath();
                    Bitmap myBitmap = BitmapFactory.decodeFile(imagePath);
                    images.add(0,myBitmap);
                }
//                else{
//                    folders.add(fs);
//                }
//                fs.delete();
//                holder.imageview.setImageBitmap(myBitmap);
            }
        }
//        re
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_album);

        selectedImages = new ArrayList<>();
        selectedNames = new ArrayList<>();

        getImages();

        rv_album = (RecyclerView) findViewById(R.id.rv_album);
        albumName_et = (EditText) findViewById(R.id.albumName_et);

        rv_album.setLayoutManager(new LinearLayoutManager(this));
        album_rv_adapter adapter = new album_rv_adapter(imageNames,images,this);
        rv_album.setAdapter(adapter);

    }

//    int serverCreateAlbum(){
//        return 0;
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.album_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){
            case R.id.create_album:
                String albumName = albumName_et.getText().toString();
                if(albumName.equals("")){
                    Toast.makeText(this, "Please enter a valid album Name!", Toast.LENGTH_SHORT).show();
                }
                else if(selectedNames.size()==0){
                    Toast.makeText(this, "Please select atleast 1 image to create album!", Toast.LENGTH_SHORT).show();
                }
                else {
                    boolean b = createAlbum(selectedNames, selectedImages, "album_" + albumName_et.getText().toString());
                    if (b) {

//                        Intent intent = new Intent(create_album.this, galleryImages.class);
//                        intent.putExtra("file", create_album.this.getFilesDir());
//                        startActivity(intent);
                        onBackPressed();
                    }
                }
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
