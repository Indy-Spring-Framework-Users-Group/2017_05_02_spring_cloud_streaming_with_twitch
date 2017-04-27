package com.indysfug.twitch.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.util.List;

/**
 * @author russell.scheerer
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TwitchChatMessage {

    private long userId;
    private String userName;
    private boolean mod;

    private String raw;
    private String tag;
    private String prefix;
    private String command;
    private String target;
    private String content;
    private List<Emote> emotes;

    @Override
    public String toString() {
        return "TwitchChatMessage{ user: " + userName + ", message: '" + content + "'}";
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @JsonIgnoreProperties(ignoreUnknown = true)
    @ToString
    public static class Emote {
        private String pattern;
        private String imageUrlSmall;
        private String imageUrlMedium;
        private String imageUrlLarge;
    }
}
