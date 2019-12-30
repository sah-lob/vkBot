package ru.sahlob.core.modules.vkpeopleparser;

import org.springframework.stereotype.Component;
import ru.sahlob.core.modules.vkpeopleparser.activity.DayActivity;
import ru.sahlob.core.modules.vkpeopleparser.activity.MinuteActivity;
import ru.sahlob.core.modules.vkpeopleparser.vkstorage.db.people.MainVKPeopleStorage;
import ru.sahlob.core.modules.vkpeopleparser.vktime.VKDay;
import ru.sahlob.core.modules.vkpeopleparser.vktime.VKHour;

import java.util.*;

@Component
public class VKPeopleAnalize {

    private MainVKPeopleStorage storage;

    public VKPeopleAnalize(MainVKPeopleStorage storage) {
        this.storage = storage;
    }

    public String jointOnlineOfTwoUsers(String name1, String name2) {

        var person1 = storage.getPerson(name1);
        var person2 = storage.getPerson(name2);
        var stringAnswer = "";
        var dayActivity1 = person1.getTodayActivity();
        var dayActivity2 = person2.getTodayActivity();

        if (dayActivity1 != null && dayActivity2 != null) {
            var activity = twoPersonsToOneDayActivity(person1, person2);
            stringAnswer += person1.getAlternativeName() + " был онлайн: " + dayActivity1.getTodayDuration() + "мин \n";
            stringAnswer += person2.getAlternativeName() + " был онлайн: " + dayActivity2.getTodayDuration() + "мин \n";

            if (activity != null) {
                var duration = activity.getTodayDuration() + " мин.";
                var info = activity.getDayActivityInfo();
                if (info.equals("")) {
                    info = "Данные пользователи вместе вк сегодня не сидели =)";
                }
                stringAnswer += "Совместный онлайн в течение дня:  " + duration + "\n";
                stringAnswer += "Информация о посещении: \n" + info;
            } else {
                stringAnswer += "\nПользователи не сидели в одно и тоже время.";
            }
        } else {
            stringAnswer += "Данные по какому-то из пользователей еще не собраны!";
        }
        return stringAnswer;
    }

    private DayActivity twoPersonsToOneDayActivity(Person person1, Person person2) {
        var vkDay = twoDaysInOne(person1, person2);
        var dayActivity = new DayActivity();
        var minuteActivities = new ArrayList<MinuteActivity>();
        var person = new Person();
        person.setActive(false);

        for (int i = 0; i < 24; i++) {
            var hour = vkDay.getVkHours()[i];
            if (hour != null && hour.hasMinutes()) {
                for (int j = 0; j < 60; j++) {
                    var min = hour.getOnlineMinutes();
                    if (min[j]) {
                        if (person.isActive()) {
                            minuteActivities.get(minuteActivities.size() - 1).incrementDuration();
                        } else {
                            person.setActive(true);
                            var minuteActivity1 = new MinuteActivity();
                            minuteActivity1.setDuration(1);
                            var finalMin = String.valueOf(j);
                            if (finalMin.length() == 1) {
                                finalMin = "0" + finalMin;
                            }
                            minuteActivity1.setStartTime(i + ":" + finalMin);
                            minuteActivities.add(minuteActivity1);
                        }
                    } else {
                        if (person.isActive()) {
                            person.setActive(false);
                        }
                    }
                }
            }
        }
        dayActivity.setDayActivities(minuteActivities);
        return dayActivity;
    }

    private VKDay twoDaysInOne(Person person1, Person person2) {
        var vkDay1 = getVKDayOfPerson(person1);
        var vkDay2 = getVKDayOfPerson(person2);
        var day1VkHoursMap = vkDay1.getVkHours();
        var day2VkHoursMap = vkDay2.getVkHours();
        var finalDay = new VKDay();
        for (int i = 0; i < finalDay.getVkHours().length; i++) {
            if (day1VkHoursMap[i] != null && day2VkHoursMap[i] != null) {
                var vkHour = new VKHour(i);
                var m1 = day1VkHoursMap[i].getOnlineMinutes();
                var m2 = day2VkHoursMap[i].getOnlineMinutes();
                for (int j = 0; j < m1.length; j++) {
                    if (m1[j] && m2[j]) {
                        vkHour.addMinute(j);
                    }
                }
                finalDay.addvkHour(vkHour);
            }
        }
        return finalDay;
    }

    private VKDay getVKDayOfPerson(Person person) {
        var dayActivity = person.getTodayActivity();
        var minuteActivities = dayActivity.getDayActivities();
        var vkDay = new VKDay();
        for (MinuteActivity m: minuteActivities) {
            vkDay.addVkHoursList(parsingTime(m));
        }
        return vkDay;
    }

    private ArrayList<VKHour> parsingTime(MinuteActivity minuteActivity) {
        var time = minuteActivity.getStartTime();
        var hour = Integer.parseInt(time.substring(0, time.indexOf(":"))
                .replaceAll(" ", "")
                .replaceAll(":", ""));
        var minute = Integer.parseInt(time.substring(time.indexOf(":"))
                .replaceAll(" ", "")
                .replaceAll(":", ""));
        var duration = minuteActivity.getDuration();
        return fillVKHour(duration, hour, minute);
    }

    private ArrayList<VKHour> fillVKHour(int duration, int hour, int minute) {
        var vkHours = new ArrayList<VKHour>();
        while (duration > 0) {
            var vkHour = new VKHour(hour);
            var minutes = vkHour.getOnlineMinutes();
            while (minute < 60 && duration > 0) {
                minutes[minute] = true;
                minute++;
                duration--;
            }
            vkHour.setOnlineMinutes(minutes);
            vkHours.add(vkHour);
            hour++;
            minute = 0;
        }
        return vkHours;
    }
}