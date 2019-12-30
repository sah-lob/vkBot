package ru.sahlob.vk;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Component;
import ru.sahlob.core.modules.vkpeopleparser.Person;
import ru.sahlob.core.modules.vkpeopleparser.VKPeopleParser;
import ru.sahlob.core.modules.vkpeopleparser.vkstorage.db.people.VKPeopleBDStorage;
import ru.sahlob.core.modules.vkpeopleparser.vkstorage.db.people.interfaces.DBPersonsRepository;
import ru.sahlob.core.modules.vkpeopleparser.vkstorage.memory.VKPeopleMemoryStorage;
import ru.sahlob.core.modules.vkpeopleparser.vktime.VKTime;
import ru.sahlob.core.observers.Observer;
import ru.sahlob.core.observers.interfaces.DBObserversRepository;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;

@Component
public class VKServer {

    private final VKPeopleParser vkPeopleParser;
    private final Messenger messenger;
    private final VKCore vkCore;

//    @Autowired
//    DBPersonsRepository personsRepository;

//    @Autowired
//    DBObserversRepository observersRepository;
//
//    @Autowired
//    VKPeopleBDStorage vkPeopleBDStorage;

    public VKServer(VKPeopleParser vkPeopleParser, Messenger messenger, VKCore vkCore) {
        this.vkPeopleParser = vkPeopleParser;
        this.messenger = messenger;
        this.vkCore = vkCore;
    }

    public void run() throws NullPointerException, ApiException, InterruptedException {
        int minutes = new Date().getMinutes();
        String days = VKTime.getDateKey(3);
        while (true) {
            Thread.sleep(300);
            if (minutes != new Date().getMinutes()) {
                vkPeopleParser.updateAllPersons();
                minutes = new Date().getMinutes();
                if (!days.equals(VKTime.getDateKey(3))) {
                    vkPeopleParser.updateDayTimer();

                    days = VKTime.getDateKey(3);
                }
            }
            try {
                var message = vkCore.getMessage();
                if (message != null) {
                    var exec = Executors.newCachedThreadPool();
                    messenger.setMessage(message);
                    exec.execute(messenger);
                }
            } catch (ClientException e) {
                System.out.println("Возникли проблемы");
                final var RECONNECT_TIME = 10000;
                System.out.println("Повторное соединение через " + RECONNECT_TIME / 1000 + " секунд");
                Thread.sleep(RECONNECT_TIME);
            }
        }
    }
}
