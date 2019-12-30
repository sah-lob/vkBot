package ru.sahlob.core.commands;

import com.vk.api.sdk.objects.messages.Message;
import org.springframework.stereotype.Component;
import ru.sahlob.core.commands.commandsmanage.Command;
import ru.sahlob.core.modules.TrafficJamParser;
import ru.sahlob.core.modules.weather.WeatherManager;

@Component
public class Anna extends Command {

    @Override
    public String getMessage(Message message) {

        var vet = "";
        vet =  new WeatherManager().todayWeatherForm();

        String bus = "https://yandex.ru/maps/api/router/buildRoute?ajax=1&clientTimezoneOffset=-180&csrfToken="
                + "&lang=ru&locale=ru_RU&pctx=djF8MzcuNzg5MDUyLDU1Ljc0OTE0NzszNy43ODg3NDYsNTUuNzQ5MDkxOzM3Ljc4ODQ1OSw1NS43ND"
                + "kwNDszNy43ODgxNDQsNTUuNzQ4OTg0OzM3Ljc4NzgzLDU1Ljc0ODkyOTszNy43ODc1MzQsNTUuNzQ4ODc4fDM3Ljc4ODU1NCw1NS43NDkzM"
                + "jUsNTY3MDk3MjRfMF8sMC44NDszNy43ODc0NzEsNTUuNzQ4NjM3LDU2NzA5NzI0XzEsMC4xMTszNy43ODg1NjUsNTUuNzQ5ODEsNTY3MDk"
                + "3MjRfMiwwLjA0OzM3Ljc4OTg4OSw1NS43NDg4MjcsNTY3MDk3MjRfMywwLjAy~&"
                + "regionId=213&results=6&rll=37.788261%2C55.748979~37.936756472695706%2C55.75036540725625&sessionId=1572950182301_711371&timeDependent%5Btime%5D=now&timeDependent%5Btype%5D=departure&timeDependentTime=1572950231810&timeDependentType=departure&type=masstransit";
        var answer = "Врямя до Анькиной работы: \n\n"
                + "Время на транспорте:" + new TrafficJamParser().getBusTime(bus);
        answer = answer + "\n";
        String auto = "https://yandex.ru/maps/api/router/buildRoute?ajax=1&avoid=tolls&clientTimezoneOffset=-180&csrfToken="
                + "&disableMasstransitColorThread=false&lang=ru&locale=ru_RU&mode=best&pctx=djF8MzcuNzg5MDUyLDU1Ljc0OTE0NzszNy43ODg"
                + "3NDYsNTUuNzQ5MDkxOzM3Ljc4ODQ1OSw1NS43NDkwNDszNy43ODgxNDQsNTUuNzQ4OTg0OzM3Ljc4NzgzLDU1Ljc0ODkyOTszNy43ODc1MzQsN"
                + "TUuNzQ4ODc4fDM3Ljc4ODU1NCw1NS43NDkzMjUsNTY3MDk3MjRfMF8sMC44NDszNy43ODc0NzEsNTUuNzQ4NjM3LDU2NzA5NzI0XzEsMC4xMTsz"
                + "Ny43ODg1NjUsNTUuNzQ5ODEsNTY3MDk3MjRfMiwwLjA0OzM3Ljc4OTg4OSw1NS43NDg4MjcsNTY3MDk3MjRfMywwLjAy~&regionId=213&resul"
                + "ts=6&rll=37.78832%2C55.748988~37.936767%2C55.750365&sessionId=1572952274997_967828&type=auto";
        answer = answer + "Время на автомабиле: " + new TrafficJamParser().getAutoTime(auto) + "\n";
        answer = answer + "\n" + vet;
        return answer;
    }

    @Override
    public String info() {
        return null;
    }
}
