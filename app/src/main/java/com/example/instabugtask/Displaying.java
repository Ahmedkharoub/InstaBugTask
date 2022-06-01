package com.example.instabugtask;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class Displaying extends AppCompatActivity {

    private TextView display;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_displaying);
        display = findViewById(R.id.display_result);
        Intent intent = getIntent();
        String str = intent.getStringExtra("display");
        display.setText(str);
    }
}