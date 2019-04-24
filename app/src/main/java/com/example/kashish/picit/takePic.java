package com.example.kashish.picit;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class takePic extends AppCompatActivity {

//    private ImageView captured_iv;
    public static final int REQUEST_CAMERA = 2;
    Intent cameraIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_pic);

//        captured_iv = (ImageView) findViewById(R.id.imageView_captured);

        cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, REQUEST_CAMERA);
//        dispatchTakePictureIntent();
//        Intent i=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        File dir=
//                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
//
//        output=new File(dir, "CameraContentDemo.jpeg");
//        i.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(output));
//
//        startActivityForResult(i, CONTENT_REQUEST);
//        onActivityResult(REQUEST_CAMERA,RESULT_OK,null);
    }

//    protected void onActivityResult(int requestCode, int resultCode, Intent data)
//    {
//        if (requestCode == REQUEST_CAMERA && resultCode == Activity.RESULT_OK)
//        {
//            Bitmap photo = (Bitmap) data.getExtras().get("data");
////            captured_iv.setImageBitmap(photo);
//            String path = saveToInternalStorage(photo);
////            Toast.makeText(takePic.this, "image stored at: "+path, Toast.LENGTH_SHORT).show();
//            startActivityForResult(cameraIntent, REQUEST_CAMERA);
//        }
//    }


    private static final int CONTENT_REQUEST=1337;
    private File output=null;

//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        Intent i=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        File dir=
//                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
//
//        output=new File(dir, "CameraContentDemo.jpeg");
//        i.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(output));
//
//        startActivityForResult(i, CONTENT_REQUEST);
//    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode,
//                                    Intent data) {
//        if (requestCode == CONTENT_REQUEST) {
//            if (resultCode == RESULT_OK) {
//                Intent i=new Intent(Intent.ACTION_VIEW);
//
//                i.setDataAndType(Uri.fromFile(output), "image/jpeg");
//                startActivity(i);
//                finish();
//            }
//        }
//    }

//    public static String saveToInternalStorage(Bitmap bitmapImage){
////        ContextWrapper cw = new ContextWrapper(getApplicationContext());
//        // path to /data/data/yourapp/app_data/imageDir
//        SimpleDateFormat formatter = new SimpleDateFormat("ddMMyyyyHHmmss");
//        Date date = new Date();
//        File mypath = new File(takePic.this.getFilesDir(), "profile"+ formatter.format(date) +".jpg");
//        // Create imageDir
////        File mypath=new File(directory,"profile.jpg");
//
//        FileOutputStream fos = null;
//        try {
//            fos = new FileOutputStream(mypath);
//            // Use the compress method on the BitMap object to write image to the OutputStream
//            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
//            fos.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
////            try {
////            } catch (IOException e) {
////                e.printStackTrace();
////            }
//        return mypath.getAbsolutePath();
//    }

//    void saveExternally(){
//        ContentValues values = new ContentValues();
//
//        File mypath = new File(this.getFilesDir(), "picit/"+MediaStore.Images.Media.DATE_TAKEN+".jpg");
//
//        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
//        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
////        values.put(MediaStore.MediaColumns.DATA, mypath);
////
////        this.context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
//    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//
//        //SAVE URI FROM GALLERY
//        if ( requestCode == REQUEST_CAMERA && resultCode == RESULT_OK ){
//
//
//            Bundle extras = data.getExtras();
//            Bitmap imageBitmap = (Bitmap) extras.get("data");
//            captured_iv.setImageBitmap(imageBitmap);
//            String path = saveToInternalStorage(imageBitmap);
//
//            Toast.makeText(takePic.this, "image saved to: "+path, Toast.LENGTH_SHORT).show();
////            CropImage.activity(null)
////                    .setGuidelines(CropImageView.Guidelines.ON)
////                    .setAspectRatio(1,1)
////                    .start(this);
//
//        }
//
//
//
//    }

//    static final int REQUEST_IMAGE_CAPTURE = 1;
//
//    private void dispatchTakePictureIntent() {
//        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        // Ensure that there's a camera activity to handle the intent
//        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
//            // Create the File where the photo should go
//            File photoFile = null;
//            try {
//                photoFile = createImageFile();
//            } catch (IOException ex) {
//                // Error occurred while creating the File
//                Toast.makeText(takePic.this, "error", Toast.LENGTH_SHORT).show();
//            }
//            // Continue only if the File was successfully created
//            if (photoFile != null) {
//                Uri photoURI = FileProvider.getUriForFile(this,
//                        "com.example.kashish.picit.fileprovider",
//                        photoFile);
//                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
//                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
//            }
//        }
//    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
//            Bundle extras = data.getExtras();
//            Bitmap imageBitmap = (Bitmap) extras.get("data");
//            captured_iv.setImageBitmap(imageBitmap);
//            String path = saveToInternalStorage(imageBitmap);
//            Toast.makeText(takePic.this, "image stored at: "+path, Toast.LENGTH_SHORT).show();
//
//        }
//    }

//    String currentPhotoPath;
//
//    private File createImageFile() throws IOException {
//        // Create an image file name
//        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
//        String imageFileName = "JPEG_" + timeStamp + "_";
//        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
//        Toast.makeText(this, storageDir.getAbsolutePath(),Toast.LENGTH_SHORT).show();
//        File image = File.createTempFile(
//                imageFileName,  /* prefix */
//                ".jpg",         /* suffix */
//                storageDir      /* directory */
//        );
//
//        // Save a file: path for use with ACTION_VIEW intents
//        currentPhotoPath = image.getAbsolutePath();
//        return image;
//    }
}
