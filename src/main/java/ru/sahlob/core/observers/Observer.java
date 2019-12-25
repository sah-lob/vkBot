package ru.sahlob.core.observers;

import ru.sahlob.core.modules.vkpeopleparser.Person;
import ru.sahlob.core.observers.roles.Role;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "observers")
public class Observer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private long vkId;
    private String name;
    @Transient
    private List<Role> roles;

    @ManyToMany()
    @JoinTable(
            name = "observers_persons",
            joinColumns = { @JoinColumn(name = "observers_id") },
            inverseJoinColumns = { @JoinColumn(name = "person_id")}
    )
    private Set<Person> persons = new HashSet<>();

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getVkId() {
        return vkId;
    }

    public void setVkId(long vkId) {
        this.vkId = vkId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public Set<Person> getPersons() {
        return persons;
    }

    public void setPersons(Set<Person> persons) {
        this.persons = persons;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        var observer = (Observer) o;
        return id == observer.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
