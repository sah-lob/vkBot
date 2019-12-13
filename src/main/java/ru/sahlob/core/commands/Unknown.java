package ru.sahlob.core.commands;
import com.vk.api.sdk.objects.messages.Message;
import ru.sahlob.core.commands.commandsmanage.Command;

public class Unknown extends Command {

    public Unknown(String name) {
        super(name);
    }

    @Override
    public String getMessage(Message message) {
        return "Такой команды нет, возможно позжеЙ";
    }


}