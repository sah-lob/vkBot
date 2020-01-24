package ru.sahlob.core.modules.vkpeopleparser;
import lombok.*;
import ru.sahlob.core.modules.vkpeopleparser.domain.DayActivity;
import ru.sahlob.core.modules.vkpeopleparser.vktime.VKTime;
import ru.sahlob.core.observers.Observer;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

@Entity
@Table(name = "persons")
@Data
@NoArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode(of = {"id"})
public class Person implements Serializable {

    @Id @GeneratedValue(strategy = GenerationType.AUTO) private Long id;
    @NonNull private String alternativeName;
    @NonNull private String name;
    private String sex;
    private Integer recordDurationAllTime = 0;
    private Integer avgDurationAllTime = 0;
    private Integer allTimeDaysCount = 0;
    @Transient private Map<String, DayActivity> activity = new HashMap<>();

    @ManyToMany
    @JoinTable(name = "observers_persons",
            joinColumns = { @JoinColumn(name = "person_id") },
            inverseJoinColumns = { @JoinColumn(name = "observers_id")}
    )
    private Set<Observer> observers = new HashSet<>();
    private boolean isActive = false;
    private int timezone = 3;

    public DayActivity getTodayActivity() {
        return activity.get(VKTime.getDateKey(timezone));
    }

    public DayActivity getActivityByDate(String date) {
        return activity.get(date);
    }

    public void deleteActivity() {
        activity = new HashMap<>();
    }

    public void updateTodayActivity(DayActivity dayActivity) {
        this.activity.put(VKTime.getDateKey(timezone), dayActivity);
    }

    public void updateActivityByDate(String date, DayActivity dayActivity) {
        this.activity.put(date, dayActivity);
    }

    public static final Comparator<Person> COMPARE_BY_DURATION = (lhs, rhs) -> rhs.getRecordDurationAllTime() - lhs.getRecordDurationAllTime();

    public static final Comparator<Person> COMPARE_BY_AVG_DURATION = (lhs, rhs) -> rhs.getAvgDurationAllTime() - lhs.getAvgDurationAllTime();

}
