package ru.sahlob.core.modules.vkpeopleparser.vktime;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of = {"hour"})
public class VKHour {

    private int hour;
    private boolean[] onlineMinutes = new boolean[60];

    public VKHour(int hour) {
        this.hour = hour;
        for (int i = 0; i < 60; i++) {
            onlineMinutes[i] = false;
        }
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

    public int getFirstOnlineMinuteOfHour() {
        var result = -1;
        for (int i = 0; i < 60; i++) {
            if (onlineMinutes[i]) {
                result = i;
                break;
            }
        }
        return result;
    }

}
