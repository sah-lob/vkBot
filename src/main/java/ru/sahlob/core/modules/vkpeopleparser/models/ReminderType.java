package ru.sahlob.core.modules.vkpeopleparser.models;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.*;

@Entity
@Data
@Table(name = "remindertypes")
@NoArgsConstructor
@EqualsAndHashCode(of = {"id"})
public class ReminderType {

    @Id @GeneratedValue(strategy = GenerationType.AUTO) private Long id;
    private String name;
    private @ElementCollection(fetch = FetchType.EAGER) List<String> notes = new ArrayList<>();

    public void addNote(String note) {
        this.notes.add(note);
    }

    public String getRandomNote() {
        return notes.get(new Random().nextInt(notes.size()));
    }

    public void addAllNotes(List<String> notes) {
        this.notes.addAll(notes);
    }
}
