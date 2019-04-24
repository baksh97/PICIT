package com.example.kashish.picit;

import android.app.Activity;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class gallery extends AppCompatActivity {

    private static final int FILE_SELECT_CODE = 0;
    private static final String TAG = "gallery" ;

//    private ArrayList<Bitmap> imageBitmaps;

    RecyclerView rv_gallery;
    FloatingActionButton fab_createAlbum;

    private ArrayList<Bitmap> images = new ArrayList<>(10);
    private ArrayList<File> folders = new ArrayList<>();

    void initViews(){
        rv_gallery = (RecyclerView) findViewById(R.id.rv_gallery);
        fab_createAlbum = (FloatingActionButton) findViewById(R.id.floatingActionButton_createAlbum);
    }

//    void getImages(){
//        for(int i=0;i<10;i++){
//            images.add(null);
//        }
//    }

    void displayImages(File file){
        images = new ArrayList<>();
        folders = new ArrayList<>();
//        File file = this.getFilesDir();
        if(file.isDirectory()){
            for(File fs: file.listFiles()){
                Log.d(TAG, fs.getAbsolutePath());
                if(!fs.isDirectory()) {
                    String imagePath = fs.getAbsolutePath();
                    Bitmap myBitmap = BitmapFactory.decodeFile(imagePath);
                    images.add(0,myBitmap);
                }
                else{
                    folders.add(fs);
                }
//                fs.delete();
//                holder.imageview.setImageBitmap(myBitmap);
            }
        }
    }

    void showFileChooser(){
        Uri selectedUri = Uri.parse(gallery.this.getFilesDir().getPath());
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setDataAndType(selectedUri, "resource/folder");

        if (intent.resolveActivityInfo(getPackageManager(), 0) != null)
        {
            startActivityForResult(intent,FILE_SELECT_CODE);
        }
        else
        {
            // if you reach this place, it means there is no any file
            // explorer app installed on your device
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        initViews();
//        getImages();

//        ArrayList<File> folders = getAllFolders()

        displayImages(this.getFilesDir());

        fab_createAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showFileChooser();
//                Intent intent = new Intent(Intent.ACTION_PICK);
//                intent.setType("image/*");
//                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
////                ContextWrapper cw = new ContextWrapper(getApplicationContext());
////                cw.g
//                Uri uri = Uri.parse(gallery.this.getFilesDir().getPath());
////                Uri uri = Uri.parse(Environment.getExternalStorageDirectory().getPath() + "/picit/");
//                intent.setDataAndType(uri,"resource/folder");
////                intent.set
//                startActivityForResult(intent, FILE_SELECT_CODE);
            }
        });

        rv_gallery.setLayoutManager(new LinearLayoutManager(this));
        gallery_rv_adapter adapter = new gallery_rv_adapter(images,this);
        rv_gallery.setAdapter(adapter);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == FILE_SELECT_CODE) {
            if(resultCode == Activity.RESULT_OK) {
                if(data.getClipData() != null) {
                    int count = data.getClipData().getItemCount();
                    Log.d(TAG,"Count: "+count);
                    int currentItem = 0;
                    while(currentItem < count) {
                        Uri imageUri = data.getClipData().getItemAt(currentItem).getUri();
                        //do something with the image (save it to some directory or whatever you need to do with it here)
                        currentItem = currentItem + 1;
                    }
                } else if(data.getData() != null) {
                    Log.d(TAG,"Here");
                    String imagePath = data.getData().getPath();
                    //do something with the image (save it to some directory or whatever you need to do with it here)
                }
            }
        }
        else{
            int count = data.getClipData().getItemCount();
            Log.d(TAG,"Count: "+count);
        }
    }
}
