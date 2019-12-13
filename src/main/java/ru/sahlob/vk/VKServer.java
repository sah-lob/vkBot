package ru.sahlob.vk;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import ru.sahlob.core.modules.vkpeopleparser.VKPeopleParser;
import ru.sahlob.core.modules.vkpeopleparser.VKTime;
import ru.sahlob.core.modules.vkpeopleparser.vkstorage.MainVKPeopleStorage;
import ru.sahlob.core.modules.vkpeopleparser.vkstorage.VKPeopleMemoryStorage;
import java.util.Date;
import java.util.concurrent.Executors;

public class VKServer {

    public static VKCore vkCore;

    static {
        try {
            vkCore = new VKCore();
        } catch (ApiException | ClientException e) {
            e.printStackTrace();
        }
    }

    public void run() throws NullPointerException, ApiException, InterruptedException {

        System.out.println("Running server...");
        VKTime.getDate(-12);
        // тест парсера
        MainVKPeopleStorage memoryStorage = MainVKPeopleStorage.getInstance();
        memoryStorage.addPerson("12275982");
        memoryStorage.addPerson("al_lb");
        // конец теста парсера.

        int i = new Date().getMinutes();
        while (true) {
            Thread.sleep(300);
            if (i != new Date().getMinutes()) {
                new VKPeopleParser().updateAllPersons();
                i = new Date().getMinutes();
            }
            try {
                var message = vkCore.getMessage();
                if (message != null) {
                    System.out.println(message);
                    var exec = Executors.newCachedThreadPool();
                    exec.execute(new Messenger(message));
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
