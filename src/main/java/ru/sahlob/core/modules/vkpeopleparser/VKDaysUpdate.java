package ru.sahlob.core.modules.vkpeopleparser;
import org.springframework.stereotype.Component;
import ru.sahlob.core.modules.vkpeopleparser.vkstorage.VKTimeStorage;
import ru.sahlob.core.modules.vkpeopleparser.vkstorage.db.people.MainVKPeopleStorage;
import ru.sahlob.core.modules.vkpeopleparser.vkstorage.db.time.VKTimeKey;
import ru.sahlob.core.modules.vkpeopleparser.vktime.VKTime;

@Component
public class VKDaysUpdate {

    private final MainVKPeopleStorage storage;
    private final VKTimeStorage timeStorage;

    public VKDaysUpdate(MainVKPeopleStorage storage, VKTimeStorage timeStorage) {
        this.storage = storage;
        this.timeStorage = timeStorage;
    }


    public void dayUpdate(String day) {
        updatePeoplesRecordes(day);
        updateDayTimer();
    }

    public void updatePeoplesRecordes(String day) {
        var persons = storage.getAllPersonsWithDayActivityByDate(day);

        for (var p: persons) {
            p.setAllTimeDaysCount(0);
                    p.setRecordDurationAllTime(0);
                    p.setAvgDurationAllTime(0);
            storage.editPerson(p);
        }
    }

//    public void updatePeoplesRecordes(String day) {
//        var persons = storage.getAllPersonsWithDayActivityByDate(day);
//        for (var p: persons) {
//            var recordDurationAlltime = storage.getPersonWithDayActivityByDate(p.getName(), day).getRecordDurationAllTime();
//            var addDaysCount = storage.getPersonWithDayActivityByDate(p.getName(), day).getAllTimeDaysCount();
//            var avgDuration = storage.getPersonWithDayActivityByDate(p.getName(), day).getAvgDurationAllTime();
//            var dayActivity = p.getActivityByDate(day);
//            if (dayActivity != null) {
//                var dayDuration = dayActivity.getTodayDuration();
//                if (recordDurationAlltime != null && addDaysCount != null && avgDuration != null) {
//                    if (recordDurationAlltime > p.getRecordDurationAllTime()) {
//                        p.setRecordDurationAllTime(recordDurationAlltime);
//                    }
//                    if (avgDuration < 0) {
//                        avgDuration = 0;
//                    }
//                    if (addDaysCount < 0) {
//                        addDaysCount = 0;
//                    }
//                    p.setAvgDurationAllTime(((avgDuration * addDaysCount) + dayDuration) / (addDaysCount + 1));
//                    p.setAllTimeDaysCount(addDaysCount + 1);
//                } else {
//                    p.setRecordDurationAllTime(0);
//                    p.setAvgDurationAllTime(0);
//                    p.setAllTimeDaysCount(0);
//                }
//            } else {
//                if (recordDurationAlltime != null && addDaysCount != null && avgDuration != null) {
//                    p.setAllTimeDaysCount(addDaysCount + 1);
//                    p.setAvgDurationAllTime((avgDuration * addDaysCount) / (addDaysCount + 1));
//                } else {
//                    p.setAllTimeDaysCount(0);
//                    p.setRecordDurationAllTime(0);
//                    p.setAvgDurationAllTime(0);
//                }
//            }
//            storage.editPerson(p);
//        }
//    }

    private void updateDayTimer() {
        var vkTimeKey = new VKTimeKey();
        vkTimeKey.setTimeKey(VKTime.getDateKey(3));
        timeStorage.addNewTime(vkTimeKey);
        if (timeStorage.getTimeCount() >= 7) {
            var key = timeStorage.deleteFirst();
            storage.deleteAllDayAndMinutesActivitiesByDay(key);
        }
    }
}