package ru.sahlob.core.modules.vkpeopleparser.vkstorage;

import ru.sahlob.core.modules.vkpeopleparser.Person;

import java.util.ArrayList;
import java.util.List;

public class VKPeopleMemoryStorage implements VKPeopleStorage {

    private static VKPeopleMemoryStorage vkPeopleMemoryStorage = new VKPeopleMemoryStorage();
    ArrayList<Person> persons = new ArrayList<>();

    public static VKPeopleMemoryStorage getInstance() {
        return vkPeopleMemoryStorage;
    }
    @Override
    public void addPerson(Person person) {
        persons.add(person);
    }

    @Override
    public Person getPerson(String name) {
        Person person = null;
        for (int i = 0; i < persons.size(); i++) {
            if (persons.get(i).getName().equals(name)) {
                person = persons.get(i);
                break;
            }
        }
        return person;
    }

    @Override
    public void editPerson(Person person) {

    }

    @Override
    public List<Person> getAllPersons() {
        return persons;
    }
}
