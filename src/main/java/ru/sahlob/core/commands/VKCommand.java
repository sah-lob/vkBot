package ru.sahlob.core.commands;

import com.vk.api.sdk.objects.messages.Message;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.stereotype.Component;
import ru.sahlob.core.commands.commandsmanage.Command;
import ru.sahlob.core.commands.commandsmanage.storage.commands.VkCommands;
import ru.sahlob.core.modules.vkpeopleparser.models.Reminder;
import ru.sahlob.core.modules.vkpeopleparser.services.multi.VKMorning;
import ru.sahlob.core.modules.vkpeopleparser.services.multi.VKPeopleRatings;
import ru.sahlob.core.modules.vkpeopleparser.services.multi.VKTwoPeopleAnalize;
import ru.sahlob.core.modules.vkpeopleparser.services.multi.reminder.ReminderService;
import ru.sahlob.core.modules.vkpeopleparser.services.multi.reminder.remindertypes.ReminderTypeManagement;
import ru.sahlob.core.modules.vkpeopleparser.services.single.VKPeopleParser;
import ru.sahlob.core.modules.vkpeopleparser.vkstorage.VKTimeStorage;
import ru.sahlob.core.modules.vkpeopleparser.vkstorage.db.people.MainVKPeopleStorage;
import ru.sahlob.core.modules.vkpeopleparser.vktime.VKTime;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Component
@Data
public class VKCommand extends Command {

    private final MainVKPeopleStorage vkPeopleMemoryStorage;
    private final VKPeopleParser vkPeopleParser;
    private final VKPeopleRatings vkPeopleRatings;
    private final VKTwoPeopleAnalize vkTwoPeopleAnalize;
    private final VKTimeStorage vkTimeStorage;
    private final VKMorning vkMorning;
    private final ReminderService reminderService;
    private final ReminderTypeManagement reminderTypeManagement;

    @Override
    public String getMessage(Message message) {
        var result = "";
        var messageBody = message.getBody().toLowerCase();

        observersLogic(message);

        // это необходимо для слежки за ирой.
        if (messageBody.equals(VkCommands.и.name())
                || messageBody.equals(VkCommands.b.name())) {
            messageBody = "шпионим 12275982";
        }

        if (messageBody.contains(VkCommands.следить.name())) {
            System.out.println(message.getUserId());
            if (messageBody.contains("al_lb")
                    || messageBody.contains("7965708")
                    || messageBody.contains("mynameisann")
                    || messageBody.contains("3501014")) {
                result = "Не по сеньке шапка";
            } else {
                result = addVkPerson(messageBody);
            }
        }

        if (messageBody.contains(VkCommands.удалить.name())) {
            if (messageBody.contains("день")) {
                result = deleteDay(messageBody);
            } else if (messageBody.contains("напоминание")) {
                result = deleteReminder(messageBody);
            }

        }

        if (messageBody.contains(VkCommands.шпионим.name())) {
            result = spy(messageBody);
        }

        if (messageBody.contains(VkCommands.досье.name())) {
            result = dossier(messageBody);
        }

        if (messageBody.contains(VkCommands.лидерыс.name())) {
            result = avgAllTimeDurationsLiders();
        } else if (messageBody.contains(VkCommands.лидеры.name())) {
            result = allTimeDurationsLiders();
        }

        if (messageBody.contains(VkCommands.чп.name())) {
            if (message.getUserId() == 7965708) {
                result = editTimeZoneVKPerson(messageBody);
            } else {
                result = "не надо лучше=)";
            }
        }

        if (messageBody.contains(VkCommands.параноики.name())) {
            result = personsSessionCount();
        }

        if (messageBody.contains(VkCommands.задротыс.name())) {
            result = personsAvgDurationRaiting();
        } else if (messageBody.contains(VkCommands.задроты.name())) {
            result = personsDurationRating();
        }

        if (messageBody.contains(VkCommands.едины.name())) {
            result = jointOnlineOfTwoUsers(messageBody);
        }

        if (messageBody.contains(VkCommands.утро.name())) {
            result = usersMorning();
        }

        if (messageBody.contains(VkCommands.даты.name())) {
            result = availableDates();
        }

        if (messageBody.contains(VkCommands.команды.name())) {
            result = info(message.getUserId());
        }

        if (messageBody.contains(VkCommands.пол.name())) {
            result = setSex(messageBody);
        }

        if (messageBody.contains(VkCommands.статистика.name())) {
            result = stats();
        }

        if (messageBody.contains(VkCommands.онлайн.name())) {
            result = online(message);
        }

        if (messageBody.contains(VkCommands.напоминать.name())) {
            result = addReminder(message);
        }

        if (messageBody.contains(VkCommands.напоминания.name())) {
            result = reminders();
        }
        if (messageBody.contains(VkCommands.много.name())) {
            result = manyRemindersTypesFields(messageBody);
        }
        if (messageBody.contains(VkCommands.добавить.name())) {
            result = addOneReminderType(messageBody);
        }

        if (result == null || result == "") {
            result = "Странно, непонятно как вы умудрились увидеть это сообщение.";
        }

        return result;
    }

