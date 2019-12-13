package ru.sahlob.core.modules.vkpeopleparser.activity;
import ru.sahlob.core.modules.vkpeopleparser.VKTime;

public class MinuteActivity {

    private String startTime;
    private int duration;

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
}
