package com.example.kashish.picit;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

import static com.example.kashish.picit.MainActivity.Uid;
import static com.example.kashish.picit.MainActivity.saveToInternalStorage;
import static com.example.kashish.picit.functions.applyFilter;
import static com.example.kashish.picit.functions.uploadPicture;

public class Filters extends AppCompatActivity {

    private static final String TAG = "Filter";
    Spinner filters_spinner;
    ImageView filteredImage_iv;
    Button save_btn;

    byte[] b;
    Bitmap bmp;

    ArrayList<String> filterNames;

    void tempFilterNames(){
        filterNames = new ArrayList<>();
        filterNames.add("eye_mask");
        filterNames.add("moustache");
        filterNames.add("jaws");
    }


    void initViews(){
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
                int id = uploadPicture(Uid);
                BitmapDrawable bitmapDrawable = (BitmapDrawable) filteredImage_iv.getDrawable();
                saveToInternalStorage(bitmapDrawable.getBitmap(), String.valueOf(id)+".jpg");
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
                Toast.makeText(Filters.this, "Filter "+position+" applied",Toast.LENGTH_SHORT).show();

                runOnUiThread(new Thread(new Runnable() {
                    @Override
                    public void run() {
//                        b = intent.getByteArrayExtra("image");
                        byte[] b2 = applyFilter(b, filterNames.get(position));

                        Toast.makeText(Filters.this, "Changing background!", Toast.LENGTH_SHORT).show();

                        Bitmap bbmp = BitmapFactory.decodeByteArray(b2, 0, b2.length);

                        Log.d(TAG,"new bitmap created!");

                        filteredImage_iv.setImageBitmap(bbmp);
//                        filteredImage_iv.setBackground(functions.bitmap2Drawable(bbmp,Filters.this));
                    }
                }));




            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        filters_spinner.setAdapter(adapter);

//        filters_spinner.set

    }
}
