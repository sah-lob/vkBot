package ru.sahlob.core.commands.commandsmanage.storage;

import ru.sahlob.core.commands.Anna;
import ru.sahlob.core.commands.VKCommand;
import ru.sahlob.core.commands.Weather;
import ru.sahlob.core.commands.commandsmanage.Command;

import java.util.HashSet;


public class MemoryStorage implements Storage {

    private static HashSet<Command> commands = new HashSet<>();

    public MemoryStorage() {
        commands.add(new Weather("weather"));
        commands.add(new Weather("погода"));
        commands.add(new Anna("аня"));
        commands.add(new VKCommand("b"));
        commands.add(new VKCommand("и"));
    }

    @Override
    public HashSet<Command> getCommands() {
        return commands;
    }

    @Override
    public void addCommnad(Command command) {

    }
}
