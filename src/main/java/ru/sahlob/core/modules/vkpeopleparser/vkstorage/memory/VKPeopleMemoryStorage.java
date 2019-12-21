package ru.sahlob.core.modules.vkpeopleparser.vkstorage.memory;

import org.springframework.stereotype.Service;
import ru.sahlob.core.modules.vkpeopleparser.Person;
import ru.sahlob.core.modules.vkpeopleparser.VKPeopleParser;
import ru.sahlob.core.modules.vkpeopleparser.vkstorage.VKPeopleStorage;

import java.util.ArrayList;
import java.util.List;

@Service
public class VKPeopleMemoryStorage implements VKPeopleStorage {

    private ArrayList<Person> persons = new ArrayList<>();

    @Override
    public void addPerson(String name, String alternativeName) {
        VKPeopleParser.altName(name);
        persons.add(new Person(name, alternativeName));
    }

    @Override
    public Person getPersonWithTodayDayActivity(String name) {
        Person person = null;
        for (Person value : persons) {
            if (value.getName().equals(name)) {
                person = value;
                break;
            }
        }
        return person;
    }

    @Override
    public void editPerson(Person person) {

    }

    @Override
    public List<Person> getAllPersonsWithTodayDayActivity() {
        return persons;
    }

    @Override
    public void editTimeZoneToPerson(String name, int timezone) {
        getPersonWithTodayDayActivity(name).setTimezone(timezone);
    }

    @Override
    public void deleteAllDayAndMinutesActivitiesByDay(String key) {

    }
}
