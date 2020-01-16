package ru.sahlob.core.modules.vkpeopleparser.vkstorage.db.time;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DBTimesRepository extends CrudRepository<VKTimeKey, Integer> {
    int countAllByIdAfter(int id);
    VKTimeKey findFirstByOrderByIdAsc();
}
