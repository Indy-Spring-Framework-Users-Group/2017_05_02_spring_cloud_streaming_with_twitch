package com.indysfug;

import com.indysfug.twitch.dto.TwitchChatMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

/**
 * @author russell.scheerer
 */
@SpringBootApplication
@EnableBinding(Processor.class)
@Slf4j
public class EmoteWatcherApplication {
    public static void main(String[] args) throws Exception {
        new SpringApplication(EmoteWatcherApplication.class).run(args);
    }

    @Qualifier(Processor.OUTPUT)
    @Autowired
    private MessageChannel emoteChannel;

    @StreamListener(Processor.INPUT)
    public void process(TwitchChatMessage twitchChatMessage) {
        log.info("Received twitch chat message");
        if (twitchChatMessage.getEmotes() != null && twitchChatMessage.getEmotes().size() > 0) {
            List<TwitchChatMessage.Emote> emotes = new ArrayList<>();

            twitchChatMessage.getEmotes().forEach(emote -> {
                Integer emoteCount = StringUtils.countOccurrencesOf(twitchChatMessage.getContent(), emote.getPattern());
                IntStream.range(0, emoteCount).forEach(nbr -> emotes.add(emote));
            });
            log.info("Emotes found in message! Sending {} emotes out to emoteChannel", emotes.size());
            emoteChannel.send(MessageBuilder.withPayload(emotes).build());
        }
    }
}
