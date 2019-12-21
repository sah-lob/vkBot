package ru.sahlob.core.modules.vkpeopleparser.activity;
import ru.sahlob.core.modules.vkpeopleparser.vktime.VKTime;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "minutes")
public class MinuteActivity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long dayActivity;
    private String startTime;
    private int duration;


    public MinuteActivity() {

    }


    public MinuteActivity(int timezone) {
        this.startTime = VKTime.getMinuteActivity(timezone);
        this.duration = 1;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void incrementDuration() {
        duration++;
    }

    public void delete4Min() {
        if (duration >= 5) {
            duration = duration - 4;
        } else {
            duration = 1;
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDayActivity() {
        return dayActivity;
    }

    public void setDayActivity(Long dayActivity) {
        this.dayActivity = dayActivity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MinuteActivity that = (MinuteActivity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
