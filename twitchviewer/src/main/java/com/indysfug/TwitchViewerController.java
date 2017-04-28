package com.indysfug;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author russell.scheerer
 */
@Controller
@Slf4j
public class TwitchViewerController {
    private final List<SseEmitter> emitters = Collections.synchronizedList(new ArrayList<>());

    @Value("${twitch.channel}")
    private String twitchChannel;

    @RequestMapping(path = "/", method = RequestMethod.GET)
    public String index(Model model) {
        model.addAttribute("twitchChannel", twitchChannel);
        return "index";
    }

    @RequestMapping(path = "/emoteStream", method = RequestMethod.GET)
    public SseEmitter stream() {
        log.info("New SSE stream request");

        SseEmitter emitter = new SseEmitter();
        emitters.add(emitter);
        emitter.onCompletion(() -> emitters.remove(emitter));
        return emitter;
    }

    @StreamListener(Sink.INPUT)
    public void process(String rawEmoteMessage) throws Exception {
        log.info("Received emote message - broadcasting to {} SSEs: {}", emitters.size(), rawEmoteMessage);
        List<SseEmitter> emittersToBeRemoved = new ArrayList<>();
        for (SseEmitter sseEmitter : emitters) {
            SseEmitter emitterToBeRemoved = null;
                try {
                    sseEmitter.send(rawEmoteMessage, MediaType.APPLICATION_JSON);
                } catch (Exception e) {
                    log.error("Error publishing to emitter - removing. Reason: {}", e.getMessage());
                    sseEmitter.complete();
                    emitterToBeRemoved = sseEmitter;
                }
            emittersToBeRemoved.add(emitterToBeRemoved);
        }
        emittersToBeRemoved.forEach(emitterToBeRemoved -> emitters.remove(emitterToBeRemoved));
    }
}
