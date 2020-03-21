package ru.sahlob.core.modules.vkpeopleparser.services.single;

import lombok.Data;
import org.springframework.stereotype.Component;
import ru.sahlob.core.modules.vkpeopleparser.domain.DayActivity;
import ru.sahlob.core.modules.vkpeopleparser.models.Person;
import ru.sahlob.core.modules.vkpeopleparser.vkstorage.VKTimeStorage;
import ru.sahlob.core.modules.vkpeopleparser.vkstorage.db.people.MainVKPeopleStorage;
import ru.sahlob.core.modules.vkpeopleparser.vktime.VKTime;
import ru.sahlob.core.observers.ObserversManagement;
import ru.sahlob.vk.TokenInfo;
import ru.sahlob.vk.VKCore;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;

@Component
@Data
public class VKPeopleParser {

    private final MainVKPeopleStorage storage;
    private final VKTimeStorage timeStorage;
    private final ObserversManagement observersManagement;
//    private final TokenInfo tokenInfo;

    public String getInfoAboutPerson(Person person, String date) {
        var stringAnswer = "";
        if (person == null) {
            stringAnswer += "Данный пользовтель почему-то не найден";
        } else {
            stringAnswer += "Данные по пользователю " + person.getAlternativeName() + """
                     для мамкиного шпиона:\040

                    """;
            if (personOnline(person)) {
                stringAnswer += """
                        Данный пользовтель сейчас онлайн.\040

                        """;
            } else {
                stringAnswer += """
                        Данный пользовтель сейчас офлайн.\040

                        """;
            }
            if (date.equals("")) {
                date = VKTime.getDateKey(person.getTimezone());
            }
            var activity = person.getActivityByDate(date);
            if (activity != null) {
                var duration = activity.getTodayDuration() + " мин.";
                var info = activity.getDayActivityInfo();
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
            stringAnswer += "Досье " + person.getAlternativeName() + """
                    :

                    """;
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
        } else {
            answer = "0";
        }
        return Integer.parseInt(answer) == 1;
    }

    public boolean personOnline(String name) {
        var answer = takeGetRequest(name);
        if (!answer.equals("")) {
            answer = answer.substring(answer.lastIndexOf("\"online\":") + 9, answer.lastIndexOf("\"online\":") + 10);
        } else {
            answer = "0";
        }
        return Integer.parseInt(answer) == 1;
    }

    public void updateAllPersons() {
        var persons = (ArrayList<Person>) storage.getAllPersonsWithTodayDayActivity();
        for (var p: persons) {
            var dateKey = VKTime.getDateKey(p.getTimezone());
            var dayActivity = p.getActivity().get(dateKey);
            if (personOnline(p)) {
                sendMessageToWaiters(p);
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
        var result = false;
        var request = takeGetRequest(name);

        if (request.contains("{\"response\":[{")) {

            var altName = request.substring(request.indexOf("\"first_name\":\"") + 14)
                    .replaceAll("\",\"last_name\":\"", " ");
            altName = altName.substring(0, altName.indexOf("\",\"is_closed\""));


            var realId = request.substring(request.indexOf("{\"response\":[{\"id\":") + 19);
            realId = realId.substring(0, realId.indexOf(",\"first_name\":\""));
            result = true;

            var person = new Person(name, altName);
            person.setRealId(Integer.valueOf(realId));
            storage.addPerson(person);
        }

        return result;
    }

    public static String altName(String name) {
        var answ = takeGetRequest(name);
        var result = answ.substring(answ.indexOf("\"first_name\":\"") + 14).replaceAll("\",\"last_name\":\"", " ");
        result = result.substring(0, result.indexOf("\",\"is_closed\""));
        return result;
    }

    public boolean userExistenceCheck(String name) {
        return storage.getPersonWithTodayDayActivity(name) != null;
    }

    private static String takeGetRequest(String name) {
        var url =  "https://api.vk.com/method/users.get?user_ids="
                   + name
                   + "&fields=online&access_token="
                   + VKCore.accessToken + "&v=5.103";
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

    private void sendMessageToWaiters(Person p) {
        if (!p.getExpectingPeople().isEmpty()) {
            observersManagement.sendMessageAllWaiters(p.getExpectingPeople(), p.getAlternativeName());
            p.setExpectingPeople(new HashSet<>());
        }
    }

}
