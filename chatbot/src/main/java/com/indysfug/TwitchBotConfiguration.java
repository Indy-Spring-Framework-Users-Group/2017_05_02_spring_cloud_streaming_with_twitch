package com.indysfug;

import com.gikk.twirk.Twirk;
import com.gikk.twirk.TwirkBuilder;
import com.gikk.twirk.events.TwirkListener;
import com.gikk.twirk.events.TwirkListenerBaseImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

/**
 * @author russell.scheerer
 */
@Configuration
public class TwitchBotConfiguration {
    @Value("${twitch.oauthToken}")
    String twitchOauthToken;

    @Value("${twitch.botNick}")
    String botNick;

    @Value("${twitch.channel}")
    String channel;

    @Bean(initMethod = "connect", destroyMethod = "close")
    Twirk twichChatClient(ChatStreamer chatStreamer) {
        Twirk twirk = new TwirkBuilder("#"+channel, botNick, twitchOauthToken)
                .setVerboseMode(false)
                .build();
        twirk.addIrcListener(getOnDisconnectListener(twirk));
        twirk.addIrcListener(chatStreamer);
        return twirk;
    }

    private static TwirkListener getOnDisconnectListener(final Twirk twirk) {
        return new TwirkListenerBaseImpl() {
            @Override
            public void onDisconnect() {
                try {
                    if (!twirk.connect()) {
                        twirk.close();
                    }
                } catch (IOException e) {
                    twirk.close();
                } catch (InterruptedException e) {
                }
            }
        };
    }
}
