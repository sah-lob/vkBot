package ru.sahlob.core.modules.vkpeopleparser.vkstorage;

import ru.sahlob.core.modules.vkpeopleparser.Person;

import java.util.List;

public interface VKPeopleStorage {

    void addPerson(String name, String alternativeName);
    Person getPerson(String name);
    void editPerson(Person person);
    List<Person> getAllPersons();
    void deletePerson(String name);
    void editTimeZoneToPerson(String name, int timezone);
}
