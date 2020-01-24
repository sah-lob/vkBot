package ru.sahlob.core.modules.vkpeopleparser.domain;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import ru.sahlob.core.modules.vkpeopleparser.vktime.VKTime;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Comparator;
import java.util.Objects;

@Entity
@Table(name = "minutes")
@Data
@NoArgsConstructor
@EqualsAndHashCode(of = {"id"})
public class MinuteActivity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long dayActivity;
    private String startTime;
    private int duration = 1;

    MinuteActivity(int timezone) {
        this.startTime = VKTime.getMinuteActivity(timezone);
    }

    public void incrementDuration() {
        duration++;
    }

    void delete4Min() {
        if (duration >= 5) {
            duration = duration - 4;
        } else {
            duration = 1;
        }
    }

    public static final Comparator<MinuteActivity> COMPARE_BY_TIME = (lhs, rhs) -> {

        var time1  = lhs.getStartTime();
        var time2 = rhs.getStartTime();
        var h1 = time1.substring(0, time1.indexOf(":"));
        var h2 = time2.substring(0, time2.indexOf(":"));
        var m1 = time1.substring(time1.indexOf(":"));
        var m2 = time2.substring(time2.indexOf(":"));
        var intH1 = Integer.parseInt(h1.replaceAll(":", ""));
        var intH2 = Integer.parseInt(h2.replaceAll(":", ""));
        var intM1 = Integer.parseInt(m1.replaceAll(":", ""));
        var intM2 = Integer.parseInt(m2.replaceAll(":", ""));

        int result = intH1 - intH2;
        if (result == 0) {
            result = intM1 - intM2;
        }
        return result;
    };

}
