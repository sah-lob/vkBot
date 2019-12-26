package ru.sahlob.core.commands;
import com.vk.api.sdk.objects.messages.Message;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import ru.sahlob.core.commands.commandsmanage.Command;
import ru.sahlob.core.modules.vkpeopleparser.VKPeopleParser;
import ru.sahlob.core.modules.vkpeopleparser.VKPeopleStats;
import ru.sahlob.core.modules.vkpeopleparser.vkstorage.db.people.MainVKPeopleStorage;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class VKCommand extends Command {

    private final MainVKPeopleStorage vkPeopleMemoryStorage;
    private final VKPeopleParser vkPeopleParser;
    private final VKPeopleStats vkPeopleStats;


    public VKCommand(MainVKPeopleStorage vkPeopleMemoryStorage, VKPeopleParser vkPeopleParser, VKPeopleStats vkPeopleStats) {
        this.vkPeopleMemoryStorage = vkPeopleMemoryStorage;
        this.vkPeopleParser = vkPeopleParser;
        this.vkPeopleStats = vkPeopleStats;
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

        if (messageBody.contains("шпионим")) {
            result = spy(messageBody);
        }

        if (messageBody.contains("ЧП")) {
            result = editTimeZoneVKPerson(messageBody);
        }

        if (messageBody.contains("белка")) {
            result = spyAll();
        }

        if (messageBody.contains("параноики")) {
            result = personsSessionCount();
        }

        if (messageBody.contains("задротыс")) {
            result = personsAvgDurationRaiting();
        } else if (messageBody.contains("задроты")) {
            result = personsDurationRating();
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
        var name = getNameOrAlternativeNameFromMessageBody(messageBody);
        name = name.replaceAll("id", "");
        return vkPeopleParser.getInfoAboutPerson(vkPeopleMemoryStorage.getPerson(name));
    }

    private String spyAll() {
        return vkPeopleParser.getInfoAboutAllPersons();
    }

    private String personsDurationRating() {
        return vkPeopleStats.getPersonsDurationRaiting();
    }

    private String personsAvgDurationRaiting() {
        return vkPeopleStats.getPersonsAvgDurationRaiting();
    }

    private String personsSessionCount() {
        return vkPeopleStats.getCountOfPersonsSessions();
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
}
