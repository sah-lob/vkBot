package ru.sahlob.core.commands.commandsmanage;
import ru.sahlob.core.commands.commandsmanage.storage.MemoryStorage;
import ru.sahlob.core.commands.commandsmanage.storage.Storage;

import java.util.HashSet;

public class CommandManager {

    private Storage storage = new MemoryStorage();

    public HashSet<Command> getCommands() {
        return storage.getCommands();
    }
    public void addCommand(Command command) {
        storage.addCommnad(command);
    }
}