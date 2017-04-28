package com.indysfug;

import com.indysfug.twitch.dto.TwitchChatMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.messaging.handler.annotation.SendTo;

/**
 * @author russell.scheerer
 */
@SpringBootApplication
@EnableBinding(Processor.class)
@Slf4j
public class ChatbotAi {

    public static void main(String[] args) throws Exception {
        new SpringApplication(ChatbotAi.class).run(args);
    }

    @StreamListener(Processor.INPUT)
    @SendTo(Processor.OUTPUT)
    public String respondToMessage(TwitchChatMessage twitchChatMessage) {
        log.info("Inspecting message: [{}]: {}", twitchChatMessage.getUserName(), twitchChatMessage.getContent());
        String messageContent = twitchChatMessage.getContent();
        String response = null;
        if (messageContent.contains("Hello @indysfug")) {
            log.info("Responding to user[{}]'s greeting.", twitchChatMessage.getUserName());
            response = "Hello @" + twitchChatMessage.getUserName() + " I hope you like emotes. CoolStoryBob";
        }
        return response;
    }
}

