package com.example.ml_vision;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import fr.ganfra.materialspinner.MaterialSpinner;

public class Plots extends AppCompatActivity {

    MaterialSpinner materialSpinner;
    ImageView imageView;
    List<String> listItems = new ArrayList<>();
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Plots");
        setContentView(R.layout.activity_plots);
        FloatingActionButton homeb = findViewById(R.id.home);
        homeb.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(Plots.this, Activity_2.class));
            }
        });
        imageView = findViewById(R.id.plot);
        listItems.add("Urea");
        listItems.add("Haemoglobin");
        listItems.add("Platelets");
        materialSpinner = findViewById(R.id.spinner);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,listItems);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        materialSpinner.setAdapter(adapter);
        materialSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position != -1)
                {
                    if(position==0)
                        imageView.setImageResource(R.drawable.urea);
                    else if(position==1)
                        imageView.setImageResource(R.drawable.hg);
                    else if(position==2)
                        imageView.setImageResource(R.drawable.platelets);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}
