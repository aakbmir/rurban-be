package com.app.rurban;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class RurbanApplication {

    public static void main(String[] args) {
        SpringApplication.run(RurbanApplication.class, args);
    }

    @Bean
    public RestTemplate getRestTemplate(){return  new RestTemplate();}
}
