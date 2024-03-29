package com.daitian;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableScheduling;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan("com.daitian.mapper")
@EnableDiscoveryClient
@EnableScheduling
public class GmallmanagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(GmallmanagerApplication.class, args);
	}

}
