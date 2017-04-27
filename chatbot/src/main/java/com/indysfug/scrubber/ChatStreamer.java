package com.indysfug.scrubber;

import com.gikk.twirk.enums.EMOTE_SIZE;
import com.gikk.twirk.events.TwirkListenerBaseImpl;
import com.gikk.twirk.types.twitchMessage.TwitchMessage;
import com.gikk.twirk.types.users.TwitchUser;
import com.indysfug.twitch.dto.TwitchChatMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
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

    private MessageChannel messageChannel;

    public ChatStreamer(@Qualifier(Source.OUTPUT) MessageChannel messageChannel) {
        this.messageChannel = messageChannel;
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
        messageChannel.send(MessageBuilder.withPayload(chatMessage).build());
    }

}
