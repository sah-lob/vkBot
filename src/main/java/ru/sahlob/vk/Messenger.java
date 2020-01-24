package ru.sahlob.vk;
import com.vk.api.sdk.objects.messages.Message;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.sahlob.core.commands.commandsmanage.Commander;

@Component
@Data
@RequiredArgsConstructor
public class Messenger implements Runnable {

    @NonNull
    private Commander commander;
    private Message message;

    @Override
    public void run() {
        commander.execute(message);
    }

}
