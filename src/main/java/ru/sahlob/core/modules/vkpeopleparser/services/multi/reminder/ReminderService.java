package ru.sahlob.core.modules.vkpeopleparser.services.multi.reminder;

import lombok.Data;
import org.springframework.stereotype.Component;
import ru.sahlob.core.modules.vkpeopleparser.models.Reminder;
import ru.sahlob.core.modules.vkpeopleparser.vkstorage.db.people.VKPeopleBDStorage;
import ru.sahlob.core.modules.vkpeopleparser.vkstorage.db.reminder.VKReminderStorage;
import ru.sahlob.vk.VKManager;

@Component
@Data
public class ReminderService {

    private final VKReminderStorage vkReminderStorage;
    private final VKPeopleBDStorage vkPeopleStorage;
    private final VKManager vkManager;

    public String addReminder(Reminder reminder) {
        if (vkPeopleStorage.getPersonWithoutActivity(reminder.getUser()) == null) {
            return "Пользователь не добавлен в систему";
        }
        return vkReminderStorage.addReminder(reminder);
    }

    public void remindUsers() {
        var reminders = vkReminderStorage.getAllReminders();
        for (var reminder: reminders) {
            var userId = reminder.getUser();
//             vkManager.sendMessage(alternativePersonName + " сейчас онлайн.", Integer.parseInt(userId));
        }
    }

}
