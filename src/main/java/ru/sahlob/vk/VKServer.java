package ru.sahlob.vk;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import lombok.Data;
import org.springframework.stereotype.Component;
import ru.sahlob.core.modules.vkpeopleparser.VKDaysUpdate;
import ru.sahlob.core.modules.vkpeopleparser.VKPeopleParser;
import ru.sahlob.core.modules.vkpeopleparser.vktime.VKTime;
import ru.sahlob.core.observers.ObserversManagement;

import java.util.Date;
import java.util.concurrent.Executors;

@Component
@Data
public class VKServer {

    private final VKPeopleParser vkPeopleParser;
    private final Messenger messenger;
    private final VKCore vkCore;
    private final VKDaysUpdate vkDaysUpdate;
    private final ObserversManagement observersManagement;

    public void run() throws NullPointerException, ApiException, InterruptedException {
        int minutes = new Date().getMinutes();
        String days = VKTime.getDateKey(3);
        while (true) {
            Thread.sleep(300);
            if (minutes != new Date().getMinutes()) {
                vkPeopleParser.updateAllPersons();
                minutes = new Date().getMinutes();
                if (!days.equals(VKTime.getDateKey(3))) {
                    vkDaysUpdate.dayUpdate(days);
                    days = VKTime.getDateKey(3);
                }
            }
            try {
                var message = vkCore.getMessage();
                if (message != null) {
                    observersManagement.checkObserver(String.valueOf(message.getUserId()), message.getBody());
//                    observersManagement.addPersonsName(String.valueOf(message.getUserId()), "popka");
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
