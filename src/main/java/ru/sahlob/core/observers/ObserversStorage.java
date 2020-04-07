package ru.sahlob.core.observers;

import lombok.Data;
import org.springframework.stereotype.Component;
import ru.sahlob.core.modules.vkpeopleparser.services.single.VKPeopleParser;
import ru.sahlob.core.observers.interfaces.DBObserversRepository;
import ru.sahlob.core.observers.roles.Roles;

import java.util.HashSet;

@Component
@Data
public class ObserversStorage {

    private final DBObserversRepository observersRepository;

    public void addObserver(String name) {
        var dataObserver = getObserver(name);
        if (dataObserver != null) {
            dataObserver.incrementCountOfRequests();
            editObserver(dataObserver);
        } else {
            var roles = new HashSet<Roles>();
            roles.add(Roles.standart);
            observersRepository.save(new Observer(name, VKPeopleParser.altName(name), roles));
        }
    }

    public void editObserver(Observer observer) {
        observersRepository.save(observer);
    }

    public Observer getObserver(String vkid) {
        return observersRepository.findFirstByName(vkid);
    }

    public void addRequest(String name, String request) {
        if (request.length() <= 255) {
            Observer observer = getObserver(name);
            observer.addRequest(request);
            editObserver(observer);
        }
    }

    public String addPersonsId (Observer observer, String personsId) {
        if (observer.getPersonsId().contains(personsId)) {
            return "Человек уже был добавлен раньше";
        } else {
            observer.getPersonsId().add(personsId);
            observersRepository.save(observer);
            return "Человек добавлен";
        }
    }
}