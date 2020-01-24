package ru.sahlob.core.modules.vkpeopleparser;
import lombok.Data;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;
import ru.sahlob.core.modules.vkpeopleparser.domain.DayActivity;
import ru.sahlob.core.modules.vkpeopleparser.vkstorage.db.people.MainVKPeopleStorage;
import ru.sahlob.core.modules.vkpeopleparser.vkstorage.VKTimeStorage;
import ru.sahlob.core.modules.vkpeopleparser.vktime.VKTime;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

@Component
@Data
public class VKPeopleParser {

    private final MainVKPeopleStorage storage;
    private final VKTimeStorage timeStorage;

    public String getInfoAboutPerson(Person person, String date) {
        var stringAnswer = "";
        if (person == null) {
            stringAnswer += "Данный пользовтель почему-то не найден";
        } else {
            stringAnswer += "Данные по пользователю " + person.getAlternativeName() + " для мамкиного шпиона: \n\n";
            if (personOnline(person)) {
                stringAnswer += "Данный пользовтель сейчас онлайн. \n\n";
            } else {
                stringAnswer += "Данный пользовтель сейчас офлайн. \n\n";
            }
            if (date.equals("")) {
                date = VKTime.getDateKey(person.getTimezone());
            }
            DayActivity activity = person.getActivityByDate(date);
            if (activity != null) {
                String duration = activity.getTodayDuration() + " мин.";
                String info = activity.getDayActivityInfo();
                if (info.equals("")) {
                    info = "Данный пользователь сегодня не сидел вк";
                }

                stringAnswer += "Онлайн в течение дня:  " + duration + "\n";
                stringAnswer += "Информация о посещении: \n" + info;
            } else {
                stringAnswer += "\nДанный пока не собраны=(";
            }
        }
        return stringAnswer;
    }

    public String getInfoAboutPersonsRecords(Person person) {
        var stringAnswer = "";
        if (person == null) {
            stringAnswer += "Данный пользовтель почему-то не найден";
        } else {
            stringAnswer += "Досье " + person.getAlternativeName() + ":\n\n";
            stringAnswer += "Максимальное время онлайн за день: " + person.getRecordDurationAllTime() + " мин.\n";
            stringAnswer += "Среднее время онлайн за день: " + person.getAvgDurationAllTime() + " мин.\n";
            stringAnswer += "Данные по пользователю собираются в течение " + person.getAllTimeDaysCount()  + " суток.";
        }
        return stringAnswer;
    }

    private boolean personOnline(Person person) {
        var answer = takeGetRequest(person.getName());
        if (!answer.equals("")) {
            answer = answer.substring(answer.lastIndexOf("\"online\":") + 9, answer.lastIndexOf("\"online\":") + 10);
        }
        return Integer.parseInt(answer) == 1;
    }

    public void updateAllPersons() {
        var persons = (ArrayList<Person>) storage.getAllPersonsWithTodayDayActivity();
        for (var p: persons) {
            var dateKey = VKTime.getDateKey(p.getTimezone());
            var dayActivity = p.getActivity().get(dateKey);
            if (personOnline(p)) {
                if (dayActivity == null) {
                    dayActivity = new DayActivity(p.getTimezone());
                }

                dayActivity.setDuration(dayActivity.getDuration() + 1);

                if (p.isActive()) {
                    dayActivity.incrementDurationOfMinuteActivities();
                } else {
                    p.setActive(true);
                    dayActivity.addNewMinuteActivity();
                }
                p.updateTodayActivity(dayActivity);
            } else {
                if (p.isActive()) {
                    if (dayActivity != null) {
                        dayActivity.delete4MinFromMinuteActivity();
                        p.updateTodayActivity(dayActivity);
                    }
                    p.setActive(false);
                } else {
                    continue;
                }
            }
            storage.editPerson(p);
        }
    }

    public boolean addNewPerson(String name) {
        Document document = null;
        try {
            if (!name.contains("id") && name.matches("[0-9]+")) {
                name = "id" + name;
            }
            HttpsURLConnection.setDefaultHostnameVerifier(
                    SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            document = Jsoup.connect("https://vk.com/" + name).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        var result = false;
        if (document != null) {
            name = name.replace("id", "");
            storage.addPerson(name);
            result = true;
        }
        return result;
    }

    public static String altName(String name) {
        String answ = takeGetRequest(name);
        String result = answ.substring(answ.indexOf("\"first_name\":\"") + 14).replaceAll("\",\"last_name\":\"", " ");
        result = result.substring(0, result.indexOf("\",\"is_closed\""));
        return result;
    }

    public boolean userExistenceCheck(String name) {
        return storage.getPersonWithTodayDayActivity(name) != null;
    }

    private static String takeGetRequest(String name) {
        var url =  "https://api.vk.com/method/users.get?user_ids=" + name
                + "&fields=online&access_token=45e239cc08f664008a981a57052505d7afa58bd0843e102ce69d5ef432c374fe7bcb6a191e101d255c940&v=5.103";
        var answer = "";
        try {
            var obj = new URL(url);
            var connection = (HttpURLConnection) obj.openConnection();
            connection.setRequestMethod("GET");
            var in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            answer = response.toString();
        } catch (Exception ignored) {
        }
        return answer;
    }

}
