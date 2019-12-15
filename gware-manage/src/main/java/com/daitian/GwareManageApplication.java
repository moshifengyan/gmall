package com.daitian;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
//@ComponentScan(basePackages = "com.daitian")
@MapperScan(basePackages = "com.daitian.mapper")
public class GwareManageApplication {

	public static void main(String[] args) {
		SpringApplication.run(GwareManageApplication.class, args);
	}
}
