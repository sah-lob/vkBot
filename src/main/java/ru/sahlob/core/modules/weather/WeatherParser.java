package ru.sahlob.core.modules.weather;

import org.apache.commons.lang3.ArrayUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import java.io.IOException;

import static java.lang.System.*;

public class WeatherParser {

    private Document doc;

    public WeatherParser() throws IOException {
        String city = "saint_petersburg";
        doc = Jsoup.connect(String.format("https://world-weather.ru/pogoda/russia/%s/", city)).get();
    }

    String[] parsingTodayWeatherFromYan() {
        Document document = null;
        try {
            document = Jsoup.connect("https://yandex.ru/pogoda/moscow/details?via=" + "ms").get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert document != null;
        var elements = document.select("div.weather-table__wrapper");
        var s = elements.text().replaceAll("утром", " QAZутром").split("QAZ")[1];

        String replacementChar = "!!";
        s = s.replaceAll("утром", replacementChar)
                .replaceAll("днём", replacementChar)
                .replaceAll("вечером", replacementChar)
                .replaceAll("ночью", replacementChar);
        var s2 = s.split(replacementChar);
        var clouds = parsingTodayClouds(document);

        return ArrayUtils.addAll(s2, clouds);
    }

    private String[] parsingTodayClouds(Document doc) {
        var allClouds = parsingAllClouds(doc);
        var todayClouds = new String[4];
        arraycopy(allClouds, 0, todayClouds, 0, 4);
        return todayClouds;
    }

    private String[] parsingAllClouds(Document doc) {
        var element2 = doc.select("td.weather-table__body-cell.weather-table__body-cell_type_condition");
        return element2.html().split("\n");
    }

    public String getWeatherTodayDescription() {
        Elements elements = doc.select("span.dw-into");
        return elements.text().split("Подробнее")[0];
    }

}