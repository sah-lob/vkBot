package ru.sahlob.core.observers;
import lombok.Data;
import org.springframework.stereotype.Component;
import ru.sahlob.core.modules.vkpeopleparser.services.single.VKPeopleParser;
import ru.sahlob.core.observers.interfaces.DBObserversRepository;

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
            observersRepository.save(new Observer(name, VKPeopleParser.altName(name)));
        }
    }

    public void editObserver(Observer observer) {
        observersRepository.save(observer);
    }

    public void removeObserver(Observer observer) {
        observersRepository.delete(observer);
    }

    public Observer getObserver(String vkid) {
        return observersRepository.findFirstByName(vkid);
    }

    public boolean observerExists(String vkid) {
        return getObserver(vkid) != null;
    }

    public void addPeronName(String observersName, String personsName) {
        var observer = getObserver(observersName);
        observer.addPersonsName(personsName);
        editObserver(observer);
    }

    public void addRequest(String name, String request) {
        if (request.length() <= 255) {
            Observer observer = getObserver(name);
            observer.addRequest(request);
            editObserver(observer);
        }
    }
}