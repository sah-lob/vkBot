package ru.sahlob.core.modules.vkpeopleparser;
import ru.sahlob.core.modules.vkpeopleparser.activity.DayActivity;

import java.util.Date;
import java.util.HashMap;


public class Person {

    private String alternativeName;
    private String name;
    private HashMap<String, DayActivity> activity;
    private boolean isActive;
    private int timezone = 3;


    public Person(String name, String alternativeName) {
        this.name = name;
        this.alternativeName = alternativeName;
        this.activity = new HashMap<>();
        this.isActive = false;
    }


    public String getAlternativeName() {
        return alternativeName;
    }

    public void setAlternativeName(String alternativeName) {
        this.alternativeName = alternativeName;
    }

    public void setActivity(HashMap<String, DayActivity> activity) {
        this.activity = activity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public HashMap<String, DayActivity> getActivity() {
        return activity;
    }

    public DayActivity getTodayActivity() {
        return activity.get(VKTime.getDateKey(timezone));
    }

    public void updateTodayActivity(DayActivity dayActivity) {
        this.activity.put(VKTime.getDateKey(timezone), dayActivity);
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public int getTimezone() {
        return timezone;
    }

    public void setTimezone(int timezone) {
        this.timezone = timezone;
    }
}
