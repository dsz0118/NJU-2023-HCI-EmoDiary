package com.example.calendar;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.customview.widget.Openable;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.calendar.myAdaptor.MyListAdaptor;
import com.example.calendar.service.TipService;
import com.example.calendar.bean.Tip;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarLayout;
import com.haibin.calendarview.CalendarView;

import com.example.calendar.databinding.ActivityMainBinding;

import java.util.List;

public class MainActivity extends AppCompatActivity  implements
        CalendarView.OnCalendarSelectListener ,
        CalendarView.OnYearChangeListener,
        View.OnClickListener{

    CalendarView calendarView;
    CalendarLayout mCalendarLayout;
    ListView tipsView;
    TipService tipHelper;
    TextView dayTime;
    FloatingActionButton addTipFltBtn;
    List<Tip> tips;

    private int mYear;

    private AppBarConfiguration mAppBarConfiguration;

    private ActivityMainBinding binding;

    public MainActivity() {
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initProperty();
        renderPage();
        initAddTipEvent();
        initClickTipEvent();
        initView();

    }

    //左上角时间显示
    private void initView() {
        mYear = calendarView.getCurYear();

        calendarView.setOnCalendarSelectListener(this);
        calendarView.setOnYearChangeListener(this);
        setTitle(String.valueOf(calendarView.getCurYear())+"年"+calendarView.getCurMonth() + "月" + calendarView.getCurDay() + "日");

    }


    private void renderPage() {

        showTips(calendarView.getSelectedCalendar());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // 获得当前菜单对象， 加载布局文件
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;   // true为显示
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        // 使用item的id判断哪一个被点击
        switch(item.getItemId()) {
            case R.id.test1:
                Intent intent1=new Intent(MainActivity.this, ResultActivity.class);
                startActivity(intent1);
                break;
            case R.id.test2:
                Intent intent=new Intent(MainActivity.this, TipsActivity.class);
                startActivity(intent);
                break;
            case R.id.test3:
                calendarView.showYearSelectLayout(mYear);
                setTitle(String.valueOf(mYear));
            case R.id.test4:
                Intent intent2=new Intent(MainActivity.this, testActivity4.class);
                startActivity(intent2);
            default:
                // 暂不处理
        }
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private void initClickTipEvent() {
        tipsView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, TipDetailActivity.class);
                intent.putExtra("tipId", tips.get(position).getId()+"");
                startActivity(intent);
            }
        });
    }

    private void initAddTipEvent() {
        addTipFltBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, EditActivity.class);
                intent.putExtra("date",calendarView.getSelectedCalendar().toString());
                startActivity(intent);
            }
        });
    }


    private void showTips(Calendar calendar) {

        tips = tipHelper.queryItemsByDayId(calendar.toString());
//        20220510
        Log.i("dd",calendar.toString());
        if (tips!=null && tips.size()>0){
            dayTime.setText(calendar.toString());
        }
        else {
            dayTime.setText("");
        }
        tipsView.setAdapter(new MyListAdaptor(this,tips));
    }

    private void initProperty() {
        calendarView =  findViewById(R.id.calendarView);
        mCalendarLayout = findViewById(R.id.calendarLayout);
        tipsView=findViewById(R.id.tipsView);
        tipHelper=new TipService();
        dayTime=findViewById(R.id.dayTime);
        addTipFltBtn=findViewById(R.id.addTipFltBtn);
        tipsView.setDividerHeight(0);
    }

    @Override
    public void onCalendarOutOfRange(Calendar calendar) {



    }

    @Override
    public void onCalendarSelect(Calendar calendar, boolean isClick) {
        mYear = calendar.getYear();
        showTips(calendar);
        setTitle(calendar.getMonth() + "月" + calendar.getDay() + "日"+String.valueOf(calendar.getYear()));

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onYearChange(int year) {
        setTitle(String.valueOf(year));
    }
}