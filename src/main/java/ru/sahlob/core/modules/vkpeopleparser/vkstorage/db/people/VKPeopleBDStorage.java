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
import ru.sahlob.core.observers.Observer;
import ru.sahlob.core.observers.roles.Roles;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static ru.sahlob.core.commands.vkcommands.answers.VKTextAnswers.PERSON_NOT_ADDED;

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
    public void editPerson(Observer observer, Person person) {
        if (hasNoAccessToUser(observer, String.valueOf(person.getRealId()))) return;
        personsRepository.save(person);
        var todayActivity = person.getTodayActivity();
        if (todayActivity != null) {
            if (getDayAndMinutesActivitiesByDate(observer, person, VKTime.getDateKey(person.getTimezone())) == null) {
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
    public void editTimeZoneToPerson(Observer observer, String name, int timezone) {
        if (hasNoAccessToUser(observer, name)) return;
        var person = personsRepository.getFirstPersonByName(name);
        if (person != null) {
            person.setTimezone(timezone);
            personsRepository.save(person);
        }
    }

    public String editSexToPerson(Observer observer, String name, String sex) {
        if (hasNoAccessToUser(observer, name)) return PERSON_NOT_ADDED;
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
            result = PERSON_NOT_ADDED;
        }
        return result;
    }

    public String addNewWaiter(Observer observer, String name, String waiter) {
        if (hasNoAccessToUser(observer, name)) return PERSON_NOT_ADDED;
        String result;
        var person = getPersonWithoutActivity(observer, name);
        if (person == null) {
            result = "Для того, чтобы получить уведомление о том, что человек вошел в вк его нужно добавить."
                    + " Для этого введите команду: следить " + name;
        } else {
            person.addExpectingPeople(waiter);
            editPerson(observer, person);
            result = "Когда человек будет онлайн, придет сообщение.";
        }
        return result;
    }

    @Override
    public String deleteAllDayAndMinutesActivitiesByDay(Observer observer, String key) {
        if (!observer.getRoles().contains(Roles.admin)) return "У вас нет прав";
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
    public Person getPersonWithTodayDayActivity(Observer observer, String name) {
        return getPersonWithActivityByDate(observer, name, "");
    }

    @Override
    public Person getPersonWithActivityByDate(Observer observer, String name, String date) {
        if (hasNoAccessToUser(observer, name)) return null;
        var person = personsRepository.getFirstPersonByName(name);

        if (person != null) {
            if (date.equals("")) {
                date = VKTime.getDateKey(person.getTimezone());
            }
            person.updateActivityByDate(date, getDayAndMinutesActivitiesByDate(observer, person, date));
        }
        return person;
    }

    public Person getPersonWithoutActivity(Observer observer, String name) {
        if (hasNoAccessToUser(observer, name)) return null;
        return personsRepository.getFirstPersonByName(name);
    }

    public Person getPersonWithoutActivityByRealId(Observer observer, Integer id) {
        if (hasNoAccessToUser(observer, id)) return null;
        return personsRepository.getFirstPersonByRealId(id);
    }

    @Override
    public List<Person> getAllPersonsWithTodayDayActivity(Observer observer) {
        return getAllPersonsWithActivityByDate(observer, "");
    }

    @Override
    public List<Person> getAllPersonsWithoutDayActivities(Observer observer) {
        return getPersonsList(observer);
    }

    @Override
    public List<Person> getAllPersonsWithActivityByDate(Observer observer, String date) {
        var persons = getPersonsList(observer);
        for (Person p : persons) {
            if (date.equals("")) {
                p.updateActivityByDate(VKTime.getDateKey(p.getTimezone()), getDayAndMinutesActivitiesByDate(observer, p, VKTime.getDateKey(p.getTimezone())));
            } else {
                p.updateActivityByDate(date, getDayAndMinutesActivitiesByDate(observer, p, date));
            }
        }
        return persons;
    }

    private DayActivity getDayAndMinutesActivitiesByDate(Observer observer, Person person, String date) {
        if (hasNoAccessToUser(observer, person.getRealId())) return null;
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

    private List<Person> getPersonsList(Observer observer) {
        var persons = new ArrayList<Person>();
        if (observer == null || observer.getRoles().contains(Roles.admin)) {
            personsRepository.findAll().forEach(persons::add);
        } else {
            Set<Integer> personsId = observer
                    .getPersonsId()
                    .stream()
                    .map(Integer::parseInt)
                    .collect(Collectors.toSet());
            persons.addAll(personsRepository.getAllByRealIdIn(personsId));
        }
        return persons;
    }

    private boolean hasNoAccessToUser(Observer observer, String name) {
        if (observer == null || observer.getRoles().contains(Roles.admin)) return false;
        var person = personsRepository.getFirstPersonByName(name);
        if (person != null) {
            return !observer.getPersonsId().contains(String.valueOf(person.getRealId()));
        } else {
            return !observer.getPersonsId().contains(name);
        }
    }

    private boolean hasNoAccessToUser(Observer observer, Integer name) {
        if (observer == null || observer.getRoles().contains(Roles.admin)) return false;
        return !observer.getPersonsId().contains(String.valueOf(name));
    }
}


