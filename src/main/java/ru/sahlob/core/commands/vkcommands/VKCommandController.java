package ru.sahlob.core.commands.vkcommands;

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
import ru.sahlob.core.observers.Observer;
import ru.sahlob.core.observers.ObserversManagement;
import ru.sahlob.core.observers.ObserversStorage;
import ru.sahlob.core.observers.roles.Roles;

import java.util.ArrayList;
import java.util.List;

import static ru.sahlob.core.commands.vkcommands.answers.VKTextAnswers.*;

@EqualsAndHashCode(callSuper = true)
@Component
@Data
public class VKCommandController extends Command {

    private final ReminderTypeManagement reminderTypeManagement;
    private final MainVKPeopleStorage vkPeopleMemoryStorage;
    private final ObserversManagement observersManagement;
    private final VKTwoPeopleAnalize vkTwoPeopleAnalize;
    private final ObserversStorage observersStorage;
    private final ReminderService reminderService;
    private final VKPeopleRatings vkPeopleRatings;
    private final VKPeopleParser vkPeopleParser;
    private final VKTimeStorage vkTimeStorage;
    private final VKMorning vkMorning;

    @Override
    public String getMessage(Message message) {
        var result = "";
        var messageBody = message.getBody().toLowerCase();

        var observer = observersLogic(message);

        if (observer.getRoles().contains(Roles.admin)) {
            // это необходимо для слежки за ирой.
            if (messageBody.equals(VkCommands.и.name())
                || messageBody.equals(VkCommands.b.name())) {
                messageBody = "шпионим 12275982";
            }

            if (messageBody.equals(VkCommands.роль.name())) {
                result = observersRoles(observer, message);
            }

        }

        if (observer.getRoles().contains(Roles.standart)) {
            if (messageBody.contains(VkCommands.следить.name())) {
                if (!observer.getRoles().contains(Roles.admin)) {
                    result = addVkPerson(observer, messageBody);
                } else {
                    if (messageBody.contains("al_lb")
                        || messageBody.contains("7965708")
                        || messageBody.contains("mynameisann")
                        || messageBody.contains("3501014")) {
                        result = "Не по сеньке шапка";
                    } else {
                        result = addVkPerson(observer, messageBody);
                    }
                }
            }

            if (messageBody.contains(VkCommands.удалить.name())) {
                if (messageBody.contains(DAY)) {
                    result = deleteDay(observer, messageBody);
                } else if (messageBody.contains(REMINDER)) {
                    result = deleteReminder(observer, messageBody);
                }

            }

            if (messageBody.contains(VkCommands.шпионим.name())) {
                result = spy(observer, messageBody);
            }

            if (messageBody.contains(VkCommands.досье.name())) {
                result = dossier(observer, messageBody);
            }

            if (messageBody.contains(VkCommands.лидерыс.name())) {
                result = avgAllTimeDurationsLiders(observer);
            } else if (messageBody.contains(VkCommands.лидеры.name())) {
                result = allTimeDurationsLiders(observer);
            }

            if (messageBody.contains(VkCommands.чп.name())) {
                if (message.getUserId() == 7965708) {
                    result = editTimeZoneVKPerson(observer, messageBody);
                } else {
                    result = "не надо лучше=)";
                }
            }

            if (messageBody.contains(VkCommands.параноики.name())) {
                result = personsSessionCount(observer);
            }

            if (messageBody.contains(VkCommands.задротыс.name())) {
                result = personsAvgDurationRaiting(observer);
            } else if (messageBody.contains(VkCommands.задроты.name())) {
                result = personsDurationRating(observer);
            }

            if (messageBody.contains(VkCommands.едины.name())) {
                result = jointOnlineOfTwoUsers(observer, messageBody);
            }

            if (messageBody.contains(VkCommands.утро.name())) {
                result = usersMorning(observer);
            }

            if (messageBody.contains(VkCommands.даты.name())) {
                result = availableDates();
            }

            if (messageBody.contains(VkCommands.команды.name())) {
                result = info(message.getUserId());
            }

            if (messageBody.contains(VkCommands.пол.name())) {
                result = setSex(observer, messageBody);
            }

            if (messageBody.contains(VkCommands.статистика.name())) {
                result = stats(observer);
            }

            if (messageBody.contains(VkCommands.онлайн.name())) {
                result = online(observer, message);
            }

            if (messageBody.contains(VkCommands.напоминать.name())) {
                result = addReminder(observer, message);
            }

            if (messageBody.contains(VkCommands.напоминания.name())) {
                result = reminders(observer);
            }

            if (messageBody.contains(VkCommands.много.name())) {
                result = manyRemindersTypesFields(observer, messageBody);
            }

            if (messageBody.contains(VkCommands.добавить.name())) {
                result = addOneReminderType(observer, messageBody);
            }
        }

        if (result == null || result.equals("")) {
            result = "Такой команды нет, введите слово 'команды', чтобы посмотреть команды, которые есть.";
        }

        return result;
    }

