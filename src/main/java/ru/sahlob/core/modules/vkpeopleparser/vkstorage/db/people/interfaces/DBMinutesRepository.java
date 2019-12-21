package ru.sahlob.core.modules.vkpeopleparser.vkstorage.db.people.interfaces;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.sahlob.core.modules.vkpeopleparser.activity.MinuteActivity;

@Repository
public interface DBMinutesRepository extends CrudRepository<MinuteActivity, Integer> {

    Iterable<MinuteActivity> findAllByDayActivity(Long id);

    void deleteAllByDayActivity(Long id);

}
