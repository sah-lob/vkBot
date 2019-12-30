package ru.sahlob.core.commands.commandsmanage;

import com.vk.api.sdk.objects.messages.Message;
import org.springframework.stereotype.Component;
import ru.sahlob.core.commands.commandsmanage.storage.INFO;

@Component
public abstract class Command implements INFO {

    private String name;

    public abstract String getMessage(Message message);

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}