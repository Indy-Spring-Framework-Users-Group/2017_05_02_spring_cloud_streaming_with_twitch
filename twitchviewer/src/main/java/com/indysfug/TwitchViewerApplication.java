package com.indysfug;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @author russell.scheerer
 */
@SpringBootApplication
@EnableBinding(Sink.class)
public class TwitchViewerApplication {
    public static void main(String[] args) throws Exception {
        new SpringApplication(TwitchViewerApplication.class).run(args);
    }

    @Configuration
    public static class WebConfig extends WebMvcConfigurerAdapter {
        @Override
        public void configureAsyncSupport(AsyncSupportConfigurer configurer) {
            configurer.setDefaultTimeout(1000000);
        }
    }
}
