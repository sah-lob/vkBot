package ru.sahlob.core.modules.vkpeopleparser.vkstorage.db.people;

import lombok.Data;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sahlob.core.modules.vkpeopleparser.models.Person;
import ru.sahlob.core.modules.vkpeopleparser.domain.DayActivity;
import ru.sahlob.core.modules.vkpeopleparser.domain.MinuteActivity;
import ru.sahlob.core.modules.vkpeopleparser.vkstorage.VKPeopleStorage;
import ru.sahlob.core.modules.vkpeopleparser.vkstorage.db.people.interfaces.DBDaysRepository;
import ru.sahlob.core.modules.vkpeopleparser.vkstorage.db.people.interfaces.DBMinutesRepository;
import ru.sahlob.core.modules.vkpeopleparser.vkstorage.db.people.interfaces.DBPersonsRepository;
import ru.sahlob.core.modules.vkpeopleparser.vktime.VKTime;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@Transactional
@Data
public class VKPeopleBDStorage implements VKPeopleStorage {

    private final DBPersonsRepository personsRepository;

    private final DBDaysRepository daysRepository;

    private final DBMinutesRepository minutesRepository;

    public void addPerson(Person person) {
        personsRepository.save(person);
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
        if (person != null) {
            person.setTimezone(timezone);
            personsRepository.save(person);
        }
    }

    public String editSexToPerson(String name, String sex) {
        String result;
        var person = personsRepository.getFirstPersonByName(name);
        if (person != null) {
            if (sex.equals("f") || sex.equals("m")) {
                person.setSex(sex);
                personsRepository.save(person);
                result = "Пол поменялся=)";
            } else {
                result = "Пол передается двумя буквами: \"m\" или \"f\"";
            }
        } else {
            result = "Такого человека нет";
        }
        return result;
    }

    public String addNewWaiter(String name, String waiter) {
        String result;
        var person = getPersonWithoutActivity(name);
        if (person == null) {
            result = "Для того, чтобы получить уведомление о том, что человек вошел в вк его нужно добавить."
                    + " Для этого введите команду: следить " + name;
        } else {
            person.addExpectingPeople(waiter);
            editPerson(person);
            result = "Когда человек будет онлайн, придет сообщение.";
        }
        return result;
    }

    @Override
    public String deleteAllDayAndMinutesActivitiesByDay(String key) {
        key = key.toUpperCase();
        ArrayList<Long> dayActivitiesId = new ArrayList<>();
        for (var d: daysRepository.findAllByKey(key)) {
            dayActivitiesId.add(d.getId());
        }
        daysRepository.deleteAllByKey(key);
        for (var id: dayActivitiesId) {
            minutesRepository.deleteAllByDayActivity(id);
        }
        return "";
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
