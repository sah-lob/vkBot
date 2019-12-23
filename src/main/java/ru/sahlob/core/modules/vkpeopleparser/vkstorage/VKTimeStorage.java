package ru.sahlob.core.modules.vkpeopleparser.vkstorage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.sahlob.core.modules.vkpeopleparser.vkstorage.db.time.DBTimesRepository;
import ru.sahlob.core.modules.vkpeopleparser.vkstorage.db.time.VKTimeKey;

@Component
public class VKTimeStorage {

    final DBTimesRepository dbTimesRepository;

    public VKTimeStorage(DBTimesRepository dbTimesRepository) {
        this.dbTimesRepository = dbTimesRepository;
    }

    public void addNewTime(VKTimeKey key) {
        dbTimesRepository.save(key);
    }

    public int getTimeCount() {
        return dbTimesRepository.countAllByIdAfter(-1);
    }

    public String deleteFirst() {
        var intew = dbTimesRepository.findAll();
        VKTimeKey minID = null;
        for (VKTimeKey timeKey: intew) {
            if (minID == null) {
                minID = timeKey;
            } else {
                if (minID.getId() > timeKey.getId()) {
                    minID = timeKey;
                }
            }
        }
        assert minID != null;
        String key = minID.getTimeKey();
        dbTimesRepository.delete(minID);
        return key;
    }

}