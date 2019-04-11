package com.example.kashish.picit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class Filters extends AppCompatActivity {

    Spinner filters_spinner;
    ImageView filteredImage_iv;

    ArrayList<String> filterNames;

    void tempFilterNames(){
        filterNames = new ArrayList<>();
        filterNames.add("filter 1");
        filterNames.add("filter 2");
    }


    void initViews(){
        filteredImage_iv = (ImageView) findViewById(R.id.imageView_filters);
        filters_spinner = (Spinner) findViewById(R.id.spinner_filters);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filters);

        initViews();
        tempFilterNames();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(),  android.R.layout.simple_spinner_dropdown_item, filterNames);
        adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);

        filters_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(Filters.this, "Filter "+position+" applied",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        filters_spinner.setAdapter(adapter);

//        filters_spinner.set

    }
}
