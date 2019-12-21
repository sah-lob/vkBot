package ru.sahlob.core.modules.vkpeopleparser.vkstorage.db.people.interfaces;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.sahlob.core.modules.vkpeopleparser.activity.DayActivity;

@Repository
public interface DBDaysRepository  extends CrudRepository<DayActivity, Integer> {

    Iterable<DayActivity> findAllByKey(String key);

    DayActivity getDayActivityByPersonAndKey(Long id, String key);

    void deleteAllByKey(String key);

}
