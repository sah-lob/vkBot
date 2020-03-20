package ru.sahlob.vk;

import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.messages.Message;
import com.vk.api.sdk.queries.messages.MessagesGetLongPollHistoryQuery;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Data
public class VKCore {

    private VkApiClient vk;
    private static int ts;
    private GroupActor actor;
    private static int maxMsgId = -1;
//    private TokenInfo tokenInfo;

    public VKCore(TokenInfo tokenInfo) throws ClientException, ApiException {
        var accessToken = tokenInfo.getGeoserverUrl();
        var groupId = 160448028;
        var transportClient = HttpTransportClient.getInstance();

        vk = new VkApiClient(transportClient);
        actor = new GroupActor(groupId, accessToken);
        ts = vk.messages().getLongPollServer(actor).execute().getTs();
//        this.tokenInfo = tokenInfo;
    }

    public Message getMessage() throws ClientException, ApiException {

        MessagesGetLongPollHistoryQuery eventsQuery = vk.messages()
                .getLongPollHistory(actor)
                .ts(ts);
        if (maxMsgId > 0) {
            eventsQuery.maxMsgId(maxMsgId);
        }
        List<Message> messages = eventsQuery
                .execute()
                .getMessages()
                .getMessages();

        if (!messages.isEmpty()) {
            try {
                ts =  vk.messages()
                        .getLongPollServer(actor)
                        .execute()
                        .getTs();
            } catch (ClientException e) {
                e.printStackTrace();
            }
        }
        if (!messages.isEmpty() && !messages.get(0).isOut()) {

                /*
                messageId - максимально полученный ID, нужен, чтобы не было ошибки 10 internal server error,
                который является ограничением в API VK. В случае, если ts слишком старый (больше суток),
                а max_msg_id не передан, метод может вернуть ошибку 10 (Internal server error).
                 */
            int messageId = messages.get(0).getId();
            if (messageId > maxMsgId) {
                maxMsgId = messageId;
            }

            return messages.get(0);
        }
        return null;
    }
}