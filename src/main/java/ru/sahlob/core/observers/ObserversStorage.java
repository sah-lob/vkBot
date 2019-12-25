package ru.sahlob.core.observers;
import org.springframework.stereotype.Component;
import ru.sahlob.core.observers.interfaces.DBObserversRepository;

@Component
public class ObserversStorage {

    private final DBObserversRepository observersRepository;

    public ObserversStorage(DBObserversRepository observersRepository) {
        this.observersRepository = observersRepository;
    }


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