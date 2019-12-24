package ru.sahlob.core.modules.vkpeopleparser;
import org.springframework.stereotype.Component;
import ru.sahlob.core.modules.vkpeopleparser.activity.DayActivity;
import ru.sahlob.core.modules.vkpeopleparser.vkstorage.db.people.MainVKPeopleStorage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class VKPeopleStats {

    private final MainVKPeopleStorage mainVKPeopleStorage;

    public VKPeopleStats(MainVKPeopleStorage mainVKPeopleStorage) {
        this.mainVKPeopleStorage = mainVKPeopleStorage;
    }

    public String getPersonsDurationRaiting() {
        var dayActivities = getSortedDayActivityListOfPersons();
        return createDurationRaitingString(dayActivities);
    }

    private List<DayActivity> getSortedDayActivityListOfPersons() {
        var persons = mainVKPeopleStorage.getAllPersons();
        var dayActivities = new ArrayList<DayActivity>();
        for (var p: persons) {
            if(p.getTodayActivity() != null) {
                var dayAct = p.getTodayActivity();
                dayAct.setDurationINFO(p.getAlternativeName());
                dayActivities.add(dayAct);
            }
        }
        dayActivities.sort(DayActivity.COMPARE_BY_DURATION);
        return dayActivities;
    }

    private String createDurationRaitingString(List<DayActivity> dayActivities) {
        StringBuilder result = new StringBuilder("Список задротов за сегодня: \n\n");
        for (int i = 0; i < dayActivities.size(); i++) {
            result.append(i + 1).append("  ")
                    .append(dayActivities.get(i)
                            .getDurationINFO() + " - ")
                    .append(dayActivities.get(i)
                            .getDuration())
                    .append(" мин.\n");
        }
        result.append("Рассчет задротов окончен!");
        return result.toString();
    }

}
