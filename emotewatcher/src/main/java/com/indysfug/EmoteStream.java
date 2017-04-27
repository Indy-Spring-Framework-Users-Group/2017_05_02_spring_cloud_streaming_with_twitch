package com.indysfug;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.messaging.SubscribableChannel;

/**
 * @author russell.scheerer
 */
public interface EmoteStream extends Processor {
    String TWITCH_EMOTES = "twitchChatEmotes";

    @Input(TWITCH_EMOTES)
    SubscribableChannel twitchEmotes();
}
