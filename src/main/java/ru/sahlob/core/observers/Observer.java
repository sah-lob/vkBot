package ru.sahlob.core.observers;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.sahlob.core.modules.vkpeopleparser.Person;
import ru.sahlob.core.observers.roles.Role;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "observers")
@Data
@EqualsAndHashCode(of = {"id"})
public class Observer {

    @Id @GeneratedValue(strategy = GenerationType.AUTO) private long id;
    private long vkId;
    private String name;
    @Transient private List<Role> roles;
    @ManyToMany() @JoinTable(
            name = "observers_persons",
            joinColumns = { @JoinColumn(name = "observers_id") },
            inverseJoinColumns = { @JoinColumn(name = "person_id")}
    )
    private Set<Person> persons = new HashSet<>();
}
