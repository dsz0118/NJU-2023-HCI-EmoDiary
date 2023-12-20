package com.example.calendar.bean;

import com.example.calendar.constant.TipState;

public class Tip {

    private Integer id;
    private String dayId;
    private String startTime;
    private String endTime;
    private String title;
    private String content;
    private String picture;
    private Integer state;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDayId() {
        return dayId;
    }

    public void setDayId(String dayId) {
        this.dayId = dayId;
    }

    public boolean isOutOfDate(){
        return false;
    }

    public boolean isDoing(){
        return false;
    }

    public boolean isWait(){
        return true;
    }

    public String getTitle() {
        return title;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Tip() {
    }

    public Tip(String dayId, String startTime, String endTime, String title, String content, String picture) {
        this.dayId=dayId;
        this.startTime = convertTimeToStore(startTime);
        this.endTime = convertTimeToStore(endTime);
        this.title = title;
        this.content = content;
        this.picture = picture;
        this.state= TipState.isNo;
    }

    public Tip(Integer id, String dayId,String startTime, String endTime, String title, String content, String picture) {
        this(dayId,startTime, endTime, title, content, picture);
        this.id = id;
    }



    public void setTitle(String title) {
        this.title = title;
    }

    public String getStartTime() {
        return convertTimeToShow(startTime);
    }

    public void setStartTime(String startTime) {
        this.startTime = convertTimeToStore(startTime);
    }


    public String getEndTime() {
        return convertTimeToShow(endTime);
    }

    public void setEndTime(String endTime) {
        this.endTime = convertTimeToStore(endTime);
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    private String convertTimeToShow(String time) {
        return time.substring(0, 2) +":"+time.substring(2,4);

    }

    private String convertTimeToStore(String time) {
        return time.substring(0, 2) + time.substring(3,5);
    }
}