    private String editTimeZoneVKPerson(Observer observer, String messageBody) {
        messageBody = messageBody.substring(3);
        var userName = messageBody.substring(0, messageBody.indexOf(WSP));
        messageBody = messageBody.substring(messageBody.indexOf(WSP) + 1);
        messageBody = messageBody.replaceAll(WSP, "");
        var timezone = Integer.parseInt(messageBody);
        vkPeopleMemoryStorage.editTimeZoneToPerson(observer, userName, timezone);
        return TIME_ZONE_HAS_BEEN_CHANGED;
    }

    private String addVkPerson(Observer observer, String messageBody) {
        var result = "";
        var name = getNameOrAlternativeNameFromMessageBody(messageBody);
        boolean checkPerson = checkPerson(observer, messageBody, name);
        if (checkPerson) {
            var person = vkPeopleParser.addNewPerson(name);
            if (person != null) {
                result = observersManagement.addPersonsName(observer,
                        String.valueOf(person.getRealId()));
            } else {
                result = INCORRECT_HUMAN_DATA_WAS_ENTERED;
            }
        } else {
            var person = vkPeopleMemoryStorage.getPersonWithoutDayActivity(null, name);
            result = observersManagement.addPersonsName(
                    observer,
                    String.valueOf(person.getRealId()));
        }
        return result;
    }

    private String spy(Observer observer, String messageBody) {
        if (messageBody.split(WSP).length > 2) {
            var m = messageBody.split(WSP);
            var name = m[1];
            name = name.replaceAll(ID, "");
            var date = m[2] + WSP + m[3].toUpperCase() + WSP + m[4];
            return observer.getPersonsId().contains(name) ?
                    vkPeopleParser
                            .getInfoAboutPerson(vkPeopleMemoryStorage
                                    .getPersonWithDayActivityByDate(observer, name, date),
                                    date) : PERSON_NOT_ADDED;
        } else {
            var name = getNameOrAlternativeNameFromMessageBody(messageBody);
            name = name.replaceAll(ID, "");
            return observer.getPersonsId().contains(name) ?
                    vkPeopleParser
                            .getInfoAboutPerson(vkPeopleMemoryStorage
                                    .getPersonWithTodayDayActivity(observer, name),
                                    "") : PERSON_NOT_ADDED;
        }
    }

    private String dossier(Observer observer, String messageBody) {
        var name = messageBody.replaceAll(DOSSIER + WSP, "");
        return personsExist(observer, name) ? vkPeopleParser.
                getInfoAboutPersonsRecords(vkPeopleMemoryStorage.
                        getPersonWithTodayDayActivity(observer, name)) : PERSON_NOT_ADDED;
    }

    private String personsDurationRating(Observer observer) {
        return vkPeopleRatings.getPersonsDurationRaiting(observer);
    }

    private String personsAvgDurationRaiting(Observer observer) {
        return vkPeopleRatings.getPersonsAvgDurationRaiting(observer);
    }

    private String allTimeDurationsLiders(Observer observer) {
        return vkPeopleRatings.getPseronsAllTimeDurationRaiting(observer);
    }

    private String avgAllTimeDurationsLiders(Observer observer) {
        return vkPeopleRatings.getPseronsAvgAllTimeDurationRaiting(observer);
    }

    private String personsSessionCount(Observer observer) {
        return vkPeopleRatings.getCountOfPersonsSessions(observer);
    }

    private boolean checkPerson(Observer observer, String messageBody, String name) {
        if (messageBody.contains(VkCommands.следить.toString())) {
            return !checkPersonForExistence(null, name);
        } else if (messageBody.contains(VkCommands.шпионим.toString())) {
            return checkPersonForExistence(observer, name);
        }
        return true;
    }

    private boolean checkPersonForExistence(Observer observer, String name) {
        return vkPeopleParser.userExistenceCheck(observer, name);
    }

    private String getNameOrAlternativeNameFromMessageBody(String messageBody) {
        return messageBody.substring(8);
    }

