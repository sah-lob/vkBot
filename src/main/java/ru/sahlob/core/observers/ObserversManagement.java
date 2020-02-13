package ru.sahlob.core.observers;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class ObserversManagement {

    private final ObserversStorage observersStorage;

    public void checkObserver(String observersName, String message) {
        observersStorage.addObserver(observersName);
        addRequest(observersName, message);
    }

    public void addPersonsName(String observersName, String personsName) {
        observersStorage.addPeronName(observersName, personsName);
    }

    public void addRequest(String observersName, String message) {
        observersStorage.addRequest(observersName, message);
    }
}
