package ru.sahlob.core.modules.vkpeopleparser.services.multi.reminder;

import lombok.Data;
import org.springframework.stereotype.Component;
import ru.sahlob.core.modules.vkpeopleparser.models.Person;
import ru.sahlob.core.modules.vkpeopleparser.models.Reminder;
import ru.sahlob.core.modules.vkpeopleparser.services.multi.reminder.frequencytypes.FrequencyType;
import ru.sahlob.core.modules.vkpeopleparser.services.single.VKPeopleParser;
import ru.sahlob.core.modules.vkpeopleparser.vkstorage.db.people.VKPeopleBDStorage;
import ru.sahlob.core.modules.vkpeopleparser.vkstorage.db.reminder.VKReminderStorage;
import ru.sahlob.vk.VKManager;

import java.util.Arrays;
import java.util.List;

@Component
@Data
public class ReminderSender {

    private final VKManager vkManager;
    private final VKPeopleParser vkPeopleParser;
    private final VKPeopleBDStorage vkPeopleStorage;
    private final VKReminderStorage vkReminderStorage;

    public void online(Integer userId, Reminder reminder) {
        if (vkPeopleParser.personOnline(String.valueOf(userId))) {
            Person person = vkPeopleStorage.getPersonWithoutActivityByRealId(userId);
            if (!person.isActive() && !reminder.getSendMsg()) {
                reminder.setSendMsg(true);
                vkManager.sendMessage("Вы сейчас онлайн.",
                        Integer.parseInt(String.valueOf(userId)));
            }
            if (person.isActive() && reminder.getSendMsg()) {
                reminder.setSendMsg(false);
            }
        } else {
            reminder.setSendMsg(false);
        }
        vkReminderStorage.editReminder(reminder);
    }

    public void everyDay(Integer userId, Reminder reminder, String frequency) {
        var time = frequency.substring(FrequencyType.каждыйДень.name().length());
    }
}
