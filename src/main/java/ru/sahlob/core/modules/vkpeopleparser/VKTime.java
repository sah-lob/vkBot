package ru.sahlob.core.modules.vkpeopleparser;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class VKTime {


    public static String getDateKey(int timezone) {
        var date = getDate(timezone);
        String str = date.getYear() + " " + date.getMonth() + " " + date.getDayOfMonth();
        System.out.println("get Date Key " + str);
        return str;
    }

    public static String getMinuteActivity(int timezone) {
        var date = getDate(timezone);
        var hours = String.valueOf(date.getHour());
        var minutes = String.valueOf(date.getMinute());
        if (hours.length() == 1) {
            hours = "0" + hours;
        }
        if (minutes.length() == 1) {
            minutes = "0" + minutes;
        }
        String str = hours + ":" + minutes;
        System.out.println("get Minute Activity " + str);
        return str;
    }

    public static OffsetDateTime getDate(int timezone) {

        String time = "";
        if (timezone > 0) {
            time += "+";
        } else {
            timezone *= -1;
            time += "-";
        }
        if (timezone < 10) {
            time += "0";
        }
        time += timezone + ":00";

        ZoneOffset zoneOffSet = ZoneOffset.of(time);
        return OffsetDateTime.now(zoneOffSet);
    }
}
