package ru.sahlob.core.modules.vkpeopleparser.vkstorage;

import ru.sahlob.core.modules.vkpeopleparser.Person;
import ru.sahlob.core.modules.vkpeopleparser.VKPeopleParser;

import java.util.List;

public class MainVKPeopleStorage {

    private static MainVKPeopleStorage mainVKPeopleStorage = new MainVKPeopleStorage();

    VKPeopleStorage vkPeopleStorage = VKPeopleMemoryStorage.getInstance();


    public static MainVKPeopleStorage getInstance() {
        return mainVKPeopleStorage;
    }

    public void addPerson(String name) {

        vkPeopleStorage.addPerson(name, VKPeopleParser.altName(name));
    }

    public Person getPerson(String name) {
        return vkPeopleStorage.getPerson(name);
    }

    public void editPerson(Person person) {
        vkPeopleStorage.editPerson(person);
    }

    public List<Person> getAllPersons() {
        return vkPeopleStorage.getAllPersons();
    }

    public void deletePerson(String name) {
        vkPeopleStorage.deletePerson(name);
    }

    public void editTimeZoneToPerson(String name, int timezone) {
        vkPeopleStorage.editTimeZoneToPerson(name, timezone);
    }
}
