package ru.sahlob.vk;
import com.petersamokhin.bots.sdk.clients.Group;
import com.petersamokhin.bots.sdk.objects.Message;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.docs.Doc;
import com.vk.api.sdk.objects.docs.DocPreview;
//import com.vk.api.sdk.objects.messages.Message;
import com.vk.api.sdk.objects.messages.MessageAttachment;
import lombok.Data;
import org.springframework.stereotype.Component;
import ru.sahlob.core.modules.vkpeopleparser.services.multi.VKDaysUpdate;
import ru.sahlob.core.modules.vkpeopleparser.services.multi.reminder.ReminderService;
import ru.sahlob.core.modules.vkpeopleparser.services.single.VKPeopleParser;
import ru.sahlob.core.modules.vkpeopleparser.vktime.VKTime;
import ru.sahlob.core.observers.ObserversManagement;

import java.net.URLEncoder;
import java.util.Collections;
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
    private final ReminderService reminderService;
    private final VKManager vkManager;

    public void run() throws NullPointerException, ApiException, InterruptedException {
        int minutes = new Date().getMinutes();
        String days = VKTime.getDateKey(3);
        do {
            Thread.sleep(300);
            if (new Date().getSeconds() % 3 == 0) {
                reminderService.remindUsers();
            }
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
//                    vkCore.getVk().messages().send(vkCore.getActor()).peerId(message.getUserId()).message("hello world").unsafeParam("keyboard", key).execute();
                    observersManagement.checkObserver(String.valueOf(message.getUserId()), message.getBody());
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
        } while (true);
    }
}
