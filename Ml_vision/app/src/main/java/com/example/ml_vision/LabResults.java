package com.example.ml_vision;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Pair;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

public class LabResults extends AppCompatActivity
{

    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab_results);
        recyclerView = findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        RecyclerView.LayoutManager rvLiLayoutManager = layoutManager;
        recyclerView.setLayoutManager(rvLiLayoutManager);
        List<Pair<String, String>> list =new ArrayList<>();
        list.add(new Pair<>("PARAMETERS", "VALUES"));
        for(int i=0;i<Activity_2.parameter.size();i++)
        {
            list.add(new Pair <> (Activity_2.parameter.get(i),Activity_2.pResult.get(i)));
        }
        ImageView selectedImage = findViewById(R.id.selectedImage);
        selectedImage.setImageURI(Activity_2.imageUri);
        RecyclerViewAdapter adapter =new RecyclerViewAdapter(this,list);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onBackPressed()
    {
        super.onResume();
    }
}
