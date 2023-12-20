package com.example.calendar.service;

import android.content.ContentValues;
import android.content.Context;

import com.example.calendar.bean.Tip;
import com.example.calendar.viewTool.StateCount;

import java.util.List;

public class TipService {

    private final MySQLiteOpenHelper mySQLiteOpenHelper;
    private final String TABLE_NAME= "tip";




    public TipService() {
        this.mySQLiteOpenHelper = MySQLiteOpenHelper.getInstance();
    }

    public long insertData(Tip data) {

       return mySQLiteOpenHelper.insertData(data,TABLE_NAME);


    }

    public int deleteItemById(String id){
        return mySQLiteOpenHelper.deleteItemById(id,TABLE_NAME);
    }

    public int updateItemById(Tip tip){
        return mySQLiteOpenHelper.updateItemById(tip.getId()+"",TABLE_NAME,tip);
    }

    public int updateStateById(String id,int state){
        ContentValues contentValues = new ContentValues();
        contentValues.put("state",state);
        return mySQLiteOpenHelper.updateItemById(id,TABLE_NAME,contentValues);
    }

    public List<Tip> queryItemsByDayId(String dayId){
        return mySQLiteOpenHelper.queryItemsByArgs(TABLE_NAME, Tip.class,"dayId=?",new String[]{dayId});

    }

    public Tip queryItemById(String id){
        return mySQLiteOpenHelper.queryItemById(TABLE_NAME, Tip.class,"id=?",new String[]{id});

    }

    public List<Tip> queryAllItems(){
        return mySQLiteOpenHelper.queryAllItems(TABLE_NAME, Tip.class);
    }

    public Long queryCountByDayId(String dayId){
        return  mySQLiteOpenHelper.queryCount(TABLE_NAME,"dayId=?",new String[]{dayId},null);
    }

    public Long queryUnfinishedCountByDayId(String dayId,String state){
//        return Long.parseLong("1");
        return  mySQLiteOpenHelper.queryCount(TABLE_NAME,"dayId=? and state=?",new String[]{dayId,state},null);
    }

    public List<StateCount> queryCountByDayIdGroupByState(String dayId){
        List<StateCount> stateCounts = mySQLiteOpenHelper.queryGroupCount(TABLE_NAME, "count(*) count, state", "dayId=?", new String[]{dayId}, "state", StateCount.class);

        return stateCounts;
    }

    public List<StateCount> queryCountByDaysGroupByState(String startDay,String endDay){
        List<StateCount> stateCounts = mySQLiteOpenHelper.queryGroupCount(TABLE_NAME, "count(*) count, state", "? <= dayId and dayId <= ?", new String[]{startDay ,endDay}, "state", StateCount.class);

        return stateCounts;
    }

}
