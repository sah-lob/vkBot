package ru.sahlob.core.commands;

import ru.sahlob.core.commands.commandsmanage.Command;
import ru.sahlob.core.modules.vkpeopleparser.VKPeopleParser;

public class VKCommand extends Command {


    public VKCommand(String name) {
        super(name);
    }

    @Override
    public String getMessage() {
        return new VKPeopleParser().onlinePeople();
    }
}
