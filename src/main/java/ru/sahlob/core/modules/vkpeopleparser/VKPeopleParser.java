package ru.sahlob.core.modules.vkpeopleparser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import ru.sahlob.core.modules.vkpeopleparser.vkstorage.VKPeopleMemoryStorage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public class VKPeopleParser {



    public String onlinePeople() {
        System.out.println(personOnline(new Person("sdf")));
//        System.out.println(new Date().toGMTString());
        return "воу воу полегче";
    }


    public boolean personOnline(Person person) {

        Document document = null;
        try {
//            document = Jsoup.connect("https://vk.com/opkeks").get();
            document = Jsoup.connect("https://vk.com/id12275982").get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        var elements = document.select("span.pp_last_activity_text");
        return elements.text().equals("Online");
    }


    public void updateAllPersons() {
        var storage = VKPeopleMemoryStorage.getInstance();
        var persons = (ArrayList<Person>) storage.getAllPersons();
        for (var p: persons) {
            if (personOnline(p)) {
                var dateKey = new Date().getYear() + " " + new Date().getMonth() + " " + new Date().getDay();
                var dayActivity = p.getActivity().get(dateKey);
                if (dayActivity == null) {
                    dayActivity = new Activity(dateKey);
                }
                dayActivity.setDuration(dayActivity.getDuration() + 1);
                dayActivity.setDurationINFO(dayActivity.getDurationINFO() + "\n" + new Date().getHours() + new Date().getMinutes());
                p.updateActivity(dateKey, dayActivity);
            }
            p.getName();
        }
    }
}
