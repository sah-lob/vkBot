package ru.sahlob.core.modules.vkpeopleparser;

import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import ru.sahlob.core.modules.vkpeopleparser.vkstorage.VKPeopleMemoryStorage;
import ru.sahlob.core.modules.vkpeopleparser.vkstorage.VKPeopleStorage;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public class VKPeopleParser {



    public String onlinePeople() {
        boolean online = personOnline(new Person("sdf"));
        String stringAnswer = "";

        var person = VKPeopleMemoryStorage.getInstance().getPerson("id12275982");
        if (person == null) {
            stringAnswer += "Данный пользовтель почему-то не найден";
        } else {
            stringAnswer += "Данные для мамкиного шпиона: \n\n";
            if (personOnline(person)) {
                stringAnswer += "Данный пользовтель сейчас онлайн";
            } else {
                stringAnswer += "Данный пользовтель сейчас офлайн";
            }

            var personActivites = person.getActivity();
            String duration = "Информации почему-то нет";
            String info = "Информации почему-то нет";
            if (personActivites != null && personActivites.get(person.getTodayActivityKey()) != null) {
                duration = String.valueOf(personActivites.get(person.getTodayActivityKey()).getDuration());
                info = personActivites.get(person.getTodayActivityKey()).getDurationINFO();
                stringAnswer += "Онлайн в течение дня:  " + duration + " мин.";
                stringAnswer += "Информация о посещении: \n" + info;
            } else {
                stringAnswer += "Информации о данном клиенте пока нет";
            }
        }
        return stringAnswer;
    }


    public boolean personOnline(Person person) {
        Document document = null;
        try {
            HttpsURLConnection.setDefaultHostnameVerifier(
                    SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            document = Jsoup.connect("https://vk.com/id12275982").get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(document.text());
        var elements = document.select("span.pp_last_activity_text");
        return elements.text().equals("Online");
    }


    public void updateAllPersons() {
        var storage = VKPeopleMemoryStorage.getInstance();
        var persons = (ArrayList<Person>) storage.getAllPersons();
        for (var p: persons) {
            if (personOnline(p)) {
                System.out.println("Ира онлайн");
                var dateKey = new Date().getYear() + " " + new Date().getMonth() + " " + new Date().getDay();
                var dayActivity = p.getActivity().get(dateKey);
                if (dayActivity == null) {
                    dayActivity = new Activity(dateKey);
                }
                dayActivity.setDuration(dayActivity.getDuration() + 1);
                dayActivity.setDurationINFO(dayActivity.getDurationINFO() + "\n" + new Date().getHours() + new Date().getMinutes());
                p.updateActivity(dateKey, dayActivity);
                System.out.println(p.getActivity().get(dateKey).getDuration() + "\n" + p.getActivity().get(dateKey).getDurationINFO());
            } else {
                System.out.println("Ира не онлайн");
            }
        }
    }
}
