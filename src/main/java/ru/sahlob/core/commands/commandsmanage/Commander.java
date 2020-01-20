package ru.sahlob.core.commands.commandsmanage;

import com.vk.api.sdk.objects.messages.Message;
import org.springframework.stereotype.Component;
import ru.sahlob.core.commands.Unknown;
import ru.sahlob.vk.VKManager;

@Component
public class Commander {

    private final CommandManager commandManager;
    private final VKManager vkManager;

    public Commander(CommandManager commandManager, VKManager vkManager) {
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
            var messageBody = message.getBody().toLowerCase();
            if (messageBody.contains("хуй")
                    || messageBody.contains("хуи")
                    || messageBody.contains("hui")
                    || messageBody.contains("пидр")
            ) {
                if (messageBody.contains("застрахуй")) {
                    msg = "наушники свои " + message.getBody();
                } else if (messageBody.contains("перестраху")) {
                    msg = "Самый умный? нах иди";
                } else {
                    msg = "Cам " + message.getBody();
                }
            } else {
                msg = "Такой команды нет, введите слово 'команды', чтобы посмотреть команды, которые есть.";
            }
        } else {
            msg = cmd.getMessage(message);
        }
        if (message.getBody().toLowerCase().contains("застрахуй")) {
            for (int i = 0; i < 5; i++) {
                vkManager.sendMessage("иди", message.getUserId());
                vkManager.sendMessage("нахуй", message.getUserId());
            }
        }
        vkManager.sendMessage(msg, message.getUserId());
    }

}