package ru.sahlob.core.modules.vkpeopleparser;
import ru.sahlob.core.modules.vkpeopleparser.activity.DayActivity;
import ru.sahlob.core.modules.vkpeopleparser.vktime.VKTime;
import ru.sahlob.core.observers.Observer;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "persons")
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String alternativeName;
    private String name;
    private String sex;
    @Transient
    private Map<String, DayActivity> activity = new HashMap<>();

    @ManyToMany
    @JoinTable(
            name = "observers_persons",
            joinColumns = { @JoinColumn(name = "person_id") },
            inverseJoinColumns = { @JoinColumn(name = "observers_id")}
    )
    private Set<Observer> observers = new HashSet<>();
    private boolean isActive;
    private int timezone = 3;

    public Person() {
    }

    public Person(String name, String alternativeName) {
        this.name = name;
        this.alternativeName = alternativeName;
        this.isActive = false;
    }

    String getAlternativeName() {
        return alternativeName;
    }

    public void setAlternativeName(String alternativeName) {
        this.alternativeName = alternativeName;
    }

    public void setActivity(Map<String, DayActivity> activity) {
        this.activity = activity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    Map<String, DayActivity> getActivity() {
        return activity;
    }

    public DayActivity getTodayActivity() {
        return activity.get(VKTime.getDateKey(timezone));
    }

    public void updateTodayActivity(DayActivity dayActivity) {
        this.activity.put(VKTime.getDateKey(timezone), dayActivity);
    }

    boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public int getTimezone() {
        return timezone;
    }

    public void setTimezone(int timezone) {
        this.timezone = timezone;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Set<Observer> getObservers() {
        return observers;
    }

    public void setObservers(Set<Observer> observers) {
        this.observers = observers;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Person person = (Person) o;
        return id.equals(person.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
