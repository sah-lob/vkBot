package ru.sahlob.core.modules.vkpeopleparser;

import org.springframework.stereotype.Component;
import ru.sahlob.core.modules.vkpeopleparser.activity.MinuteActivity;
import ru.sahlob.core.modules.vkpeopleparser.vkstorage.db.people.MainVKPeopleStorage;
import ru.sahlob.core.modules.vkpeopleparser.vktime.VKDay;

import java.util.Collections;

@Component
public class VKMorning {

    private MainVKPeopleStorage storage;
    private VKTwoPeopleAnalize analize;


    public VKMorning(MainVKPeopleStorage storage, VKTwoPeopleAnalize analize) {
        this.storage = storage;
        this.analize = analize;
    }


    public String usersMorning() {
        var result = "Кто во сколько первый раз зашел в вк:\n \n";
        var persons = storage.getAllPersons();
        for (var p: persons) {
            var vkDay = analize.getVKDayOfPerson(p);
            var vkHours = vkDay.getVkHours();

            for (int i = 0; i < 24; i++) {
                if (vkHours[i] != null && vkHours[i].hasMinutes() && i > 2) {
                    String min = String.valueOf(vkHours[i].getFirstOnlineMinuteOfHour());
                    if (min.length() < 2) {
                        min += "0" + min;
                    }
                    result += p.getAlternativeName() + " в " + i + ":" + min + " мин. \n";
                }
            }
        }
        return result;
    }




}
