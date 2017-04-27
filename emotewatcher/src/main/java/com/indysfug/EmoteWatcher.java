package com.indysfug;

import com.indysfug.twitch.dto.TwitchChatMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

/**
 * Takes incoming messages and re-broadcasts just the emotes
 *
 * @author russell.scheerer
 */
@Component
@Slf4j
public class EmoteWatcher {

    private MessageChannel emoteChannel;

    public EmoteWatcher(@Qualifier(Processor.OUTPUT) MessageChannel emoteChannel) {
        this.emoteChannel = emoteChannel;
    }

    @StreamListener(Processor.INPUT)
    public void process(TwitchChatMessage twitchChatMessage) {
        if (twitchChatMessage.getEmotes() != null && twitchChatMessage.getEmotes().size() > 0) {
            List<TwitchChatMessage.Emote> emotes = new ArrayList<>();

            twitchChatMessage.getEmotes().forEach(emote -> {
                Integer emoteCount = StringUtils.countOccurrencesOf(twitchChatMessage.getContent(), emote.getPattern());
                IntStream.range(0, emoteCount).forEach(nbr -> emotes.add(emote));
            });

            emoteChannel.send(MessageBuilder.withPayload(emotes).build());
        }
    }
}
