package ru.sahlob.core.modules.vkpeopleparser.services.multi.reminder.remindertypes.db;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.sahlob.core.modules.vkpeopleparser.models.ReminderType;

@Repository
public interface DBReminderTypeRepository extends CrudRepository<ReminderType, Integer> {
    ReminderType findFirstByName(String name);
}