    private String editTimeZoneVKPerson(String messageBody) {
        messageBody = messageBody.substring(3);
        String userName = messageBody.substring(0, messageBody.indexOf(" "));
        messageBody = messageBody.substring(messageBody.indexOf(" ") + 1);
        messageBody = messageBody.replaceAll(" ", "");
        int timezone = Integer.parseInt(messageBody);
        vkPeopleMemoryStorage.editTimeZoneToPerson(userName, timezone);
        return "Часовой пояс изменен.";
    }

    private String addVkPerson(String messageBody) {
        var result = "";
        var name = getNameOrAlternativeNameFromMessageBody(messageBody);
        boolean checkPerson = checkPerson(messageBody, name);
        if (checkPerson) {
            if (vkPeopleParser.addNewPerson(name)) {
                result = "Человечек добавлен";
            } else {
                result = "Введены некорректные данные человечка";
            }
        } else {
            result = "А человечек то уже наблюдается";
        }
        return result;
    }

    private String spy(String messageBody) {
        if (messageBody.split(" ").length > 2) {
            var m = messageBody.split(" ");
            var name = m[1];
            var date = m[2] + " " + m[3].toUpperCase() + " " + m[4];
            return vkPeopleParser.getInfoAboutPerson(vkPeopleMemoryStorage.getPersonWithDayActivityByDate(name, date), date);
        } else {
            var name = getNameOrAlternativeNameFromMessageBody(messageBody);
            name = name.replaceAll("id", "");
            return vkPeopleParser.getInfoAboutPerson(vkPeopleMemoryStorage.getPersonWithTodayDayActivity(name), "");
        }
    }

    private String dossier(String messageBody) {
        var name = messageBody.replaceAll("досье ", "");
        return vkPeopleParser.getInfoAboutPersonsRecords(vkPeopleMemoryStorage.getPersonWithTodayDayActivity(name));
    }

    private String personsDurationRating() {
        return vkPeopleRatings.getPersonsDurationRaiting();
    }

    private String personsAvgDurationRaiting() {
        return vkPeopleRatings.getPersonsAvgDurationRaiting();
    }

    private String allTimeDurationsLiders() {
        return vkPeopleRatings.getPseronsAllTimeDurationRaiting();
    }

    private String avgAllTimeDurationsLiders() {
        return vkPeopleRatings.getPseronsAvgAllTimeDurationRaiting();
    }

    private String personsSessionCount() {
        return vkPeopleRatings.getCountOfPersonsSessions();
    }

    private boolean checkPerson(String messageBody, String name) {
        if (messageBody.contains("следить")) {
            return !checkPersonForExistence(name);
        } else if (messageBody.contains("шпионим")) {
            return checkPersonForExistence(name);
        }
        return true;
    }

    private boolean checkPersonForExistence(String name) {
        return vkPeopleParser.userExistenceCheck(name);
    }

    private String getNameOrAlternativeNameFromMessageBody(String messageBody) {
        return messageBody.substring(8);
    }

    private String jointOnlineOfTwoUsers(String messageBody) {
        var ew = messageBody.split(" ");
        var name1 = ew[1].replace(" ", "");
        var name2 = ew[2].replace(" ", "");
        if (!checkPersonForExistence(name1)) {
            return "1 пользователь " + name1 + " не добавлен";
        } else if (!checkPersonForExistence(name2)) {
            return "2 пользователь " + name2 + " не добавлен";
        } else {
            return vkTwoPeopleAnalize.jointOnlineOfTwoUsers(name1, name2);
        }
    }

    private String usersMorning() {
        return vkMorning.usersMorning();
    }

