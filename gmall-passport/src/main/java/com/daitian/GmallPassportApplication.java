package com.daitian;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.feign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class GmallPassportApplication {

    public static void main(String[] args) {
        SpringApplication.run(GmallPassportApplication.class, args);
    }

}
