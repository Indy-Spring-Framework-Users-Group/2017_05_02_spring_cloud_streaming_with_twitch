package com.indysfug;

import com.gikk.twirk.Twirk;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.stereotype.Component;

/**
 * Created by russell.scheerer on 4/27/17.
 */
@Component
@Slf4j
public class ChatResponder {

    private Twirk twirk;

    public ChatResponder(Twirk twirk) {
        this.twirk = twirk;
    }

    @StreamListener(Processor.INPUT)
    public void onChatResponse(String chatResponse) {
        log.info("Sending message to channel: {}", chatResponse);
        twirk.channelMessage(chatResponse);
    }
}
