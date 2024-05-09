package com.app.rurban;

import com.app.rurban.services.EmailSenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableScheduling
public class RurbanApplication {

    public static void main(String[] args) {
        SpringApplication.run(RurbanApplication.class, args);
    }

    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }

//    @Autowired
//    EmailSenderService senderService;
//    @EventListener(ApplicationReadyEvent.class)
//    public void sendMail() {
//        senderService.sendMimeEmail("ezertsoul1617@gmail.com","verification","verifymyselftestemail");
//    }
}
