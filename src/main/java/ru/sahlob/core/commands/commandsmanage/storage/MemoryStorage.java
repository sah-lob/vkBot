package ru.sahlob.core.commands.commandsmanage.storage;

import org.springframework.context.annotation.Configuration;
import ru.sahlob.core.commands.vkcommands.VKCommandController;
import ru.sahlob.core.commands.commandsmanage.Command;
import ru.sahlob.core.commands.commandsmanage.storage.commands.VkCommands;

import java.util.*;
import java.util.stream.Collectors;

@Configuration
public class MemoryStorage implements Storage {

    private static HashSet<Command> commands = new HashSet<>();

    public MemoryStorage(VKCommandController vkCommandController) {
        vkCommandController.addAllNames(
                Arrays.stream(VkCommands.values())
                        .map(Enum::toString)
                        .collect(Collectors.toList()));
        commands.addAll(List.of(vkCommandController));
    }

    @Override
    public HashSet<Command> getCommands() {
        return commands;
    }
}
