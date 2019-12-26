package ru.sahlob.core.commands.commandsmanage;

import com.vk.api.sdk.objects.messages.Message;
import org.springframework.stereotype.Component;

@Component
public abstract class Command {



    private String name;

    public abstract String getMessage(Message message);

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}