package ru.sahlob.core.modules.vkpeopleparser.vkstorage;

import ru.sahlob.core.modules.vkpeopleparser.models.Person;

import java.util.List;

public interface VKPeopleStorage {

    void addPerson(Person person);
    Person getPersonWithTodayDayActivity(String name);
    void editPerson(Person person);
    List<Person> getAllPersonsWithTodayDayActivity();
    List<Person> getAllPersonsWithoutDayActivities();
    void editTimeZoneToPerson(String name, int timezone);
    String deleteAllDayAndMinutesActivitiesByDay(String key);
    Person getPersonWithActivityByDate(String name, String date);
    List<Person> getAllPersonsWithActivityByDate(String date);
}
