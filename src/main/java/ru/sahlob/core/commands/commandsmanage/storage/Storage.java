package ru.sahlob.core.commands.commandsmanage.storage;

import org.springframework.stereotype.Component;
import ru.sahlob.core.commands.commandsmanage.Command;

import java.util.HashSet;

@Component
public interface Storage {

    HashSet<Command> getCommands();
}
