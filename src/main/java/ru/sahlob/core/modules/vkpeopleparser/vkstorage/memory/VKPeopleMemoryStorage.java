package ru.sahlob.core.modules.vkpeopleparser.vkstorage.memory;

import org.springframework.stereotype.Service;
import ru.sahlob.core.modules.vkpeopleparser.models.Person;
import ru.sahlob.core.modules.vkpeopleparser.services.single.VKPeopleParser;
import ru.sahlob.core.modules.vkpeopleparser.vkstorage.VKPeopleStorage;
import ru.sahlob.core.observers.Observer;

import java.util.ArrayList;
import java.util.List;

@Service
public class VKPeopleMemoryStorage implements VKPeopleStorage {

    @Override
    public void addPerson(Person person) {
    }

    @Override
    public Person getPersonWithTodayDayActivity(Observer observer, String name) {
        return null;
    }

    @Override
    public void editPerson(Observer observer, Person person) {

    }

    @Override
    public List<Person> getAllPersonsWithTodayDayActivity(Observer observer) {
        return null;
    }

    @Override
    public List<Person> getAllPersonsWithoutDayActivities(Observer observer) {
        return null;
    }

    @Override
    public void editTimeZoneToPerson(Observer observer, String name, int timezone) {

    }

    @Override
    public String deleteAllDayAndMinutesActivitiesByDay(Observer observer, String key) {
        return null;
    }

    @Override
    public Person getPersonWithActivityByDate(Observer observer, String name, String date) {
        return null;
    }

    @Override
    public List<Person> getAllPersonsWithActivityByDate(Observer observer, String date) {
        return null;
    }
}
