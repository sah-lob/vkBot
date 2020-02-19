package ru.sahlob.core.modules.vkpeopleparser.models;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
@EqualsAndHashCode(of = {"id"})
@NoArgsConstructor
@RequiredArgsConstructor
public class Reminder {
    @Id @GeneratedValue(strategy = GenerationType.AUTO) private Long id;
    @NonNull private String user;
    @NonNull private String reminderType;
    @NonNull private String frequency;
    @NonNull private String reminderTimeDuration;
    private String reminderTimePassed = "0";
    private String lastTimePoint;
}
