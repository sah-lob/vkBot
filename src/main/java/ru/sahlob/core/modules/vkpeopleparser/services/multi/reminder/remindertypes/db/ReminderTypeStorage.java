package ru.sahlob.core.modules.vkpeopleparser.services.multi.reminder.remindertypes.db;

import lombok.Data;
import org.springframework.stereotype.Component;
import ru.sahlob.core.modules.vkpeopleparser.models.ReminderType;

@Component
@Data
public class ReminderTypeStorage {

    private final DBReminderTypeRepository dbReminderTypeRepository;

    public String addReminderType(ReminderType reminderType) {
        String result;
        if (dbReminderTypeRepository.findFirstByName(reminderType.getName()) == null) {
            dbReminderTypeRepository.save(reminderType);
            result = "тип напоминаний добавлен";
        } else {
            result = "такой тип напоминаний уже существует.";
        }
        return result;
    }

    public void editReminderType(ReminderType reminderType) {
        dbReminderTypeRepository.save(reminderType);
    }

    public ReminderType getGetReminderTypeByName(String name) {
        return dbReminderTypeRepository.findFirstByName(name);
    }
}
