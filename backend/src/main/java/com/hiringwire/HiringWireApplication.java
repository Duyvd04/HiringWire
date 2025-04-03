package com.hiringwire;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class HiringWireApplication {

	public static void main(String[] args) {
		SpringApplication.run(HiringWireApplication.class, args);
	}

}
