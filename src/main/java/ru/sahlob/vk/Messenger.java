package ru.sahlob.vk;

import com.vk.api.sdk.objects.messages.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.sahlob.core.commands.commandsmanage.Commander;

@Component
public class Messenger implements Runnable {

    @Autowired
    Commander commander;

    private Message message;

    @Override
    public void run() {
        commander.execute(message);
    }


    public Commander getCommander() {
        return commander;
    }

    public void setCommander(Commander commander) {
        this.commander = commander;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }
}
