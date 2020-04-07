package ru.sahlob.core.modules.vkpeopleparser.vkstorage;

import ru.sahlob.core.modules.vkpeopleparser.models.Person;
import ru.sahlob.core.observers.Observer;

import java.util.List;

public interface VKPeopleStorage {

    void addPerson(Person person);
    Person getPersonWithTodayDayActivity(Observer observer, String name);
    void editPerson(Observer observer, Person person);
    List<Person> getAllPersonsWithTodayDayActivity(Observer observer);
    List<Person> getAllPersonsWithoutDayActivities(Observer observer);
    void editTimeZoneToPerson(Observer observer, String name, int timezone);
    String deleteAllDayAndMinutesActivitiesByDay(Observer observer, String key);
    Person getPersonWithActivityByDate(Observer observer, String name, String date);
    List<Person> getAllPersonsWithActivityByDate(Observer observer, String date);
}
