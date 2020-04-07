package ru.sahlob.core.modules.vkpeopleparser.services.single;

import lombok.Data;
import org.springframework.stereotype.Component;
import ru.sahlob.core.modules.vkpeopleparser.domain.DayActivity;
import ru.sahlob.core.modules.vkpeopleparser.models.Person;
import ru.sahlob.core.modules.vkpeopleparser.vkstorage.VKTimeStorage;
import ru.sahlob.core.modules.vkpeopleparser.vkstorage.db.people.MainVKPeopleStorage;
import ru.sahlob.core.modules.vkpeopleparser.vktime.VKTime;
import ru.sahlob.core.observers.Observer;
import ru.sahlob.core.observers.ObserversManagement;
import ru.sahlob.vk.VKCore;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;

import static ru.sahlob.core.commands.vkcommands.answers.VKTextAnswers.*;

@Component
@Data
public class VKPeopleParser {

    private final MainVKPeopleStorage storage;
    private final VKTimeStorage timeStorage;
    private final ObserversManagement observersManagement;

    public String getInfoAboutPerson(Person person, String date) {

        var stringAnswer = "";

        if (person == null) {
            stringAnswer += PERSON_NOT_ADDED;
        } else {
            stringAnswer += DATA_FOR_THE_USER + WSP + person.getAlternativeName() + TWO_NL;

            var firstName = person.getAlternativeName()
                    .substring(0, person.getAlternativeName().indexOf(WSP));

            stringAnswer += firstName + WSP + (personOnline(person) ? NOW_ONLINE : NOW_OFFLINE) + TWO_NL;

            if (date.equals("")) {
                date = VKTime.getDateKey(person.getTimezone());
            }
            var activity = person.getActivityByDate(date);
            if (activity != null) {
                var duration = activity.getTodayDuration() + WSP + MIN;
                var info = activity.getDayActivityInfo();
                if (info.equals("")) {
                    info = firstName + WSP + TODAY_NOT_IN_VK + NL;
                }
                stringAnswer += ONLINE_PER_DAY + ":" + WSP + duration + WSP + MIN + NL;
                stringAnswer += INFORMATION_ABOUT_VISITS + ":" + NL + info;
            } else {
                stringAnswer += NL + DATA_HAS_NOT_YET_BEEN_COLLECTED;
            }
        }
        return stringAnswer;
    }

    public String getInfoAboutPersonsRecords(Person person) {
        var stringAnswer = "";
        if (person == null) {
            stringAnswer += PERSON_NOT_ADDED;
        } else {
            stringAnswer += DOSSIER + WSP + person.getAlternativeName() + ":" + TWO_NL;
            stringAnswer += MAX_TIME_PER_DAY + ":" + WSP + person.getRecordDurationAllTime() + WSP + MIN + NL;
            stringAnswer += AVG_TIME_PER_DAY + ":" + WSP + person.getAvgDurationAllTime() + WSP + MIN + NL;
            stringAnswer += USER_DATA_COLLECTED_OVER + ":" + WSP + person.getAllTimeDaysCount() + " суток.";
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
        var persons = (ArrayList<Person>) storage.getAllPersonsWithTodayDayActivity(null);
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
            storage.editPerson(null, p);
        }
    }

    public Person addNewPerson(String name) {

        var request = takeGetRequest(name);

        if (request.contains("{\"response\":[{")) {

            var altName = request.substring(request.indexOf("\"first_name\":\"") + 14)
                    .replaceAll("\",\"last_name\":\"", " ");
            altName = altName.substring(0, altName.indexOf("\",\"is_closed\""));

            var realId = request.substring(request.indexOf("{\"response\":[{\"id\":") + 19);
            realId = realId.substring(0, realId.indexOf(",\"first_name\":\""));
            var person = new Person(name, altName);
            person.setRealId(Integer.valueOf(realId));
            storage.addPerson(person);
            return person;
        }

        return null;
    }

    public static String altName(String name) {
        var answ = takeGetRequest(name);
        var result = answ.substring(answ.indexOf("\"first_name\":\"") + 14).replaceAll("\",\"last_name\":\"", " ");
        result = result.substring(0, result.indexOf("\",\"is_closed\""));
        return result;
    }

    public boolean userExistenceCheck(Observer observer, String name) {
        return storage.getPersonWithTodayDayActivity(observer, name) != null;
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
