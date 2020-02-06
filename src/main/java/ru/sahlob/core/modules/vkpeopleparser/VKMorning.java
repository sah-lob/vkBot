package ru.sahlob.core.modules.vkpeopleparser;

import lombok.Data;
import org.springframework.stereotype.Component;
import ru.sahlob.core.modules.vkpeopleparser.domain.Morning;
import ru.sahlob.core.modules.vkpeopleparser.vkstorage.db.people.MainVKPeopleStorage;
import java.util.ArrayList;

@Component
@Data
public class VKMorning {

    private MainVKPeopleStorage storage;
    private VKTwoPeopleAnalize analize;

    public String usersMorning() {
        var result = new StringBuilder("Кто во сколько первый раз зашел в вк:\n \n");
        var persons = storage.getAllPersonsWithTodayDayActivity();
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
            result.append(morningTime.getName()).append(" в ").append(morningTime.getTime()).append("\n");
        }
        return result.toString();
    }
}
