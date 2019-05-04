package com.example.kashish.picit;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

import static com.example.kashish.picit.MainActivity.Uid;
import static com.example.kashish.picit.MainActivity.saveToInternalStorage;
import static com.example.kashish.picit.functions.applyFilter;
import static com.example.kashish.picit.functions.saveImageOnFirebaseStorage;
import static com.example.kashish.picit.functions.uploadPicture;

public class Filters extends AppCompatActivity {

    private static final String TAG = "Filter";
    Spinner filters_spinner;
    ImageView filteredImage_iv;
    Button save_btn;
    ProgressBar pb_filters;

    byte[] b;
    Bitmap bmp;

    ArrayList<String> filterNames;

    void tempFilterNames(){
        filterNames = new ArrayList<>();
        filterNames.add("Select Filter");
        filterNames.add("eye_mask");
        filterNames.add("moustache");
        filterNames.add("jaws");
        filterNames.add("grayscale");
        filterNames.add("blue");
        filterNames.add("red");
        filterNames.add("negative");
        filterNames.add("contrast_improvement");
    }


    void initViews(){
        pb_filters = (ProgressBar) findViewById(R.id.progressBar_filters);
        pb_filters.setVisibility(View.INVISIBLE);
        filteredImage_iv = (ImageView) findViewById(R.id.imageView_filters);
        filters_spinner = (Spinner) findViewById(R.id.spinner_filters);
        save_btn = (Button) findViewById(R.id.button_save_filter);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filters);

        initViews();
        tempFilterNames();

        final Intent intent = getIntent();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(),  android.R.layout.simple_spinner_dropdown_item, filterNames);
        adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);

        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pb_filters.setVisibility(View.VISIBLE);
                int id = uploadPicture(Uid);
                BitmapDrawable bitmapDrawable = (BitmapDrawable) filteredImage_iv.getDrawable();
                saveToInternalStorage(bitmapDrawable.getBitmap(), String.valueOf(id)+".jpg");
                saveImageOnFirebaseStorage(Filters.this,bitmapDrawable.getBitmap(), id);

                pb_filters.setVisibility(View.INVISIBLE);

//                saveToInternalStorage()
                onBackPressed();
            }
        });

//        Intent intent

        b = intent.getByteArrayExtra("image");
//        b = applyFilter(b, filterNames.get(position));

        bmp = BitmapFactory.decodeByteArray(b, 0, b.length);

//        filteredImage_iv.setBackground(functions.bitmap2Drawable(bmp,Filters.this));
        filteredImage_iv.setImageBitmap(bmp);
        filters_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {
//                Toast.makeText(Filters.this, "Filter "+position+" applied",Toast.LENGTH_SHORT).show();

                if(position!=0) {
                    pb_filters.setVisibility(View.VISIBLE);
                    new FilterApply().execute(position);
//                    final Handler h = new Handler() {
//                        public void handleMessage(Message msg){
//                            if(msg.what == 0){
//                                Toast.makeText(Filters.this, "Could not apply filter! Is there a face in this image?", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    };

//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                        b = intent.getByteArrayExtra("image");

//                        filteredImage_iv.setBackground(functions.bitmap2Drawable(bbmp,Filters.this));
//                        }
//                    }));

                }



            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        filters_spinner.setAdapter(adapter);

//        filters_spinner.set

    }

    class FilterApply extends AsyncTask<Integer, Void, Void> {

        @Override
        protected Void doInBackground(Integer ... params) {
            int position = (int)params[0];

            final byte[] b2 = applyFilter(b, filterNames.get(position));

            if (b2 == null) {
                Log.d(TAG, "here: b2 is null");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pb_filters.setVisibility(View.INVISIBLE);
//                                h.sendEmptyMessage(0);
                        Toast.makeText(Filters.this, "Could not apply filter! Is there a face in this image?", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
//                                Toast.makeText(Filters.this, "Changing background!", Toast.LENGTH_SHORT).show();


                Log.d(TAG, "new bitmap created!");
                final Bitmap bbmp = BitmapFactory.decodeByteArray(b2, 0, b2.length);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        filteredImage_iv.setImageBitmap(bbmp);
                        pb_filters.setVisibility(View.INVISIBLE);
                    }
                });


            }
            return null;
        }
    }

}
