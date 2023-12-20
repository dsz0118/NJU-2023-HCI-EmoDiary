package com.example.calendar.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextUtils;

import com.example.calendar.constant.TipState;
import com.example.calendar.service.TipService;
import com.example.calendar.tool.MyTool;
import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.MonthView;

/**
 * 演示一个变态需求的月视图
 * Created by huanghaibin on 2018/2/9.
 */

public class CustomMonthView extends MonthView {

    /**
     * 24节气画笔
     */
    Paint paint;
    TipService tipService;
    private String currentCalendar;




    public CustomMonthView(Context context) {
        super(context);
        paint=new Paint();
        tipService=new TipService();


        currentCalendar=MyTool.getCurrentCalendar();
    }


    //这里绘制选中的日子样式，看需求需不需要继续调用onDrawSchem
    @Override
    protected boolean onDrawSelected(Canvas canvas, Calendar calendar, int x, int y, boolean hasScheme) {
        int mRadius = Math.min(mItemWidth, mItemHeight) / 2;
        int cx = x + mItemWidth / 2;
        int cy = y + mItemHeight / 2;
        canvas.drawCircle(cx, cy, mRadius, mSelectedPaint);
//        Long aLong = tipService.queryCountByDayId(calendar.toString());//2
//        List<StateCount> stateCounts = tipService.queryCountByDayIdGroupByState(calendar.toString());
//        tipService.queryCountByDaysGroupByState("20220501",calendar.toString());

        return true;
    }

    //这里绘制标记的日期样式
    @Override
    protected void onDrawScheme(Canvas canvas, Calendar calendar, int x, int y) {



    }

    //这里绘制文本，把某个日期换成特殊字符串了，
    @Override
    protected void onDrawText(Canvas canvas, Calendar calendar, int x, int y, boolean hasScheme, boolean isSelected) {
        int cx = x + mItemWidth / 2;
        int top = y - mItemHeight / 6;

        canvas.drawText(String.valueOf(calendar.getDay()), cx, mTextBaseLine + top,
                calendar.isCurrentDay() ? mCurDayTextPaint :
                        calendar.isCurrentMonth() ? mCurMonthTextPaint : mOtherMonthTextPaint);

        //如果有任务
        if (hasTask(calendar.toString())){
            //未完成
            if (hasUnifinishedTask(calendar.toString())){
                int current = Integer.parseInt(currentCalendar);
                int time = Integer.parseInt(calendar.toString());
                //过去
                if (current > time){
                    paint.setColor(Color.RED);
                }

                //现在
                else if (current==time){
                    paint.setColor(Color.BLACK);
                }
                else {
                    paint.setColor(Color.GRAY);
                }

            }
            //完成
            else {
                paint.setColor(Color.GREEN);
            }
            canvas.drawCircle(cx, mTextBaseLine + y + mItemHeight / 10,10,paint);

        }



    }

    private boolean hasTask(String calendar){

        Long aLong = tipService.queryCountByDayId(calendar.toString());
        if (aLong>0){
            return true;
        }
        return false;
    }

    private boolean hasUnifinishedTask(String calendar){

        Long aLong = tipService.queryUnfinishedCountByDayId(calendar.toString(), TipState.isNo+"");
        if (aLong>0){
            return true;
        }
        return false;
    }
}
