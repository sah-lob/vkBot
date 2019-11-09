package ru.sahlob.core.commands.commandsmanage.storage;

import ru.sahlob.core.commands.commandsmanage.Command;

import java.util.HashSet;

public interface Storage {

    HashSet<Command> getCommands();
    void addCommnad(Command command);
}