    private String deleteDay(String messageBody) {
        var key = messageBody.replaceAll("удалить день ", "");
        return vkPeopleMemoryStorage.deleteAllDayAndMinutesActivitiesByDay(key);
    }

    private String deleteReminder(String messageBody) {
        var reminderId = messageBody.replaceAll("удалить напоминание ", "");
        return reminderService.deleteReminder(reminderId);
    }

    private String availableDates() {
        var list = vkTimeStorage.getAllAvlDays();
        StringBuilder result = new StringBuilder("Доступные даты: \n");
        for (var l : list) {
            result.append(l).append("\n");
        }
        return result.toString();
    }

    private String setSex(String messageBody) {
        var mas = messageBody.split(" ");
        var name = mas[1];
        var sex = mas[2];
        return vkPeopleMemoryStorage.editSexToPerson(name, sex);
    }

    private String stats() {
        return vkPeopleRatings.getMainStats();
    }

    private String info(Integer id) {
        var list = new ArrayList<>();
        list.add("'следить " + id + "' - добавить пользователя, за которым нужно следить.");
        list.add("'шпионим " + id + "' - наблюдать, сколько пользователь был онлайн.");
        list.add("'шпионим " + id + " " + VKTime.getDateKey(3) + "' - наблюдать, сколько пользователь был онлайн.");
        list.add("'даты' - данные пользователей хроняться только за эти дни.");
        list.add("'задроты' - топ лист по длительности сессии сегодня из всех людей, за которыми наблюдают.");
        list.add("'задротыс' - топ лист по средней длительности сессии по пользователям.");
        list.add("'параноики' - топ лист из людей, кто чаще заходил.");
        list.add("'лидеры' - топ лист по длительности сессии людей за все время.");
        list.add("'лидерыс' - топ лист по средней длительности сессии людей за все время.");
        list.add("'ЧП " + id + "' +3 - изменить часовой пояс у пользователя.(возможно не работает).");
        list.add("'едины " + id + " " + id + "' - смотреть сколько времени люди были онлайн одновременно.");
        list.add("'досье " + id + "' - смотреть максимальное время и среднюю длину сессий пользователя.");
        list.add("'команды' - команды");
        StringBuilder result = new StringBuilder("Команды вк:\n");
        for (int i = 0; i < list.size(); i++) {
            result.append(i + 1).append(". ").append(list.get(i)).append("\n");
        }
        return result.toString();
    }

    private String online(Message message) {
        String messageBody = message.getBody();
        messageBody = messageBody.substring(7);
        messageBody = messageBody.replaceAll("id", "");
        return vkPeopleMemoryStorage.addNewWaiter(messageBody, String.valueOf(message.getUserId()));
    }

    private String addReminder(Message message) {
        var mas = message.getBody().split(" ");
        var name = mas[1];
        var reminderType = mas[2];
        var frequency = mas[3];
        var reminderTimeDuration = mas[4];
        var reminder = new Reminder();
        reminder.setReminderType(reminderType);
        reminder.setFrequency(frequency);
        reminder.setReminderTimeDuration(reminderTimeDuration);
        return reminderService.addReminder(reminder, name);
    }

    private String reminders() {
        return reminderService.showAllReminders();
    }

    private String manyRemindersTypesFields(String messageBody) {
        var result = "";
        String answer = messageBody.replaceFirst("много строк ", "");
        String reminderTypeName = answer.substring(0, answer.indexOf("\n"));
        String[] body = answer.substring(answer.indexOf("\n") + 1).split("!-");

        if (reminderTypeName.length() > 0 && body.length > 0) {
            reminderTypeManagement.addManyNotesToReminderType(reminderTypeName, List.of(body));
            result = "Ваш список добавлен.";
        } else {
            result = "Список не добавлен, так как там нет элементов или названия списка.";
        }

        return result;
    }

    private String addOneReminderType(String messageBody) {
        String result;
        if (messageBody.contains("добавить список")) {
            var answer = messageBody.replaceFirst("добавить список ", "");
            if (answer.length() > 0) {
                result = reminderTypeManagement.addReminderType(answer);
            } else {
                result = "Название списка не может быть пустым.";
            }
        } else {
            var answer = messageBody.replaceFirst("добавить в список ", "");
            var body = List.of(answer.split(""));
            var reminderTypeName = body.get(0);
            var note = body.get(1);
            result = reminderTypeManagement.addNoteToReminderType(reminderTypeName, note);
        }

        return result;
    }

    private void observersLogic(Message message) {

    }
}
