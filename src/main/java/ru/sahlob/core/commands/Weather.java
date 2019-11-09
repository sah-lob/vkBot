package ru.sahlob.core.commands;
import ru.sahlob.core.commands.commandsmanage.Command;
import ru.sahlob.core.modules.weather.WeatherParser;

import java.io.IOException;

public class Weather extends Command {

    public Weather(String name) {
        super(name);
    }

    @Override
    public String getMessage() {
        return getWeather();
    }

    private String getWeather() {
        String weather;
        try {
            weather = new WeatherParser().getWeatherTodayDescription();
        } catch (IOException e) {
            weather = "не удалось получить погоду";
        }

        return weather;
    }
}