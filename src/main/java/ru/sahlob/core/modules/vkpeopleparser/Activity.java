package ru.sahlob.core.modules.vkpeopleparser;

public class Activity {

    private String data;
    private int duration;
    private String durationINFO;

    public Activity(String data) {
        this.data = data;
        this.duration = 0;
        this.durationINFO = "";
    }


    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }


    public String getDurationINFO() {
        return durationINFO;
    }

    public void setDurationINFO(String durationINFO) {
        this.durationINFO = durationINFO;
    }
}
