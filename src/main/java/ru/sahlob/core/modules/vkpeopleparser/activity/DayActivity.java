package ru.sahlob.core.modules.vkpeopleparser.activity;
import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "days")
public class DayActivity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long person;
    private int duration;
    private String durationINFO;
    private String key;

    @Transient
    private List<MinuteActivity> dayActivities = new ArrayList<>();

    private int timezone;

    public DayActivity() {
    }

    public DayActivity(int timezone) {
        this.duration = 0;
        this.durationINFO = "";
        this.timezone = timezone;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }


    public String getDurationINFO() {
        return durationINFO;
    }

    public void setDurationINFO(String durationINFO) {
        this.durationINFO = durationINFO;
    }


    public List<MinuteActivity> getDayActivities() {
        return dayActivities;
    }

    public void setDayActivities(List<MinuteActivity> dayActivities) {
        this.dayActivities = dayActivities;
    }

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getTimezone() {
        return timezone;
    }

    public void setTimezone(int timezone) {
        this.timezone = timezone;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DayActivity that = (DayActivity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public Long getPerson() {
        return person;
    }

    public void setPerson(Long person) {
        this.person = person;
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
