package ru.sahlob.core.commands.commandsmanage;
import lombok.Data;
import org.springframework.stereotype.Component;
import ru.sahlob.core.commands.commandsmanage.storage.MemoryStorage;

import java.util.HashSet;

@Component
@Data
public class CommandManager {

    private final MemoryStorage storage;

    public HashSet<Command> getCommands() {
        return storage.getCommands();
    }
}