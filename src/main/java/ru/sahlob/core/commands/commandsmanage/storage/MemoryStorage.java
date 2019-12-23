package ru.sahlob.core.commands.commandsmanage.storage;
import org.springframework.stereotype.Component;
import ru.sahlob.core.commands.Anna;
import ru.sahlob.core.commands.VKCommand;
import ru.sahlob.core.commands.Weather;
import ru.sahlob.core.commands.commandsmanage.Command;

import java.util.HashSet;

@Component
public class MemoryStorage implements Storage {

    private final VKCommand vkCommand;
    private final VKCommand vkCommand2;
    private final VKCommand vkCommand3;
    private final VKCommand vkCommand4;
    private final VKCommand vkCommand5;
    private final VKCommand vkCommand6;
    private final VKCommand vkCommand7;
    private final VKCommand vkCommand8;
    private final Weather weather;
    private final Weather weather2;
    private final Anna anna;


    private static HashSet<Command> commands = new HashSet<>();

    public MemoryStorage(VKCommand vkCommand, VKCommand vkCommand2, VKCommand vkCommand3, VKCommand vkCommand4, VKCommand vkCommand5, VKCommand vkCommand6, VKCommand vkCommand7, Weather weather, Weather weather2, Anna anna, VKCommand vkCommand8) {
        this.vkCommand = vkCommand;
        this.vkCommand2 = vkCommand2;
        this.vkCommand3 = vkCommand3;
        this.vkCommand4 = vkCommand4;
        this.vkCommand5 = vkCommand5;
        this.vkCommand6 = vkCommand6;
        this.vkCommand7 = vkCommand7;
        this.vkCommand8 = vkCommand8;
        this.weather = weather;
        this.weather2 = weather2;
        this.anna = anna;
    }

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
        vkCommand8.setName("задроты");
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
