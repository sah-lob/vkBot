package ru.sahlob.core.modules;

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

    public String annaWeather() {
        Document document = null;
        try {
            document = Jsoup.connect("https://yandex.ru/pogoda/moscow/details?via=ms").get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        var elements = document.select("div.weather-table__wrapper");
        var element2 = document.select("td.weather-table__body-cell.weather-table__body-cell_type_condition");
        var tucha = element2.html().split("\n");
        var s = elements.text().replaceAll("утром", " QAZутром").split("QAZ")[1];
        var ns = s.split(" ");
        int k = 0;
        var ss = "Погода сегодня: \n";
        ss = ss + "\n Утро: ";
        int num1 = 0;
        int num2 = 0;
        for (int i = 0; i < ns.length; i++) {
            if (k == 1) {
                num1 = Integer.parseInt(ns[i].substring(0, ns[i].indexOf("°")));
            } else if (k == 2) {
                System.out.println(ns[i]);
                num2 = Integer.parseInt(ns[i].substring(0, ns[i].indexOf("°")));
            } else if (k == 3) {
                double d = (Double.valueOf(num1) + Double.valueOf(num2)) / Double.valueOf(2);
                ss = ss + d + "°";
            }
            k++;
            if (k == 5) {
                if (i == 4) {
                    ss = ss + "  " + tucha[0];
                    ss = ss + "\n День: ";
                } else if (i == 9) {
                    ss = ss + "  " + tucha[1];
                    ss = ss + "\n Вечер: ";
                } else if (i == 14) {
                    ss = ss + "  " + tucha[2];
                    ss = ss + "\n Ночь: ";
                }
                k = 0;
            }
        }
        ss = ss + "  " + tucha[3];
        return ss;
    }

    public String sunsetValue() {
        Document document = null;
        try {
            document = Jsoup.connect("https://yandex.ru/pogoda/moscow/details?via=ms").get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Elements elements = document.select("dd.sunrise-sunset__value");
        var s = elements.text();
        System.out.println(s);
        return s;
    }

    public String getWeatherTodayDescription() {
        Elements elements = doc.select("span.dw-into");
        return elements.text().split("Подробнее")[0];
    }

}