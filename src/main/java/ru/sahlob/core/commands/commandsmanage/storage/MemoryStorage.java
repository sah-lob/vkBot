package ru.sahlob.core.commands.commandsmanage.storage;
import lombok.Data;
import org.springframework.stereotype.Component;
import ru.sahlob.core.commands.Anna;
import ru.sahlob.core.commands.VKCommand;
import ru.sahlob.core.commands.Weather;
import ru.sahlob.core.commands.commandsmanage.Command;

import java.util.HashSet;

@Component
@Data
public class MemoryStorage implements Storage {

    private final VKCommand vkCommand;
    private final VKCommand vkCommand2;
    private final VKCommand vkCommand3;
    private final VKCommand vkCommand4;
    private final VKCommand vkCommand5;
    private final VKCommand vkCommand6;
    private final VKCommand vkCommand8;
    private final VKCommand vkCommand9;
    private final VKCommand vkCommand10;
    private final VKCommand vkCommand11;
    private final VKCommand vkCommand12;
    private final VKCommand vkCommand13;
    private final VKCommand vkCommand14;
    private final VKCommand vkCommand15;
    private final VKCommand vkCommand16;
    private final VKCommand vkCommand17;
    private final VKCommand vkCommand18;
    private final VKCommand vkCommand19;
    private final VKCommand vkCommand20;
    private final VKCommand vkCommand21;
    private final Weather weather;
    private final Weather weather2;
    private final Anna anna;
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
        vkCommand8.setName("задроты");
        vkCommand9.setName("задротыс");
        vkCommand10.setName("параноики");
        vkCommand11.setName("едины");
        vkCommand12.setName("утро");
        vkCommand13.setName("удалить");
        vkCommand14.setName("досье");
        vkCommand15.setName("лидеры");
        vkCommand16.setName("лидерыс");
        vkCommand17.setName("даты");
        vkCommand18.setName("команды");
        vkCommand19.setName("чп");
        vkCommand20.setName("пол");
        vkCommand21.setName("статистика");
        commands.add(vkCommand);
        commands.add(vkCommand2);
        commands.add(vkCommand3);
        commands.add(vkCommand4);
        commands.add(vkCommand5);
        commands.add(vkCommand6);
        commands.add(vkCommand8);
        commands.add(vkCommand9);
        commands.add(vkCommand10);
        commands.add(vkCommand11);
        commands.add(vkCommand12);
        commands.add(vkCommand13);
        commands.add(vkCommand14);
        commands.add(vkCommand15);
        commands.add(vkCommand16);
        commands.add(vkCommand17);
        commands.add(vkCommand18);
        commands.add(vkCommand19);
        commands.add(vkCommand20);
        commands.add(vkCommand21);
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
