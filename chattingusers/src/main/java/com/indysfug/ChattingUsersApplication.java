package com.indysfug;

import com.indysfug.twitch.dto.TwitchChatMessage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;
import java.util.TreeSet;

/**
 * @author russell.scheerer
 */
@SpringBootApplication
@EnableBinding(Sink.class)
@RestController
public class ChattingUsersApplication {

    public static void main(String[] args) throws Exception {
        new SpringApplication(ChattingUsersApplication.class).run(args);
    }

    private Set<String> chattingUsers = new TreeSet<>();

    @RequestMapping("/usersThatDroppedALine")
    public Set<String> allChattingUsers() {
        return chattingUsers;
    }

    @StreamListener(Sink.INPUT)
    public void receiveTwitchChatMessage(TwitchChatMessage twitchChatMessage) {
        chattingUsers.add(twitchChatMessage.getUserName());
    }
}
