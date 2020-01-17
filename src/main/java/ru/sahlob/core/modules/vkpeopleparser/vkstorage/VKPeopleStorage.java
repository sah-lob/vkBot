package ru.sahlob.core.modules.vkpeopleparser.vkstorage;

import ru.sahlob.core.modules.vkpeopleparser.Person;

import java.util.List;

public interface VKPeopleStorage {

    void addPerson(String name, String alternativeName);
    Person getPersonWithTodayDayActivity(String name);
    void editPerson(Person person);
    List<Person> getAllPersonsWithTodayDayActivity();
    void editTimeZoneToPerson(String name, int timezone);
    void deleteAllDayAndMinutesActivitiesByDay(String key);
    Person getPersonWithActivityByDate(String name, String date);
    List<Person> getAllPersonsWithActivityByDate(String date);
}
