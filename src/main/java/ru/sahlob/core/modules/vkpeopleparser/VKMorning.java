package ru.sahlob.core.modules.vkpeopleparser;

import org.springframework.stereotype.Component;
import ru.sahlob.core.modules.vkpeopleparser.activity.MinuteActivity;
import ru.sahlob.core.modules.vkpeopleparser.vkstorage.db.people.MainVKPeopleStorage;

import java.util.Collections;

@Component
public class VKMorning {

    private MainVKPeopleStorage storage;


    public VKMorning(MainVKPeopleStorage storage) {
        this.storage = storage;
    }


    public String usersMorning() {
        var result = "Кто во сколько первый раз зашел в вк:\n";
        var persons = storage.getAllPersons();
        for (var p: persons) {
            if (p.getTodayActivity() != null) {
                var minuteActivities = p.getTodayActivity().getDayActivities();
                if (minuteActivities != null && minuteActivities.size() > 0) {
                    Collections.sort(minuteActivities, MinuteActivity.COMPARE_BY_TIME);
                    var activity = minuteActivities.get(0);
                    result += p.getAlternativeName() + "проснулся в " + activity.getStartTime() + " мин. \n";
                }
            }
        }
        return result;
    }




}
