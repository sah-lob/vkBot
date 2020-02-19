package ru.sahlob.core.modules.vkpeopleparser.vkstorage.db.people;
import lombok.Data;
import org.springframework.stereotype.Component;
import ru.sahlob.core.modules.vkpeopleparser.models.Person;
import ru.sahlob.core.modules.vkpeopleparser.services.single.VKPeopleParser;

import java.util.List;

@Component
@Data
public class MainVKPeopleStorage {

    private final VKPeopleBDStorage vkPeopleStorage;

    public void addPerson(Person person) {
        vkPeopleStorage.addPerson(person);
    }

    public String addNewWaiter(String name, String waitersName) {
        return vkPeopleStorage.addNewWaiter(name, waitersName);
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

    public List<Person> getAllPersonsWithoutDayActivity() {
        return vkPeopleStorage.getAllPersonsWithoutDayActivities();
    }

    public void editTimeZoneToPerson(String name, int timezone) {
        vkPeopleStorage.editTimeZoneToPerson(name, timezone);
    }

    public String editSexToPerson(String name, String sex) {
        return vkPeopleStorage.editSexToPerson(name, sex);
    }

    public String deleteAllDayAndMinutesActivitiesByDay(String key) {
        return vkPeopleStorage.deleteAllDayAndMinutesActivitiesByDay(key);
    }

    public Person getPersonWithDayActivityByDate(String name, String date) {
        return vkPeopleStorage.getPersonWithActivityByDate(name, date);
    }

    public List<Person> getAllPersonsWithDayActivityByDate(String date) {
        return vkPeopleStorage.getAllPersonsWithActivityByDate(date);
    }
}
