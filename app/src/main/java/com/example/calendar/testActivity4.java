package com.example.calendar;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.calendar.ui.main.MainFragment;

public class testActivity4 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test4);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, MainFragment.newInstance())
                    .commitNow();
        }
    }
}