package ru.sahlob.core.modules.vkpeopleparser.services.multi.reminder;

import lombok.Data;
import org.springframework.stereotype.Component;
import ru.sahlob.core.modules.vkpeopleparser.models.Reminder;
import ru.sahlob.core.modules.vkpeopleparser.models.ReminderType;
import ru.sahlob.core.modules.vkpeopleparser.services.multi.reminder.frequencytypes.FrequencyType;
import ru.sahlob.core.modules.vkpeopleparser.services.multi.reminder.remindertypes.ReminderTypeManagement;
import ru.sahlob.core.modules.vkpeopleparser.vkstorage.db.people.VKPeopleBDStorage;
import ru.sahlob.core.modules.vkpeopleparser.vkstorage.db.reminder.VKReminderStorage;
import ru.sahlob.vk.VKManager;

import java.util.List;

@Component
@Data
public class ReminderService {

    private final VKManager vkManager;
    private final ReminderSender reminderSender;
    private final VKPeopleBDStorage vkPeopleStorage;
    private final VKReminderStorage vkReminderStorage;
    private final ReminderTypeManagement reminderTypeManagement;

    public String addReminder(Reminder reminder, String personName) {
        var person = vkPeopleStorage.getPersonWithoutActivity(personName);
        if (person == null) {
            return "Пользователь не добавлен в систему";
        }
        reminder.setUserId(person.getRealId());
        reminder.setUserName(person.getAlternativeName());
        return vkReminderStorage.addReminder(reminder, personName);
    }

    public String deleteReminder(String reminderId) {
        return vkReminderStorage.deleteReminder(reminderId);
    }

    public String showAllReminders() {
        List<Reminder> list = vkReminderStorage.getAllReminders();

        if (list.isEmpty()) {
            return "Напоминаний еще нет";
        }
        String result = "Созданные напоминания: \n";

        for (int i = 0; i < list.size(); i++) {
            result += (i + 1) + ") "
                    + list.get(i).getUserName()
                    + "  " + list.get(i).getId()
                    + "  " + list.get(i).getFrequency()
                    + "  " + list.get(i).getReminderType()
                    + "  " + list.get(i).getReminderTimePassed() + "\n\n";
        }

        return result;
    }

    public void remindUsers() {
        String frequency = "онлайн";
        var reminders = vkReminderStorage.getAllReminders();
        for (Reminder reminder: reminders) {
            if (reminder.getFrequency().equals(frequency)) {
                var userId = reminder.getUserId();
                if (frequency.equals(FrequencyType.онлайн.name())) {
                    reminderSender.online(userId, reminder);
                } else if (frequency.contains(FrequencyType.каждыйДень.name())) {
                    reminderSender.everyDay(userId, reminder, frequency);
                } else if (frequency.contains(FrequencyType.каждыйМесяц.name())) {
                    System.out.println("later");
//                    reminderSender.everyMonth(userId, reminder, frequency);
                } else if (frequency.contains(FrequencyType.каждыйГод.name())) {
                    System.out.println("later");
//                    reminderSender.everyYear(userId, reminder, frequency);
                }
            }
        }
    }
}
