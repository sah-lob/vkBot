package ru.sahlob.core.commands.commandsmanage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.sahlob.core.commands.commandsmanage.storage.MemoryStorage;
import ru.sahlob.core.commands.commandsmanage.storage.Storage;

import java.util.HashSet;

@Component
public class CommandManager {

    @Autowired
    private MemoryStorage storage;

    public HashSet<Command> getCommands() {
        storage.updateCommands();
        return storage.getCommands();
    }

    public void addCommand(Command command) {
        storage.addCommnad(command);
    }
}