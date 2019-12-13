package ru.sahlob.core.commands;
import com.vk.api.sdk.objects.messages.Message;
import ru.sahlob.core.commands.commandsmanage.Command;
import ru.sahlob.core.modules.vkpeopleparser.VKPeopleParser;
import ru.sahlob.core.modules.vkpeopleparser.vkstorage.MainVKPeopleStorage;
import ru.sahlob.core.modules.vkpeopleparser.vkstorage.VKPeopleMemoryStorage;

public class VKCommand extends Command {

    private VKPeopleParser vkPeopleParser = VKPeopleParser.getInstance();

    public VKCommand(String name) {
        super(name);
    }

    @Override
    public String getMessage(Message message) {
        var result = "";
        var messageBody = message.getBody();

        // это необходимо для слежки за ирой.
        if (messageBody.equals("и") || messageBody.equals("b")) {
            messageBody = "шпионим 12275982";
        }

        if (messageBody.contains("следить")) {
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
        return result;
    }

    public String editTimeZoneVKPerson(String messageBody) {
        messageBody = messageBody.substring(3);
        String userName = messageBody.substring(0, messageBody.indexOf(" "));
        messageBody = messageBody.substring(messageBody.indexOf(" ") + 1);
        messageBody = messageBody.replaceAll(" ", "");
        int timezone = Integer.parseInt(messageBody);
        MainVKPeopleStorage vkPeopleMemoryStorage = MainVKPeopleStorage.getInstance();
        vkPeopleMemoryStorage.editTimeZoneToPerson(userName, timezone);
        return "Часовой пояс изменен.";
    }

    public String addVkPerson(String messageBody) {
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

    public String spy(String messageBody) {
        var name = getNameOrAlternativeNameFromMessageBody(messageBody);
        name = name.replaceAll("id", "");
        return vkPeopleParser.getInfoAboutPerson(MainVKPeopleStorage.getInstance().getPerson(name));
    }

    public String spyAll() {
        return vkPeopleParser.getInfoAboutAllPersons();
    }

    public boolean checkPerson(String messageBody, String name) {
        if (messageBody.contains("следить")) {
            return !checkPersonForExistence(name);
        } else if (messageBody.contains("шпионим")) {
            return checkPersonForExistence(name);
        }
        return true;
    }

    public boolean checkPersonForExistence(String name) {
        return vkPeopleParser.userExistenceCheck(name);
    }

    public String getNameOrAlternativeNameFromMessageBody(String messageBody) {
        var name = messageBody.substring(8);
        System.out.println(name);
        return name;
    }
}
