package ru.sahlob.core.commands.commandsmanage.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.sahlob.core.commands.Anna;
import ru.sahlob.core.commands.VKCommand;
import ru.sahlob.core.commands.Weather;
import ru.sahlob.core.commands.commandsmanage.Command;

import java.util.HashSet;

@Component
public class MemoryStorage implements Storage {

    @Autowired
    private VKCommand vkCommand;
    @Autowired
    private VKCommand vkCommand2;
    @Autowired
    private VKCommand vkCommand3;
    @Autowired
    private VKCommand vkCommand4;
    @Autowired
    private VKCommand vkCommand5;
    @Autowired
    private VKCommand vkCommand6;
    @Autowired
    private VKCommand vkCommand7;
    @Autowired
    private Weather weather;
    @Autowired
    private Weather weather2;
    @Autowired
    private Anna anna;


    private static HashSet<Command> commands = new HashSet<>();

    public void updateCommands() {
        weather.setName("weather");
        weather2.setName("погода");
        anna.setName("аня");
        vkCommand.setName("шпионим");
        vkCommand2.setName("look");
        vkCommand3.setName("следить");
        vkCommand4.setName("м");
        vkCommand5.setName("и");
        vkCommand6.setName("b");
        vkCommand7.setName("белка");
        commands.add(vkCommand);
        commands.add(vkCommand2);
        commands.add(vkCommand3);
        commands.add(vkCommand4);
        commands.add(vkCommand5);
        commands.add(vkCommand6);
        commands.add(vkCommand7);
        commands.add(weather);
        commands.add(weather2);
        commands.add(anna);
    }

    @Override
    public HashSet<Command> getCommands() {
        return commands;
    }

    @Override
    public void addCommnad(Command command) {

    }
}
