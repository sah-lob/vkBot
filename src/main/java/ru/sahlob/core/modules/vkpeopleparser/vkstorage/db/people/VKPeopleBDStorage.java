package ru.sahlob.core.modules.vkpeopleparser.vkstorage.db.people;
import lombok.Data;
import org.springframework.transaction.annotation.Transactional;
import ru.sahlob.core.modules.vkpeopleparser.Person;
import ru.sahlob.core.modules.vkpeopleparser.VKPeopleParser;
import ru.sahlob.core.modules.vkpeopleparser.domain.MinuteActivity;
import ru.sahlob.core.modules.vkpeopleparser.vktime.VKTime;
import ru.sahlob.core.modules.vkpeopleparser.domain.DayActivity;
import ru.sahlob.core.modules.vkpeopleparser.vkstorage.VKPeopleStorage;
import ru.sahlob.core.modules.vkpeopleparser.vkstorage.db.people.interfaces.DBDaysRepository;
import ru.sahlob.core.modules.vkpeopleparser.vkstorage.db.people.interfaces.DBMinutesRepository;
import ru.sahlob.core.modules.vkpeopleparser.vkstorage.db.people.interfaces.DBPersonsRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@Transactional
@Data
public class VKPeopleBDStorage implements VKPeopleStorage {

    private final DBPersonsRepository personsRepository;

    private final DBDaysRepository daysRepository;

    private final DBMinutesRepository minutesRepository;

    @Override
    public void addPerson(String name, String alternativeName) {
        personsRepository.save(new Person(name, alternativeName));
    }

    @Override
    public void editPerson(Person person) {
        personsRepository.save(person);
        var todayActivity = person.getTodayActivity();
        if (todayActivity != null) {
            if (getDayAndMinutesActivitiesByDate(person, VKTime.getDateKey(person.getTimezone())) == null) {
                todayActivity = new DayActivity(person.getTimezone());
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
    }

    @Override
    public void editTimeZoneToPerson(String name, int timezone) {
        var person = personsRepository.getFirstPersonByName(name);
        person.setTimezone(timezone);
        personsRepository.save(person);
    }

    public void editSexToPerson(String name, String sex) {
        var person = personsRepository.getFirstPersonByName(name);
        person.setSex(sex);
        personsRepository.save(person);
    }

    public void addNewWaiter(String name, String waiter) {
        Person person = getPersonWithoutActivity(name);
        if (person == null) {
            addPerson(name, VKPeopleParser.altName(name));
            person = getPersonWithoutActivity(name);
        }
        person.addExpectingPeople(waiter);
        editPerson(person);
    }

    @Override
    public void deleteAllDayAndMinutesActivitiesByDay(String key) {
        key = key.toUpperCase();
        ArrayList<Long> dayActivitiesId = new ArrayList<>();
        for (var d: daysRepository.findAllByKey(key)) {
            dayActivitiesId.add(d.getId());
        }
        daysRepository.deleteAllByKey(key);
        for (var id: dayActivitiesId) {
            minutesRepository.deleteAllByDayActivity(id);
        }
    }

    @Override
    public Person getPersonWithTodayDayActivity(String name) {
        return getPersonWithActivityByDate(name, "");
    }

    @Override
    public Person getPersonWithActivityByDate(String name, String date) {
        var person = personsRepository.getFirstPersonByName(name);


        if (person != null) {
            if (date.equals("")) {
                date = VKTime.getDateKey(person.getTimezone());
            }
            person.updateActivityByDate(date, getDayAndMinutesActivitiesByDate(person, date));
        }
        return person;
    }

    public Person getPersonWithoutActivity(String name) {
        var person = personsRepository.getFirstPersonByName(name);
        return  person;
    }
    @Override
    public List<Person> getAllPersonsWithTodayDayActivity() {
        return getAllPersonsWithActivityByDate("");
    }

    @Override
    public List<Person> getAllPersonsWithoutDayActivities() {
        var persons = personsRepository.findAll();
        return StreamSupport.stream(persons.spliterator(), false)
                .collect(Collectors.toList());
    }

    @Override
    public List<Person> getAllPersonsWithActivityByDate(String date) {
        var persons = personsRepository.findAll();
        var result =
                StreamSupport.stream(persons.spliterator(), false)
                        .collect(Collectors.toList());
        for (int i = 0; i < result.size(); i++) {
            var p = result.get(i);
            if (date.equals("")) {
                result.get(i).updateActivityByDate(VKTime.getDateKey(p.getTimezone()), getDayAndMinutesActivitiesByDate(p, VKTime.getDateKey(p.getTimezone())));
            } else {
                result.get(i).updateActivityByDate(date, getDayAndMinutesActivitiesByDate(p, date));
            }
        }
        return result;
    }

    private DayActivity getDayAndMinutesActivitiesByDate(Person person, String date) {
        DayActivity dayActivity = null;
        try {
            dayActivity = daysRepository.getDayActivityByPersonAndKey(person.getId(), date);
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
