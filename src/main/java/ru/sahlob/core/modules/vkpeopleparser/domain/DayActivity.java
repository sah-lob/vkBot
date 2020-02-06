package ru.sahlob.core.modules.vkpeopleparser.domain;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "days")
@Data
@EqualsAndHashCode(of = {"id"})
@NoArgsConstructor
@RequiredArgsConstructor
public class DayActivity implements Serializable {

    @Id @GeneratedValue(strategy = GenerationType.AUTO) private Long id;
    private Long person;
    private int duration = 0;
    private String durationINFO = "";
    private String key;
    @Transient private List<MinuteActivity> dayActivities = new ArrayList<>();
    @NonNull  private int timezone;

    public void addNewDayActivities(MinuteActivity minuteActivity) {
        dayActivities.add(minuteActivity);
    }

    public void incrementDurationOfMinuteActivities() {
        if (dayActivities.size() == 0) {
            addNewMinuteActivity();
        }
        dayActivities.get(dayActivities.size() - 1).incrementDuration();
    }

    public void addNewMinuteActivity() {
        dayActivities.add(new MinuteActivity(timezone));
    }

    public void delete4MinFromMinuteActivity() {
        if (dayActivities.size() > 0) {
            dayActivities.get(dayActivities.size() - 1).delete4Min();
        }
    }

    public int getTodayDuration() {
        var duration = 0;
        if (dayActivities.size() == 0) {
            return 0;
        }
        for (var minuteAct: dayActivities) {
            duration += minuteAct.getDuration();
        }
        return duration;
    }

    public String getDayActivityInfo() {
        var dayActivityInfo = "";
        if (!dayActivities.isEmpty()) {
            for (var activity: dayActivities) {
                dayActivityInfo += activity.getStartTime() + " в течение " + activity.getDuration() + "мин.\n";
            }
        }
        return dayActivityInfo;
    }

    public static final Comparator<DayActivity> COMPARE_BY_DURATION = (lhs, rhs) -> rhs.getTodayDuration() - lhs.getTodayDuration();

    public static final Comparator<DayActivity> COMPARE_BY_AVG_DURATION = (lhs, rhs) -> {
        var rAllDuration = rhs.getTodayDuration();
        var rcount = rhs.dayActivities.size();
        var lAllDuration = lhs.getTodayDuration();
        var lcount = lhs.dayActivities.size();

        if (rcount ==  0 && lcount == 0) {
            return 0;
        } else if (rcount == 0) {
            return -1;
        } else if (lcount == 0) {
            return 1;
        } else {
            return (rAllDuration / rcount) - (lAllDuration / lcount);
        }
    };

    public static final Comparator<DayActivity> COMPARE_BY_SESSION_COUNT = (lhs, rhs) -> rhs.getDayActivities().size() - lhs.getDayActivities().size();
}
