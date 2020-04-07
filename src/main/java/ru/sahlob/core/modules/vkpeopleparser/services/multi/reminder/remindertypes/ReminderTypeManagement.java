package ru.sahlob.core.modules.vkpeopleparser.services.multi.reminder.remindertypes;

import lombok.Data;
import org.springframework.stereotype.Component;
import ru.sahlob.core.modules.vkpeopleparser.models.ReminderType;
import ru.sahlob.core.modules.vkpeopleparser.services.multi.reminder.remindertypes.db.ReminderTypeStorage;

import java.util.List;

@Component
@Data
public class ReminderTypeManagement {

    private final ReminderTypeStorage reminderTypeStorage;

    public String addReminderType(String name) {
        var reminderType = new ReminderType();
        reminderType.setName(name);
       return reminderTypeStorage.addReminderType(reminderType);
    }

    public ReminderType getReminderTypeByName(String name) {
        return reminderTypeStorage.getGetReminderTypeByName(name);
    }
    

    public String getRandomReminderTypeNote(String reminderTypeName) {
        return reminderTypeStorage.getGetReminderTypeByName(reminderTypeName).getRandomNote();
    }

    public String addNoteToReminderType(String reminderTypeName, String note) {
        String result;
        var reminderType = reminderTypeStorage.getGetReminderTypeByName(reminderTypeName);
        if (reminderType != null) {
            reminderType.addNote(note);
            reminderTypeStorage.editReminderType(reminderType);
            result = "Запись добавлен.";
        } else {
            result = "Данный тип напоминания пока еще не добавлен.";
        }
        return result;
    }

    public void addManyNotesToReminderType(String reminderTypeName, List<String> notes) {
        var reminderType = reminderTypeStorage.getGetReminderTypeByName(reminderTypeName);
        if (reminderType != null) {
            reminderType.addAllNotes(notes);
            reminderTypeStorage.editReminderType(reminderType);
        }
    }
}
