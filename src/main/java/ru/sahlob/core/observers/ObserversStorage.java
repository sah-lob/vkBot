package ru.sahlob.core.observers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.sahlob.core.observers.interfaces.DBObserversRepository;

@Component
public class ObserversStorage {

    @Autowired
    private DBObserversRepository observersRepository;


    public void saveObserver(Observer observer) {
        observersRepository.save(observer);
    }

    public void removeObserver(Observer observer) {
        observersRepository.delete(observer);
    }

    public Observer getObserver(Long vkid) {
        return observersRepository.findFirstByVkId(vkid);
    }
}