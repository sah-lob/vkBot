package ru.sahlob.core.commands.commandsmanage;

import com.vk.api.sdk.objects.messages.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.sahlob.core.commands.Unknown;
import ru.sahlob.vk.VKManager;

@Component
public class Commander {

    @Autowired
    private Unknown unknown;

    @Autowired
    private CommandManager commandManager;

    @Autowired
    VKManager vkManager;

    /**
     * Обработка сообщений, получаемых через сервис Вконтакте. Имеет ряд дополнительной информации.
     * @param message сообщение (запрос) пользователя
     */
    public void execute(Message message) {

        String body = "";
        if (message.getFwdMessages() == null) {
            body = message.getBody().toLowerCase();
        } else if (message.getBody().isEmpty()) {
            body = message.getFwdMessages().get(0).getBody().toLowerCase();
        }

        Command cmd = null;
        for (var command : commandManager.getCommands()) {
            if (body.contains(command.getName())) {
                cmd = command;
                break;
            }
        }
        if (cmd == null) {
            unknown.setName("unknown");
            cmd = unknown;
        }

        var msg = cmd.getMessage(message);
        vkManager.sendMessage(msg, message.getUserId());
    }

}