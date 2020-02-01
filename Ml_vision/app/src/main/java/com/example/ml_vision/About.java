package com.example.ml_vision;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import com.mikhaellopez.circularimageview.CircularImageView;

public class About extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setTitle("About");
        setContentView(R.layout.activity_about);
        CircularImageView circularImageView = findViewById(R.id.cimg);
        circularImageView.setCircleColor(Color.WHITE);
        circularImageView.setCircleColorStart(Color.BLACK);
        circularImageView.setCircleColorEnd(Color.RED);
        circularImageView.setCircleColorDirection(CircularImageView.GradientDirection.TOP_TO_BOTTOM);
        circularImageView.setBorderWidth(10f);
        circularImageView.setBorderColor(Color.BLACK);
        circularImageView.setBorderColorStart(Color.BLACK);
        circularImageView.setBorderColorEnd(Color.RED);
        circularImageView.setBorderColorDirection(CircularImageView.GradientDirection.TOP_TO_BOTTOM);
        circularImageView.setShadowEnable(true);
        circularImageView.setShadowRadius(15f);
        circularImageView.setShadowColor(Color.RED);
        circularImageView.setShadowGravity(CircularImageView.ShadowGravity.CENTER);
        TextView textView = findViewById(R.id.textView3);
        textView.setText("This app helps you with the insurance approval process in a fast and efficient way. Just upload the report and get it verified instantly along with trend charts for key parameters.");
    }
}
