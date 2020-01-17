package ru.sahlob.core.modules.vkpeopleparser.vkstorage.db.people;
import org.springframework.stereotype.Component;
import ru.sahlob.core.modules.vkpeopleparser.Person;
import ru.sahlob.core.modules.vkpeopleparser.VKPeopleParser;

import java.util.List;

@Component
public class MainVKPeopleStorage {

    private final VKPeopleBDStorage vkPeopleStorage;

    public MainVKPeopleStorage(VKPeopleBDStorage vkPeopleStorage) {
        this.vkPeopleStorage = vkPeopleStorage;
    }


    public void addPerson(String name) {
        vkPeopleStorage.addPerson(name, VKPeopleParser.altName(name));
    }

    public Person getPersonWithTodayDayActivity(String name) {
        return vkPeopleStorage.getPersonWithTodayDayActivity(name);
    }

    public void editPerson(Person person) {
        vkPeopleStorage.editPerson(person);
    }

    public List<Person> getAllPersonsWithTodayDayActivity() {
        return vkPeopleStorage.getAllPersonsWithTodayDayActivity();
    }

    public void editTimeZoneToPerson(String name, int timezone) {
        vkPeopleStorage.editTimeZoneToPerson(name, timezone);
    }

    public void deleteAllDayAndMinutesActivitiesByDay(String key) {
        vkPeopleStorage.deleteAllDayAndMinutesActivitiesByDay(key);
    }

    public Person getPersonWithDayActivityByDate(String name, String date) {
        return vkPeopleStorage.getPersonWithActivityByDate(name, date);
    }

    public List<Person> getAllPersonsWithDayActivityByDate(String date) {
        return vkPeopleStorage.getAllPersonsWithActivityByDate(date);
    }
}
