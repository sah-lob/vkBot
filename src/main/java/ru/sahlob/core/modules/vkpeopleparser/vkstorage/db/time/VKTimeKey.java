package ru.sahlob.core.modules.vkpeopleparser.vkstorage.db.time;

import javax.persistence.*;

@Entity
@Table(name = "VKTime")
public class VKTimeKey {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String timeKey;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTimeKey() {
        return timeKey;
    }

    public void setTimeKey(String timeKey) {
        this.timeKey = timeKey;
    }
}
