package ru.sahlob.core.modules.vkpeopleparser.services.multi;
import lombok.Data;
import org.springframework.stereotype.Component;
import ru.sahlob.core.modules.vkpeopleparser.vkstorage.VKTimeStorage;
import ru.sahlob.core.modules.vkpeopleparser.vkstorage.db.people.MainVKPeopleStorage;
import ru.sahlob.core.modules.vkpeopleparser.vkstorage.db.time.VKTimeKey;
import ru.sahlob.core.modules.vkpeopleparser.vktime.VKTime;
import ru.sahlob.core.observers.Observer;

@Component
@Data
public class VKDaysUpdate {

    private final MainVKPeopleStorage storage;
    private final VKTimeStorage timeStorage;

    public void dayUpdate(String day) {
        updatePeoplesRecordes(null, day);
        updateDayTimer(null);
    }

    private void updatePeoplesRecordes(Observer observer, String day) {
        var persons = storage.getAllPersonsWithDayActivityByDate(null, day);
        for (var p: persons) {
            var recordDurationAlltime = p.getRecordDurationAllTime();
            var addDaysCount = p.getAllTimeDaysCount();
            var avgDuration = p.getAvgDurationAllTime();
            var dayActivity = p.getActivityByDate(day);
            if (dayActivity != null) {
                var dayDuration = dayActivity.getTodayDuration();
                if (recordDurationAlltime != null && addDaysCount != null && avgDuration != null) {
                    if (recordDurationAlltime < dayDuration) {
                        p.setRecordDurationAllTime(dayDuration);
                    }
                    if (avgDuration < 0) {
                        avgDuration = 0;
                    }
                    if (addDaysCount < 0) {
                        addDaysCount = 0;
                    }
                    p.setAvgDurationAllTime(((avgDuration * addDaysCount) + dayDuration) / (addDaysCount + 1));
                    p.setAllTimeDaysCount(addDaysCount + 1);
                } else {
                    p.setRecordDurationAllTime(0);
                    p.setAvgDurationAllTime(0);
                    p.setAllTimeDaysCount(0);
                }
            } else {
                if (recordDurationAlltime != null && addDaysCount != null && avgDuration != null) {
                    p.setAllTimeDaysCount(addDaysCount + 1);
                    p.setAvgDurationAllTime((avgDuration * addDaysCount) / (addDaysCount + 1));
                } else {
                    p.setAllTimeDaysCount(0);
                    p.setRecordDurationAllTime(0);
                    p.setAvgDurationAllTime(0);
                }
            }
            storage.editPerson(observer, p);
        }
    }

    private void updateDayTimer(Observer observer) {
        var vkTimeKey = new VKTimeKey();
        vkTimeKey.setTimeKey(VKTime.getDateKey(3));
        timeStorage.addNewTime(vkTimeKey);
        if (timeStorage.getTimeCount() >= 7) {
            var key = timeStorage.deleteFirst();
            storage.deleteAllDayAndMinutesActivitiesByDay(observer, key);
        }
    }
}