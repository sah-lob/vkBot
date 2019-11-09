package ru.sahlob.core.commands;
import ru.sahlob.core.commands.commandsmanage.Command;

public class Unknown extends Command {

    public Unknown(String name) {
        super(name);
    }

    @Override
    public String getMessage() {
        return "Такой команды нет, возможно позжеЙ";
    }


}