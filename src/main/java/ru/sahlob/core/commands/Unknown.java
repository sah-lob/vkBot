package ru.sahlob.core.commands;
import com.vk.api.sdk.objects.messages.Message;
import org.springframework.stereotype.Component;
import ru.sahlob.core.commands.commandsmanage.Command;

@Component
public class Unknown extends Command {



    @Override
    public String getMessage(Message message) {
        return "Такой команды нет, а надо ли?";
    }
}