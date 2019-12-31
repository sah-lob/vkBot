package ru.sahlob.core.modules.vkpeopleparser;

import org.springframework.stereotype.Component;
import ru.sahlob.core.modules.vkpeopleparser.domain.Morning;
import ru.sahlob.core.modules.vkpeopleparser.vkstorage.db.people.MainVKPeopleStorage;
import java.util.ArrayList;

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
        var morningTimes = new ArrayList<Morning>();
        for (var p: persons) {
            var vkDay = analize.getVKDayOfPerson(p);
            var vkHours = vkDay.getVkHours();
            for (int i = 0; i < 24; i++) {
                if (vkHours[i] != null && vkHours[i].hasMinutes() && i > 3) {
                    morningTimes.add(new Morning(i, vkHours[i].getFirstOnlineMinuteOfHour(), p.getAlternativeName()));
                    break;
                }
            }
        }
        morningTimes.sort(Morning.COMPARE_BY_TIME);
        for (var morningTime : morningTimes) {
            result += morningTime.getName() + " в " + morningTime.getTime() + "\n";
        }
        return result;
    }
}
