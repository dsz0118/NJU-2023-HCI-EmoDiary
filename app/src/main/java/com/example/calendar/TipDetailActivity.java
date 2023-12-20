package com.example.calendar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.calendar.bean.Tip;
import com.example.calendar.service.TipService;

public class TipDetailActivity extends AppCompatActivity {

    TextView tipTitleText;
    TextView tipContentText;
    TextView dateText;
    TextView timeText;
    TipService tipService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tip_detail);
        initProperty();
        renderPageByInitialData();
    }

//    Intent intent = getIntent();
//        String date = intent.getStringExtra("date");
    private void renderPageByInitialData() {
        Intent intent = getIntent();
        String id = intent.getStringExtra("tipId");
        Tip tip = tipService.queryItemById(id);
        if (tip!=null){
            tipTitleText.setText(tip.getTitle());
            tipContentText.setText(tip.getContent());
            dateText.setText(tip.getDayId());
            timeText.setText(tip.getStartTime()+"-"+tip.getEndTime());
        }

    }

    private void initProperty() {
        tipTitleText=findViewById(R.id.tipTitleText);
        tipContentText=findViewById(R.id.tipContentText);
        dateText=findViewById(R.id.dateText);
        timeText=findViewById(R.id.timeText);
        tipService=new TipService();
    }
}