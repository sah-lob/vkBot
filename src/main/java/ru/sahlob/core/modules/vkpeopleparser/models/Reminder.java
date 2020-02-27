package ru.sahlob.core.modules.vkpeopleparser.models;

import lombok.*;

import javax.persistence.*;

@Entity
@Data
@Table(name = "reminder")
@NoArgsConstructor
@EqualsAndHashCode(of = {"id"})
public class Reminder {
    @Id @GeneratedValue(strategy = GenerationType.AUTO) private Long id;
    private Integer userId = -1;
    private String userName;
    private String reminderType;
    private String frequency;
    private String reminderTimeDuration;
    private String reminderTimePassed = "0";
    private String lastTimePoint = "0";
    private Boolean sendMsg = false;
}
