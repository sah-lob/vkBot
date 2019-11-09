package ru.sahlob.core.commands.commandsmanage;

public abstract class Command {

    private final String name;

    public Command(String name) {
        this.name = name;
    }

    public abstract String getMessage();

    public String getName() {
        return name;
    }
}