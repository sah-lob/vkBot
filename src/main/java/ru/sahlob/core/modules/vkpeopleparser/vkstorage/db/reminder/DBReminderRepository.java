package ru.sahlob.core.modules.vkpeopleparser.vkstorage.db.reminder;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.sahlob.core.modules.vkpeopleparser.models.Reminder;

@Repository
public interface DBReminderRepository extends CrudRepository<Reminder, Integer> {
}
