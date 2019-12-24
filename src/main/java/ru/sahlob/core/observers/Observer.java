package ru.sahlob.core.observers;

import ru.sahlob.core.observers.roles.Role;

import java.util.List;
import java.util.Objects;

public class Observer {

    private long id;
    private long vkId;
    private String name;
    private List<Role> roles;

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
