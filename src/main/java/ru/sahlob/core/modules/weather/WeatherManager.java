package ru.sahlob.core.modules.weather;
import java.io.IOException;

public class WeatherManager {

    private WeatherParser weatherParser;

    {
        try {
            weatherParser = new WeatherParser();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String todayWeatherForm() {
        var todayWeather = weatherParser.parsingTodayWeatherFromYan("ms");
        String weatherForm = "Погода сегодня!\n";
        double maxWind = 0;
        StringBuilder wheatherString = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            if (i == 0) {
                wheatherString.append("Утро: ");
            } else if (i == 1) {
                wheatherString.append("День: ");
            } else if (i == 2) {
                wheatherString.append("Вечер: ");
            } else {
                wheatherString.append("Ночь: ");
            }

            var partOfTodayWeather = todayWeather[i + 1].split(" ");

            int num1;
            int num2;

            if (partOfTodayWeather.length == 4) {
                num1 = Integer.parseInt(partOfTodayWeather[1].substring(0, partOfTodayWeather[1].indexOf("°")));
                num2 = num1;
                double wind = Double.parseDouble(partOfTodayWeather[2].replace(",", "."));
                if (maxWind < wind) {
                    maxWind = wind;
                }
            } else {
                num1 = Integer.parseInt(partOfTodayWeather[1].substring(0, partOfTodayWeather[1].indexOf("°")));
                num2 = Integer.parseInt(partOfTodayWeather[2].substring(0, partOfTodayWeather[2].indexOf("°")));
                double wind = Double.parseDouble(partOfTodayWeather[3].replace(",", "."));
                if (maxWind < wind) {
                    maxWind = wind;
                }
            }
            int finalTemperature = (int) (((double) num1 + (double) num2) / 2d);
            wheatherString.append(finalTemperature).append("°  ").append(todayWeather[i + 5]).append("\n");
        }
        if (maxWind > 6) {
            weatherForm += "\n Сегодня будет сильный ветер! \n";
        }
        weatherForm += wheatherString;
        return weatherForm;
    }
}
