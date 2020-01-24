package ru.sahlob.core.observers.interfaces;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.sahlob.core.observers.Observer;

@Repository
public interface DBObserversRepository extends CrudRepository<Observer, Integer> {
    Observer findFirstByVkId(Long id);
}
