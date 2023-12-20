package com.example.calendar;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.calendar.constant.TipState;
import com.example.calendar.service.TipService;
import com.example.calendar.tool.MyTool;
import com.example.calendar.viewTool.StateCount;
import com.lihang.chart.utils.ChartCircleItem;
import com.lihang.chart.utils.ChartHistogramItem;
import com.lihang.chart.view.ChartCircleView;
import com.lihang.chart.view.ChartHistogramView;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ResultActivity extends AppCompatActivity {

    TextView startDate;
    TextView endDate;
    TipService tipService;
    Button checkButton;

    private ChartCircleView chartCircleView;
    private ChartHistogramView chartHistogramView;

    Integer FINISH_NUM=0;
    Integer OUTDATE_NUM=0;
    Integer FUlTURE_NUM=0;

    Calendar calendar = Calendar.getInstance();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        initProperty();
        initStartDateEvent();
        initEndDateEvent();
        initCheckBtnEvent();

        renderPage();

    }

    private void initCheckBtnEvent() {
        checkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                renderPage();
            }
        });
    }

    private void renderPage() {
        String start = startDate.getText().toString();
        String end = endDate.getText().toString();
        end = end;
        renderChart(start,end);
    }

    private void renderChart(String start, String end) {
        initTipStateProperty(start,end);
        ArrayList<ChartCircleItem> circleList = new ArrayList<>();
        ArrayList<ChartHistogramItem> histogramList = new ArrayList<>();


        circleList.add(new ChartCircleItem(FINISH_NUM, R.color.green, "完成"));
        circleList.add(new ChartCircleItem(OUTDATE_NUM,R.color.red, "逾期"));
        circleList.add(new ChartCircleItem(FUlTURE_NUM, R.color.grey, "待完成"));
        chartCircleView.setItems(circleList);
        histogramList.add(new ChartHistogramItem(FINISH_NUM,R.color.green,"完成",true));
        histogramList.add(new ChartHistogramItem(OUTDATE_NUM,R.color.red,"逾期",true));
        histogramList.add(new ChartHistogramItem(FUlTURE_NUM,R.color.grey,"待完成",true));
        chartHistogramView.setItems(histogramList);


    }

    private void initTipStateProperty(String start,String end) {
        int startTime = Integer.parseInt(start);
        int endTime = Integer.parseInt(end);
        String currentCalendar = MyTool.getCurrentCalendar();
        int currentTime = Integer.parseInt(currentCalendar);


        if (endTime<=currentTime){  //全是过去的时间
            List<StateCount> stateCounts = tipService.queryCountByDaysGroupByState(start, end);
            for (StateCount stateCount : stateCounts){
                if (stateCount.getState().equals(TipState.isOk)){
                    FINISH_NUM=stateCount.getCount();
                }else if (stateCount.getState().equals(TipState.isNo)){
                    OUTDATE_NUM=stateCount.getCount();
                }
            }
            FUlTURE_NUM=0;
        }else if (startTime>currentTime){//全是未来的时间
            FINISH_NUM=0;
            OUTDATE_NUM=0;
            List<StateCount> stateCounts = tipService.queryCountByDaysGroupByState(start, end);
            for (StateCount stateCount : stateCounts){
                if (stateCount.getState().equals(TipState.isNo)){
                    FUlTURE_NUM=stateCount.getCount();
                }
            }

        }
        else {//一半是未来的时间
            List<StateCount> stateCounts = tipService.queryCountByDaysGroupByState(start, currentCalendar);
            for (StateCount stateCount : stateCounts){
                if (stateCount.getState().equals(TipState.isOk)){
                    FINISH_NUM=stateCount.getCount();
                }else if (stateCount.getState().equals(TipState.isNo)){
                    OUTDATE_NUM=stateCount.getCount();
                }
            }


            List<StateCount> stateCounts1 = tipService.queryCountByDaysGroupByState(String.valueOf(currentTime+1), end);
            for (StateCount stateCount : stateCounts1){
                if (stateCount.getState().equals(TipState.isNo)){
                    FUlTURE_NUM=stateCount.getCount();
                }
            }

        }


    }


    private void initEndDateEvent() {
        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyTool.showDatePickerDialog(ResultActivity.this,endDate,calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH)+1,
                        calendar.get(Calendar.DAY_OF_MONTH));
            }
        });
    }



    private void initStartDateEvent() {
        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyTool.showDatePickerDialog(ResultActivity.this,startDate,calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));
            }
        });
    }

    private void initProperty() {
        startDate=findViewById(R.id.startDate);
        endDate=findViewById(R.id.endDate);
        tipService=new TipService();
        chartCircleView = findViewById(R.id.cv_circle);
        chartHistogramView = findViewById(R.id.cv_hg);
        checkButton=findViewById(R.id.checkButton);
    }
}