    private String jointOnlineOfTwoUsers(Observer observer, String messageBody) {
        var ew = messageBody.split(WSP);
        var name1 = ew[1].replace(WSP, "");
        var name2 = ew[2].replace(WSP, "");
        if (!checkPersonForExistence(observer, name1)) {
            return "1 пользователь " + name1 + " не добавлен";
        } else if (!checkPersonForExistence(observer, name2)) {
            return "2 пользователь " + name2 + " не добавлен";
        } else {
            return vkTwoPeopleAnalize.jointOnlineOfTwoUsers(observer, name1, name2);
        }
    }

    private String usersMorning(Observer observer) {
        return vkMorning.usersMorning(observer);
    }

    private String deleteDay(Observer observer, String messageBody) {
        var key = messageBody.replaceAll(DELETE_DAY + WSP, "");
        return vkPeopleMemoryStorage.deleteAllDayAndMinutesActivitiesByDay(observer, key);
    }

    private String deleteReminder(Observer observer, String messageBody) {
        var reminderId = messageBody.replaceAll(DELETE_REMINDER + WSP, "");
        if(observer.getPersonsId().contains(reminderId) || observer.getRoles().contains(Roles.admin)){
            return reminderService.deleteReminder(reminderId);
        }
        return PERSON_NOT_ADDED;
    }

    private String availableDates() {
        var list = vkTimeStorage.getAllAvlDays();
        StringBuilder result = new StringBuilder(AVAILABLE_DATES + ": \n");
        for (var l : list) {
            result.append(l).append("\n");
        }
        return result.toString();
    }

    private String setSex(Observer observer, String messageBody) {
        var mas = messageBody.split(WSP);
        var name = mas[1];
        var sex = mas[2];
        return vkPeopleMemoryStorage.editSexToPerson(observer, name, sex);
    }

    private String stats(Observer observer) {
        return vkPeopleRatings.getMainStats(observer);
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

    private String online(Observer observer, Message message) {
        String messageBody = message.getBody();
        messageBody = messageBody.substring(7);
        messageBody = messageBody.replaceAll(ID, "");
        return vkPeopleMemoryStorage.addNewWaiter(observer, messageBody, String.valueOf(message.getUserId()));
    }

    private String addReminder(Observer observer, Message message) {
        var mas = message.getBody().split(WSP);
        var name = mas[1];
        var reminderType = mas[2];
        var frequency = mas[3];
        var reminderTimeDuration = mas[4];
        var reminder = new Reminder();
        reminder.setReminderType(reminderType);
        reminder.setFrequency(frequency);
        reminder.setReminderTimeDuration(reminderTimeDuration);
        return reminderService.addReminder(observer, reminder, name);
    }

    private String reminders(Observer observer) {
        return reminderService.showAllReminders();
    }

    private String manyRemindersTypesFields(Observer observer, String messageBody) {
        var result = "";
        var answer = messageBody.replaceFirst(MANY_LINES + WSP, "");
        var reminderTypeName = answer.substring(0, answer.indexOf("\n"));
        var body = answer.substring(answer.indexOf("\n") + 1)
                .split(LINE_SEPARATOR_IN_THE_REMINDER_LIST);

        if (reminderTypeName.length() > 0 && body.length > 0) {
            reminderTypeManagement.addManyNotesToReminderType(reminderTypeName, List.of(body));
            result = YOUR_LIST_HAS_BEEN_ADDED;
        } else {
            result = LIST_WAS_NOT_ADDED_BECAUSE_THERE_ARE_NO_ITEMS_OR_NAMES_OF_THE_LIST;
        }

        return result;
    }

    private String addOneReminderType(Observer observer, String messageBody) {
        String result;
        if (messageBody.contains(ADD_LIST)) {
            var answer = messageBody.replaceFirst(ADD_LIST + WSP, "");
            if (answer.length() > 0) {
                result = reminderTypeManagement.addReminderType(answer);
            } else {
                result = NAME_OF_LIST_CANNOT_BE_EMPTY;
            }
        } else {
            var answer = messageBody.replaceFirst(ADD_TO_LIST + WSP, "");
            var body = List.of(answer.split(""));
            var reminderTypeName = body.get(0);
            var note = body.get(1);
            result = reminderTypeManagement.addNoteToReminderType(reminderTypeName, note);
        }
        return result;
    }

    private Observer observersLogic(Message message) {
        return observersStorage.getObserver(String.valueOf(message.getUserId()));
    }

    private String observersRoles(Observer observer, Message message) {
        return null;
    }

    private boolean personsExist(Observer observer, String personsId) {
        return observer.getPersonsId().contains(personsId);
    }

}
