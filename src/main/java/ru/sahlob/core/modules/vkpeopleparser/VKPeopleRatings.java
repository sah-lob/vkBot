package ru.sahlob.core.modules.vkpeopleparser;
import org.springframework.stereotype.Component;
import ru.sahlob.core.modules.vkpeopleparser.domain.DayActivity;
import ru.sahlob.core.modules.vkpeopleparser.vkstorage.db.people.MainVKPeopleStorage;

import java.util.ArrayList;
import java.util.List;

@Component
public class VKPeopleRatings {

    private final MainVKPeopleStorage storage;

    public VKPeopleRatings(MainVKPeopleStorage mainVKPeopleStorage) {
        this.storage = mainVKPeopleStorage;
    }

    public String getPersonsDurationRaiting() {
        var dayActivities = getSortedDayActivityListOfPersons();
        dayActivities.sort(DayActivity.COMPARE_BY_DURATION);

        StringBuilder result = new StringBuilder("Список задротов за сегодня: \n\n");
        for (int i = 0; i < dayActivities.size(); i++) {
            result.append(i + 1).append("  ").append(dayActivities.get(i)
                    .getDurationINFO()).append(" - ")
                    .append(dayActivities.get(i)
                            .getTodayDuration())
                    .append(" мин.\n");
        }
        result.append("Рассчет задротов окончен!");
        return result.toString();
    }

    public String getPersonsAvgDurationRaiting() {
        var dayActivities = getSortedDayActivityListOfPersons();
        var newDayActivities = new ArrayList<DayActivity>();
        for (var d: dayActivities) {
            if (d.getDayActivities().size() != 0) {
                newDayActivities.add(d);
            }
        }
        newDayActivities.sort(DayActivity.COMPARE_BY_AVG_DURATION);
        var result = new StringBuilder("Список лидеров по средней длине сессии за сегодня: \n\n");
        for (int i = 0; i < newDayActivities.size(); i++) {
            double data = newDayActivities.get(i).getTodayDuration() /  newDayActivities.get(i).getDayActivities().size();

            result.append(i + 1).append("  ").append(newDayActivities.get(i).getDurationINFO()).append(" - ")
                    .append(data)
                    .append(" мин.\n");
        }
        result.append("Рассчет задротов окончен!");
        return result.toString();
    }

    public String getCountOfPersonsSessions() {

        var dayActivities = getSortedDayActivityListOfPersons();
        dayActivities.sort(DayActivity.COMPARE_BY_SESSION_COUNT);

        StringBuilder result = new StringBuilder("Список главных параноиков за сегодня: \n\n");

        for (int i = 0; i < dayActivities.size(); i++) {
            result.append(i + 1).append("  ").append(dayActivities.get(i)
                    .getDurationINFO()).append(" - ")
                    .append(dayActivities.get(i)
                            .getDayActivities().size())
                    .append(" раз(а).\n");
        }
        result.append("Рассчет параноиков окончен!");
        return result.toString();
    }

    private List<DayActivity> getSortedDayActivityListOfPersons() {
        var persons = storage.getAllPersonsWithTodayDayActivity();
        var dayActivities = new ArrayList<DayActivity>();
        for (var p: persons) {
            if (p.getTodayActivity() != null) {
                var dayAct = p.getTodayActivity();
                dayAct.setDurationINFO(p.getAlternativeName() + "  " + p.getName());
                dayActivities.add(dayAct);
            }
        }
        return dayActivities;
    }
}
