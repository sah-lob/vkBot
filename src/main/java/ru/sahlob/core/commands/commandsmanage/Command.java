package ru.sahlob.core.commands.commandsmanage;

import com.vk.api.sdk.objects.messages.Message;
import lombok.Data;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
@Data
public abstract class Command {

    private List<String> names = new ArrayList<>();

    public void addAllNames(List<String> names) {
        this.names.addAll(names);
    }

    public abstract String getMessage(Message message);
}