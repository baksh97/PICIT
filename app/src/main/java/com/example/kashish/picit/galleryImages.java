package com.example.kashish.picit;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.util.ArrayList;

public class galleryImages extends AppCompatActivity {

    private static final int FILE_SELECT_CODE = 0;
    private static final String TAG = "galleryImages" ;

//    private ArrayList<Bitmap> imageBitmaps;

    RecyclerView rv_gallery_images;
    FloatingActionButton fab_createAlbum;

    private ArrayList<Bitmap> images = new ArrayList<>();
    private ArrayList<String> names = new ArrayList<>();
//    private ArrayList<File> folders = new ArrayList<>();
//    private ArrayList<File> imagesFiles = new ArrayList<>();

    void initViews(){
        rv_gallery_images = (RecyclerView) findViewById(R.id.rv_gallery_images);
        fab_createAlbum = (FloatingActionButton) findViewById(R.id.floatingActionButton_createAlbum);
    }

//    void getImages(){
//        for(int i=0;i<10;i++){
//            images.add(null);
//        }
//    }

    void displayImages(File file){
        images = new ArrayList<>();
        names = new ArrayList<>();
//        folders = new ArrayList<>();
//        imagesFiles = new ArrayList<>();
//        File file = this.getFilesDir();
        if(file.isDirectory()){
            for(File fs: file.listFiles()){
                Log.d(TAG, fs.getAbsolutePath());
                if(!fs.isDirectory() && fs.getAbsolutePath().endsWith(".jpg")) {
//                    imagesFiles.add(0,fs);
                    String imagePath = fs.getAbsolutePath();
                    Bitmap myBitmap = BitmapFactory.decodeFile(imagePath);
                    images.add(0,myBitmap);
                    names.add(fs.getName());
                }
//                else{
//                    folders.add(fs);
//                }
//                fs.delete();
//                holder.imageview.setImageBitmap(myBitmap);
            }
        }
    }

//    void showFileChooser(){
//        Toast.makeText(this, "this clicked", Toast.LENGTH_SHORT).show();
//        Uri selectedUri = Uri.parse(gallery.this.getFilesDir().getPath());
//        Intent intent = new Intent(Intent.ACTION_PICK);
//        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
//        intent.setDataAndType(selectedUri, "resource/folder");
//
//        if (intent.resolveActivityInfo(getPackageManager(), 0) != null)
//        {
//            startActivityForResult(intent,FILE_SELECT_CODE);
//        }
//        else
//        {
//            // if you reach this place, it means there is no any file
//            // explorer app installed on your device
//        }
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_images);

        initViews();
//        getImages();

//        ArrayList<File> folders = getAllFolders()

        Intent intent = getIntent();
        File file = (File)intent.getExtras().get("file");
        displayImages(file);

        fab_createAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(galleryImages.this, create_album.class));
//                showFileChooser();
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

//        if(folders.size()==0){
//            rv_gallery_album.setVisibility(View.INVISIBLE);
//        }

        rv_gallery_images.setLayoutManager(new LinearLayoutManager(this));
        galleryImages_rv_adapter adapter = new galleryImages_rv_adapter(names,images,this);
        rv_gallery_images.setAdapter(adapter);




    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if(requestCode == FILE_SELECT_CODE) {
//            if(resultCode == Activity.RESULT_OK) {
//                if(data.getClipData() != null) {
//                    int count = data.getClipData().getItemCount();
//                    Log.d(TAG,"Count: "+count);
//                    int currentItem = 0;
//                    while(currentItem < count) {
//                        Uri imageUri = data.getClipData().getItemAt(currentItem).getUri();
//                        //do something with the image (save it to some directory or whatever you need to do with it here)
//                        currentItem = currentItem + 1;
//                    }
//                } else if(data.getData() != null) {
//                    Log.d(TAG,"Here");
//                    String imagePath = data.getData().getPath();
//                    //do something with the image (save it to some directory or whatever you need to do with it here)
//                }
//            }
//        }
//        else{
//            int count = data.getClipData().getItemCount();
//            Log.d(TAG,"Count: "+count);
//        }
//    }
}
