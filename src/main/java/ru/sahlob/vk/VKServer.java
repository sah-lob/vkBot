package ru.sahlob.vk;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.sahlob.core.modules.vkpeopleparser.VKPeopleParser;
import ru.sahlob.core.modules.vkpeopleparser.vktime.VKTime;
import ru.sahlob.core.modules.vkpeopleparser.vkstorage.db.people.MainVKPeopleStorage;

import java.util.Date;
import java.util.concurrent.Executors;

@Component
public class VKServer {

    @Autowired
    private MainVKPeopleStorage storage;

    @Autowired
    private VKPeopleParser vkPeopleParser;

    @Autowired
    private Messenger messenger;

    @Autowired
    public VKCore vkCore;

    public void run() throws NullPointerException, ApiException, InterruptedException {
        VKTime.getDate(-12);

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
                final int RECONNECT_TIME = 10000;
                System.out.println("Повторное соединение через " + RECONNECT_TIME / 1000 + " секунд");
                Thread.sleep(RECONNECT_TIME);
            }
        }
    }
}
