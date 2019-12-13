package ru.sahlob.core.modules.vkpeopleparser.activity;

import java.util.ArrayList;

public class DayActivity {

    private int duration;
    private String durationINFO;
    private ArrayList<MinuteActivity> dayActivities;
    private int timezone;

    public DayActivity(int timezone) {
        this.duration = 0;
        this.durationINFO = "";
        this.timezone = timezone;
        this.dayActivities = new ArrayList<>();
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


    public ArrayList<MinuteActivity> getDayActivities() {
        return dayActivities;
    }

    public void setDayActivities(ArrayList<MinuteActivity> dayActivities) {
        this.dayActivities = dayActivities;
    }

    public void addNewDayActivities(MinuteActivity minuteActivity) {
        dayActivities.add(minuteActivity);
    }

    public void incrementDurationOfMinuteActivities() {
        System.out.println(dayActivities.size());
        dayActivities.get(dayActivities.size() - 1).incrementDuration();
    }

    public void addNewMinuteActivity() {
        System.out.println(dayActivities.size());
        dayActivities.add(new MinuteActivity(timezone));
        System.out.println(dayActivities.size() + "   размер минутной активности.");
    }

    public void delete4MinFromMinuteActivity() {
        dayActivities.get(dayActivities.size() - 1).delete4Min();
    }

    public int getTodayDuration() {
        var duration = 0;
        if (dayActivities.size() == 0) {
            return 0;
        }
        for (var minuteAct: dayActivities) {
            duration += minuteAct.getDuration();
        }
        return duration;
    }

    public String getDayActivityInfo() {
        var dayActivityInfo = "";
        if (dayActivities.isEmpty()) {
            return "Данный пользователь сегодня не сидел вк";
        }
        for (var activity: dayActivities) {
            dayActivityInfo += activity.getStartTime() + " в течение " + activity.getDuration() + "мин.\n";
        }
        return dayActivityInfo;
    }
}
