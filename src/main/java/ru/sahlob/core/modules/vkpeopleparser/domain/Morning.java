package ru.sahlob.core.modules.vkpeopleparser.domain;

import lombok.Data;
import lombok.NonNull;

import java.io.Serializable;
import java.util.Comparator;

@Data
public class Morning implements Serializable {

    private int hour;
    private int minute;
    private String time;
    private String name;

    public Morning(int hour, int minute, String name) {
        this.hour = hour;
        this.minute = minute;
        this.name = name;
        time = String.valueOf(minute);
        if (time.length() == 1) {
            time = "0" + time;
        }
        time = hour + ":" + time;
    }

    public static final Comparator<Morning> COMPARE_BY_TIME = (lhs, rhs) -> {
        if (lhs.getHour() == rhs.getHour()) {
            return lhs.getMinute() - rhs.getMinute();
        }
            return 0;
    };
}
