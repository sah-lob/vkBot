package ru.sahlob.core.modules.vkpeopleparser.vktime;

import java.util.Objects;

public class VKHour {

    private int hour;
    private boolean[] onlineMinutes = new boolean[60];

    public VKHour(int hour) {
        this.hour = hour;
        for (int i = 0; i < 60; i++) {
            onlineMinutes[i] = false;
        }
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }


    public boolean[] getOnlineMinutes() {
        return onlineMinutes;
    }

    public void setOnlineMinutes(boolean[] onlineMinutes) {
        this.onlineMinutes = onlineMinutes;
    }

    public void addMinute(int i) {
        onlineMinutes[i] = true;
    }


    public boolean hasMinutes() {
        var result = false;
        for (boolean onlineMinute : onlineMinutes) {
            if (onlineMinute) {
                result = true;
                break;
            }
        }
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        var vkHour = (VKHour) o;
        return hour == vkHour.hour;
    }

    @Override
    public int hashCode() {
        return Objects.hash(hour);
    }


}
