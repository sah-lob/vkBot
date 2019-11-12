package ru.sahlob.core.modules.vkpeopleparser;
import java.util.Date;
import java.util.HashMap;


public class Person {

    private String name;
    private HashMap<String, Activity> activity;


    public Person(String name) {
        this.name = name;
        activity = new HashMap<>();
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public HashMap<String, Activity> getActivity() {
        return activity;
    }

    public void setActivity(HashMap<String, Activity> activity) {
        this.activity = activity;
    }

    public void updateActivity(String dateKey, Activity activity) {
        this.activity.put(dateKey, activity);
    }

    public String getTodayActivityKey() {
        return new Date().getYear() + " " + new Date().getMonth() + " " + new Date().getDay();
    }
}
