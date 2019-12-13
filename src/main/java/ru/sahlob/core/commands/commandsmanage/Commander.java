package ru.sahlob.core.commands.commandsmanage;

import com.vk.api.sdk.objects.messages.Message;
import ru.sahlob.core.commands.Unknown;
import ru.sahlob.vk.VKManager;

public class Commander {

    /**
     * Обработка сообщений, получаемых через сервис Вконтакте. Имеет ряд дополнительной информации.
     * @param message сообщение (запрос) пользователя
     */
    public static void execute(Message message) {

        String body = "";
        if (message.getFwdMessages() == null) {
            body = message.getBody().toLowerCase();
        } else if (message.getBody().isEmpty()) {
            body = message.getFwdMessages().get(0).getBody().toLowerCase();
        }

        Command cmd = null;
        for (var command : new CommandManager().getCommands()) {
            if (body.contains(command.getName())) {
                cmd = command;
                break;
            }
        }
        if (cmd == null) {
            cmd = new Unknown("unknown");
        }

        var msg = cmd.getMessage(message);
        System.out.println(message);
        System.out.println(msg);
        new VKManager().sendMessage(msg, message.getUserId());
    }

}