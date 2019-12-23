package ru.sahlob.core.commands.commandsmanage;
import org.springframework.stereotype.Component;
import ru.sahlob.core.commands.commandsmanage.storage.MemoryStorage;

import java.util.HashSet;

@Component
public class CommandManager {

    private final MemoryStorage storage;

    public CommandManager(MemoryStorage storage) {
        this.storage = storage;
    }

    public HashSet<Command> getCommands() {
        storage.updateCommands();
        return storage.getCommands();
    }
}