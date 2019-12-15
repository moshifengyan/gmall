package com.daitian;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@EnableFeignClients
@MapperScan("com.daitian.mapper")
public class GmallCartApplication {

    public static void main(String[] args) {
        SpringApplication.run(GmallCartApplication.class, args);
    }

}
