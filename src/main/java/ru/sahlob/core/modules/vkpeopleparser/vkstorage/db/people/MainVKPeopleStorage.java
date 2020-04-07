package ru.sahlob.core.modules.vkpeopleparser.vkstorage.db.people;

import lombok.Data;
import org.springframework.stereotype.Component;
import ru.sahlob.core.modules.vkpeopleparser.models.Person;
import ru.sahlob.core.observers.Observer;

import java.util.List;

@Component
@Data
public class MainVKPeopleStorage {

    private final VKPeopleBDStorage vkPeopleStorage;

    public void addPerson(Person person) {
        vkPeopleStorage.addPerson(person);
    }

    public String addNewWaiter(Observer observer, String name, String waitersName) {
        return vkPeopleStorage.addNewWaiter(observer, name, waitersName);
    }

    public Person getPersonWithTodayDayActivity(Observer observer, String name) {
        return vkPeopleStorage.getPersonWithTodayDayActivity(observer, name);
    }

    public void editPerson(Observer observer, Person person) {
        vkPeopleStorage.editPerson(observer, person);
    }

    public List<Person> getAllPersonsWithTodayDayActivity(Observer observer) {
        return vkPeopleStorage.getAllPersonsWithTodayDayActivity(observer);
    }

    public List<Person> getAllPersonsWithoutDayActivity(Observer observer) {
        return vkPeopleStorage.getAllPersonsWithoutDayActivities(observer);
    }

    public void editTimeZoneToPerson(Observer observer, String name, int timezone) {
        vkPeopleStorage.editTimeZoneToPerson(observer, name, timezone);
    }

    public String editSexToPerson(Observer observer, String name, String sex) {
        return vkPeopleStorage.editSexToPerson(observer, name, sex);
    }

    public String deleteAllDayAndMinutesActivitiesByDay(Observer observer, String key) {
        return vkPeopleStorage.deleteAllDayAndMinutesActivitiesByDay(observer, key);
    }

    public Person getPersonWithDayActivityByDate(Observer observer, String name, String date) {
        return vkPeopleStorage.getPersonWithActivityByDate(observer, name, date);
    }

    public Person getPersonWithoutDayActivity(Observer observer, String name) {
        return vkPeopleStorage.getPersonWithoutActivity(observer, name);
    }

    public List<Person> getAllPersonsWithDayActivityByDate(Observer observer, String date) {
        return vkPeopleStorage.getAllPersonsWithActivityByDate(observer, date);
    }
}
