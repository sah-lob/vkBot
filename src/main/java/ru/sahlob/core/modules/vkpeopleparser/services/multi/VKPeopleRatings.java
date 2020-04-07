package ru.sahlob.core.modules.vkpeopleparser.services.multi;
import lombok.Data;
import org.springframework.stereotype.Component;
import ru.sahlob.core.modules.vkpeopleparser.domain.DayActivity;
import ru.sahlob.core.modules.vkpeopleparser.models.Person;
import ru.sahlob.core.modules.vkpeopleparser.vkstorage.db.people.MainVKPeopleStorage;
import ru.sahlob.core.observers.Observer;

import java.util.ArrayList;
import java.util.List;

import static ru.sahlob.core.commands.vkcommands.answers.VKTextAnswers.TWO_NL;

@Component
@Data
public class VKPeopleRatings {

    private final MainVKPeopleStorage storage;

    public String getPersonsDurationRaiting(Observer observer) {

        var dayActivities = getSortedDayActivityListOfPersons(observer);
        dayActivities.sort(DayActivity.COMPARE_BY_DURATION);

        StringBuilder result = new StringBuilder("Список задротов за сегодня: " + TWO_NL);
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

    public String getPersonsAvgDurationRaiting(Observer observer) {
        var dayActivities = getSortedDayActivityListOfPersons(observer);
        var newDayActivities = new ArrayList<DayActivity>();
        for (var d: dayActivities) {
            if (d.getDayActivities().size() != 0) {
                newDayActivities.add(d);
            }
        }
        newDayActivities.sort(DayActivity.COMPARE_BY_AVG_DURATION);
        var result = new StringBuilder("Список лидеров по средней длине сессии за сегодня: " + TWO_NL);
        for (int i = 0; i < newDayActivities.size(); i++) {
            double data = newDayActivities.get(i).getTodayDuration() /  newDayActivities.get(i).getDayActivities().size();

            result.append(i + 1).append("  ").append(newDayActivities.get(i).getDurationINFO()).append(" - ")
                    .append(data)
                    .append(" мин.\n");
        }
        result.append("Рассчет задротов окончен!");
        return result.toString();
    }

    public String getPseronsAllTimeDurationRaiting(Observer observer) {
        var persons = storage.getAllPersonsWithoutDayActivity(observer);
        persons.sort(Person.COMPARE_BY_DURATION);
        StringBuilder result = new StringBuilder("""
                Главные задроты за все время:\040

                """);


        for (int i = 0; i < persons.size(); i++) {
            result.append(i + 1).append(". ").append(persons.get(i).getAlternativeName()).append(" рекордные для себя ").append(persons.get(i).getRecordDurationAllTime()).append(" мин.\n");
        }
        return result.toString();
    }

    public String getPseronsAvgAllTimeDurationRaiting(Observer observer) {
        var persons = storage.getAllPersonsWithoutDayActivity(observer);
        persons.sort(Person.COMPARE_BY_AVG_DURATION);
        StringBuilder result = new StringBuilder("""
                Рейтинг по средним сессиям:\040

                """);

        for (int i = 0; i < persons.size(); i++) {
            result.append(i + 1).append(". ").append(persons.get(i).getAlternativeName()).append(" в среднем ").append(persons.get(i).getAvgDurationAllTime()).append(" мин.\n");
        }
        return result.toString();
    }

    public String getCountOfPersonsSessions(Observer observer) {

        var dayActivities = getSortedDayActivityListOfPersons(observer);
        dayActivities.sort(DayActivity.COMPARE_BY_SESSION_COUNT);

        StringBuilder result = new StringBuilder("""
                Список главных параноиков за сегодня:\040

                """);

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

    public String getMainStats(Observer observer) {
        var persons = storage.getAllPersonsWithoutDayActivity(observer);
        var maleDuration = 0;
        var maxMaleDuration = 0;
        var maleAvgDuration = 0;
        var maleCount = 0;
        var femaleDuration = 0;
        var femaleAvgDuration = 0;
        var maxFemaleDuration = 0;
        var femaleCount = 0;
        for (var p: persons) {
            if (p.getSex() != null) {
                if (p.getSex().equals("m")) {
                    maleCount++;
                    maleDuration += p.getRecordDurationAllTime();
                    maleAvgDuration += p.getAvgDurationAllTime();
                    if (maxMaleDuration < p.getRecordDurationAllTime()) {
                        maxMaleDuration = p.getRecordDurationAllTime();
                    }
                } else {
                    femaleCount++;
                    femaleDuration += p.getRecordDurationAllTime();
                    femaleAvgDuration += p.getAvgDurationAllTime();
                    if (maxFemaleDuration < p.getRecordDurationAllTime()) {
                        maxFemaleDuration = p.getRecordDurationAllTime();
                    }
                }
            }
        }
        int maxSession = Math.max(maxFemaleDuration, maxMaleDuration);
        var result = """
                Статистика по палате.

                """;
        result += "Самая продолжительная сессия у мужчин: " + maxMaleDuration + " мин.\n";
        result += "Самая продолжительная сессия у женщин: " + maxFemaleDuration + " мин.\n";
        result += "Самая продолжительная сессия: " + maxSession + " мин.\n";
        if (maleCount > 0 && femaleCount > 0) {
            result += "Средняя максимальная длинна сессии у мужчин: " + (maleDuration / maleCount) + " мин.\n";
            result += "Средняя максимальная длинна сессии у женщин: " + (femaleDuration / femaleCount) + " мин.\n";
            result += "Средняя максимальная длинна сессии у всех: " + ((maleDuration + femaleDuration) / (maleCount + femaleCount)) + " мин.\n";
            result += "Средняя сессия у мужчин: " + (maleAvgDuration / maleCount) + " мин.\n";
            result += "Средняя сессия у женщин: " + (femaleAvgDuration / femaleCount) + " мин.\n";
            result += "Средняя сессия у всех: " + ((femaleAvgDuration + maleAvgDuration) / (femaleCount + maleCount)) + " мин.\n";
        }
        result += "Количество наблюдаемых мужчин: " + maleCount + "\n";
        result += "Количество наблюдаемых женщин: " + femaleCount + "\n";

        return result;
    }

    private List<DayActivity> getSortedDayActivityListOfPersons(Observer observer) {

        var persons = storage.getAllPersonsWithTodayDayActivity(observer);
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
