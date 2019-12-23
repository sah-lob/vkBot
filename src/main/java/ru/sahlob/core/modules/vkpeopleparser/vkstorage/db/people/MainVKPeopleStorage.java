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

    public Person getPerson(String name) {
        return vkPeopleStorage.getPersonWithTodayDayActivity(name);
    }

    public void editPerson(Person person) {
        vkPeopleStorage.editPerson(person);
    }

    public List<Person> getAllPersons() {
        return vkPeopleStorage.getAllPersonsWithTodayDayActivity();
    }

    public void editTimeZoneToPerson(String name, int timezone) {
        vkPeopleStorage.editTimeZoneToPerson(name, timezone);
    }

    public void deleteAllDayAndMinutesActivitiesByDay(String key) {
        vkPeopleStorage.deleteAllDayAndMinutesActivitiesByDay(key);
    }
}