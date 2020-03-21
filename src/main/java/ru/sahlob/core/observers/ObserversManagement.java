package ru.sahlob.core.observers;

import lombok.Data;
import org.springframework.stereotype.Component;
import ru.sahlob.vk.VKManager;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

@Data
@Component
public class ObserversManagement {

    private final VKManager vkManager;
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

    public void sendMessageAllWaiters(Set<String> set, String alternativePersonName) {
        for (var s : set) {
            vkManager.sendMessage(alternativePersonName + " сейчас онлайн.", Integer.parseInt(s));
        }
    }
}
