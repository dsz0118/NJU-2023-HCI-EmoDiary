package com.example.calendar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.calendar.myAdaptor.MyRecyclerAdaptor;
import com.example.calendar.bean.Tip;
import com.example.calendar.service.TipService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class TipsActivity extends AppCompatActivity {

    RecyclerView tipsRecycler;
    TipService tipService;
    FloatingActionButton addTipFltBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tips);

        initProperty();
        initAddTipEvent();
        renderPageByInitialData();
    }

    private void initAddTipEvent() {
        addTipFltBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TipsActivity.this, EditActivity.class);
                startActivity(intent);
            }
        });
    }

    private void renderPageByInitialData() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        tipsRecycler.setLayoutManager(layoutManager);
        List<Tip> tips = tipService.queryAllItems();
        tipsRecycler.setAdapter(new MyRecyclerAdaptor(this,tips));

    }

    private void initProperty() {
        tipsRecycler=findViewById(R.id.tipRecycler);
        tipService = new TipService();
        addTipFltBtn=findViewById(R.id.addTipFltBtn);

    }
}