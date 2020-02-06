package ru.sahlob.core.modules.vkpeopleparser.domain;

import org.junit.Test;
import java.util.Arrays;
import java.util.Date;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class DayActivityTest {

    @Test
    public void addNewDayActivity() {
        DayActivity dayActivity = new DayActivity();
        dayActivity.addNewMinuteActivity();
        assertThat(dayActivity.getDayActivities().size(), is(1));
    }

    @Test
    public void addCustomDayActivities() {
        DayActivity dayActivity = new DayActivity();
        dayActivity.addNewDayActivities(new MinuteActivity());
        assertThat(dayActivity.getDayActivities().size(), is(1));
    }

    @Test
    public void addTwoNewDaysActivities() {
        DayActivity dayActivity = new DayActivity();
        dayActivity.setDayActivities(Arrays.asList(new MinuteActivity(), new MinuteActivity()));
        assertThat(dayActivity.getDayActivities().size(), is(2));
    }

    @Test
    public void createMinuteActivityAndIncrementDuration() {
        DayActivity dayActivity = new DayActivity();
        dayActivity.addNewMinuteActivity();
        dayActivity.incrementDurationOfMinuteActivities();
        assertThat(dayActivity.getDayActivities().get(0).getDuration(), is(2));
    }

    @Test
    public void createMinuteActivityWithDuration50minutesThanDelete4Minute() {
        DayActivity dayActivity = new DayActivity();
        dayActivity.addNewMinuteActivity();
        for (int i = 0; i < 50; i++) {
            dayActivity.incrementDurationOfMinuteActivities();
        }
        dayActivity.delete4MinFromMinuteActivity();
        assertThat(dayActivity.getDayActivities().get(0).getDuration(), is(47));
    }
}