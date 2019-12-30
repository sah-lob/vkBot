package ru.sahlob.core.commands;
import com.vk.api.sdk.objects.messages.Message;
import org.springframework.stereotype.Component;
import ru.sahlob.core.commands.commandsmanage.Command;
import ru.sahlob.core.modules.weather.WeatherParser;

import java.io.IOException;

@Component
public class Weather extends Command {

    @Override
    public String getMessage(Message message) {
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

    @Override
    public String info() {
        return "";
    }
}