package ru.sahlob.core.modules.vkpeopleparser.vkstorage.db.people.interfaces;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.sahlob.core.modules.vkpeopleparser.models.Person;

@Repository
public interface DBPersonsRepository extends CrudRepository<Person, Integer> {

    Person getFirstPersonByName(String name);
    Person getFirstPersonByRealId(Integer id);
}
