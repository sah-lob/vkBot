package ru.sahlob.core.commands.commandsmanage;

import com.vk.api.sdk.objects.messages.Message;
import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public abstract class Command {

    private String name;

    public abstract String getMessage(Message message);
}