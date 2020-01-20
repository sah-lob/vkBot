package ru.sahlob.core.commands;
import com.vk.api.sdk.objects.messages.Message;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import ru.sahlob.core.commands.commandsmanage.Command;
import ru.sahlob.core.modules.vkpeopleparser.VKMorning;
import ru.sahlob.core.modules.vkpeopleparser.VKTwoPeopleAnalize;
import ru.sahlob.core.modules.vkpeopleparser.VKPeopleParser;
import ru.sahlob.core.modules.vkpeopleparser.VKPeopleRatings;
import ru.sahlob.core.modules.vkpeopleparser.vkstorage.VKTimeStorage;
import ru.sahlob.core.modules.vkpeopleparser.vkstorage.db.people.MainVKPeopleStorage;
import ru.sahlob.core.modules.vkpeopleparser.vktime.VKTime;

import java.util.ArrayList;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class VKCommand extends Command {

    private final MainVKPeopleStorage vkPeopleMemoryStorage;
    private final VKPeopleParser vkPeopleParser;
    private final VKPeopleRatings vkPeopleRatings;
    private final VKTwoPeopleAnalize vkTwoPeopleAnalize;
    private final VKTimeStorage vkTimeStorage;
    private final VKMorning vkMorning;


    public VKCommand(MainVKPeopleStorage vkPeopleMemoryStorage,
                     VKPeopleParser vkPeopleParser,
                     VKPeopleRatings vkPeopleRatings,
                     VKTwoPeopleAnalize vkTwoPeopleAnalize,
                     VKMorning vkMorning,
                     VKTimeStorage vkTimeStorage) {
        this.vkPeopleMemoryStorage = vkPeopleMemoryStorage;
        this.vkPeopleParser = vkPeopleParser;
        this.vkPeopleRatings = vkPeopleRatings;
        this.vkTwoPeopleAnalize = vkTwoPeopleAnalize;
        this.vkMorning = vkMorning;
        this.vkTimeStorage = vkTimeStorage;
    }

    @Override
    public String getMessage(Message message) {
        var result = "";
        var messageBody = message.getBody().toLowerCase();

        // это необходимо для слежки за ирой.
        if (messageBody.equals("и") || messageBody.equals("b")) {
            messageBody = "шпионим 12275982";
        }

        if (messageBody.contains("следить")) {
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

        if (messageBody.contains("удалить")) {
            result = deleteDay(messageBody);
        }

        if (messageBody.contains("шпионим")) {
            result = spy(messageBody);
        }

        if (messageBody.contains("досье")) {
            result = dossier(messageBody);
        }

        if (messageBody.contains("лидерыс")) {
            result = avgAllTimeDurationsLiders();
        } else if (messageBody.contains("лидеры")) {
            result = allTimeDurationsLiders();
        }

        if (messageBody.contains("чп")) {
            if (message.getUserId() == 7965708) {
                result = editTimeZoneVKPerson(messageBody);
            } else {
                result = "не надо лучше=)";
            }
        }

        if (messageBody.contains("параноики")) {
            result = personsSessionCount();
        }

        if (messageBody.contains("задротыс")) {
            result = personsAvgDurationRaiting();
        } else if (messageBody.contains("задроты")) {
            result = personsDurationRating();
        }

        if (messageBody.contains("едины")) {
            result = jointOnlineOfTwoUsers(messageBody);
        }

        if (messageBody.contains("утро")) {
            result = usersMorning();
        }

        if (messageBody.contains("даты")) {
            result = availableDates();
        }

        if (messageBody.contains("команды")) {
            result = info(message.getUserId());
        }

        if (messageBody.contains("пол")) {
            result = setSex(messageBody);
        }

        if (messageBody.contains("статистика")) {
            result = stats();
        }
        if (result == null) {
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
        var key = messageBody.replaceAll("удалить ", "");
        vkPeopleMemoryStorage.deleteAllDayAndMinutesActivitiesByDay(key);
        return "Возможно удалины=)";
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
        var result = "";
        if (sex.equals("m") || sex.equals("f")) {
            vkPeopleMemoryStorage.editSexToPerson(name, sex);
            result = "Пол поменялся=)";
        } else {
            result = "Пол был введен неправильно нужно 'f' - женский или 'm'- мужской";
        }
        return result;
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
}
