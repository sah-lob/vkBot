package ru.sahlob.core.commands.commandsmanage;

import com.vk.api.sdk.objects.messages.Message;

public abstract class Command {

    private final String name;

    public Command(String name) {
        this.name = name;
    }

    public abstract String getMessage(Message message);

    public String getName() {
        return name;
    }
}