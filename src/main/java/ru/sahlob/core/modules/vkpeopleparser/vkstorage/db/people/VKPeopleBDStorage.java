package ru.sahlob.core.modules.vkpeopleparser.vkstorage.db.people;

import ru.sahlob.core.modules.vkpeopleparser.Person;
import ru.sahlob.core.modules.vkpeopleparser.activity.MinuteActivity;
import ru.sahlob.core.modules.vkpeopleparser.vktime.VKTime;
import ru.sahlob.core.modules.vkpeopleparser.activity.DayActivity;
import ru.sahlob.core.modules.vkpeopleparser.vkstorage.VKPeopleStorage;
import ru.sahlob.core.modules.vkpeopleparser.vkstorage.db.people.interfaces.DBDaysRepository;
import ru.sahlob.core.modules.vkpeopleparser.vkstorage.db.people.interfaces.DBMinutesRepository;
import ru.sahlob.core.modules.vkpeopleparser.vkstorage.db.people.interfaces.DBPersonsRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class VKPeopleBDStorage implements VKPeopleStorage {

    private final DBPersonsRepository personsRepository;

    private final DBDaysRepository daysRepository;

    private final DBMinutesRepository minutesRepository;

    public VKPeopleBDStorage(DBPersonsRepository personsRepository, DBDaysRepository daysRepository, DBMinutesRepository minutesRepository) {
        this.personsRepository = personsRepository;
        this.daysRepository = daysRepository;
        this.minutesRepository = minutesRepository;
    }

    @Override
    public void addPerson(String name, String alternativeName) {
        personsRepository.save(new Person(name, alternativeName));
    }

    @Override
    public Person getPersonWithTodayDayActivity(String name) {
        var person = personsRepository.getFirstPersonByName(name);
        if (person != null) {
            person.updateTodayActivity(getTodsyDayAndMinutesActivities(person));
        }
        return person;
    }

    @Override
    public void editPerson(Person person) {
        personsRepository.save(person);

        var todayActivity = person.getTodayActivity();

        if (getTodsyDayAndMinutesActivities(person) == null) {
            todayActivity.setPerson(person.getId());
            todayActivity.setKey(VKTime.getDateKey(person.getTimezone()));
            daysRepository.save(todayActivity);
        }

        var minuteActivies = todayActivity.getDayActivities();

        for (var m : minuteActivies) {
            m.setDayActivity(todayActivity.getId());
            minutesRepository.save(m);
        }

    }

    @Override
    public List<Person> getAllPersonsWithTodayDayActivity() {
        var persons = personsRepository.findAll();
        var result =
                StreamSupport.stream(persons.spliterator(), false)
                        .collect(Collectors.toList());
        for (var p: result) {
            p.updateTodayActivity(getTodsyDayAndMinutesActivities(p));
        }

        return result;
    }

    @Override
    public void editTimeZoneToPerson(String name, int timezone) {
    }

    @Override
    public void deleteAllDayAndMinutesActivitiesByDay(String key) {

        ArrayList<Long> dayActivitiesId = new ArrayList<>();
        for (var d: daysRepository.findAllByKey(key)) {
            dayActivitiesId.add(d.getId());
        }
        daysRepository.deleteAllByKey(key);
        for (var id: dayActivitiesId) {
            minutesRepository.deleteAllByDayActivity(id);
        }
    }

    private DayActivity getTodsyDayAndMinutesActivities(Person person) {

        DayActivity dayActivity = null;
        try {
             dayActivity = daysRepository.getDayActivityByPersonAndKey(person.getId(), VKTime.getDateKey(person.getTimezone()));
        } catch (Exception ignored) {
        }
        if (dayActivity != null) {
            var minuteActivities = minutesRepository.findAllByDayActivity(dayActivity.getId());
            if (minuteActivities != null) {
                var minuteActivities1 =
                        StreamSupport.stream(minuteActivities.spliterator(), false)
                                .sorted(MinuteActivity.COMPARE_BY_TIME).collect(Collectors.toList());
                dayActivity.setDayActivities(minuteActivities1);
            }
        }

        return dayActivity;
    }
}
