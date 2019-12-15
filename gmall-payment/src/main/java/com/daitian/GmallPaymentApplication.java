package com.daitian;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan("com.daitian.mapper")
@EnableFeignClients
@EnableDiscoveryClient
public class GmallPaymentApplication {
    public static void main(String[] args) {
        SpringApplication.run(GmallPaymentApplication.class,args);
    }
}
