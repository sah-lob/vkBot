package ru.sahlob.core.commands.commandsmanage;

import com.vk.api.sdk.objects.messages.Message;
import org.springframework.stereotype.Component;
import ru.sahlob.core.commands.Unknown;
import ru.sahlob.vk.VKManager;

@Component
public class Commander {

    private  Unknown unknown;
    private final CommandManager commandManager;
    private final VKManager vkManager;

    public Commander(Unknown unknown, CommandManager commandManager, VKManager vkManager) {
        this.unknown = unknown;
        this.commandManager = commandManager;
        this.vkManager = vkManager;
    }

    /**
     * Обработка сообщений, получаемых через сервис Вконтакте. Имеет ряд дополнительной информации.
     * @param message сообщение (запрос) пользователя
     */
    public void execute(Message message) {
        var body = "";
        if (message.getFwdMessages() == null) {
            var index = message.getBody().indexOf(" ");
            if (index > -1) {
                body = message.getBody().substring(0, index);
            } else {
                body = message.getBody();
            }
            body = body.replaceAll(" ", "").toLowerCase();
        } else if (message.getBody().isEmpty()) {
            body = message.getFwdMessages().get(0).getBody().toLowerCase();
        }

        Command cmd = null;
        for (var command : commandManager.getCommands()) {
            if (body.equals(command.getName())) {
                cmd = command;
                break;
            }
        }
        String msg;
        if (cmd == null) {
            if (message.getBody().contains("хуй")
                    || message.getBody().contains("хуи")
                    || message.getBody().contains("hui")
            ) {
                msg = "Cам " + message.getBody();
            } else {
                msg = "Такой команды нет, а надо ли?";
            }
        } else {
            msg = cmd.getMessage(message);
        }
        vkManager.sendMessage(msg, message.getUserId());
    }

}