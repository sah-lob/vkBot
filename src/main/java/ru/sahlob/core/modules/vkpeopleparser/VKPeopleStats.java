package ru.sahlob.core.modules.vkpeopleparser;
import org.springframework.stereotype.Component;
import ru.sahlob.core.modules.vkpeopleparser.activity.DayActivity;
import ru.sahlob.core.modules.vkpeopleparser.vkstorage.db.people.MainVKPeopleStorage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class VKPeopleStats {

    private final VKPeopleParser peopleParser;
    private final MainVKPeopleStorage mainVKPeopleStorage;

    public VKPeopleStats(VKPeopleParser peopleParser, MainVKPeopleStorage mainVKPeopleStorage) {
        this.peopleParser = peopleParser;
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
            var dayAct = p.getTodayActivity();
            dayAct.setDurationINFO(p.getAlternativeName());
            dayActivities.add(dayAct);
        }
        Collections.sort(dayActivities, DayActivity.COMPARE_BY_DURATION);
        return dayActivities;
    }

    private String createDurationRaitingString(List<DayActivity> dayActivities) {
        var result = "Список задротов за сегодня: \n\n";
        for (int i = 0; i < dayActivities.size(); i++) {
            result += (i + 1) + "  " + dayActivities.get(i).getDayActivityInfo() + dayActivities.get(i).getDuration() + " мин.\n";
        }
        result += "Рассчет задротов окончен!";
        return result;
    }

}
