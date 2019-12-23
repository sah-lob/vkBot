package ru.sahlob.vk;
import com.vk.api.sdk.objects.messages.Message;
import org.springframework.stereotype.Component;
import ru.sahlob.core.commands.commandsmanage.Commander;

@Component
public class Messenger implements Runnable {

    private Commander commander;
    private Message message;

    public Messenger(Commander commander) {
        this.commander = commander;
    }

    @Override
    public void run() {
        commander.execute(message);
    }

    public void setMessage(Message message) {
        this.message = message;
    }
}
