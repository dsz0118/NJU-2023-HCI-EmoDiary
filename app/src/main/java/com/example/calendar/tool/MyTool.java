package com.example.calendar.tool;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;

public class MyTool {

    public static void showToast(Context context, boolean isTure, String successText, String failText) {
        if (isTure){
            Toast.makeText(context,successText, Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(context,failText, Toast.LENGTH_SHORT).show();
        }
    }

    public static void showToast(Context context, boolean isTure, String successText, String failText,DoInterface success, DoInterface fail) {
        if (isTure){
            Toast.makeText(context,successText, Toast.LENGTH_SHORT).show();
            if (success!=null){
                success.run();
            }
        }
        else {
            Toast.makeText(context,failText, Toast.LENGTH_SHORT).show();
            if (fail!=null){
                fail.run();

            }
        }
    }

    public static AlertDialog.Builder getDialog(Context context,String message){
        AlertDialog.Builder dialog = new AlertDialog.Builder(context).setTitle("确认框").setMessage(message);
        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        return dialog;

    }

    public static String convertTwoNumberString(int i) {

        String s = i+"";
        if (s.length()==1){
            s=0+s;
        }
        return s;
    }

    public static Uri getDefaultPictureUri(Context context,String name){
        Uri imageUrl=null;
        File imageTemp = new File(context.getExternalCacheDir(),name);
        try {
            imageTemp.createNewFile();//创建出文件
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (Build.VERSION.SDK_INT>24){
            //
            imageUrl= FileProvider.getUriForFile(context,"com.example.calendar.fileprovider",imageTemp);//获取文件路径
        }else {
            imageUrl = Uri.fromFile(imageTemp);//获得文件的路径
        }

        return imageUrl;
    }

    public static Bitmap getBitmapForUri(Context context,Uri imageUrl){
        Bitmap bitmap = null;
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(imageUrl);
            bitmap = BitmapFactory.decodeStream(inputStream);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return bitmap;
    }


    public static  void showDatePickerDialog(Context context, TextView input, int year, int month, int day) {
        new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                input.setText(year+MyTool.convertTwoNumberString(month)+MyTool.convertTwoNumberString(dayOfMonth));
            }
        },year,month,day).show();
    }

    public static void  showTimePickerDialog(Context context,TextView input,int hour,int minute) {
        new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                input.setText(MyTool.convertTwoNumberString(hourOfDay)+":"+MyTool.convertTwoNumberString(minute));
            }
        },hour,minute,true).show();
    }

    public static String getCurrentCalendar(){
        java.util.Calendar calendarUtil = java.util.Calendar.getInstance();
        int currentYear = calendarUtil.get(java.util.Calendar.YEAR);
        int currentMonth = calendarUtil.get(java.util.Calendar.MONTH)+1;
        int currentDay = calendarUtil.get(java.util.Calendar.DAY_OF_MONTH);

        return currentYear+MyTool.convertTwoNumberString(currentMonth)+MyTool.convertTwoNumberString(currentDay);
    }

}
