package ru.sahlob.vk;

import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.users.UserXtrCounters;
import com.vk.api.sdk.queries.messages.MessagesSendQuery;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.sahlob.core.commands.commandsmanage.storage.commands.VkCommands;
import ru.sahlob.core.observers.ObserversStorage;

import java.util.*;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class VKManager {

    private final VKCore vkCore;
    private final ObserversStorage observersStorage;

    public void sendMessage(String msg, int peerId) {
        if (msg == null) {
            return;
        }

        var requests = getSortedRequests(observersStorage.getObserver(String.valueOf(peerId)).getRequests());

        try {
            var keyboard = """
                    {
                      "one_time": false,
                      "buttons": [
                        [
                   """;

            var but = new StringBuilder();

            for (int i = 0; i < requests.size(); i++) {
                but.append("""
                                    {
                                      "action": {
                                        "type": "text",
                                        "payload": "{\\"button\\": \\"%s\\"}",
                                        "label": "%s"
                                      },
                                      "color": "positive"
                                    }
                        """.formatted(i + 1, requests.get(i)));
                if (i != requests.size() -1) {
                    but = new StringBuilder(but.substring(0, but.length() - 1));
                    but.append(",");
                }
            }
            keyboard += but.substring(0, but.length()-1) + "\n";

            keyboard  += """
                        ]
                      ]
                    }\040""";

            vkCore.getVk().messages()
                    .send(vkCore.getActor())
                    .peerId(peerId).message(msg)
                    .unsafeParam("keyboard", keyboard)
                    .execute();
        } catch (ApiException | ClientException e) {
            e.printStackTrace();
        }
    }

    public MessagesSendQuery getSendQuery() {
        return vkCore.getVk().messages().send(vkCore.getActor());
    }

    /**
     * Обращается к VK API и получает объект, описывающий пользователя.
     * @param id идентификатор пользователя в VK
     * @return {@link UserXtrCounters} информацию о пользователе
     * @see UserXtrCounters
     */
    public UserXtrCounters getUserInfo(int id) {
        try {
            return vkCore.getVk().users()
                    .get(vkCore.getActor())
                    .userIds(String.valueOf(id))
                    .execute()
                    .get(0);
        } catch (ApiException | ClientException e) {
            e.printStackTrace();
        }
        return null;
    }


    private List<String> getSortedRequests(Map<String, Integer> requests) {

        var result5 = requests.entrySet()
                .stream()
                .sorted((Map.Entry.comparingByValue(Comparator.reverseOrder()))).limit(4)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

        var resultList = new ArrayList<>(result5.keySet());
        if (resultList.size() < 4) {
            var variants = List.of(
                    VkCommands.задроты.toString(),
                    VkCommands.задротыс.toString(),
                    VkCommands.лидеры.toString(),
                    VkCommands.лидерыс.toString(),
                    VkCommands.статистика.toString(),
                    VkCommands.параноики.toString(),
                    VkCommands.команды.toString());
            var set = new HashSet<>(resultList);
            for (var v: variants) {
                if (resultList.size() < 4 && !set.contains(v)) {
                    resultList.add(v);
                }
            }
        }
        return resultList;
    }
}