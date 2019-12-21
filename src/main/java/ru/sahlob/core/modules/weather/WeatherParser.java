package ru.sahlob.core.modules.weather;

import org.apache.commons.lang3.ArrayUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import java.io.IOException;

public class WeatherParser {

    private String city = "saint_petersburg";
    private Document doc;

    public WeatherParser() throws IOException {
        doc = Jsoup.connect(String.format("https://world-weather.ru/pogoda/russia/%s/", city)).get();
    }
    public WeatherParser(String city) throws IOException {
        this.city = city;
        doc = Jsoup.connect(String.format("https://world-weather.ru/pogoda/russia/%s/", city)).get();
    }

    public String[] parsingTodayWeatherFromYan(String city) {
        Document document = null;
        try {
            document = Jsoup.connect("https://yandex.ru/pogoda/moscow/details?via=" + city).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        var elements = document.select("div.weather-table__wrapper");
        var s = elements.text().replaceAll("утром", " QAZутром").split("QAZ")[1];

        String replacementChar = "!!";
        s = s.replaceAll("утром", replacementChar)
                .replaceAll("днём", replacementChar)
                .replaceAll("вечером", replacementChar)
                .replaceAll("ночью", replacementChar);
        var s2 = s.split(replacementChar);
        var clouds = parsingTodayClouds(document);

        var k = ArrayUtils.addAll(s2, clouds);
        return k;
    }

    public String[] parsingTodayClouds(Document doc) {
        var allClouds = parsingAllClouds(doc);
        var todayClouds = new String[4];
        for (int i = 0; i < 4; i++) {
            todayClouds[i] = allClouds[i];
        }
        return todayClouds;
    }

    public String[] parsingAllClouds(Document doc) {
        var element2 = doc.select("td.weather-table__body-cell.weather-table__body-cell_type_condition");
        return element2.html().split("\n");
    }

    public String sunsetValue() {
        Document document = null;
        try {
            document = Jsoup.connect("https://yandex.ru/pogoda/moscow/details?via=ms").get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return document.select("dd.sunrise-sunset__value").text();
    }

    public String getWeatherTodayDescription() {
        Elements elements = doc.select("span.dw-into");
        return elements.text().split("Подробнее")[0];
    }

}