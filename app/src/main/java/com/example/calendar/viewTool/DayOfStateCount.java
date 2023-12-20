package com.example.calendar.viewTool;

public class DayOfStateCount {
    private String dayId;
    private StateCount stateCount;

    public String getDayId() {
        return dayId;
    }

    public void setDayId(String dayId) {
        this.dayId = dayId;
    }

    public StateCount getStateCount() {
        return stateCount;
    }

    public void setStateCount(StateCount stateCount) {
        this.stateCount = stateCount;
    }

    public DayOfStateCount(String dayId, StateCount stateCount) {
        this.dayId = dayId;
        this.stateCount = stateCount;
    }
}
