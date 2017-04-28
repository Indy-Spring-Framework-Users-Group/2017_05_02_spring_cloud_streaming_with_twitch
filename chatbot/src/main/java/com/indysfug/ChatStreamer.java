package com.indysfug;

import com.gikk.twirk.Twirk;
import com.gikk.twirk.enums.EMOTE_SIZE;
import com.gikk.twirk.events.TwirkListenerBaseImpl;
import com.gikk.twirk.types.twitchMessage.TwitchMessage;
import com.gikk.twirk.types.users.TwitchUser;
import com.indysfug.twitch.dto.TwitchChatMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

/**
 * @author russell.scheerer
 */
@Component
@Slf4j
public class ChatStreamer extends TwirkListenerBaseImpl {

    private MessageChannel outputChannel;

    public ChatStreamer(@Qualifier(Processor.OUTPUT) MessageChannel outputChannel) {
        this.outputChannel = outputChannel;
    }

    @Override
    public void onPrivMsg(TwitchUser sender, TwitchMessage message) {
        TwitchChatMessage chatMessage = TwitchChatMessage.builder()
                .userId(sender.getUserID())
                .userName(sender.getUserName())
                .command(message.getCommand())
                .content(message.getContent())
                .emotes(message.getEmotes().stream().map(e ->
                        TwitchChatMessage.Emote.builder()
                                .pattern(e.getPattern())
                                .imageUrlSmall(e.getEmoteImageUrl(EMOTE_SIZE.SMALL))
                                .imageUrlMedium(e.getEmoteImageUrl(EMOTE_SIZE.MEDIUM))
                                .imageUrlLarge(e.getEmoteImageUrl(EMOTE_SIZE.LARGE))
                                .build()).collect(Collectors.toList()))
                .mod(sender.isMod())
                .prefix(message.getPrefix())
                .raw(message.getRaw())
                .tag(message.getTag())
                .target(message.getTarget())
                .build();
        log.info("Streaming message: {}", chatMessage.toString());
        outputChannel.send(MessageBuilder.withPayload(chatMessage).build());
    }

}
