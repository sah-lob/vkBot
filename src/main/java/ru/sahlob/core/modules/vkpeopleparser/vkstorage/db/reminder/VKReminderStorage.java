package ru.sahlob.core.modules.vkpeopleparser.vkstorage.db.reminder;

import lombok.Data;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sahlob.core.modules.vkpeopleparser.models.Reminder;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@Data
public class VKReminderStorage {

    private final DBReminderRepository reminderRepository;

    public String addReminder(Reminder reminder, String personName) {
        reminderRepository.save(reminder);
        return "Напоминенае для " + personName + " добавлено.";
    }

    public void editReminder(Reminder reminder) {
        reminderRepository.save(reminder);
    }

    public List<Reminder> getAllReminders() {
        List<Reminder> result = new ArrayList<>();
        reminderRepository.findAll().forEach(result::add);
        return result;
    }

    public String deleteReminder(String reminderId) {

        if (reminderRepository.findById(Integer.valueOf(reminderId)).isPresent()) {
            reminderRepository.deleteById(Integer.valueOf(reminderId));
            return "напоминание удалено";
        } else {
            return "такого напоминания нет";
        }
    }
}
